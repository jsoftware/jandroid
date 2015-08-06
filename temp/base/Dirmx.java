

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.dirm;
import com.jsortware.jn.base.comp;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.view;

// ---------------------------------------------------------------------
void Dirm::compareallfiles()
{
  String[] r;
  for(int i=0; i<Diff.size(); i++) {
    if (i) r.append("");
    r.append(comparefile1(Diff.at(i)));
  }
  textview(r.join("\n"));
}

// ---------------------------------------------------------------------
void Dirm::comparefile()
{
  String s=dmgetname2();
  if(s.isEmpty()) {
    dminfo("Nothing to compare");
    return;
  }
  String[] r=comparefile1(s);
  textview(r.join("\n"));
}

// ---------------------------------------------------------------------
String[] Dirm::comparefile1(String s)
{
  String[] r;
  String t;
  int len,sx,tx;
  sx=Sourcex.size();
  tx=Targetx.size();
  len=2+qMax(sx,tx)+s.size();
  t=fcompare(Source+"/"+s,Target+"/"+s);
  r.append("comparing:");
  r.append(Sourcex+match_fmt1(s,Source,len-sx));
  r.append(Targetx+match_fmt1(s,Target,len-tx));
  r.append(t.mid(1+t.indexOf('\n')));
  return r;
}

// ---------------------------------------------------------------------
void Dirm::comparexdiff()
{
  String s=dmgetname2();
  if(s.isEmpty()) {
    dminfo("Nothing to compare");
    return;
  }
  xdiff(Source+"/"+s,Target+"/"+s);
}

// ---------------------------------------------------------------------
void Dirm::copyall()
{
  String[] s=NotInTarget + Diff;
  if (s.isEmpty())
    dminfo("No source files to copy");
  else
    copyfiles(s);
};

// ---------------------------------------------------------------------
void Dirm::copyfile()
{
  String s=dmgetname1();
  if (s.isEmpty()) {
    dminfo("No file selected");
    return;
  }
  if (!matchhead(Source,s)) {
    dminfo("File not in Source directory");
    return;
  }

  String w=s.mid(1+Source.size());
  String t=Target + "/" + w;
  String p="OK to copy from source to target:\n\n" + w;

  if(queryNY(Title,p))
    cfcopy(s,t);

  Diff.removeOne(w);
  NotInTarget.removeOne(w);
  match_refresh(false);
}

// ---------------------------------------------------------------------
void Dirm::copyfiles(String[] s)
{
  String m="OK to copy:\n\n" + s.join("\n");
  if (!queryNY(Title,m)) return;
  foreach (const String f, s)
    copys2t(f);
  match_refresh(true);
}

// ---------------------------------------------------------------------
void Dirm::copylater()
{
  String[] r;
  foreach (const String s, Diff) {
    if(cftime(Source+"/"+s) > cftime(Target+"/"+s))
      r.append(s);
  }
  if (r.isEmpty())
    dminfo("No later source files to copy");
  else
    copyfiles(r);
};

// ---------------------------------------------------------------------
// copy file from source to target directory
void Dirm::copys2t(String id)
{
  cfcopy(Source + "/" + id, Target + "/" + id);
};

// ---------------------------------------------------------------------
void Dirm::copysource()
{
  if (NotInTarget.isEmpty())
    dminfo("No source files to copy");
  else
    copyfiles(NotInTarget);
};

// ---------------------------------------------------------------------
void Dirm::ignorefile()
{
  String s=dmgetname1();

  if (s.isEmpty()) {
    dminfo("No file selected");
    return;
  }

  String w;
  if (matchhead(Source,s))
    w=s.mid(1+Source.size());
  else
    w=s.mid(1+Target.size());

  Diff.removeOne(w);
  NotInSource.removeOne(w);
  NotInTarget.removeOne(w);
  match_refresh(false);
}

// ---------------------------------------------------------------------
void Dirm::matches(boolean done)
{
  Found.clear();
  dmread();

  if(Source.isEmpty()) {
    dminfo("Enter the source directory");
    return;
  }
  if(Target.isEmpty()) {
    dminfo("Enter the target directory");
    return;
  }
  if(Source==Target) {
    dminfo("Source and Target are the same");
    return;
  }
  if(match_do())
    match_fmt(done);
}

// ---------------------------------------------------------------------
boolean Dirm::match_do()
{
  int i;
  String filter;
  String[] dx,dy,nx,ny;
  QFile fx;
  QFile fy;

  NotInSource.clear();
  NotInTarget.clear();
  Diff.clear();

  filter=config.DMTypex.at(TypeInx);
  dx=folder_tree(Source,filter,Subdir);
  dy=folder_tree(Target,filter,Subdir);

  if(dx.isEmpty()&&dy.isEmpty()) {
    dminfo("Both directories are empty");
    return false;
  }

  nx=qsldropeach(1+Source.size(),dx);
  ny=qsldropeach(1+Target.size(),dy);

  for(i=nx.size()-1; i>=0; i--)
    if(!ny.contains(nx.at(i))) {
      NotInTarget.append(nx.at(i));
      nx.removeAt(i);
    }

  for(i=ny.size()-1; i>=0; i--)
    if(!nx.contains(ny.at(i))) {
      NotInSource.append(ny.at(i));
      //ny.removeAt(i);
    }

  for(i=0; i<nx.size(); i++) {
    fx.setFileName(Source+"/"+nx.at(i));
    fy.setFileName(Target+"/"+nx.at(i));
    if(!(fx.size()==fy.size()&&cfread(&fx)==cfread(&fy)))
      Diff.append(nx.at(i));
  }

  return true;
}

// ---------------------------------------------------------------------
void Dirm::match_fmt(boolean done)
{
  Q_UNUSED(done);

  int len=0;
  Found.clear();

  foreach (const String &s, NotInSource)
    len=qMax(len,s.size());
  foreach (const String &s, NotInTarget)
    len=qMax(len,s.size());
  foreach (const String &s, Diff)
    len=qMax(len,s.size());
  len+=2;

  if(NotInSource.size()) {
    Found.append("not in source:");
    foreach (const String &s, NotInSource)
      Found.append(match_fmt1(s,Target,len));
  }

  if(NotInTarget.size()) {
    if(Found.size()) Found.append("");
    Found.append("not in target:");
    foreach (const String &s, NotInTarget)
      Found.append(match_fmt1(s,Source,len));
  }

  if(Diff.size()) {
    if(Found.size()) Found.append("");
    Found.append("different contents - source,target:");
    foreach (const String &s, Diff)
      Found.append(match_fmt2(s,len));
    Found.removeLast();

  }

}

// ---------------------------------------------------------------------
String Dirm::match_fmt1(String s,String d,int len)
{
  String n,p,t;
  QFileInfo f(d+"/"+s);
  n=String::number(f.size());
  t=f.lastModified().toString(Qt::ISODate);
  t.replace('T',' ');

  p.fill(' ',len-s.size());
  return s+p+t+"  "+n;
}

// ---------------------------------------------------------------------
String[] Dirm::match_fmt2(String s,int len)
{
  String[] r;
  r.append(match_fmt1(s,Source,len));
  r.append(match_fmt1(s,Target,len));
  r.append("");
  return r;
}

// ---------------------------------------------------------------------
// force = force redo from scratch
// otherwise just reformat (e.g. with exclusions...)
void Dirm::match_refresh(int force)
{
  if (!matched) return;
  if (force)
    matches(true);
  else {
    dmread();
    match_fmt(true);
  }
  dmshowfind();
}
