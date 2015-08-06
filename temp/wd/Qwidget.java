// View control

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.qwidget;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
QWidgex::QWidgex(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="qwidget";
  widget=new View;
}

// ---------------------------------------------------------------------
String QWidgex::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void QWidgex::set(String p,String v)
{
  Child::set(p,v);
}

// ---------------------------------------------------------------------
String QWidgex::state()
{
  return "";
}
