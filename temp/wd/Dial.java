

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.dial;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// optional parms are:
// "v" - notchesvisible
// "w" - wrapping
// minimum
// single step
// page step
// maximum
// position

// ---------------------------------------------------------------------
Dial::Dial(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="dial";
  QDial *w=new QDial();
  String qn=Util.s2q(n);
  widget=(View*) w;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"v w")) return;
  w.setObjectName(qn);
  childStyle(opt);

  int i=0;
  if ((i<opt.size()) && (opt.at(i).equals("w"||opt.at(i)=="v"))) {
    if (opt.at(i).equals("w"))
      w.setWrapping(true);
    else
      w.setNotchesVisible(true);
    i++;
  }
  if ((i<opt.size()) && (opt.at(i).equals("w"||opt.at(i)=="v"))) {
    if (opt.at(i).equals("w"))
      w.setWrapping(true);
    else
      w.setNotchesVisible(true);
    i++;
  }
  if (i<opt.size()) {
    w.setMinimum(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setSingleStep(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setPageStep(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setMaximum(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setSliderPosition(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  connect(w,SIGNAL(valueChanged(int)),
          this,SLOT(valueChanged()));
}

// ---------------------------------------------------------------------
void Dial::valueChanged()
{
  event="changed";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String Dial::get(String p,String v)
{
  QDial *w=(QDial*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("max")+"\012"+ "min"+"\012"+ "notchesvisible"+"\012"+ "page"+"\012"+ "pos"+"\012"+ "step"+"\012"+ "wrap"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("min"))
    r=Util.i2s(w.minimum());
  else if (p.equals("max"))
    r=Util.i2s(w.maximum());
  else if (p.equals("wrap"))
    r=Util.i2s(w.wrapping());
  else if (p.equals("notchesvisible"))
    r=Util.i2s(w.notchesVisible());
  else if (p.equals("step"))
    r=Util.i2s(w.singleStep());
  else if (p.equals("page"))
    r=Util.i2s(w.pageStep());
  else if (p.equals("pos"|| p=="value"))
    r=Util.i2s(w.sliderPosition());
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Dial::set(String p,String v)
{
  QDial *w=(QDial*) widget;
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
  else if (cmd.equals("wrap"))
    w.setWrapping(arg.at(0)!="0");
  else if (cmd.equals("notchesvisible"))
    w.setNotchesVisible(arg.at(0)!="0");
  else if (cmd.equals("step"))
    w.setSingleStep(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("page"))
    w.setPageStep(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("pos"|| cmd=="value"))
    w.setSliderPosition(Util.c_strtoi(v));
  else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Dial::state()
{
  QDial *w=(QDial*) widget;
  return spair(id,Util.i2s(w.sliderPosition()));
}
