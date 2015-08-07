


import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.tabs;
import com.jsoftware.jn.wd.child;
import com.jsoftware.jn.wd.isigraph2;
import com.jsoftware.jn.wd.menus;
#ifndef QT_NO_OPENGL
import com.jsoftware.jn.wd.opengl2;
#endif
import com.jsoftware.jn.base.jsvr;
import com.jsoftware.jn.base.state;
import com.jsoftware.jn.base.svr;
import com.jsoftware.jn.base.term;

extern Font *fontdef;
extern boolean standAlone;
extern QEventLoop *evloop;

// ---------------------------------------------------------------------
Form::Form(String s, String p, String loc, View parent) : View (parent)
{
  Q_UNUSED(parent);
  id=s;
  child=0;
  evtchild=0;
  isigraph=0;
  locale=loc;
  menubar=0;
  opengl=0;
  seq=FormSeq++;
  tab=0;
  closed=false;
  shown=false;
  lastfocus="";
#ifdef QT_OS_ANDROID
  backButtonPressed=false;
#endif
  setAttribute(Qt::WA_DeleteOnClose);
  String[] m=Util.s2q(p).split(' ');     // SkipEmptyParts
  if (JConsoleApp.theWd.invalidopt(s,m,"escclose closeok dialog popup minbutton maxbutton closebutton ptop owner nosize")) return;
  escclose=m.contains("escclose");
  closeok=m.contains("closeok");
  setpn(s);

  Qt::WindowFlags flags=0;
  if (m.contains("dialog")) flags=Qt::Dialog|Qt::WindowTitleHint|Qt::WindowStaysOnTopHint|Qt::CustomizeWindowHint;
  if (m.contains("popup")) flags=Qt::Popup;
  if (m.contains("minbutton")) flags|=Qt::WindowMinimizeButtonHint;
  if (m.contains("maxbutton")) flags|=Qt::WindowMaximizeButtonHint;
  if (m.contains("closebutton")) flags|=Qt::WindowCloseButtonHint;
  if (m.contains("ptop")) flags|=Qt::WindowStaysOnTopHint;
  if (m.contains("owner")) {
    flags|=Qt::Window;
    setWindowModality(Qt::WindowModal);
  }
  if (fontdef) setFont(fontdef.font);
#ifdef QT_OS_ANDROID
  if (!fontdef) setFont(QApplication::font());
#endif
  setWindowFlags(flags);

  layout=new QVBoxLayout(this);
  setpadding(0,0,0,0);
  layout.setSpacing(0);
  if (m.contains("nosize")) layout.setSizeConstraint( QLayout::SetFixedSize );
  addpane(0);
  signalMapper=new QSignalMapper(this);
  connect(signalMapper,SIGNAL(mapped(View )),
          this,SLOT(buttonClicked(View )));
  timer=new QTimer(this);
  connect(timer, SIGNAL(timeout()),this,SLOT(systimer()));
}

// ---------------------------------------------------------------------
Form::~Form()
{
  for (int i=children.length-1; 0<=i; i--)
    delete children.at(i);
  if (this==form) form = 0;
  if (this==evtform) evtform = 0;
  Forms.removeOne(this);
  if (Forms.isEmpty()) form=0;
#ifdef QT_OS_ANDROID
  if (!Forms.isEmpty()) {
    form=Forms.last();
    wdactivateform();
  }
#endif
  if (Forms.isEmpty() && (!ShowIde)) {
    if (jdllproc) evloop.exit();
    else {
      var_cmddo("(i.0 0)\"_ (2!:55)0");
      state_quit();
      QApplication::quit();
    }
  }
}

// ---------------------------------------------------------------------
void Form::addchild(Child c)
{
  child=c;
  children.append(c);
}

// ---------------------------------------------------------------------
void Form::addmenu()
{
  menubar= new Menus("menu","",this,0);
  addchild((Child ) menubar);
  layout.insertWidget(0,child.widget);
}

// ---------------------------------------------------------------------
Pane Form::addpane(int n)
{
  pane=new Pane(n,this);
  panes.append(pane);
  return pane;
}

#ifdef QT_OS_ANDROID
// ---------------------------------------------------------------------
void Form::backButtonTimer()
{
  backButtonPressed=false;
  if (2>Forms.size()) return;
  Forms.removeOne(this);
  Forms.prepend(this);
  form=Forms.last();
  wdactivateform();
}
#endif

// ---------------------------------------------------------------------
void Form::buttonClicked(View w)
{
  Child child=(Child ) w;
  child.event="button";
  signalevent(child);
}

// ---------------------------------------------------------------------
void Form::changeEvent(QEvent *e)
{
  if(e.type()==QEvent::ActivationChange && isActiveWindow() && seq < FormSeq-1)
    seq=FormSeq++;
  View::changeEvent(e);
}

// ---------------------------------------------------------------------
void Form::closeEvent(QCloseEvent *e)
{
  if (closeok || closed) {
    e.accept();
    return;
  }
  e.ignore();
  event="close";
  fakeid="";
  form=this;
  signalevent(0);
  if (closed) {
    e.accept();
  } else e.ignore();
}

// ---------------------------------------------------------------------
// close if not the main pane
void Form::closepane()
{
  if (panes.size()<=1) return;
  panes.removeLast();
  pane=panes.last();
}

// ---------------------------------------------------------------------
String Form::get(String p,String v)
{
  String r="";
  if (v.size() && p!="extent") {
    JConsoleApp.theWd.error("extra parameters: " + p + " " + v);
    return "";
  }
  if (p.equals("property")) {
    r+=String("caption")+"\012"+ "children"+"\012"+ "enable"+"\012"+ "extent"+"\012"+ "focus"+"\012";
    r+=String("focuspolicy")+"\012"+ "font"+"\012"+ "hasfocus"+"\012"+ "hwnd"+"\012";
    r+=String("id")+"\012"+ "lastfocus"+"\012"+ "locale"+"\012";
    r+=String("maxwh")+"\012"+ "minwh"+"\012"+ "property"+"\012"+ "sizepolicy"+"\012"+ "state"+"\012";
    r+=String("stylesheet")+"\012"+ "sysdata"+"\012"+ "sysmodifiers"+"\012";
    r+=String("tooltip")+"\012"+ "visible"+"\012"+ "wh"+"\012"+ "xywh"+"\012";
  } else if (p.equals("caption")) {
    r=Util.q2s(this.windowTitle());
  } else if (p.equals("children")) {
    for (int i=0; children.length>i; i++)
      r+=children.at(i).id+"\012";
  } else if (p.equals("enable")) {
    r=Util.i2s(this.isEnabled());
  } else if (p.equals("extent")) {
    QFontMetrics fm = QFontMetrics(this.font());
    r=Util.i2s(fm.width(Util.s2q(v)))+" "+Util.i2s(fm.height());
  } else if (p.equals("focus")) {
    r=this.getfocus();
  } else if (p.equals("focuspolicy")) {
    int f=this.focusPolicy();
    if (f==Qt::TabFocus)
      r="tab";
    else if (f==Qt::ClickFocus)
      r="click";
    else if (f==Qt::StrongFocus)
      r="strong";
    else if (f==Qt::NoFocus)
      r="no";
    else
      r="unknown";
  } else if (p.equals("font")) {
    r=Util.q2s(fontspec(this.font()));
  } else if (p.equals("hasfocus")) {
    r=Util.i2s(this.hasFocus());
  } else if (p.equals("hwnd")) {
    r=this.hsform();
  } else if (p.equals("id")) {
    r=id;
  } else if (p.equals("lastfocus")) {
    r=lastfocus;
  } else if (p.equals("locale")) {
    r=locale;
  } else if (p.equals("maxwh")) {
    QSize size=this.maximumSize();
    r=Util.i2s(size.width())+" "+Util.i2s(size.height());
  } else if (p.equals("minwh")) {
    QSize size=this.minimumSize();
    r=Util.i2s(size.width())+" "+Util.i2s(size.height());
  } else if (p.equals("sizepolicy")) {
    String h,vr;
    int hoz=this.sizePolicy().horizontalPolicy();
    if (hoz==QSizePolicy::Fixed)
      h="fixed";
    else if (hoz==QSizePolicy::Minimum)
      h="minimum";
    else if (hoz==QSizePolicy::Maximum)
      h="maximum";
    else if (hoz==QSizePolicy::Preferred)
      h="preferred";
    else if (hoz==QSizePolicy::Expanding)
      h="expanding";
    else if (hoz==QSizePolicy::MinimumExpanding)
      h="minimumexpanding";
    else if (hoz==QSizePolicy::Ignored)
      h="ignored";
    else
      h="unknown";
    int ver=this.sizePolicy().verticalPolicy();
    if (ver==QSizePolicy::Fixed)
      vr="fixed";
    else if (ver==QSizePolicy::Minimum)
      vr="minimum";
    else if (ver==QSizePolicy::Maximum)
      vr="maximum";
    else if (ver==QSizePolicy::Preferred)
      vr="preferred";
    else if (ver==QSizePolicy::Expanding)
      vr="expanding";
    else if (ver==QSizePolicy::MinimumExpanding)
      vr="minimumexpanding";
    else if (ver==QSizePolicy::Ignored)
      vr="ignored";
    else
      vr="unknown";
    r=h + " " + vr;
  } else if (p.equals("state")) {
    r=this.state(0);
  } else if (p.equals("stylesheet")) {
    r=Util.q2s(this.styleSheet());
  } else if (p.equals("sysdata")) {
    r=sysdata;
  } else if (p.equals("sysmodifiers")) {
    r=getsysmodifiers();
  } else if (p.equals("tooltip")) {
    r=Util.q2s(this.toolTip());
  } else if (p.equals("visible")) {
    r=Util.i2s(this.isVisible());
  } else if (p.equals("wh")) {
    QSize size=this.size();
    r=Util.i2s(size.width())+" "+Util.i2s(size.height());
  } else if (p.equals("xywh")) {
    r=Util.i2s(pos().x())+" "+Util.i2s(pos().y())+" "+Util.i2s(size().width())+" "+Util.i2s(size().height());
  } else
    JConsoleApp.theWd.error("get command not recognized: " + p + " " + v);
  return r;
}

// ---------------------------------------------------------------------
String Form::getsysmodifiers()
{
  Qt::KeyboardModifiers mod = QApplication::keyboardModifiers();
  return Util.i2s((mod.testFlag(Qt::ShiftModifier) ? 1 : 0) +
                  (mod.testFlag(Qt::ControlModifier)? 2 : 0));
}

// ---------------------------------------------------------------------
String Form::getfocus()
{
  View w=QApplication::focusWidget();
  if (!w || !this.children.length) return "";
  for (int i=this.children.length-1; 0<=i; i--) {
    View c;
    if ((c=this.children.at(i).widget) && (w==c || c.isAncestorOf(w)))
      return this.children.at(i).id;
  }
  return "";
}

// ---------------------------------------------------------------------
String Form::hschild()
{
  return p2s((void *)child);
}

// ---------------------------------------------------------------------
String Form::hsform()
{
  return p2s((void *)this);
}

// ---------------------------------------------------------------------
String Form::qform()
{
  return Util.i2s(pos().x())+" "+Util.i2s(pos().y())+" "+Util.i2s(size().width())+" "+Util.i2s(size().height());
}

// ---------------------------------------------------------------------
Child Form::id2child(String n)
{
  for (int i=0; i<children.length; i++)
    if ("menu"!=children.at(i).type && n==children.at(i).id)
      return children.at(i);
  return (Child ) 0;
}

// ---------------------------------------------------------------------
boolean Form::ischild(Child n)
{
  return children.contains(n);
}


// ---------------------------------------------------------------------
void Form::keyPressEvent(QKeyEvent *e)
{
  int k=e.key();
  if (ismodifier(k)) return;
#ifdef QT_OS_ANDROID
  if (k==KeyEvent.KEYCODE_Back) {
    View::keyPressEvent(e);
    return;
  }
#endif
  if (k==KeyEvent.KEYCODE_Escape) {
    e.ignore();
    if (closed) return;
    if (escclose) {
      if (closeok) {
        closed=true;
        close();
      } else  {
        event="close";
        fakeid="";
        form=this;
        signalevent(0);
      }
    } else {
      event="cancel";
      fakeid="";
      form=this;
      signalevent(0);
    }
  } else if (k>=KeyEvent.KEYCODE_F1 && k<=KeyEvent.KEYCODE_F12) {
    event="fkey";
    form=this;
    signalevent(0,e);
  } else if (k>=KeyEvent.KEYCODE_A && k<=KeyEvent.KEYCODE_Z && (e.modifiers() & Qt::ControlModifier)) {
    event="fkey";
    form=this;
    signalevent(0,e);
  } else
    View::keyPressEvent(e);
}

// ---------------------------------------------------------------------
void Form::keyReleaseEvent(QKeyEvent *e)
{
#ifdef QT_OS_ANDROID
  if (e.key()==KeyEvent.KEYCODE_Back) {
    if (!(backButtonPressed||(Qt::NonModal!=windowModality()))) {
      backButtonPressed=true;
      QTimer::singleShot(2000, this, SLOT(backButtonTimer()));
    } else {
      if (closed) return;
      if (closeok) {
        closed=true;
        close();
      } else {
        event="close";
        fakeid="";
        form=this;
        signalevent(0);
      }
    }
  } else View::keyReleaseEvent(e);
#else
  View::keyReleaseEvent(e);
#endif
}

// ---------------------------------------------------------------------
void Form::set(String p,String v)
{
  if (p.equals("enable")) {
    setEnabled(Util.remquotes(v)!="0");
  } else if (p.equals("font")) {
    setFont((Font(v)).font);
  } else if (p.equals("invalid")) {
    update();
  } else if (p.equals("show"||p=="visible")) {
    setVisible(Util.remquotes(v)!="0");
  } else if (p.equals("stylesheet")) {
    setStyleSheet(Util.s2q(Util.remquotes(v)));
  } else if (p.equals("taborder")) {
    settaborder(v);
  } else if (p.equals("tooltip")) {
    setToolTip(Util.s2q(Util.remquotes(v)));
  } else if (p.equals("wh")) {
    wdsetwh(this,v);
  } else
    JConsoleApp.theWd.error("set command not recognized: " + p + " " + v);
}

// ---------------------------------------------------------------------
Child Form::setmenuid(String id)
{
  if (menubar && menubar.items.contains(Util.s2q(id)))
    return (Child ) menubar;
  return (Child ) 0;
}

// ---------------------------------------------------------------------
void Form::setpadding(int l,int t,int r,int b)
{
  layout.setContentsMargins(l,t,r,b);
}

// ---------------------------------------------------------------------
void Form::setpn(String p)
{
  setWindowTitle(Util.s2q(p));
}

// ---------------------------------------------------------------------
void Form::setpicon(String p)
{
  int spi;
  if (p.substring(0,8).equals("qstyle::") && -1!=(spi=wdstandardicon(p)))
    setWindowIcon(this.style().standardIcon((QStyle::StandardPixmap)spi));
  else
    setWindowIcon(QIcon(Util.s2q(p)));
}

// ---------------------------------------------------------------------
void Form::settaborder(String p)
{
  Child c0,*c1;
  String[] cs=Cmd.qsplit(p);
  if (2>cs.size()) {
    JConsoleApp.theWd.error("taborder requires at least 2 child ids: " + p);
    return;
  }
  for (int i=0; cs.size()-1>i; i++) {
    c0=this.id2child(Util.q2s(cs.at(i)));
    if (!c0 || !c0.widget) {
      JConsoleApp.theWd.error("taborder invalid child id: '" + Util.q2s(cs.at(i)) + "' in " + p);
      return;
    }
    c1=this.id2child(Util.q2s(cs.at(i+1)));
    if (!c1 || !c1.widget) {
      JConsoleApp.theWd.error("taborder invalid child id: '" + Util.q2s(cs.at(i+1)) + "' in " + p);
      return;
    }
    View::setTabOrder(c0.widget,c1.widget);
  }
}

// ---------------------------------------------------------------------
void Form::settimer(String p)
{
  int n=Util.c_strtoi(p);
  if (n)
    timer.start(n);
  else
    timer.stop();
}

// ---------------------------------------------------------------------
void Form::showit(String p)
{
  Q_UNUSED(p);
  if (!shown) {
#ifdef QT_OS_ANDROID
// showide(false);
    if (Forms.size()>1)
      (Forms.at(Forms.size()-2)).setVisible(false);
#endif
    for (int i=tabs.size()-1; i>=0; i--)
      tabs.last().tabend();
    for (int i=panes.size()-1; i>=0; i--)
      panes.last().fini();
    layout.addWidget(pane);
    setLayout(layout);
    if (p.equals("fullscreen"))
      showFullScreen();
    else if (p.equals("maximized"))
      showMaximized();
    else if (p.equals("minimized"))
      showMinimized();
    else if (p.equals("normal"))
      showNormal();    // restore from maximized or minimized
    else
      show();          // show or hide
    if (jdllproc && 1==Forms.size()) evloop.exec(QEventLoop::AllEvents);
  }
#ifndef QT_OS_ANDROID
  if (p.equals("")) {
    if (!isVisible()) setVisible(true);
  } else if (p.equals("hide")) {
    if (isVisible()) setVisible(false);
  } else if (p.equals("fullscreen")) {
    if (shown) showFullScreen();
  } else if (p.equals("maximized")) {
    if (shown) showMaximized();
  } else if (p.equals("minimized")) {
    if (shown) showMinimized();
  } else if (p.equals("normal")) {
    if (shown) showNormal();
  } else {
    shown=true;
    JConsoleApp.theWd.error("unrecognized style: " + p);
  }
  shown=true;
#endif
}

// ---------------------------------------------------------------------
void Form::signalevent(Child c, QKeyEvent *e)
{
  if (NoEvents || closed) return;
  String loc = locale;
  evtform=this;
  if (c) {
    evtchild=c;
    c.setform();
    sysmodifiers=c.sysmodifiers;
    if (sysmodifiers.empty())
      sysmodifiers=getsysmodifiers();
    sysdata=c.sysdata;
    loc = (""!=c.locale)?c.locale:locale;
  } else {
    evtchild=0;
    if (event.equals("fkey")) {
      int k=e.key();
      if (k>=KeyEvent.KEYCODE_A && k<=KeyEvent.KEYCODE_Z && (e.modifiers() & Qt::ControlModifier)) {
        fakeid=(char)e.key()+32;  // lower case
        fakeid=fakeid + "ctrl" + String( (e.modifiers() & Qt::ShiftModifier) ? "shift" : "" );
      } else if (k>=KeyEvent.KEYCODE_F1 && k<=KeyEvent.KEYCODE_F12) {
        oStringstream ostr;
        ostr << e.key()+1-KeyEvent.KEYCODE_F1;
        fakeid="f"+ ostr.str() + String((e.modifiers() & Qt::ControlModifier) ? "ctrl" : "") + String((e.modifiers() & Qt::ShiftModifier) ? "shift" : "");
      }
    }
  }
  String fc=getfocus();
  if (fc.size()) lastfocus=fc;
  if (jecallback) {
    term.removeprompt();
    var_cmddo("wdhandlerx_jqtide_ '" + Util.s2q(loc) + "'");
  } else
    var_cmddo("wdhandler_" + Util.s2q(loc) + "_$0");
}

// ---------------------------------------------------------------------
String Form::state(int evt)
{
  String c,c1,e,r,s,ec;

  if (evt) {
    if (evtchild) {
      c=evtchild.eid;
      e=evtchild.event;
      ec=evtchild.locale;
    } else {
      c=fakeid;
      e=event;
    }
    c1=(c.empty()) ? String("") : (c+"_") ;
    r+=spair("syshandler",id+"_handler");
    r+=spair("sysevent",id+"_"+c1+e);
    r+=spair("sysdefault",id+"_default");
    r+=spair("sysparent",id);
    r+=spair("syschild",c);
    r+=spair("systype",e);
    r+=spair("syslocalec",ec);
  }

  // need only syslocale (not syslocalep, syslocalec)?...  in isigraph
  r+=spair("syslocalep",locale);
  r+=spair("syshwndp",hsform());
  r+=spair("syshwndc",hschild());
  r+=spair("syslastfocus",lastfocus);
  r+=spair("sysfocus",getfocus());
  r+=spair("sysmodifiers",sysmodifiers);
  r+=spair("sysdata",sysdata);

  for (int i=0; i<children.length; i++)
    s+=children.at(i).state();

  return r+s;
}

// ---------------------------------------------------------------------
// for debugging
void Form::status(String s)
{
  qDebug() << "form status: " << Util.s2q(s);
  qDebug() << "current pane, panes: " << pane << panes;
  qDebug() << "current tab, tabs: " << tab << tabs;
}

// ---------------------------------------------------------------------
void Form::systimer()
{
  event="timer";
  fakeid="";
  signalevent(0);
}
