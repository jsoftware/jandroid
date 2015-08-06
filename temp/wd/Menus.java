

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.menus;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;

// ---------------------------------------------------------------------
Menus::Menus(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="menu";
  curMenu=0;
  id="";
  widget=(View*) new QMenuBar(f);
  widget.setSizePolicy (QSizePolicy::Ignored, QSizePolicy::Maximum);
}

// ---------------------------------------------------------------------
QAction *Menus::makeact(String id, String p)
{
  String[] s=Cmd.qsplit(p);
  String text=s.value(0);
  String shortcut=s.value(1);
  QAction *r = new QAction(text,widget);
  String name=Util.s2q(id);
  r.setObjectName(name);
  r.setMenuRole(QAction::NoRole);
  if (shortcut.size())
    r.setShortcut(shortcut);
  items[name]=r;
  return r;
}

// ---------------------------------------------------------------------
int Menus::menu(String c, String p)
{
  if (curMenu==0)
    return 1;
  curMenu.addAction(makeact(c,p));
  return 0;
}

// ---------------------------------------------------------------------
int Menus::menupop(String c)
{
  String s=Util.s2q(c);
  if (curMenu==0) {
    curMenu=((QMenuBar*) widget).addMenu(s);
    connect(curMenu,SIGNAL(triggered(QAction *)),
            this,SLOT(menu_triggered(QAction *)));
  } else
    curMenu=curMenu.addMenu(s);
  curMenu.menuAction().setMenuRole(QAction::NoRole);
  menus.append(curMenu);
  return 0;
}

// ---------------------------------------------------------------------
int Menus::menupopz()
{
  if (menus.isEmpty()) return 0;
  menus.removeLast();
  if (menus.isEmpty())
    curMenu=0;
  else
    curMenu=menus.last();
  return 0;
}

// ---------------------------------------------------------------------
int Menus::menusep()
{
  if (curMenu==0)
    return 1;
  curMenu.addSeparator();
  return 0;
}

// ---------------------------------------------------------------------
void Menus::menu_triggered(QAction *a)
{
  event="button";
  eid=Util.q2s(a.objectName());
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String Menus::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void Menus::set(String p,String v)
{
  String id,m,parm,t;
  String[] sel,opt;

  sel=Cmd.qsplit(p);
  if (sel.size()!=2) {
    JConsoleApp.theWd.error("invalid menu command: " + p + " " + v);
    return;
  }

  id=sel.at(0);
  m=sel.at(1);
  t=Util.s2q(v);

  if (t.size()==0) {
    t=m;
    m="checked";
  }

  if (m.equals("checked" || m=="value")) {
    QAction *a=items.value(id);
    a.setCheckable(true);
    a.setChecked(t.equals("1"));
  } else if (m.equals("enable")) {
    items.value(id).setEnabled(t.equals("1"));
  } else
    Child::set(p,v);
}

// ---------------------------------------------------------------------
String Menus::state()
{
  return "";
}

