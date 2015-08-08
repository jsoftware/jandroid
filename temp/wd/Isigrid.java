// Grid control

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.isigrid;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.../grid/qgrid;

// ---------------------------------------------------------------------
IsiGrid::IsiGrid(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="isigrid";
  String qn=n;
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"cube")) return;
  if (opt.length==0)
    style="";
  else
    style=opt[0];
  widget=new QGrid(style,this,p);
  childStyle(opt);
}

// ---------------------------------------------------------------------
String IsiGrid::get(String p,String v)
{
  return super.get(p,v);
}

// ---------------------------------------------------------------------
void IsiGrid::set(String p,String v)
{
  ((QGrid *)widget).set(p,v);
}

// ---------------------------------------------------------------------
String IsiGrid::state()
{
  return ((QGrid *)widget).state(event);
}
