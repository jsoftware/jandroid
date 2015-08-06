

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.checkbox;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
CheckBox::CheckBox(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="checkbox";
  QCheckBox *w=new QCheckBox;
#ifdef QT_OS_ANDROID
  w.setStyleSheet(checkboxstyle(20*DM_density));
#endif
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);
  w.setText(qn);
  iconFile="";
  connect(w,SIGNAL(stateChanged(int)),
          this,SLOT(stateChanged()));
}

// ---------------------------------------------------------------------
String CheckBox::get(String p,String v)
{
  QCheckBox *w=(QCheckBox*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("caption")+"\012"+ "icon"+"\012"+ "text"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("caption"||p=="text"))
    r=Util.q2s(w.text());
  else if (p.equals("icon"))
    r=iconFile;
  else if (p.equals("value"))
    r=w.isChecked()?(String)"1":(String)"0";
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void CheckBox::set(String p,String v)
{
  QCheckBox *w=(QCheckBox*) widget;
  if (p.equals("caption" || p=="text"))
    w.setText(Util.s2q(Util.remquotes(v)));
  else if (p.equals("icon")) {
    String[] qs=Cmd.qsplit(v);
    String[] sizes;
    if (!qs.size()) {
      JConsoleApp.theWd.error("missing parameters: " + p + " " + v);
      return;
    }
    if (qs.size()==2) {
      String t=qs.at(1);
      if (qshasonly(t,"0123456789x")) {
        sizes=t.split('x');
        if (sizes.size()<2) {
          JConsoleApp.theWd.error("invalid icon width, height: " + p + " " + v);
          return;
        }
      } else {
        JConsoleApp.theWd.error("invalid icon width, height: " + p + " " + v);
        return;
      }
    }  else if (qs.size()>2) {
      JConsoleApp.theWd.error("extra parameters: " + p + " " + v);
      return;
    }
    iconFile=Util.remquotes(Util.q2s(qs.at(0)));
    int spi;
    if (iconFile.substring(0,8).equals("qstyle::") && -1!=(spi=wdstandardicon(iconFile)))
      w.setIcon(w.style().standardIcon((QStyle::StandardPixmap)spi));
    else
      w.setIcon(QIcon(Util.s2q(iconFile)));
    if (qs.size()==2)
      w.setIconSize(QSize(Util.c_strtoi(Util.q2s(sizes.at(0))),Util.c_strtoi(Util.q2s(sizes.at(1)))));
  } else if (p.equals("value"))
    w.setChecked(Util.remquotes(v)!="0");
  else Child::set(p,v);
}

// ---------------------------------------------------------------------
String CheckBox::state()
{
  QCheckBox *w=(QCheckBox*) widget;
  return spair(id,w.isChecked()?(String)"1":(String)"0");
}

// ---------------------------------------------------------------------
void CheckBox::stateChanged()
{
  event="button";
  pform.signalevent(this);
}
