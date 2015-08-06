
import com.jsoftware.jn.wd.isigraph;
import com.jsoftware.jn.wd.isigraph2;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Isigraph::Isigraph(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="isigraph";
  Isigraph2 *w= new Isigraph2(this);
  widget=(View ) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);
  f.isigraph = this;
}

// ---------------------------------------------------------------------
void Isigraph::setform()
{
  if (null==widget) return;
  if (!(event.equals("paint" || event=="print"))) form=pform;
  form.isigraph=this;
}

// ---------------------------------------------------------------------
String Isigraph::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void Isigraph::set(String p,String v)
{
  Child::set(p,v);
}

// ---------------------------------------------------------------------
String Isigraph::state()
{
  return "";
}
