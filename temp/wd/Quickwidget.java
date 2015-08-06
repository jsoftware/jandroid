

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.quickwidget;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
QuickWidget::QuickWidget(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="quickwidget";

  int mode=1;
  String[] m=Util.s2q(s).split(' ');     // SkipEmptyParts
  if (m.size() && (m.at(0).equals("0"||m.at(0)=="1"))) mode=!!Util.c_strtoi(Util.q2s(m.at(0)));
  QSurfaceFormat format;
  int l=m.indexOf("version");
  if ((l!=-1) && (l<m.size()-1) && 0!=m.at(l+1).toDouble()) {
    int ver1,ver2;
    String s=m.at(l+1);
    int d=s.indexOf(".");
    if (d==-1) {
      ver1=s.toInt();
      ver2=0;
    } else {
      ver1=s.mid(0,d).toInt();
      ver2=s.mid(d+1).toInt();
    }
//    qDebug() << String::number(ver1) << String::number(ver2);
    format.setVersion(ver1,ver2);
  }
  if (m.contains("compatibility")) format.setProfile(QSurfaceFormat::CompatibilityProfile);
  else format.setProfile(QSurfaceFormat::CoreProfile);

  QQuickWidget *w=new QQuickWidget;
  w.setFormat(format);
// enum ResizeMode { SizeViewToRootObject, SizeRootObjectToView }
  w.setResizeMode((QQuickWidget::ResizeMode)(resizeMode=mode));
  widget=(View ) w;
  String qn=Util.s2q(n);
  w.setObjectName(qn);
  childStyle(m);
  connect(w, SIGNAL(statusChanged( QQuickWidget::Status )), this, SLOT(statusChanged( QQuickWidget::Status)));
  connect(w, SIGNAL(sceneGraphError(QQuickWindow::SceneGraphError,String)), this, SLOT(sceneGraphError(QQuickWindow::SceneGraphError,String)));
}

// ---------------------------------------------------------------------
String QuickWidget::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void QuickWidget::set(String p,String v)
{
  QQuickWidget *w = (QQuickWidget *)widget;
  if (p.equals("source")) {
    String t = Util.s2q(Util.remquotes(v));
    if (t.contains("://"))
      sourceUrl = QUrl(t);
    else sourceUrl = QUrl::fromLocalFile(t);
    w.setSource(sourceUrl);
    w.show();
  } else if (p.equals("resizemode")) {
    w.setResizeMode((QQuickWidget::ResizeMode)(resizeMode=!!Util.c_strtoi(v)));
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
void QuickWidget::statusChanged(QQuickWidget::Status status)
{
  QQuickWidget *w = (QQuickWidget *)widget;
  if (status == QQuickWidget::Error) {
    String[] errors;
    foreach (const QQmlError &error, w.errors()) errors.append(error.toString());
    info("QtQuick",errors.join(StringLiteral(", ")));
//    qDebug() << errors.join(StringLiteral(", "));
  }
}


// ---------------------------------------------------------------------
void QuickWidget::sceneGraphError(QQuickWindow::SceneGraphError, const String &message)
{
  info("QtQuick",message);
//  qDebug() << message;
}

// ---------------------------------------------------------------------
String QuickWidget::state()
{
  return "";
}
