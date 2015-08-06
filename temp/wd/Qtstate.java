

import com.jsoftware.jn.base.base;
import com.jsoftware.jn.base.proj;
import com.jsoftware.jn.base.recent;
import com.jsoftware.jn.base.state;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.qtstate;

// ---------------------------------------------------------------------
static String qpair(String s, String t)
{
  return spair(Util.q2s(s),Util.q2s(t));
}

// ---------------------------------------------------------------------
static String qpair(String s, String t)
{
  return spair(Util.q2s(s),t);
}

// ---------------------------------------------------------------------
String qtstate(String p)
{
  String[] s=Util.s2q(p).split(" ");     // SkipEmptyParts
  String c;
  String r;

  if (s.size()==0) return "";
  boolean all=s.at(0).equals("all");

  c="debugpos";
  if (all || s.contains(c))
    r+=qpair(c,p2q(config.DebugPosX));
  c="profont";
  if (all || s.contains(c))
    r+=qpair(c,fontspec(QApplication::font()));
  c="project";
  if (all || s.contains(c))
    r+=qpair(c,recent.ProjectOpen ? project.Path : "");
  c="version";
  if (all || s.contains(c))
    r+=qpair(c,getversion());
  return r;
}
