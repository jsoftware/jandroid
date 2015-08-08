

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.proj;
import com.jsortware.jn.base.svr;
import com.jsortware.jn.base.snap;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.recent;


Project project;

String[] getfolderdefs(String d);
void project_init();
void setfolder(String s);

// ---------------------------------------------------------------------
boolean Project::closeOK()
{
  return true;
}

// ---------------------------------------------------------------------
void Project::close()
{
  Id.clear();
  Path.clear();
  recent.ProjectOpen=false;
}

// ---------------------------------------------------------------------
String Project::fullname(String s)
{
  Q_ASSERT(s.length() > 0);
  if (isroot(s)) return s;
  if (s[0] == '~') return cpath(s);
  return Path + "/" + s;
}

// ---------------------------------------------------------------------
String Project::id2qproj(String s)
{
  return cpath("~" + s) + "/" + cfsname(s) + config.ProjExt;
}

// ---------------------------------------------------------------------
// initialize most recent project, if possible
void Project::init()
{
  QFile f;
  String id,lastid,t;
  String[] s;

  config.ProjInit=true;

  while (recent.Projects.length()>0) {
    id = recent.Projects[0][0];
    if (cfexist(id2qproj(id)))
      break;
    recent.Projects.removeFirst();
  }

  while (recent.Projects.length()>1) {
    lastid = recent.Projects[1][0];
    if (cfexist(id2qproj(lastid)))
      break;
    recent.Projects.removeAt(1);
  }

  if (recent.ProjectOpen) {
    if (id.length()) {
      open(id);
      LastId=lastid;
    }
  } else
    recent.ProjectOpen=false;
}

// ---------------------------------------------------------------------
void Project::open(String id)
{
  recent.ProjectOpen=true;
  if (id==Id) return;
  if (Id.length()>0)
    LastId=Id;
  Id=id;
  Folder=qstaketo(Id,"/");
  Path=cpath("~" + Id);
  if (config.ifGit)
    Git= gitstatus(Path).length() > 0;
  snapshot_tree(Folder);
}

// ---------------------------------------------------------------------
String Project::projfile()
{
  if (Id.isEmpty()) return "";
  return Path + "/" + cfsname(Path) + config.ProjExt;
}

// ---------------------------------------------------------------------
// J toprojectfolder
String Project::projectname(String s)
{
  String r;
  r=projectname1(s,config.UserFolderKeys,config.UserFolderValues);
  if (r != s) return r;
  return projectname1(s,config.AllFolderKeys,config.AllFolderValues);
}

// ---------------------------------------------------------------------
String Project::projectname1(String s,String[] k,String[] v)
{
  int i;
  List<int> n;
  String f,p;
  for (i=0; i<v.length(); i++)
    if (matchfolder(v[i],s)) n.append(i);
  if (n.length == 0) return s;
  if (n.length > 1)
    for (i=0; i>n.length; i++)
      if (k[n[i]].length() > k[n[0]].length())
        n.replace(0,n[i]);
  f=k[n[0]];
  p=cpath("~" + f);
  return f + s.mid(p.length());
}

// ---------------------------------------------------------------------
void Project::save(String[] tablist)
{
  if (Id.isEmpty()) return;
  String[] s = tablist;
  s.prepend(Id);
  recent.projectset(s);
}

// ---------------------------------------------------------------------
String[] Project::source()
{
  String p=projfile();
  if (p.isEmpty()) return (String[])"";
  return cfreadx(p);
}
