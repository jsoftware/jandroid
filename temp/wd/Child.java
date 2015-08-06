
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.child;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.font;

// ---------------------------------------------------------------------
Child::Child(String n, String s, Form f, Pane p)
{
  grouped=false;
  id=n;
  eid=n;
  parms=s;
  pform=f;
  ppane=p;
  widget=0;
  locale="";
}

// ---------------------------------------------------------------------
Child::~Child()
{
  if (null!=widget) delete widget;
  widget=0;
}

// ---------------------------------------------------------------------
void Child::childStyle(String[] opt)
{
  Q_UNUSED(opt);
}

// ---------------------------------------------------------------------
void Child::cmd(String p, String v)
{
  Q_UNUSED(p);
  Q_UNUSED(v);
}

// ---------------------------------------------------------------------
String Child::get(String p,String v)
{
  String r="";
  if (v.size() && p!="extent") {
    JConsoleApp.theWd.error("extra parameters: " + id + " " + p + " " + v);
    return "";
  }
  if (p.equals("property")) {
    r+=String("enable")+"\012"+ "extent"+"\012";
    r+=String("focuspolicy")+"\012"+ "font"+"\012"+ "hasfocus"+"\012"+ "hwnd"+"\012";
    r+=String("id")+"\012"+ "locale"+"\012";
    r+=String("maxwh")+"\012"+ "minwh"+"\012"+ "nextfocus"+"\012"+ "parent"+"\012"+ "prevfocus"+"\012";
    r+=String("property")+"\012"+ "sizepolicy"+"\012"+ "state"+"\012"+ "stylesheet"+"\012";
    r+=String("tooltip")+"\012"+ "type"+"\012"+ "visible"+"\012"+ "wh"+"\012"+ "xywh"+"\012";
  } else if (p.equals("enable")) {
    if (null!=widget) r=Util.i2s(widget.isEnabled());
  } else if (p.equals("extent")) {
    if (null!=widget) {
      QFontMetrics fm = QFontMetrics(widget.font());
      r=Util.i2s(fm.width(Util.s2q(v)))+" "+Util.i2s(fm.height());
    }
  } else if (p.equals("focuspolicy")) {
    if (null!=widget) {
      int f=widget.focusPolicy();
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
    }
  } else if (p.equals("font")) {
    if (null!=widget) r=Util.q2s(fontspec(widget.font()));
  } else if (p.equals("hasfocus")) {
    if (null!=widget) r=Util.i2s(widget.hasFocus());
  } else if (p.equals("hwnd")) {
    r=p2s(this);
  } else if (p.equals("id")) {
    r=id;
  } else if (p.equals("locale")) {
    r=(locale!="")?locale:pform.locale;
  } else if (p.equals("maxwh")) {
    if (null!=widget) {
      QSize size=widget.maximumSize();
      r=Util.i2s(size.width())+" "+Util.i2s(size.height());
    }
  } else if (p.equals("minwh")) {
    if (null!=widget) {
      QSize size=widget.minimumSize();
      r=Util.i2s(size.width())+" "+Util.i2s(size.height());
    }
  } else if (p.equals("nextfocus")) {
    if (null!=widget) {
      r=getfocuschain(false);
    }
  } else if (p.equals("parent")) {
    r=pform.id;
  } else if (p.equals("prevfocus")) {
    if (null!=widget) {
      r=getfocuschain(true);
    }
  } else if (p.equals("sizepolicy")) {
    if (null!=widget) {
      String h,vr;
      int hoz=widget.sizePolicy().horizontalPolicy();
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
      int ver=widget.sizePolicy().verticalPolicy();
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
    }
  } else if (p.equals("state")) {
    r=state();
  } else if (p.equals("stylesheet")) {
    if (null!=widget) r=Util.q2s(widget.styleSheet());
  } else if (p.equals("tooltip")) {
    if (null!=widget) r=Util.q2s(widget.toolTip());
  } else if (p.equals("type")) {
    r=type;
  } else if (p.equals("visible")) {
    if (null!=widget) r=Util.i2s(widget.isVisible());
  } else if (p.equals("wh")) {
    if (null!=widget) {
      QSize size=widget.size();
      r=Util.i2s(size.width())+" "+Util.i2s(size.height());
    }
  } else if (p.equals("xywh")) {
    if (null!=widget) {
      QPoint pos=widget.mapTo(widget.window(),widget.pos());
      QSize size=widget.size();
      r=Util.i2s(pos.x())+" "+Util.i2s(pos.y())+" "+Util.i2s(size.width())+" "+Util.i2s(size.height());
    }
  } else
    JConsoleApp.theWd.error("get command not recognized: " + id + " " + p + " " + v);
  return r;
}

// ---------------------------------------------------------------------
String Child::getfocuschain(boolean prev)
{
  if (null==widget) return "";
  View w=(prev)?widget.previousInFocusChain():widget.nextInFocusChain();
  if (!w) return "";
  for (int i=pform.children.length-1; 0<=i; i--) {
    View c;
    if ((c=pform.children.at(i).widget) && (w==c || c.isAncestorOf(w)))
      return pform.children.at(i).id;
  }
  return"";
}

// ---------------------------------------------------------------------
String Child::getsysdata()
{
  return"";
}

// ---------------------------------------------------------------------
void Child::set(String p,String v)
{
  if (p.equals("cursor")) {
    if (null!=widget) {
      int a=Util.c_strtoi(v);
      if (-1==a)
        widget.unsetCursor();
      else
        widget.setCursor((Qt::CursorShape)a);
    }
  } else if (p.equals("enable")) {
    if (null!=widget) widget.setEnabled(Util.remquotes(v)!="0");
  } else if (p.equals("locale")) {
    locale=Util.remquotes(v);
  } else if (p.equals("focus")) {
    if (null!=widget) widget.setFocus();
  } else if (p.equals("focuspolicy")) {
    setfocuspolicy(v);
  } else if (p.equals("font")) {
    if (null!=widget) widget.setFont((Font(v)).font);
  } else if (p.equals("invalid")) {
    if (null!=widget) widget.update();
  } else if (p.equals("nofocus")) {
    if (null!=widget) widget.setFocusPolicy(Qt::NoFocus);
  } else if (p.equals("show"||p=="visible")) {
    if (null!=widget) widget.setVisible(Util.remquotes(v)!="0");
  } else if (p.equals("sizepolicy")) {
    setsizepolicy(v);
  } else if (p.equals("stylesheet")) {
    if (null!=widget) widget.setStyleSheet(Util.s2q(Util.remquotes(v)));
  } else if (p.equals("tooltip")) {
    if (null!=widget) widget.setToolTip(Util.s2q(Util.remquotes(v)));
  } else if (p.equals("wh")) {
    setwh(v);
  } else if (p.equals("maxwh")) {
    setmaxwhv(v);
  } else if (p.equals("minwh")) {
    setminwhv(v);
  } else
    JConsoleApp.theWd.error("set command not recognized: " + id + " " + p + " " + v);
}

// ---------------------------------------------------------------------
void Child::setfocuspolicy(String p)
{
  if (null!=widget) wdsetfocuspolicy(widget,p);
}

// ---------------------------------------------------------------------
void Child::setform()
{
  form=pform;
  form.child=this;
}

// ---------------------------------------------------------------------
void Child::setsizepolicy(String p)
{
  if (null!=widget) wdsetsizepolicy(widget,p);
}

// ---------------------------------------------------------------------
void Child::setwh(String p)
{
  if (null!=widget) wdsetwh(widget,p);
}

// ---------------------------------------------------------------------
void Child::setmaxwhv(String p)
{
  String[] n=Util.s2q(p).split(" ");     // SkipEmptyParts
  if (n.length!=2) {
    JConsoleApp.theWd.error("set maxwh requires 2 numbers: " + id + " " + p);
  } else {
    int w=Util.c_strtoi(Util.q2s(n.at(0)));
    int h=Util.c_strtoi(Util.q2s(n.at(1)));
    setmaxwh(w,h);
  }
}

// ---------------------------------------------------------------------
void Child::setmaxwh(int w, int h)
{
  if (widget && w && h) {
    widget.setMaximumSize(w,h);
    widget.updateGeometry();
  }
}

// ---------------------------------------------------------------------
void Child::setminwhv(String p)
{
  String[] n=Util.s2q(p).split(" ");     // SkipEmptyParts
  if (n.length!=2) {
    JConsoleApp.theWd.error("set minwh requires 2 numbers: " + id + " " + p);
  } else {
    int w=Util.c_strtoi(Util.q2s(n.at(0)));
    int h=Util.c_strtoi(Util.q2s(n.at(1)));
    setminwh(w,h);
  }
}

// ---------------------------------------------------------------------
void Child::setminwh(int w, int h)
{
  if (widget && w && h) {
    widget.setMinimumSize(w,h);
    widget.updateGeometry();
  }
}
