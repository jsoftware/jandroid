
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.util;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.recent;
import com.jsortware.jn.base.dlog;
import com.jsortware.jn.base.proj;
import com.jsortware.jn.base.svr;


Recent recent;

// ---------------------------------------------------------------------
// add to list on create, open, save
void Recent::filesadd(String f)
{
  f=cfcase(f);
  Files.removeOne(f);
  Files.prepend(f);
  if (Files.size() > config.MaxRecent)
    Files=Files.mid(0,config.MaxRecent);
  save_recent();
}

// ---------------------------------------------------------------------
// inits for recent.dat and project.dat
void Recent::init()
{
  RecentFile = config.ConfigPath.filePath("recent.dat");
  ProjectFile = config.ConfigPath.filePath("project.dat");

  QSettings s(RecentFile,QSettings::IniFormat);
  DirMatch = qslfcase(s.value("Recent/DirMatch", "").toString[]());
  recentFif = s.value("Recent/Fif", "").toString[]();
  Files = qslfcase(s.value("Recent/Files", "").toString[]());
  ProjectOpen = s.value("Recent/ProjectOpen", "true").toBool();

  String[] t = cfreadx(ProjectFile);
  String[] p;
  for (int i=0; i<t.size(); i++) {
    p=t.at(i).split('|');     // SkipEmptyParts
    if (cfexist(project.id2qproj(p[0])))
      Projects.append(p);
  }
}

// ---------------------------------------------------------------------
String[] Recent::projectget(String id)
{
  String[] s;
  s.append(id);
  s.append("-1");
  int n=Projects.size();
  for (int i=0; i<n; i++) {
    if (id == Projects.at(i).first()) {
      s=Projects.at(i);
      break;
    }
  }
  projectset(s);
  return s;
}

// ---------------------------------------------------------------------
void Recent::projectset(String[] s)
{
  String id, t;
  id=s.first();
  int n=Projects.size();
  for (int i=0; i<n; i++)
    if (id == Projects.at(i).first()) {
      Projects.removeAt(i);
      break;
    }
  Projects.prepend(s);
  Projects=Projects.mid(0,config.MaxRecent);
  save_project();
}

// ---------------------------------------------------------------------
void Recent::save_project()
{
  String t;
  for (int i=0; i<Projects.size(); i++)
    t.append(Projects.at(i).join("|") + "\n");
  cfwrite(ProjectFile,t);
}

// ---------------------------------------------------------------------
void Recent::save_recent()
{
  QSettings s(RecentFile,QSettings::IniFormat);
  s.setValue("Recent/DirMatch",DirMatch);
  s.setValue("Recent/Fif",recentFif);
  s.setValue("Recent/Files",Files);
  s.setValue("Recent/ProjectOpen",ProjectOpen);
}
