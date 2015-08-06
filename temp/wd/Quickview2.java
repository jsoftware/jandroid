

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.quickview2;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.base.utils;

extern QuickView2 * quickview2;

// ---------------------------------------------------------------------
QuickView2::QuickView2(String n, String s, int resizemode, String glver) : QQuickView()
{
  String qn=Util.s2q(n);
  setObjectName(qn);
// enum ResizeMode { SizeViewToRootObject, SizeRootObjectToView }
  setTitle(qn);
  setResizeMode((QQuickView::ResizeMode)(this.resizeMode=resizemode));

  QSurfaceFormat format;
  if (!glver.empty()) {
    int ver1,ver2;
    String::size_type d=glver.find(".",0);
    if (d == String::npos) {
      ver1=atoi(glver.Util.c_str());
      ver2=0;
    } else {
      ver1=atoi(glver.substring(0,d).Util.c_str());
      ver2=atoi(glver.substring(d+1).Util.c_str());
    }
//    qDebug() << String::number(ver1) << String::number(ver2);
    format.setVersion(ver1,ver2);
  }
  format.setProfile(QSurfaceFormat::CoreProfile);
  setFormat(format);

  QObject::connect((QObject*)this.engine(), SIGNAL(quit()), this, SLOT(closeview()));
  String t = Util.s2q(s);
  if (t.contains("://"))
    sourceUrl = QUrl(t);
  else sourceUrl = QUrl::fromLocalFile(t);
  setSource(sourceUrl);
  connect(this, SIGNAL(statusChanged(QQuickView::Status)), this, SLOT(statusChanged(QQuickView::Status)));
#ifdef QT53
  connect(this, SIGNAL(sceneGraphError(QQuickWindow::SceneGraphError,String)), this, SLOT(sceneGraphError(QQuickWindow::SceneGraphError,String)));
#endif
}

// ---------------------------------------------------------------------
void QuickView2::statusChanged(QQuickView::Status status)
{
  if (status == QQuickView::Error) {
    String[] errors;
    foreach (const QQmlError &error, this.errors()) errors.append(error.toString());
    info("QtQuick",errors.join(StringLiteral(", ")));
//    qDebug() << errors.join(StringLiteral(", "));
  }
}

#ifdef QT53
// ---------------------------------------------------------------------
void QuickView2::sceneGraphError(QQuickWindow::SceneGraphError, const String &message)
{
  info("QtQuick",message);
//  qDebug() << message;
}
#endif

// ---------------------------------------------------------------------
void QuickView2::keyPressEvent(QKeyEvent *event)
{
#ifdef QT_OS_ANDROID
  int key=event.key();
  if (key==Qt::Key_Back) {
    QQuickView::keyPressEvent(event);
    return;
  }
#endif
  QQuickView::keyPressEvent(event);
}

// ---------------------------------------------------------------------
void QuickView2::keyReleaseEvent(QKeyEvent *e)
{
#ifdef QT_OS_ANDROID
  if (e.key()==Qt::Key_Back) {
    showide(true);
    if (Forms.size()>0)
      (Forms.at(Forms.size()-1)).setVisible(true);
    close();
  } else QQuickView::keyReleaseEvent(e);
#else
  QQuickView::keyReleaseEvent(e);
#endif
}

// ---------------------------------------------------------------------
void QuickView2::closeview ()
{
#ifdef QT_OS_ANDROID
  showide(true);
  if (Forms.size()>0)
    (Forms.at(Forms.size()-1)).setVisible(true);
#endif
  close();
}

