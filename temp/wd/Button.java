

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.button;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Button::Button(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="button";
  QPushButton *w=new QPushButton;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"default")) return;
  w.setObjectName(qn);
  childStyle(opt);
  w.setText(qn);
  iconFile="";
  if (opt.contains("default"))
    w.setDefault(true);
  connect(w,SIGNAL(clicked()),f.signalMapper,SLOT(map()));
  f.signalMapper.setMapping(w,(View*)this);
}

// ---------------------------------------------------------------------
String Button::get(String p,String v)
{
  QPushButton *w=(QPushButton*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("caption")+"\012"+ "icon"+"\012"+ "text"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("caption"||p=="text"))
    r=Util.q2s(w.text());
  else if (p.equals("icon"))
    r=iconFile;
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Button::set(String p,String v)
{
  QPushButton *w=(QPushButton*) widget;
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
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Button::state()
{
  return "";
}
