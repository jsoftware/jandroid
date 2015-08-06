// for debug only
// #define DEBUG_WEBSOCKET

#ifdef DEBUG_WEBSOCKET
#endif

import com.jsortware.jn.base.wscln;
import com.jsortware.jn.base.jsvr;
import com.jsortware.jn.base.svr;
import com.jsortware.jn.base.util;


#define   ONOPEN        0
#define   ONCLOSE       1
#define   ONMESSAGE     2
#define   ONERROR       3
#define   ONSSLERROR    4
#define   ONSTATECHANGE 5
#define   ONPONG        6

extern WsCln *wscln;

void wscln_handler(void *, QWebSocket*);

// ---------------------------------------------------------------------
WsCln::WsCln() : servers()
{
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Websocket client started");
#endif
}

// ---------------------------------------------------------------------
WsCln::~WsCln()
{
  QWebSocket* socket;
  foreach (socket, servers) {
#ifdef QT53
    socket.close();
#else
    socket.disconnectFromHost();
#endif
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Websocket client terminated");
#endif
}

// ---------------------------------------------------------------------
void * WsCln::openurl(String url)
{
  url.trimmed();
  if (url.isEmpty()) {
#ifdef DEBUG_WEBSOCKET
    qDebug() << String("empty url");
#endif
    return (void *) 0;
  } else if (!(url.startsWith("ws://", Qt::CaseInsensitive)||url.startsWith("wss://", Qt::CaseInsensitive))) {
#ifdef DEBUG_WEBSOCKET
    qDebug() << String("url not ws:// ro wss://");
#endif
    return (void *) 0;
  }

#ifdef QT53
  QWebSocket* socket = new QWebSocket();
#else
  int port=80;
  if (url.startsWith("wss://", Qt::CaseInsensitive)) port=443;
  QWebSocket* socket = new QWebSocket(this, NULL, QtWebsocket::WS_V13);
#endif

  QObject::connect(socket, SIGNAL(connected()), this, SLOT(onConnected()));
  QObject::connect(socket, SIGNAL(disconnected()), this, SLOT(onDisconnected()));
  QObject::connect(socket, SIGNAL(stateChanged(QAbstractSocket::SocketState)), this, SLOT(onStateChanged(QAbstractSocket::SocketState)));
  QObject::connect(socket, SIGNAL(JConsoleApp.theWd.error(QAbstractSocket::SocketError)), this, SLOT(onError(QAbstractSocket::SocketError)));
  QObject::connect(socket, SIGNAL(sslErrors(const List<QSslError>&)), this, SLOT(onSslErrors(const List<QSslError>&)));
#ifdef QT53
  QObject::connect(socket, SIGNAL(textMessageReceived(String)), this, SLOT(onTextMessageReceived(String)));
  QObject::connect(socket, SIGNAL(binaryMessageReceived(QByteArray)), this, SLOT(onBinaryMessageReceived(QByteArray)));
#else
  QObject::connect(socket, SIGNAL(frameReceived(String)), this, SLOT(onTextMessageReceived(String)));
  QObject::connect(socket, SIGNAL(frameReceived(QByteArray)), this, SLOT(onBinaryMessageReceived(QByteArray)));
#endif

  servers << socket;

#ifdef DEBUG_WEBSOCKET
  qDebug() << String("try connect to: ") + url;
#endif

#ifdef QT53
  socket.open(QUrl(url));
#else
  if (2>url.count(QChar(':'))) {
    socket.connectToHost(url.toUtf8(), port);
  } else {
    String[] n = url.split(":");     // SkipEmptyParts
    String p;
    int i;
    if (0<=(i=n.at(2).lastIndexOf(QChar('/')))) {
      port=n.at(2).left(i).toInt();
      p = n.at(0)+":"+n.at(1)+n.at(2).mid(i);
    } else {
      port=n.at(2).toInt();
      p = n.at(0)+":"+n.at(1);
    }
#ifdef DEBUG_WEBSOCKET
    qDebug() << String("parsed url: ") + p + " port: " + String::number(port);
#endif
    socket.connectToHost(p.toUtf8(), port);
  }
#endif

  return (void *) socket;
}

// ---------------------------------------------------------------------
void WsCln::onConnected()
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
  wscln_handler((void *)ONOPEN,socket);

#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 connected: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif
}

// ---------------------------------------------------------------------
void WsCln::onDisconnected()
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 disconnected: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif

  wscln_handler((void *)ONCLOSE,socket);

  servers.removeAll(socket);
  socket.deleteLater();
}

// ---------------------------------------------------------------------
void WsCln::messageReceived(QWebSocket* socket, QByteArray ba, boolean binary)
{
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 message received: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
  qDebug() << "number of bytes received: " + String::number(ba.size());
  qDebug() << String(ba.toHex());
#endif

  jsetc((char *)"wsc0_jrx_",(C*)ba.data(), ba.size());
  if (binary)
    jsetc((char *)"wsc1_jrx_",(C*)"binary", 6);
  else
    jsetc((char *)"wsc1_jrx_",(C*)"text", 4);
  wscln_handler((void *)ONMESSAGE,socket);
}

// ---------------------------------------------------------------------
void WsCln::onTextMessageReceived(String message)
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
  messageReceived(socket, message.toUtf8(), false);
}

// ---------------------------------------------------------------------
void WsCln::onBinaryMessageReceived(QByteArray message)
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
  messageReceived(socket, message, true);
}

// ---------------------------------------------------------------------
void WsCln::onError(QAbstractSocket::SocketError error)
{
  Q_UNUSED(error);
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 error: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif

  String er = Util.q2s(socket.errorString()) + '\012';
  jsetc((char *)"wsc0_jrx_",(C*)er.Util.c_str(), er.size());
  jsetc((char *)"wsc1_jrx_",(C*)"text", 4);
  wscln_handler((void *)ONERROR,socket);
}

// ---------------------------------------------------------------------
void WsCln::onSslErrors(const List<QSslError> &errors)
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 ssl error: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif

  String er = "";
  for (int i=0, sz=errors.size(); i<sz; i++) {
    er = er + Util.q2s(errors.at(i).errorString()) + '\012';
  }
  jsetc((char *)"wsc0_jrx_",(C*)er.Util.c_str(), er.size());
  jsetc((char *)"wsc1_jrx_",(C*)"text", 4);
  wscln_handler((void *)ONSSLERROR,socket);
}

// ---------------------------------------------------------------------
void WsCln::onStateChanged(QAbstractSocket::SocketState socketState)
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
  String st;
  switch (socketState) {
  case QAbstractSocket::UnconnectedState:
    st = "Unconnected";
    break;
  case QAbstractSocket::HostLookupState:
    st = "HostLookup";
    break;
  case QAbstractSocket::ConnectingState:
    st = "Connecting";
    break;
  case QAbstractSocket::ConnectedState:
    st = "Connected";
    break;
  case QAbstractSocket::BoundState:
    st = "Bound";
    break;
  case QAbstractSocket::ClosingState:
    st = "Closing";
    break;
  case QAbstractSocket::ListeningState:
    st = "Listening";
    break;
  default:
    st = "Unknown";
    break;
  }

#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 statechange: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0')) << Util.s2q(st);;
#endif
  jsetc((char *)"wsc0_jrx_",(C*)st.Util.c_str(), st.size());
  jsetc((char *)"wsc1_jrx_",(C*)"text", 4);
  wscln_handler((void *)ONSTATECHANGE,socket);
}

// ---------------------------------------------------------------------
void WsCln::onPong(quint64 elapsedTime, const QByteArray & payload)
{
  Q_UNUSED(elapsedTime);
  Q_UNUSED(payload);
  QWebSocket* socket = qobject_cast<QWebSocket *>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("ping: %1 ms").arg(elapsedTime);
#endif

// no need to call J until there is a reason
//  String s = "wscln_handler_z_ " + p2s((void *)ONPONG) + " " + p2s((void *)socket);
//  jedo((char *)s.Util.c_str());
}

// ---------------------------------------------------------------------
String WsCln::querySocket()
{
  QWebSocket* socket;
  String s = "";
  foreach (socket, servers) {
    s = s + p2s((void *)socket) + '\012';
  }
  return s;
}

// ---------------------------------------------------------------------
#ifdef QT53
String WsCln::state(void * server)
{
  String s = "";
  QWebSocket* socket = (QWebSocket*)server;
  if (!hasSocket(socket)) return s;
  s += spair("errorString", socket.errorString());
  s += spair("isValid", socket.isValid()?(String)"1":(String)"0");
  s += spair("localAddress", socket.localAddress().toString());
  s += spair("localPort", String::number(socket.localPort()));
  s += spair("origin", socket.origin());
  s += spair("peerAddress", socket.peerAddress().toString());
  s += spair("peerName", socket.peerName());
  s += spair("peerPort", String::number(socket.peerPort()));
  s += spair("requestUrl", socket.requestUrl().toDisplayString());
  s += spair("resourceName", socket.resourceName());
  return s;
}
#endif

// ---------------------------------------------------------------------
boolean WsCln::hasSocket(void * server)
{
  QWebSocket* socket = (QWebSocket*)server;
  return servers.contains(socket);
}

// ---------------------------------------------------------------------
void WsCln::disconnect(void * server)
{
  QWebSocket* socket;
  if (server) {
    socket = (QWebSocket*)server;
    if (servers.contains(socket))
#ifdef QT53
      socket.close();
#else
      socket.disconnectFromHost();
#endif
  }
}

// ---------------------------------------------------------------------
I WsCln::write(void * server, const char * msg, I len, boolean binary)
{
  I r = -1;
  QByteArray ba;
  String s;
  QWebSocket* socket;
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Server 0x%1 write: ").arg((quintptr)server , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif
  if (binary)
    ba = QByteArray(msg,len);
  else
    s = String::fromUtf8(msg,len);
  if (server) {
    socket = (QWebSocket*)server;
    if (servers.contains(socket)) {
      if (binary)
        r = socket.sendBinaryMessage(ba);
      else
        r = socket.sendTextMessage(s);
    }
  } else {
    foreach (socket, servers) {
      if (binary)
        r = socket.sendBinaryMessage(ba);
      else
        r = socket.sendTextMessage(s);
    }
  }
  return r;
}

// ---------------------------------------------------------------------
void wscln_handler(void *n, QWebSocket* socket)
{
  String s="(i.0 0)\"_ wscln_handler_z_ " + p2s(n) + " " + p2s((void *)socket);
  jcon.cmddo(s);
}
