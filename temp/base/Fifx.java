

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.fif;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.utils;


// ---------------------------------------------------------------------
void Fif::search()
{

  String[] r;
  if (Search.isEmpty()) {
    finfo ("No search defined");
    return;
  }
  r=searchdo();
  found.clear();
  found.addItems(r);

  if(!ifResults) {
#ifndef SMALL_SCREEN
    QSize s=size();
    resize(s.width(),qMax(Pos[3],s.height()+100));
#endif
    found.show();
    ifResults=true;
  }
}

// ---------------------------------------------------------------------
String[] Fif::searchdo()
{
  String file,txt;
  String[] r;

  String p=cpath(Path);
  String[] files=folder_tree(p,Type,Subdir);

  if (files.isEmpty())
    return r;

  if (Matchcase)
    what=Search;
  else
    what=Search.toLower();

  ifRegex=Assign || Name || Regex;
  if (ifRegex) {
    if (Assign)
      pat.setPattern(config.Rxnna+what+rxassign(config.DefExt,false));
    else if (Name)
      pat.setPattern(config.Rxnna+what+config.Rxnnz);
    else
      pat.setPattern(what);
  }

  for (int i=0; i<files.length(); i++)
    r.append(searchfile(files[i]));

  int len=p.length()+1;
  for (int i=0; i<r.length(); i++)
    r.replace(i,r[i].mid(len));

  return r;
}

// ---------------------------------------------------------------------
String[] Fif::searchfile(String file)
{
  int n,p=0;
  List<int> hit;

  String dat,txt;
  String[] r;

  dat=txt=cfread(file);
  if (txt.isEmpty()) return r;

  if (!Matchcase)
    txt=txt.toLower();

  if (Regex)
    hit=searchfilex(txt);
  else {
    if (ifRegex) {
      while (0<=(n=txt.indexOf(pat,p))) {
        hit.append(n);
        p=n+1;
      }
    } else {
      while (0<=(n=txt.indexOf(what,p))) {
        hit.append(n);
        p=n+1;
      }
    }
  }
  if(hit.length()==0) return r;
  return searchformat(file,hit,dat);
}

// ---------------------------------------------------------------------
// search files line by line for regex
List<int> Fif::searchfilex(String txt)
{
  int i,m,n,p=0;
  List<int> r;
  String[] s=txt.split('\n');

  for (i=0; i<s.length(); i++) {
    m=0;
    while (0<=(n=s[i].indexOf(pat,m))) {
      r.append(p+n);
      m=n+1;
    }
    p+=s[i].length()+1;
  }
  return r;
}

// ---------------------------------------------------------------------
String[] Fif::searchformat(String file,List<int>hit, String txt)
{
  String[] r;

  if (Fileonly) {
    r.append(file+":"+String::number(hit.length()));
    return r;
  }

  hit=lineindex(hit,txt);
  hit=removedups(hit);
  String[] lines=txt.split("\n");
  foreach(int i,hit)
    r.append(String::number(i) + ": " + lines[i]);
  r=qslprependeach(file+":",r);
  return r;
}

// ---------------------------------------------------------------------
// convert hits into lines
List<int> Fif::lineindex(const List<int> hit,const String txt)
{
  int i,p=0;
  List<int> r;
  List<int> end=lineends(txt);
  for (i=0; i<hit.length(); i++) {
    while(end[p]<hit[i])
      p++;
    r.append(p);
  }
  return r;
}

// ---------------------------------------------------------------------
// get text file line ends
List<int> Fif::lineends(const String txt)
{
  int n,p=0;
  List<int>r;
  while(0<=(n=txt.indexOf('\n',p))) {
    r.append(n);
    p=n+1;
  }
  r.append(txt.length());
  return r;
}

// ---------------------------------------------------------------------
// remove dups from sorted list
List<int> Fif::removedups(const List<int> hit)
{
  int p,q;
  List<int> r;
  if (hit.length()==0) return r;
  p=hit[0];
  r.append(p);
  for(int i=1; i<hit.length(); i++) {
    q=hit[i];
    if (p!=q) r.append((p=q));
  }
  return r;
}
