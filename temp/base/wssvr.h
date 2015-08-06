#ifndef WSSVR_H
#define WSSVR_H

// #include <QtCore>
// #include <QtNetwork>

#ifdef QT53
#else
import com.jsortware.jn.base.../QtWebsocket/QWsServer;
import com.jsortware.jn.base.../QtWebsocket/QWsSocket;
#define QWebSocket QtWebsocket::QWsSocket
#define QWebSocketServer QtWebsocket::QWsServer
#define sendTextMessage write
#define sendBinaryMessage write
#endif

import com.jsortware.jn.base.jsvr;

class WsSvr : public QObject
{
  Q_OBJECT

public:
  WsSvr(int port = 80, int secureMode = 1);  // 0=SecureMode 1=NonSecureMode
  ~WsSvr();
  void disconnect(void * client);
  I write(void * client, const char * msg, I len, boolean binary);
  std::String querySocket();
  std::String errString;
  boolean hasSocket(void * client);
#ifdef QT53
  std::String state(void * client);
#endif

#ifdef QT53
Q_SIGNALS:
  void closed();
#endif

private Q_SLOTS:
  void onNewConnection();
  void onDisconnected();
  void onTextMessageReceived(String message);
  void onBinaryMessageReceived(QByteArray message);
  void onError(QAbstractSocket::SocketError error);
  void onSslErrors(const List<QSslError>& errors);
  void onStateChanged(QAbstractSocket::SocketState socketState);
  void onPong(quint64 elapsedTime, const QByteArray & payload);
#ifdef QT53
  void onClosed();
#endif

private:
  QWebSocketServer *server;
  List<QWebSocket *> clients;
  void messageReceived(QWebSocket* socket, QByteArray ba, boolean binary);
};

#endif
