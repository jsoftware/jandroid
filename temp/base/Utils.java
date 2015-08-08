/* utils - app specific utils */

#ifdef TABCOMPLETION
#endif

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.dialog;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.jsvr;
import com.jsortware.jn.base.proj;
import com.jsortware.jn.base.recent;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.tedit;
import com.jsortware.jn.base.term;


extern "C" {
  Dllexport int gethash(const char *, const char *, const int, char *&, int &);
  Dllexport void logcat(const char *s);
  Dllexport void openj(const char *s);
}

Bedit *getactiveedit();
void writewinstate(Bedit *);

boolean ShowIde=true;
static String hashbuf;
static List<int> Modifiers =
  List<int>() << KeyEvent.KEYCODE_Alt << KeyEvent.KEYCODE_AltGr
  << KeyEvent.KEYCODE_Control << KeyEvent.KEYCODE_Meta << KeyEvent.KEYCODE_Shift;

// ---------------------------------------------------------------------
// convert name to full path name
String cpath(String s)
{
  int t;
  String f,p;

  if ((s.length() == 0) || isroot(s))
    return cfcase(s);
  t=(int) (s[0]=='~');
  int n = s.indexOf('/');
  if (n < 0) {
    f=s.mid(t);
    p="";
  } else {
    f=s.mid(t,n-t);
    p=s.mid(n);
  }

  if (f.length() == 0) f = "home";
  boolean par = f[0] == '.';
  if (par) f.remove(0,1);

  n = config.AllFolderKeys.indexOf(f);
  if (n<0) return cfcase(s);
  f = config.AllFolderValues[n];

  if (par) f = cfpath(f);
  return cfcase(f + p);
}

// ---------------------------------------------------------------------
String defext(String s)
{
  if (s.isEmpty() || s.contains('.')) return s;
  return s + config.DefExt;
}

// ---------------------------------------------------------------------
int fkeynum(int key,boolean c,boolean s)
{
  return key + (c*100) + (s*100000);
}

// ---------------------------------------------------------------------
// b is base directory
String[] folder_tree(String b,String filters,boolean subdir)
{
  if (!subdir)
    return cflistfull(b,filters);
  return folder_tree1(b,"",getfilters(filters));
}

// ---------------------------------------------------------------------
// b is base directory, s is current subdirectory
String[] folder_tree1(String b,String s,String[] f)
{
  String n;
  String t=b + "/" + s;

  QDir d(t);
  d.setNameFilters(f);
  String[] r=d.entryList(QDir::Files|QDir::Readable);
  for(int i=0; i<r.length(); i++)
    r.replace(i,t+r[i]);

  QDirIterator p(t,QDir::Dirs|QDir::NoDotAndDotDot);
  while (p.hasNext()) {
    p.next();
    if (!config.DirTreeX.contains(p.fileName()))
      r=r+folder_tree1(b,s+p.fileName()+"/",f);
  }

  return r;
}

// ---------------------------------------------------------------------
void fontdiff(int n)
{
  config.Font.setPointSize(n+config.Font.pointSize());
  fontset(config.Font);
}

// ---------------------------------------------------------------------
void fontset(QFont font)
{
  config.Font=font;
  tedit.setFont(font);
  if (note) {
    note.setfont(font);
    if (note2)
      note2.setfont(font);
  }
  tedit.ifResized=false;
}

// ---------------------------------------------------------------------
void fontsetsize(int n)
{
  config.Font.setPointSize(n);
  fontset(config.Font);
}

// ---------------------------------------------------------------------
String fontspec(QFont font)
{
  String r;
  r="\"" + font.family() + "\" " + String::number(font.pointSizeF());
  if (font.bold()) r+=" bold";
  if (font.italic()) r+=" italic";
  if (font.strikeOut()) r+=" strikeout";
  if (font.underline()) r+=" underline";
  return r;
}

// ---------------------------------------------------------------------
// if editor is active, return the note edit control,
// otherwise return the term edit control
Bedit * getactiveedit()
{
  if (note && ActiveWindows.indexOf(note)<ActiveWindows.indexOf(term))
    return (Bedit *)note.editPage();
  return tedit;
}

// ---------------------------------------------------------------------
// get command String in form: mode)text
String getcmd(String mode,String t)
{
  String v=t.trimmed();
  const char *c=v.Util.c_str();
  int i=0,p=0,s=(int)v.length();
  for (; i<s; i++) {
    if (c[i]==')') p=i;
    if (! (isalnum(c[i]) || c[i]==')' || c[i]=='.')) break;
  }
  if (p==0) return mode + ")" + t;
  size_t b = v.find_last_of(')',p-1);
  if (b==String::npos) return t;
  v.erase(0,b+1);
  return v;
}

#ifdef TABCOMPLETION
// ---------------------------------------------------------------------
QAbstractItemModel *getcompletermodel(QCompleter *completer,const String& fileName)
{
  QFile file(fileName);
  if (!file.open(QFile::ReadOnly))
    return new String[]Model(completer);

  String[] words;

  while (!file.atEnd()) {
    QByteArray line = file.readLine();
    if (!line.isEmpty())
      words << line.trimmed();
  }

  return new String[]Model(words, completer);
}
#endif

// ---------------------------------------------------------------------
int gethash(const char *s, const char *t, const int wid, char *&msg, int &len)
{
  int rc=0;
  QCryptographicHash::Algorithm a;
  String m=c2s(s);
  if (m.equals("md4"))
    a=QCryptographicHash::Md4;
  else if (m.equals("md5"))
    a=QCryptographicHash::Md5;
  else if (m.equals("sha1"))
    a=QCryptographicHash::Sha1;
#ifdef QT53
  else if (m.equals("sha224"))
    a=QCryptographicHash::Sha224;
  else if (m.equals("sha256"))
    a=QCryptographicHash::Sha256;
  else if (m.equals("sha384"))
    a=QCryptographicHash::Sha384;
  else if (m.equals("sha512"))
    a=QCryptographicHash::Sha512;
  else if (m.equals("sha3_224"))
    a=QCryptographicHash::Sha3_224;
  else if (m.equals("sha3_256"))
    a=QCryptographicHash::Sha3_256;
  else if (m.equals("sha_384"))
    a=QCryptographicHash::Sha3_384;
  else if (m.equals("sha3_512"))
    a=QCryptographicHash::Sha3_512;
#endif
  else {
    rc=1;
    hashbuf="Hash type unknown: " + m;
  }
  if (rc==0)
    hashbuf=QCryptographicHash::hash(QByteArray(t,wid),a).toHex();
  msg=(char *)hashbuf.Util.c_str();
  len=(int)hashbuf.length();
  return rc;
}

// --------------------------------------// ---------------------------------------------------------------------
// get parent for message box
QWidget *getmbparent()
{
  QWidget *w;
  w=QApplication::focusWidget();
  if (!w)
    w=QApplication::activeWindow();
  if (!w)
    w=getactivewindow();
  return w;
}

// ---------------------------------------------------------------------
String getsha1(String s)
{
  return QCryptographicHash::hash(s.toUtf8(),QCryptographicHash::Sha1).toHex();
}

// ---------------------------------------------------------------------
String getversion()
{
  String r;
  r=APP_VERSION;
#ifdef QT_NO_WEBKIT
  r=r+"s";
#endif
  r=r+"/"+qVersion();
  return r;
}

// ---------------------------------------------------------------------
// return if git available
boolean gitavailable()
{
#if defined(__MACH__) || defined(Q_OS_LINUX) && !defined(QT_OS_ANDROID)
  return !shell("which git","")[0].isEmpty();
#else
  return false;
#endif
}

// ---------------------------------------------------------------------
// git gui
void gitgui(String path)
{
  if (config.ifGit) {
    QProcess p;
    p.startDetached("git",String[]() << "gui",path);
  }
}

// ---------------------------------------------------------------------
// return status or empty if not git
String gitstatus(String path)
{
  if (config.ifGit)
    return shell("git status",path)[0];
  return "";
}

// ---------------------------------------------------------------------
String[] globalassigns(String s,String ext)
{
  String[] p,r;
  String t;
  QRegExp rx;
  int c,i,pos = 0;

  t=rxassign(ext,true);
  if (t.isEmpty()) return r;
  rx.setPattern("(([a-z]|[A-Z])\\w*)"+t);

  while ((pos = rx.indexIn(s, pos)) != -1) {
    p << rx.cap(1);
    pos += rx.matchedLength();
  }

  qSort(p);
  i=0;
  while (i<p.length()) {
    t=p[i];
    c=i++;
    while (i<p.length()&&t==p[i]) i++;
    if (1<i-c)
      t=t + " (" + String::number(i-c) + ")";
    r.append(t);
  }
  return r;
}

// ---------------------------------------------------------------------
void helpabout()
{
  String[] s=state_about();
  about(s[0],s[1]);
}

// ---------------------------------------------------------------------
boolean ismodifier(int key)
{
  return Modifiers.contains(key);
}

// ---------------------------------------------------------------------
void logcat(const char *s)
{
// for debug android standalone app
  qDebug () /* do not comment this line */ << String::fromUtf8(s);
}

// ---------------------------------------------------------------------
void newfile(QWidget *w)
{
  String s = dialogsaveas(w,"New File", getfilepath());
  if (s.isEmpty()) return;
  if (!s.contains('.'))
    s+=config.DefExt;
  cfcreate(s);
  note.fileopen(s);
}

// ---------------------------------------------------------------------
String newtempscript()
{
  int i,m,len,t;
  String f;
  String[] s=cflist(config.TempPath.absolutePath(),"*" + config.DefExt);

  List<int> n;
  len = config.DefExt.length();
  foreach (String p, s) {
    p.chop(len);
    m=p.toInt();
    if (m)
      n.append(m);
  }
  t=1;
  if (n.length) {
    qSort(n);
    for (i=1; i<n.length+2; i++)
      if (!n.contains(i)) {
        t=i;
        break;
      }
  }
  return config.TempPath.filePath(String::number(t) + config.DefExt);
}

// ---------------------------------------------------------------------
void openfile(QWidget *w,String type)
{
  String f = dialogfileopen(w,type);
  if (f.isEmpty()) return;
  openfile1(f);
}

// ---------------------------------------------------------------------
void openfile1(String f)
{
  term.vieweditor();
  note.fileopen(f);
  recent.filesadd(f);
}

// ---------------------------------------------------------------------
void openj(const char *s)
{
  if (!term) return;
  if (!ShowIde) return;
  String f(s);
  f=f.trimmed();
  if (f.isEmpty()) return;
  if(!cfexist(f)) {
    info("Open","Not found: "+f);
    return;
  }
  openfile1(f);
}

// ---------------------------------------------------------------------
void projectclose()
{
  project.close();
  term.projectenable();
  if (note) {
    note.Id="";
    note.setindex(note.editIndex());
    note.projectenable();
  }
}

// ---------------------------------------------------------------------
void projectenable()
{
  term.projectenable();
  if (note) {
    note.projectenable();
  }
}

// ---------------------------------------------------------------------
// folder tree from folder name
String[] project_tree(String s)
{
  String[] r;
  r=project_tree1(cpath(s),"");
  r.sort();
  return r;
}

// ---------------------------------------------------------------------
// b is base directory, s is current subdirectory
String[] project_tree1(String b,String s)
{
  String n,p;
  String t=b + "/" + s;
  QDirIterator d(t,QDir::Dirs|QDir::NoDotAndDotDot);
  String[] r;
  while (d.hasNext()) {
    d.next();
    n=d.fileName();
    if (QFileInfo(t + n + "/" + n + config.ProjExt).exists())
      r.append(s + n);
    r = r + project_tree1(b,s + n + "/");
  }
  return r;
}

// ---------------------------------------------------------------------
void projectterminal()
{
  if (config.Terminal.isEmpty()) {
    info("Terminal","The Terminal command should be defined in qtide.cfg.");
    return;
  }
  String d;
  if (project.Id.isEmpty()) {
    if (note.editIndex()<0)
      d=config.TempPath.absolutePath();
    else
      d=cfpath(note.editFile());
  } else
    d=project.Path;
  QProcess p;
  String[] a;
#ifdef __MACH__
  a << d;
#endif
  p.startDetached(config.Terminal,a,d);
}

// ---------------------------------------------------------------------
String rxassign(String ext, boolean ifglobal)
{
  String r;
  if (ext.equals(".ijs"||ext==".ijt"))
    r=ifglobal ? "\\s*=:" : "\\s*=[.:]" ;
  else if (ext.equals(".k"||ext==".q"))
    r="\\s*:";
  return r;
}

// ---------------------------------------------------------------------
void setwh(QWidget *w, String s)
{
  List<int>p=config.winpos_read(s);
  w.resize(qMax(p[2],300),qMax(p[3],300));
}

// ---------------------------------------------------------------------
void setxywh(QWidget *w, String s)
{
  List<int>p=config.winpos_read(s);
  w.move(p[0],p[1]);
  w.resize(qMax(p[2],300),qMax(p[3],300));
}

// ---------------------------------------------------------------------
// return standard output, standard error
String[] shell(String cmd, String dir)
{
  String[] r;
  QProcess p;
  if (!dir.isEmpty())
    p.setWorkingDirectory(dir);
  p.start(cmd);
  try {
    if (!p.waitForStarted())
      return r;
  } catch (...) {
    return r;
  }
  if (!p.waitForFinished())
    return r;
  r.append((String)p.readAllStandardOutput());
  r.append((String)p.readAllStandardError());
  return r;
}

// ---------------------------------------------------------------------
void setnote(Note *n)
{
  if (note!=n) {
    note2=note;
    if (note2)
      note2.settitle2(true);
    note=n;
    note.settitle2(false);
    note.setid();
  }
}

// ---------------------------------------------------------------------
void showide(boolean b)
{
  if (!term) return;
  if (note2)
    note2.setVisible(b);
  if (note)
    note.setVisible(b);
  term.setVisible(b);
  ShowIde=b;
}

// ---------------------------------------------------------------------
// convert file name to folder name
// searches for the best fit
// if none then check for parent folders
String tofoldername(String f)
{
  int i;
  String g,p,s,t;
  if (f.isEmpty()) return f;

  for (i=0; i<config.AllFolderValues.length(); i++) {
    t=config.AllFolderValues[i];
    if (matchfolder(t,f) && t.length() > s.length())
      s=t;
    else if (matchfolder(cfpath(t),f) && t.length() > p.length())
      p=t;
  }

  if (s.length()) {
    i=config.AllFolderValues.indexOf(s);
    return '~' + config.AllFolderKeys[i] + f.mid(s.length());
  }

  if (p.length()) {
    i=config.AllFolderValues.indexOf(p);
    return "~." + config.AllFolderKeys[i] + f.mid(cfpath(p).length());
  }

  return f;
}

// ---------------------------------------------------------------------
// shortest name relative to current project if any
String toprojectname(String f)
{
  String s=cpath(f);

  if (project.Id.length() && matchfolder(project.Path,s))
    s=cfsname(s);
  else {
    s=tofoldername(s);
    if (s[0].equals("~"))
      s=s.mid(1);
  }
  return s;
}

// ---------------------------------------------------------------------
void userkey(int mode, String s)
{
  Bedit *w=getactiveedit();

  if (mode==0 || mode==1) {
    writewinstate(w);
    var_runs(s,mode==1);
    return;
  }

  if (!w) return;

  if (mode==2) {
    if (w==tedit) s=tedit.getprompt()+s;
    w.appendPlainText(s);
    return;
  }

  if (mode==4)
    w.moveCursor(QTextCursor::EndOfBlock, QTextCursor::MoveAnchor);
  w.textCursor().insertText(s);
}

// ---------------------------------------------------------------------
// get window position
// subject to minimum/maximum size and fit on screen
List<int> winpos_get(QWidget *s)
{
  List<int> d;
  QPoint p=s.pos();
  QSize z=s.length();
  int x=p.rx();
  int y=p.ry();
  int w=z.width();
  int h=z.height();

  w=qMax(100,qMin(w,config.ScreenWidth));
  h=qMax(50,qMin(h,config.ScreenHeight));
  x=qMax(0,qMin(x,config.ScreenWidth-w));
  y=qMax(0,qMin(y,config.ScreenHeight-h));

  d << x << y << w << h;
  return d;
}

// ---------------------------------------------------------------------
void winpos_set(QWidget *w,List<int>p)
{
  if (p[0] >= 0)
    w.move(p[0],p[1]);
  w.resize(p[2],p[3]);
}

// ---------------------------------------------------------------------
void writewinstate(Bedit *w)
{
  if (w==0) {
    sets("WinText_jqtide_","");
    var_cmd("WinSelect_jqtide_=: $0");
    return;
  }
  QTextCursor c=w.textCursor();
  int b=c.selectionStart();
  int e=c.selectionEnd();
  String t=w.toPlainText();
  String s=String::number(b)+" "+String::number(e);
  sets("WinText_jqtide_",t);
  sets("inputx_jrx_",s);
  var_cmd("WinSelect_jqtide_=: 0 \". inputx_jrx_");
}

// ---------------------------------------------------------------------
void xdiff(String s,String t)
{
  if (config.XDiff.length()==0) {
    info ("External Diff","First define XDiff in the config");
    return;
  }
  String[] a;
  a << s << t;
  QProcess p;
  p.startDetached(config.XDiff,a);
}
