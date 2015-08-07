
#if defined(WEBKITVIEW)

#ifdef QT50
#else
#endif
import com.jsoftware.jn.wd.webkitview;

#define QTWEBVIEW QWebView
#define QWEBVIEW WebKitView
#define WEBVIEW WebView

#elif defined(WEBENGINEVIEW)

import com.jsoftware.jn.wd.webengineview;

#define QTWEBVIEW QWebEngineView
#define QWEBVIEW WebEngineView
#define WEBVIEW WebEngine

#endif

import com.jsoftware.jn.wd.form;

// ---------------------------------------------------------------------
QWEBVIEW::QWEBVIEW(Child c, View parent) : QTWEBVIEW(parent)
{
  Q_UNUSED(parent);
  pchild = c;
  setMouseTracking(true);           // for mmove event
  setFocusPolicy(Qt::StrongFocus);  // for char event
}

// ---------------------------------------------------------------------
void QWEBVIEW::buttonEvent(QEvent::Type type, QMouseEvent *event)
{
  String lmr = "";
  switch (event.button()) {
  case Qt::LeftButton:
    lmr = "l";
    break;
  case Qt::MidButton:
    lmr = "m";
    break;
  case Qt::RightButton:
    lmr = "r";
    break;
  default:
    break;
  }

  String evtname = "mmove";
  switch (type) {
  case QEvent::MouseButtonPress:
    evtname = "mb" + lmr + "down";
    break;
  case QEvent::MouseButtonRelease:
    evtname = "mb" + lmr + "up";
    break;
  case QEvent::MouseButtonDblClick:
    evtname = "mb" + lmr + "dbl";
    break;
  case QEvent::MouseMove:
    evtname = "mmove";
    break;
  default:
    break;
  }

  // sysmodifiers = shift+2*control
  // sysdata = mousex,mousey,gtkwh,button1,button2,control,shift,button3,0,0,wheel
  char sysmodifiers[20];
  sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt::CTRL))) + (!!(event.modifiers() & Qt::SHIFT)));
  char sysdata[200];
  sprintf(sysdata , "%d %d %d %d %d %d %d %d %d %d %d %d", event.x(), event.y(), this.width(), this.height(), (!!(event.buttons() & Qt::LeftButton)), (!!(event.buttons() & Qt::MidButton)), (!!(event.modifiers() & Qt::CTRL)), (!!(event.modifiers() & Qt::SHIFT)), (!!(event.buttons() & Qt::RightButton)), 0, 0, 0);

  pchild.event=evtname;
  pchild.sysmodifiers=String(sysmodifiers);
  pchild.sysdata=String(sysdata);
  pchild.pform.signalevent(pchild);
}

// ---------------------------------------------------------------------
void QWEBVIEW::wheelEvent(QWheelEvent *event)
{
  char deltasign = ' ';
  int delta = event.delta() / 8;  // degree
  if (delta<0) {
    delta = -delta;
    deltasign = '_';
  }

  // sysmodifiers = shift+2*control
  // sysdata = mousex,mousey,gtkwh,button1,button2,control,shift,button3,0,0,wheel
  char sysmodifiers[20];
  sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt::CTRL))) + (!!(event.modifiers() & Qt::SHIFT)));
  char sysdata[200];
  sprintf(sysdata , "%d %d %d %d %d %d %d %d %d %d %d %c%d", event.x(), event.y(), this.width(), this.height(), (!!(event.buttons() & Qt::LeftButton)), (!!(event.buttons() & Qt::MidButton)), (!!(event.modifiers() & Qt::CTRL)), (!!(event.modifiers() & Qt::SHIFT)), (!!(event.buttons() & Qt::RightButton)), 0, 0, deltasign, delta);

  pchild.event=String("mwheel");
  pchild.sysmodifiers=String(sysmodifiers);
  pchild.sysdata=String(sysdata);
  pchild.pform.signalevent(pchild);
  QTWEBVIEW::wheelEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::mousePressEvent(QMouseEvent *event)
{
  buttonEvent(QEvent::MouseButtonPress, event);
  QTWEBVIEW::mousePressEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::mouseMoveEvent(QMouseEvent *event)
{
  buttonEvent(QEvent::MouseMove, event);
  QTWEBVIEW::mouseMoveEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::mouseDoubleClickEvent(QMouseEvent *event)
{
  buttonEvent(QEvent::MouseButtonDblClick, event);
  QTWEBVIEW::mouseDoubleClickEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::mouseReleaseEvent(QMouseEvent *event)
{
  buttonEvent(QEvent::MouseButtonRelease, event);
  QTWEBVIEW::mouseReleaseEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::focusInEvent(QFocusEvent *event)
{
  Q_UNUSED(event);
  pchild.event="focus";
  pchild.sysmodifiers="";
  pchild.sysdata="";
  pchild.pform.signalevent(pchild);
  QTWEBVIEW::focusInEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::focusOutEvent(QFocusEvent *event)
{
  Q_UNUSED(event);
  pchild.event="focuslost";
  pchild.sysmodifiers="";
  pchild.sysdata="";
  pchild.pform.signalevent(pchild);
  QTWEBVIEW::focusOutEvent(event);
}

// ---------------------------------------------------------------------
void QWEBVIEW::keyPressEvent(QKeyEvent *event)
{
  int key1=0;
  int key=event.key();
  if (ismodifier(key)) return;
#ifdef QT_OS_ANDROID
  if (key==KeyEvent.KEYCODE_Back) {
    View::keyPressEvent(event);
    return;
  }
#endif
  if ((key>0x10000ff)||((key>=KeyEvent.KEYCODE_F1)&&(key<=KeyEvent.KEYCODE_F12))) {
    View::keyPressEvent(event);
    return;
  } else
    key1=translateqkey(key);
  char sysmodifiers[20];
  sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt::CTRL))) + (!!(event.modifiers() & Qt::SHIFT)));
  char sysdata[20];
  if (key==key1)
    sprintf(sysdata , "%s", event.text().toUtf8().constData());
  else sprintf(sysdata , "%s", String(QChar(key1)).toUtf8().constData());

  pchild.event=String("char");
  pchild.sysmodifiers=String(sysmodifiers);
  pchild.sysdata=String(sysdata);
  pchild.pform.signalevent(pchild);
  QTWEBVIEW::keyPressEvent(event);
}
