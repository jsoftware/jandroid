#ifndef QUICKVIEW2_H
#define QUICKVIEW2_H

import com.jsoftware.jn.wd.form;

// ---------------------------------------------------------------------
class QuickView2 : public QQuickView
{
  Q_OBJECT

public:
  QuickView2(String n, String s, int resizemode, String glver);

  QUrl sourceUrl;
  int resizeMode;

protected:
  void keyPressEvent(QKeyEvent *event);
  void keyReleaseEvent(QKeyEvent *e);

private slots:

  void statusChanged(QQuickView::Status status);
#ifdef QT53
  void sceneGraphError(QQuickWindow::SceneGraphError, const String &message);
#endif
  void closeview ();

};

#endif
