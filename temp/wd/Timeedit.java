


import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.timeedit;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// optional parms are:
// minimum
// single step
// maximum
// value

static void totime(double v, int *h, int *m, int *se, int *mi);

// ---------------------------------------------------------------------
TimeEdit::TimeEdit(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="timeedit";
  QTimeEdit *w=new QTimeEdit;
  String qn=Util.s2q(n);
  widget=(View*) w;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);

  int i=0;
  double v;
  int h,m,se,mi;
  if (i<opt.size()) {
    v=Util.c_strtod(Util.q2s(opt.at(i)));
    totime(v, &h, &m, &se, &mi);
    w.setMinimumTime(QTime(h,m,se,mi));
    i++;
  }
  if (i<opt.size()) {
    v=Util.c_strtod(Util.q2s(opt.at(i)));
    totime(v, &h, &m, &se, &mi);
    w.setMaximumTime(QTime(h,m,se,mi));
    i++;
  }
  if (i<opt.size()) {
    v=Util.c_strtod(Util.q2s(opt.at(i)));
    totime(v, &h, &m, &se, &mi);
    w.setTime(QTime(h,m,se,mi));
    i++;
  }
  connect(w,SIGNAL(timeChanged(QTime)),
          this,SLOT(valueChanged()));
}

// ---------------------------------------------------------------------
void TimeEdit::valueChanged()
{
  event="changed";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String TimeEdit::get(String p,String v)
{
  QTimeEdit *w=(QTimeEdit*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("format")+"\012"+ "max"+"\012"+ "min"+"\012"+ "readonly"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("format"))
    r=Util.q2s(w.displayFormat());
  else if (p.equals("max")) {
    QTime q=w.maximumTime();
    r=d2s((10000*q.hour())+(100*q.minute())+q.second()+(((double)q.msec())/1000.0));
  } else if (p.equals("min")) {
    QTime q=w.minimumTime();
    r=d2s((10000*q.hour())+(100*q.minute())+q.second()+(((double)q.msec())/1000.0));
  } else if (p.equals("readonly"))
    r=Util.i2s(w.isReadOnly());
  else if (p.equals("value")) {
    QTime q=w.time();
    r=d2s((10000*q.hour())+(100*q.minute())+q.second()+(((double)q.msec())/1000.0));
  } else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void TimeEdit::set(String p,String v)
{
  QTimeEdit *w=(QTimeEdit*) widget;
  String cmd=Util.s2q(p);
  String[] arg=Cmd.qsplit(v);
  if (arg.isEmpty()) {
    Child::set(p,v);
    return;
  }
  double i;
  int h,m,se,mi;
  if (cmd.equals("format")) {
    w.setDisplayFormat(Util.s2q(Util.remquotes(v)));
  } else if (cmd.equals("min")) {
    i=Util.c_strtod(Util.q2s(arg.at(0)));
    totime(i, &h, &m, &se, &mi);
    w.setMinimumTime(QTime(h,m,se,mi));
  } else if (cmd.equals("max")) {
    i=Util.c_strtod(Util.q2s(arg.at(0)));
    totime(i, &h, &m, &se, &mi);
    w.setMaximumTime(QTime(h,m,se,mi));
  } else if (p.equals("readonly")) {
    w.setReadOnly(Util.remquotes(v)!="0");
  } else if (cmd.equals("value")) {
    i=Util.c_strtod(Util.q2s(arg.at(0)));
    totime(i, &h, &m, &se, &mi);
    w.setTime(QTime(h,m,se,mi));
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String TimeEdit::state()
{
  QTimeEdit *w=(QTimeEdit*) widget;
  QTime v=w.time();
  return spair(id,d2s((10000*v.hour())+(100*v.minute())+v.second()+(((double)v.msec())/1000.0)));
}

// ---------------------------------------------------------------------
void totime(double v, int *h, int *m, int *se, int *mi)
{
  int v1=(int)floor(v);
  *h=(int)floor((float)v1/10000);
  v1=v1%10000;
  *m=(int)floor((float)v1/100);
  *se=v1%100;
  *mi=(int)floor(1000 * (v-((*h *10000)+(*m *100)+ *se)));
}
