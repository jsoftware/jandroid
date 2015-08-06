

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.slider;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// optional parms are:
// "v" - vertical
// tick placement
// minimum
// single step
// page step
// maximum
// position

// ---------------------------------------------------------------------
Slider::Slider(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="slider";
  QSlider *w=new QSlider(Qt::Horizontal);
  String qn=Util.s2q(n);
  widget=(View*) w;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"v")) return;
  w.setObjectName(qn);
  childStyle(opt);

  int i=0;
  if ((i<opt.size()) && (opt.at(i).equals("v"))) {
    w.setOrientation(Qt::Vertical);
    i++;
  }
  if (i<opt.size()) {
    w.setTickPosition((QSlider::TickPosition)Util.c_strtoi(Util.q2s(opt.at(i))));
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
void Slider::valueChanged()
{
  event="changed";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String Slider::get(String p,String v)
{
  QSlider *w=(QSlider*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("max")+"\012"+ "min"+"\012"+ "page"+"\012"+ "pos"+"\012"+ "step"+"\012"+ "tic"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("min"))
    r=Util.i2s(w.minimum());
  else if (p.equals("max"))
    r=Util.i2s(w.maximum());
  else if (p.equals("tic"))
    r=Util.i2s(w.tickPosition());
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
void Slider::set(String p,String v)
{
  QSlider *w=(QSlider*) widget;
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
  else if (cmd.equals("tic"))
    w.setTickPosition((QSlider::TickPosition)(Util.c_strtoi(Util.q2s(arg.at(0)))));
  else if (cmd.equals("step"))
    w.setSingleStep(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("page"))
    w.setPageStep(Util.c_strtoi(Util.q2s(arg.at(0))));
  else if (cmd.equals("pos"|| cmd=="value"))
    w.setSliderPosition(Util.c_strtoi(v));
  else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Slider::state()
{
  QSlider *w=(QSlider*) widget;
  return spair(id,Util.i2s(w.sliderPosition()));
}
