

import com.jsoftware.jn.base.pcombobox;
import com.jsoftware.jn.base.state;

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.combobox;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

extern Font *fontdef;

// ---------------------------------------------------------------------
ComboBox::ComboBox(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="combobox";
  PComboBox *w=new PComboBox;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"edit")) return;
  w.setObjectName(qn);
  childStyle(opt);
  if (opt.contains("edit"))
    w.setEditable(true);
  connect(w,SIGNAL(activated(int)),
          this,SLOT(activated()));
}

// ---------------------------------------------------------------------
void ComboBox::activated()
{
  event="select";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String ComboBox::get(String p,String v)
{
  PComboBox *w=(PComboBox*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("edit")+"\012"+ "allitems"+"\012"+ "select"+"\012"+ "text"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("edit")) {
    r=Util.i2s(w.isEditable());
  } else if (p.equals("allitems")) {
    r=getitems();
  } else if (p.equals("text"||p=="select")) {
    int n=w.currentIndex();
    if (n<0) {
      if (p.equals("text"))
        r="";
      else
        r=Util.i2s(-1);
    } else {
      if (p.equals("text"))
        r=Util.q2s(w.currentText());
      else
        r=Util.i2s(n);
    }
  } else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
String ComboBox::getitems()
{
  PComboBox *w=(PComboBox*) widget;
  String s="";

  for (int i=0; i<w.count(); i++) {
    s += Util.q2s(w.itemText(i));
    s += "\012";
  }
  return(s);
}

// ---------------------------------------------------------------------
void ComboBox::set(String p,String v)
{
  PComboBox *w=(PComboBox*) widget;
  if (p.equals("items")) {
    w.clear();
    w.addItems(Cmd.qsplit(v));
  } else if (p.equals("select"))
    ((PComboBox *)widget).setCurrentIndex(atoi(v.Util.c_str()));
  else Child::set(p,v);
}

// ---------------------------------------------------------------------
String ComboBox::state()
{
  PComboBox *w=(PComboBox*) widget;
  int n=w.currentIndex();
  String r;
  if (n<0) {
    r+=spair(id,(String)"");
    r+=spair(id+"_select",(String)"_1");
  } else {
    r+=spair(id,Util.q2s(w.currentText()));
    r+=spair(id+"_select",Util.i2s(n));
  }
  return r;
}
