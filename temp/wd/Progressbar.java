

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.progressbar;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
ProgressBar::ProgressBar(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="progressbar";
  QProgressBar *w=new QProgressBar;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);

  int i=0;
  if ((i<opt.size()) && (opt.at(i).equals("v"))) {
    i++;
    w.setOrientation(Qt::Vertical);
  }
  if (i<opt.size()) {
    w.setMinimum(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setMaximum(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setValue(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
}

// ---------------------------------------------------------------------
String ProgressBar::get(String p,String v)
{
  QProgressBar *w=(QProgressBar*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("max")+"\012"+ "min"+"\012"+ "pos"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("min"))
    r=Util.i2s(w.minimum());
  else if (p.equals("max"))
    r=Util.i2s(w.maximum());
  else if (p.equals("pos"|| p=="value"))
    r=Util.i2s(w.value());
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void ProgressBar::set(String p,String v)
{
  QProgressBar *w=(QProgressBar*) widget;
  String cmd=Util.s2q(p);
  String[] arg=Cmd.qsplit(v);
  if (arg.isEmpty()) {
    Child::set(p,v);
    return;
  }
  if (cmd.equals("min"))
    w.setMinimum(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("max"))
    w.setMaximum(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("pos" || cmd=="value"))
    w.setValue(Util.c_strtoi((v)));
  else Child::set(p,v);
}

// ---------------------------------------------------------------------
String ProgressBar::state()
{
  QProgressBar *w=(QProgressBar*) widget;
  return spair(id,Util.i2s(w.value()));
}
