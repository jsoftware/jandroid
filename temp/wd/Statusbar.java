

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.statusbar;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
StatusBar::StatusBar(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="statusbar";
  QStatusBar *w=new QStatusBar;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);
}

// ---------------------------------------------------------------------
String StatusBar::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void StatusBar::set(String p,String v)
{
  QStatusBar *w=(QStatusBar*) widget;
  String[] arg=Cmd.qsplit(v);

  int m=0;
  int n=arg.size();
  String s;

  if (n>0)
    s=arg.at(0);
  if (n>1)
    m=Util.c_strtoi(Util.q2s(arg.at(1)));

  if (p.equals("show") && n)
    w.showMessage(s,m);
  else if (p.equals("clear"))
    w.clearMessage();

  else if (p.equals("addlabel")) {
    QLabel *lab=new QLabel();
    labels.append(s);
    labelw.append(lab);
    w.addWidget(lab,0);
  } else if (p.equals("addlabelp")) {
    QLabel *lab=new QLabel();
    labels.append(s);
    labelw.append(lab);
    w.addPermanentWidget(lab,0);
  } else if (p.equals("setlabel")) {
    if (n<2) {
      JConsoleApp.theWd.error("setlabel needs label id and text: " + id + " " + p + " " + v);
      return;
    }
    m=labels.indexOf(s);
    if (m<0) {
      JConsoleApp.theWd.error("label not found: " + id + " " + p + " " + v);
      return;
    }
    labelw.at(m).setText(arg.at(1));
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String StatusBar::state()
{
  return "";
}
