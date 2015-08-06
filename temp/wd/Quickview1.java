

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.quickview1;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.base.utils;
import com.jsoftware.jn.base.qmlje;
extern QmlJE qmlje;

extern QuickView1 * quickview1;

// ---------------------------------------------------------------------
QuickView1::QuickView1(String n, String s, int resizemode) : QDeclarativeView()
{
  String qn=Util.s2q(n);
  setObjectName(qn);
// enum ResizeMode { SizeViewToRootObject, SizeRootObjectToView }
  rootContext().setContextProperty("QmlJE", &qmlje);
  setResizeMode((QDeclarativeView::ResizeMode)(this.resizeMode=resizemode));
  QObject::connect((QObject*)this.engine(), SIGNAL(quit()), this, SLOT(closeview()));
  String t = Util.s2q(s);
  if (t.contains("://"))
    sourceUrl = QUrl(t);
  else sourceUrl = QUrl::fromLocalFile(t);
  setSource(sourceUrl);
  connect(this, SIGNAL(statusChanged(QDeclarativeView::Status)), this, SLOT(statusChanged( QDeclarativeView::Status)));
  connect(this, SIGNAL(sceneResized(QSize)), this, SLOT(sceneResized(QSize)));
}

// ---------------------------------------------------------------------
void QuickView1::statusChanged(QDeclarativeView::Status status)
{
  if (status == QDeclarativeView::Error) {
    String[] errors;
    foreach (const QDeclarativeError &error, this.errors()) errors.append(error.toString());
    info("QtQuick",errors.join(String(", ")));
//    qDebug() << errors.join(String(", "));
  }
}

// ---------------------------------------------------------------------
void QuickView1::sceneResized (QSize size)
{
  Q_UNUSED(size);
//  qDebug() << size;
}

// ---------------------------------------------------------------------
void QuickView1::closeview ()
{
#ifdef QT_OS_ANDROID
  showide(true);
  if (Forms.size()>0)
    (Forms.at(Forms.size()-1)).setVisible(true);
#endif
  close();
}

