
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.snap;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.proj;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.recent;


String[] SnapTrees;

boolean snapfcopy(String from,String to);
boolean snapshot1(bool,String,String);
boolean ss_erase(String p,String s);
String[] ss_files(String d);
boolean ss_make(String from,String to);
boolean ss_match(String p, String q);

// ---------------------------------------------------------------------
boolean snapfcopy(String from,String to)
{
  QFile f(from);
  return f.copy(to);
}

// ---------------------------------------------------------------------
String snapgetpath(String p)
{
  String r = snappath(p);
  QDir d;
  QFile *f;
  if (!d.exists(r)) {
    d.mkpath(r);
    f = new QFile(r + "/dir.txt");
    cfwrite(f,p);
  }
  return r;
}

// ---------------------------------------------------------------------
String snappath(String p)
{
  return config.SnapPath.filePath(".snp/" + getsha1(p));
}

// ---------------------------------------------------------------------
boolean snaprmdir(String p)
{
  if (!matchhead(config.SnapPath.absolutePath(),p)) return false;
  return cfrmdir(p);
}

// ---------------------------------------------------------------------
void snapshot(boolean force, String path)
{
  if (config.Snapshots==0) return;
  snapshot1(force,ss_date(),path);
}

// ---------------------------------------------------------------------
boolean snapshot1(boolean force, String today, String path)
{
  String p=snapgetpath(path);
  if (p.size() == 0) return true;
  p += "/";
  String pfx = p + "s" + today;
  String seq = "001";
  QDir qdir(p);
  String[] d;
  String[] f;
  boolean donetoday=false;

  f.append("s*");
  d=qdir.entryList(f);

  if (d.size()) {
    d.sort();
    donetoday = d.last().mid(1,6) == today;
  }

// force snap
  if (force) {
    if (d.size()>0 && ss_match (p + d.last(),path)) {
      ss_info("Last snapshot matches current project.");
      return true;
    }
    if (donetoday) {
      String t(String("000%1" ).arg(1+d.last().right(3).toInt()));
      seq=t.right(3);
    }
    ss_make (path,pfx + seq);
    ss_info("New snapshot: " + cfsname(pfx + seq));

  }
// auto snap
  else {
    if (donetoday) return true;
    if (d.size()>0 && ss_match (p + d.last(),path)) return true;
    ss_make (path,pfx + seq);
  }

  for (int i=0; i<d.size()-config.Snapshots; i++)
    ss_erase(p,d.at(i));

  return true;
}

// ---------------------------------------------------------------------
void snapshot_tree(String folder)
{
  if (config.Snapshots == 0 || folder.size() == 0) return;
  if (SnapTrees.contains(folder)) return;
  String p=cpath("~" + folder);
  String[] folders=project_tree(p);
  String t=ss_date();
  for (int i=0; i<folders.size(); i++)
    snapshot1(false,t,p + "/" + folders.at(i));
  SnapTrees.append(folder);
}

// ---------------------------------------------------------------------
String ss_date()
{
  return QDate::currentDate().toString("yyMMdd");
}

// ---------------------------------------------------------------------
boolean ss_erase(String p,String s)
{
  return snaprmdir(p + s);
}

// ---------------------------------------------------------------------
String[] ss_files(String s)
{
  String[] y=cflist(s,"");

  if (config.Snapshotx.isEmpty()) return y;

  String[] x=cflist(s,config.Snapshotx);
  String[] r;
  foreach(String s,y)
    if(!x.contains(s))
      r << s;
  return r;
}

// ---------------------------------------------------------------------
void ss_info(String s)
{
  info("Snapshot",s);
}

// ---------------------------------------------------------------------
String[] ss_list(String p)
{
  QDir d(p);
  String[] t=d.entryList(String[]() << "s*");
  t.sort();
  return qslreverse(t);
}

// ---------------------------------------------------------------------
// ss_make - make a snapshot
boolean ss_make(String from, String to)
{
  from += "/";
  to += "/";

  if (!ss_mkdir(to)) return false;
  String[] f=ss_files(from);
  for (int i=0; i<f.size(); i++)
    snapfcopy(from+f.at(i),to+f.at(i));
  return true;
}

// ---------------------------------------------------------------------
boolean ss_match(String p, String q)
{
  String[] pf=ss_files(p);
  String[] qf=ss_files(q);
  if (pf.size() != qf.size()) return false;
  pf.sort();
  qf.sort();
  QFile *pn, *qn;
  boolean match;
  for (int i=0; i<pf.size(); i++) {
    pn=new QFile(p + "/" + pf.at(i));
    qn=new QFile(q + "/" + pf.at(i));
    match = (cfread(pn) == cfread(qn));
    delete pn;
    delete qn;
    if (!match) return false;
  }
  return true;
}

// ---------------------------------------------------------------------
boolean ss_mkdir(String s)
{
  QDir d(s);
  if (d.exists() || d.mkpath(s))
    return true;
  info("Snap","Unable to create snapshot directory: " + s);
  return false;
}
