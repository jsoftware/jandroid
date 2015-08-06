#ifndef WSCLN_H
#define WSCLN_H

// #include <QtCore>
// #include <QtNetwork>

#ifdef QT53
#else
import com.jsortware.jn.base.../QtWebsocket/QWsSocket;
#define QWebSocket QtWebsocket::QWsSocket
#define QWebSocketServer QtWebsocket::QWsServer
#define sendTextMessage write
#define sendBinaryMessage write
#endif

import com.jsortware.jn.base.jsvr;

class WsCln : public QObject
{
  Q_OBJECT

public:
  WsCln();
  ~WsCln();
  void * openurl(String url);
  void disconnect(void * server);
  I write(void * server, const char * msg, I len, boolean binary);
  std::String querySocket();
  boolean hasSocket(void * server);
#ifdef QT53
  std::String state(void * server);
#endif

private Q_SLOTS:
  void onConnected();
  void onDisconnected();
  void onTextMessageReceived(String message);
  void onBinaryMessageReceived(QByteArray message);
  void onError(QAbstractSocket::SocketError error);
  void onSslErrors(const List<QSslError> &errors);
  void onStateChanged(QAbstractSocket::SocketState socketState);
  void onPong(quint64 elapsedTime, const QByteArray & payload);

private:
  List<QWebSocket *> servers;
  void messageReceived(QWebSocket* socket, QByteArray ba, boolean binary);
};

#endif
