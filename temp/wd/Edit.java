

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.edit;
import com.jsoftware.jn.wd.lineedit;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Edit::Edit(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="edit";
  LineEdit *w=new LineEdit(this);
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"password readonly left right center")) return;
  if (1<(opt.contains("left")?1:0) + (opt.contains("right")?1:0) + (opt.contains("center")?1:0)) {
    JConsoleApp.theWd.error("conflicting child style: " + n + " " + Util.q2s(opt.join(" ")));
    return;
  }
  w.setObjectName(qn);
  focusSelect=false;
  childStyle(opt);

  if (opt.contains("password"))
    w.setEchoMode(LineEdit::Password);

  if (opt.contains("readonly"))
    w.setReadOnly(true);

  if (opt.contains("left"))
    w.setAlignment(Qt::AlignVCenter|Qt::AlignLeft);
  else if (opt.contains("right"))
    w.setAlignment(Qt::AlignVCenter|Qt::AlignRight);
  else if (opt.contains("center"))
    w.setAlignment(Qt::AlignVCenter|Qt::AlignHCenter);

  connect(w,SIGNAL(returnPressed()),
          this,SLOT(returnPressed()));
}

// ---------------------------------------------------------------------
void Edit::returnPressed()
{
  event="button";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String Edit::get(String p,String v)
{
  LineEdit *w=(LineEdit*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("alignment")+"\012"+ "focusselect"+"\012"+ "inputmask"+"\012"+ "limit"+"\012"+ "readonly"+"\012"+ "select"+"\012"+ "text"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("alignment")) {
    if ((w.alignment())&Qt::AlignRight)
      r=String("right");
    else if ((w.alignment())&Qt::AlignHCenter)
      r=String("center");
    else
      r=String("left");
  } else if (p.equals("focusselect"))
    r=Util.i2s(focusSelect);
  else if (p.equals("inputmask"))
    r=Util.q2s(w.inputMask());
  else if (p.equals("limit"))
    r=Util.i2s(w.maxLength());
  else if (p.equals("readonly"))
    r=Util.i2s(w.isReadOnly());
  else if (p.equals("text"))
    r=Util.q2s(w.text());
  else if (p.equals("select")) {
    int b,e;
    b=w.selectionStart();
    if (b<0)
      b=e=w.cursorPosition();
    else
      e=b+w.selectedText().size();
    r=Util.i2s(b)+" "+Util.i2s(e);
  } else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Edit::set(String p,String v)
{
  LineEdit *w = (LineEdit *)widget;
  String[] opt=Cmd.qsplit(v);

  if (p.equals("text"))
    w.setText(Util.s2q(Util.remquotes(v)));
  else if (p.equals("cursorposition")) {
    if (opt.isEmpty()) {
      JConsoleApp.theWd.error("set cursorposition requires 1 number: " + id + " " + p);
      return;
    }
    int p=Util.c_strtoi(Util.q2s(opt.at(0)));
    p=qMax(0,qMin(p,w.text().length()));
    w.setCursorPosition(p);
  } else if (p.equals("limit")) {
    if (opt.isEmpty()) {
      JConsoleApp.theWd.error("set limit requires 1 number: " + id + " " + p);
      return;
    }
    w.setMaxLength(Util.c_strtoi(Util.q2s(opt.at(0))));
  } else if (p.equals("focusselect")) {
    focusSelect=Util.remquotes(v)!="0";
  } else if (p.equals("focus")) {
    w.setFocus();
    if (focusSelect) w.selectAll();
  } else if (p.equals("readonly")) {
    w.setReadOnly(Util.remquotes(v)!="0");
  } else if (p.equals("select")) {
    w.selectAll();
  } else if (p.equals("alignment")) {
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
  } else if (p.equals("inputmask")) {
// see http://qt-project.org/doc/qt-4.8/qlineedit.html#inputMask-prop
    if (opt.isEmpty())
      w.setInputMask("");
    else
      w.setInputMask(opt.at(0));
  } else if (p.equals("intvalidator")) {
    if (opt.isEmpty())
      w.setValidator(0);
    else if (2>opt.size()) {
      JConsoleApp.theWd.error("set intvalidator requires 2 numbers: " + id + " " + p);
      return;
    } else {
      w.setLocale(QLocale::C);
      QIntValidator *validator=new QIntValidator(Util.c_strtoi(Util.q2s(opt.at(0))),Util.c_strtoi(Util.q2s(opt.at(1))),w);
      validator.setLocale(QLocale::C);
      w.setValidator(validator);
    }
  } else if (p.equals("doublevalidator")) {
    if (opt.isEmpty())
      w.setValidator(0);
    else if (3>opt.size()) {
      JConsoleApp.theWd.error("set doublevalidator requires 3 numbers: " + id + " " + p);
      return;
    } else {
      w.setLocale(QLocale::C);
      QDoubleValidator *validator=new QDoubleValidator(Util.c_strtod(Util.q2s(opt.at(0))),Util.c_strtod(Util.q2s(opt.at(1))),Util.c_strtoi(Util.q2s(opt.at(2))),w);
      validator.setLocale(QLocale::C);
      w.setValidator(validator);
    }
  } else if (p.equals("regexpvalidator")) {
// see http://qt-project.org/doc/qt-4.8/qregexp.html
    if (opt.isEmpty())
      w.setValidator(0);
    else {
      QRegExp rx(opt.at(0));
      w.setValidator(new QRegExpValidator(rx,w));
    }
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Edit::state()
{
  LineEdit *w=(LineEdit*) widget;
  int b,e;
  b=w.selectionStart();
  if (b<0)
    b=e=w.cursorPosition();
  else
    e=b+w.selectedText().size();
  String r;
  r+=spair(id,Util.q2s(w.text()));
  r+=spair(id+"_select",Util.i2s(b)+" "+Util.i2s(e));
  return r;
}
