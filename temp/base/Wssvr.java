// for debug only
// #define DEBUG_WEBSOCKET

#ifdef DEBUG_WEBSOCKET
#endif

import com.jsortware.jn.base.wssvr;
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

extern WsSvr *wssvr;

void wssvr_handler(void *, QWebSocket*);

// ---------------------------------------------------------------------
WsSvr::WsSvr(int port, int secureMode) : clients()
{
  errString = "";
#ifdef QT53
  server = new QWebSocketServer("JWebserver", (QWebSocketServer::SslMode)secureMode, this);
#else
  Q_UNUSED(secureMode);
  server = new QWebSocketServer(this, (QtWebsocket::Protocol)1);
#endif
  if (! server.listen(QHostAddress::Any, port)) {
    errString = Util.q2s(server.errorString());
#ifdef DEBUG_WEBSOCKET
    qDebug() << "Error: Can't launch websocket server";
    qDebug() << String("QWsServer error : %1").arg(server.errorString());
#endif
    return;
  } else {
#ifdef DEBUG_WEBSOCKET
    qDebug() << String("Websocket server is listening on port %1").arg(port);
#endif
    QObject::connect(server, SIGNAL(newConnection()), this, SLOT(onNewConnection()));
#ifdef QT53
    QObject::connect(server, SIGNAL(closed()), this, SLOT(onClosed()));
#endif
  }
}

// ---------------------------------------------------------------------
#ifdef QT53
WsSvr::~WsSvr() {}

// ---------------------------------------------------------------------
void WsSvr::onClosed()
#else
WsSvr::~WsSvr()
#endif
{
  QWebSocket* socket;
  foreach (socket, clients) {
#ifdef QT53
    socket.close();
#else
    socket.disconnectFromHost();
#endif
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Websocket server terminated");
#endif
}

// ---------------------------------------------------------------------
void WsSvr::onNewConnection()
{
  QWebSocket *socket = server.nextPendingConnection();

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

  clients << socket;
  wssvr_handler((void *)ONOPEN, socket);

#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Client 0x%1 connected: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif
}

// ---------------------------------------------------------------------
void WsSvr::onDisconnected()
{
  QWebSocket *socket = qobject_cast<QWebSocket *>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Client 0x%1 disconnected: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif

  wssvr_handler((void *)ONCLOSE, socket);

  if (socket) {
    clients.removeAll(socket);
    socket.deleteLater();
  }
}

// ---------------------------------------------------------------------
void WsSvr::messageReceived(QWebSocket* socket, QByteArray ba, boolean binary)
{
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Client 0x%1 message received: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
  qDebug() << "number of bytes received: " + String::number(ba.size());
  qDebug() << String(ba.toHex());
#endif
  jsetc((char *)"wss0_jrx_",(C*)ba.data(), ba.size());
  if (binary)
    jsetc((char *)"wss1_jrx_",(C*)"binary", 6);
  else
    jsetc((char *)"wss1_jrx_",(C*)"text", 4);
  wssvr_handler((void *)ONMESSAGE, socket);
}

// ---------------------------------------------------------------------
void WsSvr::onTextMessageReceived(String message)
{
  QWebSocket *socket = qobject_cast<QWebSocket *>(sender());
  if (socket == 0) {
    return;
  }
  messageReceived(socket, message.toUtf8(), false);
}

// ---------------------------------------------------------------------
void WsSvr::onBinaryMessageReceived(QByteArray message)
{
  QWebSocket *socket = qobject_cast<QWebSocket *>(sender());
  if (socket == 0) {
    return;
  }
  messageReceived(socket, message, true);
}

// ---------------------------------------------------------------------
void WsSvr::onError(QAbstractSocket::SocketError error)
{
  Q_UNUSED(error);
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Client 0x%1 error: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif

  String er = Util.q2s(socket.errorString()) + '\012';
  jsetc((char *)"wss0_jrx_",(C*)er.Util.c_str(), er.size());
  jsetc((char *)"wss1_jrx_",(C*)"text", 4);
  wssvr_handler((void *)ONERROR, socket);

}

// ---------------------------------------------------------------------
void WsSvr::onSslErrors(const List<QSslError>& errors)
{
  QWebSocket* socket = qobject_cast<QWebSocket*>(sender());
  if (socket == 0) {
    return;
  }
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Client 0x%1 ssl error: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif

  String er = "";
  for (int i=0, sz=errors.size(); i<sz; i++) {
    er = er + Util.q2s(errors.at(i).errorString()) + '\012';
  }
  jsetc((char *)"wss0_jrx_",(C*)er.Util.c_str(), er.size());
  jsetc((char *)"wss1_jrx_",(C*)"text", 4);
  wssvr_handler((void *)ONSSLERROR, socket);

}

// ---------------------------------------------------------------------
void WsSvr::onStateChanged(QAbstractSocket::SocketState socketState)
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
  qDebug() << String("Client 0x%1 statechange: ").arg((quintptr)socket , QT_POINTER_SIZE * 2, 16, QChar('0')) << Util.s2q(st);;
#endif
  jsetc((char *)"wss0_jrx_",(C*)st.Util.c_str(), st.size());
  jsetc((char *)"wss1_jrx_",(C*)"text", 4);
  wssvr_handler((void *)ONSTATECHANGE, socket);
}

// ---------------------------------------------------------------------
void WsSvr::onPong(quint64 elapsedTime, const QByteArray & payload)
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
//  String s = "(i.0 0)\"_ wssvr_handler_z_ " + p2s((void *)ONPONG) + " " + p2s((void *)socket);
//  jedo((char *)s.Util.c_str());
}

// ---------------------------------------------------------------------
String WsSvr::querySocket()
{
  QWebSocket* client;
  String s = "";
  foreach (client, clients) {
    s = s + p2s((void *)client) + '\012';
  }
  return s;
}

// ---------------------------------------------------------------------
#ifdef QT53
String WsSvr::state(void * client)
{
  String s = "";
  QWebSocket* socket = (QWebSocket*)client;
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
boolean WsSvr::hasSocket(void * client)
{
  QWebSocket* socket = (QWebSocket*)client;
  return clients.contains(socket);
}

// ---------------------------------------------------------------------
void WsSvr::disconnect(void * client)
{
  QWebSocket* socket;
  if (client) {
    socket = (QWebSocket *)client;
    if (clients.contains(socket))
#ifdef QT53
      socket.close();
#else
      socket.disconnectFromHost();
#endif
  }
}

// ---------------------------------------------------------------------
I WsSvr::write(void * client, const char * msg, I len, boolean binary)
{
  I r=-1;
  QByteArray ba;
  String s;
  QWebSocket* socket;
#ifdef DEBUG_WEBSOCKET
  qDebug() << String("Client 0x%1 write: ").arg((quintptr)client , QT_POINTER_SIZE * 2, 16, QChar('0'));
#endif
  if (binary)
    ba = QByteArray(msg,len);
  else
    s = String::fromUtf8(msg,len);
  if (client) {
    socket = (QWebSocket *)client;
    if (clients.contains(socket)) {
      if (binary)
        r = socket.sendBinaryMessage(ba);
      else
        r = socket.sendTextMessage(s);
    }
  } else {
    foreach (socket, clients) {
      if (binary)
        r = socket.sendBinaryMessage(ba);
      else
        r = socket.sendTextMessage(s);
    }
  }
  return r;
}

// ---------------------------------------------------------------------
void wssvr_handler(void *n, QWebSocket* socket)
{
  String s="(i.0 0)\"_ wssvr_handler_z_ " + p2s(n) + " " + p2s((void *)socket);
  jcon.cmddo(s);
}
