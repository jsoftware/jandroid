

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.spinbox;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// optional parms are:
// minimum
// single step
// maximum
// value

// ---------------------------------------------------------------------
SpinBox::SpinBox(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="spinbox";
  QSpinBox *w=new QSpinBox;
  String qn=Util.s2q(n);
  widget=(View*) w;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);
  w.setLocale(QLocale::C);

  int i=0;
  if (i<opt.size()) {
    w.setMinimum(Util.c_strtoi(Util.q2s(opt.at(i))));
    i++;
  }
  if (i<opt.size()) {
    w.setSingleStep(Util.c_strtoi(Util.q2s(opt.at(i))));
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
  connect(w,SIGNAL(valueChanged(int)),
          this,SLOT(valueChanged()));
}

// ---------------------------------------------------------------------
void SpinBox::valueChanged()
{
  event="changed";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String SpinBox::get(String p,String v)
{
  QSpinBox *w=(QSpinBox*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("max")+"\012"+ "min"+"\012"+ "readonly"+"\012"+ "step"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("min"))
    r=Util.i2s(w.minimum());
  else if (p.equals("max"))
    r=Util.i2s(w.maximum());
  else if (p.equals("step"))
    r=Util.i2s(w.singleStep());
  else if (p.equals("readonly"))
    r=Util.i2s(w.isReadOnly());
  else if (p.equals("value"))
    r=Util.i2s(w.value());
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void SpinBox::set(String p,String v)
{
  QSpinBox *w=(QSpinBox*) widget;
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
  else if (p.equals("readonly"))
    w.setReadOnly(Util.remquotes(v)!="0");
  else if (cmd.equals("step"))
    w.setSingleStep(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("value"))
    w.setValue(Util.c_strtoi(v));
  else Child::set(p,v);
}

// ---------------------------------------------------------------------
String SpinBox::state()
{
  QSpinBox *w=(QSpinBox*) widget;
  return spair(id,Util.i2s(w.value()));
}
