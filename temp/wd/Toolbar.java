

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.toolbar;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

#ifdef QT_OS_ANDROID
extern float DM_density;
#endif

// ---------------------------------------------------------------------
ToolBar::ToolBar(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="toolbar";

  QToolBar *w=new QToolBar;
  widget=(View*) w;
  String qn=n;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"vertical")) return;
  childStyle(opt);

  if (opt.contains("vertical"))
    w.setOrientation(Qt::Vertical);
  if (opt.length) {
    String t=opt[0];
    if (qshasonly(t,"0123456789x")) {
      String[] sizes=t.split('x');
      if (sizes.length()<2) {
        JConsoleApp.theWd.error("invalid icon width, height: " + t);
        return;
      }
      w.setIconSize(QSize(Util.c_strtoi(sizes[0]),Util.c_strtoi(sizes[1])));
    }
  }

  connect(w,SIGNAL(actionTriggered(QAction *)),
          this,SLOT(actionTriggered(QAction *)));
}

// ---------------------------------------------------------------------
void ToolBar::actionTriggered(QAction *a)
{
  event="button";
  eid=a.objectName();
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void ToolBar::makeact(String[] opt)
{
  if (opt.length<3) {
    JConsoleApp.theWd.error("toolbar add needs id, text, image: " + id);
    return;
  }
  QToolBar *w=(QToolBar *)widget;
  String id=opt[0];
  String text=opt[1];
  String iconFile=Util.remquotes(opt[2]);
  QIcon image;
  int spi;
  if (iconFile.substring(0,8).equals("qstyle::") && -1!=(spi=wdstandardicon(iconFile)))
    image=w.style().standardIcon((QStyle::StandardPixmap)spi);
  else
    image=QIcon(iconFile);
  if (image.isNull()) {
    JConsoleApp.theWd.error("invalid icon image: " + opt[2]);
    return;
  }
  QAction *a=w.addAction(image,text);
  acts.append(a);
}

// ---------------------------------------------------------------------
String ToolBar::get(String p,String v)
{
  return super.get(p,v);
}

// ---------------------------------------------------------------------
void ToolBar::set(String p,String v)
{

  QToolBar *w=(QToolBar *)widget;
  String[] opt=Cmd.qsplit(v);
  if (p.equals("add"))
    makeact(opt);
  else if (p.equals("addsep"))
    w.addSeparator();
  else if (p.equals("checkable"))
    setbutton(p,opt);
  else if (p.equals("checked"))
    setbutton(p,opt);
  else if (p.equals("enable")) {
    if (opt.isEmpty()) super.set(p,v);
    else if (1==opt.length && (!opt[0].isEmpty()) && opt[0][0].isNumber()) super.set(p,v);
    else setbutton(p,opt);
  } else
    super.set(p,v);
}

// ---------------------------------------------------------------------
QAction * ToolBar::getaction(String id)
{
  for (int i=0; i<acts.length(); i++)
    if (acts[i].objectName()==id) return acts[i];
  return 0;
}

// ---------------------------------------------------------------------
void ToolBar::setbutton(String p, String[] opt)
{
  boolean n=true;
  if (opt.isEmpty()) {
    JConsoleApp.theWd.error("set toolbar requires button_id: " + p);
    return;
  } else if (1<opt.length)
    n=!!Util.c_strtoi(opt[1]);
  String btnid= opt[0];
  QAction * a=getaction(btnid);
  if (!a) {
    JConsoleApp.theWd.error("set toolbar cannot find button_id: " + p + " " + btnid);
    return;
  }
  if (p.equals("checkable"))
    a.setCheckable(n);
  else if (p.equals("checked"))
    a.setChecked(n);
  else if (p.equals("enable"))
    a.setEnabled(n);
  else {
    JConsoleApp.theWd.error("set toolbar attribute error: " + p);
    return;
  }
}

// ---------------------------------------------------------------------
String ToolBar::state()
{
  return "";
}
