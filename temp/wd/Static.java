

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.static;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Static::Static(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="static";
  QLabel *w=new QLabel;
  widget=(View ) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"staticbox left right center sunken raised panel")) return;
  if (1<(opt.contains("left")?1:0) + (opt.contains("right")?1:0) + (opt.contains("center")?1:0)) {
    JConsoleApp.theWd.error("conflicting child style: " + n + " " + Util.q2s(opt.join(" ")));
    return;
  }
  if (1<(opt.contains("sunken")?1:0) + (opt.contains("raised")?1:0) + (opt.contains("panel")?1:0)) {
    JConsoleApp.theWd.error("conflicting child style: " + n + " " + Util.q2s(opt.join(" ")));
    return;
  }
  w.setObjectName(qn);
  childStyle(opt);
  w.setWordWrap(true);
  if (!opt.contains("staticbox"))
    w.setText(qn);
  if (opt.contains("left"))
    w.setAlignment(Qt::AlignVCenter|Qt::AlignLeft);
  else if (opt.contains("right"))
    w.setAlignment(Qt::AlignVCenter|Qt::AlignRight);
  else if (opt.contains("center"))
    w.setAlignment(Qt::AlignVCenter|Qt::AlignHCenter);
  if (opt.contains("sunken")) {
    w.setFrameStyle(QFrame::Sunken|QFrame::Panel);
    w.setMargin(4);
  } else if (opt.contains("raised")) {
    w.setFrameStyle(QFrame::Raised|QFrame::Panel);
    w.setMargin(4);
  } else if (opt.contains("panel")) {
    w.setFrameStyle(QFrame::Panel);
    w.setMargin(4);
  }
}

// ---------------------------------------------------------------------
String Static::get(String p,String v)
{
  QLabel *w=(QLabel*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("alignment")+"\012"+ "caption"+"\012"+ "text"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("caption"||p=="text"))
    r=Util.q2s(w.text());
  else if (p.equals("alignment")) {
    if ((w.alignment())&Qt::AlignRight)
      r=String("right");
    else if ((w.alignment())&Qt::AlignHCenter)
      r=String("center");
    else
      r=String("left");
  } else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Static::set(String p,String v)
{
  QLabel *w=(QLabel*) widget;
  String[] opt=Cmd.qsplit(v);
  if (p.equals("caption" || p=="text"))
    w.setText(Util.s2q(Util.remquotes(v)));
  else if (p.equals("alignment")) {
    if (opt.isEmpty()) {
      JConsoleApp.theWd.error("set alignment requires 1 argument: " + id + " " + p);
      return;
    }
    if (opt.at(0).equals("left"))
      w.setAlignment(Qt::AlignVCenter|Qt::AlignLeft);
    else if (opt.at(0).equals("right"))
      w.setAlignment(Qt::AlignVCenter|Qt::AlignRight);
    else if (opt.at(0).equals("center"))
      w.setAlignment(Qt::AlignVCenter|Qt::AlignHCenter);
    else {
      JConsoleApp.theWd.error("set alignment requires left, right or center: " + id + " " + p);
      return;
    }
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Static::state()
{
  return "";
}
