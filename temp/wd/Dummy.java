
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.dummy;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Dummy::Dummy(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="dummy";
  widget=0;
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
  childStyle(opt);
}

// ---------------------------------------------------------------------
String Dummy::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void Dummy::set(String p,String v)
{
  Child::set(p,v);
}

// ---------------------------------------------------------------------
String Dummy::state()
{
  return "";
}
