
import com.jsortware.jn.base.base;
#ifdef JQT
import com.jsortware.jn.base.jsvr;
#endif
import com.jsortware.jn.base.nedit;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.nmain;
import com.jsortware.jn.base.nside;
import com.jsortware.jn.base.ntabs;
import com.jsortware.jn.base.menu;
import com.jsortware.jn.base.proj;
import com.jsortware.jn.base.psel;
import com.jsortware.jn.base.recent;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.svr;
import com.jsortware.jn.base.term;
import com.jsortware.jn.base.tedit;
import com.jsortware.jn.base.view;

#ifdef QT_OS_ANDROID
extern "C" void android_exec_host(char*,char*,char*,int);
#endif


Note *note=0;
Note *note2=0;

// ---------------------------------------------------------------------
Note::Note()
{
  EditText="Edit";
  if (!config.ProjInit)
    project.init();
  setAttribute(Qt::WA_DeleteOnClose);
  setFocusPolicy(Qt::StrongFocus);
  sideBarShow=true;
  QVBoxLayout layout=new QVBoxLayout;
  layout.setContentsMargins(layout.contentsMargins());
  layout.setSpacing(0);
  menuBar = new Menu();
  split = new QSplitter(0);
  sideBar = new Nside();
#ifdef SMALL_SCREEN
  sideBar.hide();
  sideBarShow=false;
#endif
  mainBar = new Nmain(this);
  split.addWidget(sideBar);
  split.addWidget(mainBar);
  split.setStretchFactor(1,1);
  List<int> w;
  w << 175 << 175;
  split.setSizes(w);
  layout.addWidget(menuBar);
  layout.addWidget(split);
  layout.setStretchFactor(split,1);
  setLayout(layout);
  setWindowTitle("[*]" + EditText);
  setpos();
  menuBar.createActions();
  menuBar.createMenus("note");
  menuBar.createMenus_fini("note");
  scriptenable();
  setWindowIcon(QIcon(":/images/jgreen.png"));
  QMetaObject::connectSlotsByName(this);
}

// ---------------------------------------------------------------------
Note::~Note()
{
  delactivewindow(this);
  if (this==note2)
    note2=0;
  else {
    note=0;
    if (note2)
      setnote(note2);
  }
}

// ---------------------------------------------------------------------
void Note::activate()
{
  setid();
  activateWindow();
  raise();
  int n=editIndex();
  if (n>=0)
    tabs.currentWidget().setFocus();
}

// ---------------------------------------------------------------------
void Note::changeEvent(QEvent *event)
{
  if (NoEvents) return;
  if (event.type()==QEvent::ActivationChange && isActiveWindow())  {
    setactivewindow(this);
    setnote(this);
    projectenable();
    QWidget::changeEvent(event);
  }
}

// ---------------------------------------------------------------------
void Note::closeEvent(QCloseEvent *event)
{
  if (!saveall()) {
    event.ignore();
    return;
  }
  projectsave();
  if (note2) {
    setnote(note2);
    note.setFocus();
  }
  QWidget::closeEvent(event);
}

// ---------------------------------------------------------------------
int Note::count()
{
  return tabs.count();
}

// ---------------------------------------------------------------------
int Note::editIndex()
{
  return tabs.currentIndex();
}

// ---------------------------------------------------------------------
String Note::editFile()
{
  if (tabs.count()==0) return "";
  return ((Nedit *)tabs.currentWidget()).fname;
}

// ---------------------------------------------------------------------
Nedit *Note::editPage()
{
  return (Nedit *)tabs.currentWidget();
}

// ---------------------------------------------------------------------
String Note::editText()
{
  return ((Nedit *)tabs.currentWidget()).toPlainText();
}

// ---------------------------------------------------------------------
// close tab with file
void Note::fileclose(String f)
{
  tabs.tabclosefile(f);
}

// ---------------------------------------------------------------------
boolean Note::fileopen(String s,int line)
{
  setid();
  return tabs.tabopen(s,line);
}

// ---------------------------------------------------------------------
boolean Note::filereplace(String s,int line)
{
  setid();
  return tabs.tabreplace(s,line);
}

// ---------------------------------------------------------------------
String Note::gettabstate()
{
  return tabs.gettabstate();
}

// ---------------------------------------------------------------------
void Note::keyPressEvent(QKeyEvent *event)
{
  switch (event.key()) {
  case Qt::Key_Escape:
    if (config.EscClose)
      close();
  default:
    QWidget::keyPressEvent(event);
  }
}

// ---------------------------------------------------------------------
void Note::keyReleaseEvent(QKeyEvent *event)
{
#ifdef QT_OS_ANDROID
  if (event.key() == Qt::Key_Back) {
    term.activate();
  } else QWidget::keyReleaseEvent(event);
#else
  QWidget::keyReleaseEvent(event);
#endif
}

// ---------------------------------------------------------------------
void Note::loadscript(String s,boolean show)
{
  if (note.saveall())
    tedit.loadscript(s,show);
}

// ---------------------------------------------------------------------
void Note::newtemp()
{
  String f=newtempscript();
  cfcreate(f);
  openfile1(f);
}

// ---------------------------------------------------------------------
void Note::on_lastprojectAct_triggered()
{
  projectsave();
  project.open(project.LastId);
  projectopen(true);
}

// ---------------------------------------------------------------------
void Note::on_openprojectAct_triggered()
{
  new Psel();
}

// ---------------------------------------------------------------------
void Note::on_runallAct_triggered()
{
  runlines(true);
}

#ifdef QT_OS_ANDROID
// ---------------------------------------------------------------------
void Note::on_xeditAct_triggered()
{
  savecurrent();
  String fn=editFile();
  if (fn.isEmpty()) return;
  android_exec_host((char *)"android.intent.action.VIEW",fn.prepend("file://").toUtf8().data(),(char *)"text/plain",0);
}

// ---------------------------------------------------------------------
void Note::on_markCursorAct_triggered()
{
  if (tabs.count()==0) return;
  Nedit *ned=((Nedit *)tabs.currentWidget());
  ned.cu0 = ned.textCursor();
}
#endif

// ---------------------------------------------------------------------
void Note::prettyprint()
{
  int n,pos,top;
  String r;
  savecurrent();
  Nedit *e=editPage();
  var_cmd("require PPScript_jp_");
  var_set("arg_jpp_",editText());
  r=var_cmdr("pplintqt_jpp_ arg_jpp_");
  if (r.isEmpty()) return;
  if (r.at(0)=='0') {
    pos=e.readcurpos();
    top=e.readtop();
    r.remove(0,1);
    settext(r);
    e.settop(top);
    e.setcurpos(pos);
  } else {
    r.remove(0,1);
    n=r.indexOf(' ');
    selectline(r.mid(0,n).toInt());
    info ("Format Script",r.mid(n+1));
  }
}

// ---------------------------------------------------------------------
void Note::projectenable()
{
  boolean b=project.Id.size()>0;
  foreach(QAction *s, menuBar.ProjectEnable)
    s.setEnabled(b);
  if (config.ifGit) {
    b=project.Git;
    foreach(QAction *s, menuBar.GitEnable)
      s.setEnabled(b);
  }
}

// ---------------------------------------------------------------------
void Note::projectopen(boolean b)
{
  tabs.projectopen(b);
  scriptenable();
  projectenable();
}

// ---------------------------------------------------------------------
void Note::projectsave()
{
  setid();
  if (Id.size())
    project.save(tabs.gettablist());
}

// ---------------------------------------------------------------------
void Note::replacetext(Nedit *e, String txt)
{
  QTextDocument *doc=e.document();
  QTextCursor c(doc);
  c.select(QTextCursor::Document);
  c.insertText(txt);
}

// ---------------------------------------------------------------------
boolean Note::saveall()
{
  return tabs.tabsaveall();
}

// ---------------------------------------------------------------------
void Note::savecurrent()
{
  tabs.tabsave(editIndex());
}

// ---------------------------------------------------------------------
void Note::scriptenable()
{
  boolean b=tabs.count();
  menuBar.selMenu.setEnabled(b);
  foreach(QAction *s, menuBar.ScriptEnable)
    s.setEnabled(b);
  mainBar.runallAct.setEnabled(b);
}

// ---------------------------------------------------------------------
void Note::scriptglobals()
{
  savecurrent();
  Nedit *e=editPage();
  String t=editText();
  String dext=cfext(e.fname);
  String[] n=globalassigns(t,dext);
  textview("Script Globals","in script: " + e.sname,n.join("\n"));
}

// ---------------------------------------------------------------------
void Note::selectline(int linenum)
{
  editPage().selectline(linenum);
}

// ---------------------------------------------------------------------
void Note::select_line(String s)
{
  int pos,len;
  String com,hdr,ftr,txt;
  String[] mid;
  Nedit *e=editPage();
  config.filepos_set(e.fname,e.readtop());
  txt=e.readselect_line(&pos,&len);
  hdr=txt.mid(0,pos);
  mid=txt.mid(pos,len).split('\n');
  ftr=txt.mid(pos+len);
  mid=select_line1(mid,s,&pos,&len);
  replacetext(e,hdr+mid.join("\n")+ftr);
  e.settop(config.filepos_get(e.fname));
  e.setselect(pos,len);
  siderefresh();
}

// ---------------------------------------------------------------------
String[] Note::select_line1(String[] mid,String s,int *pos, int *len)
{
  int i;
  String com, comment, p, t;

  if (s.equals("sort")) {
    mid.sort();
    return mid;
  }

  if (s.equals("wrap")) {
#ifdef JQT
    sets("inputx_jrx_",Util.q2s(mid.join("\n")));
    return Util.s2q(dors("70 foldtext inputx_jrx_")).split("\n");
#else
    return mid;
#endif
  }

  comment=editPage().getcomment();
  if (comment.isEmpty()) return mid;
  com=comment+" ";

  if (s.equals("minus")) {
    for(i=0; i<mid.size(); i++) {
      p=mid.at(i);
      if (matchhead(comment,p) && (!matchhead(com+"----",p))
          && (!matchhead(com+"====",p)))
        p=p.mid(comment.size());
      if (p.size() && (p.at(0)==' '))
        p=p.mid(1);
      mid.replace(i,p);
    }
    *len=mid.join("a").size();
    return mid;
  }

  if (s.equals("plus")) {
    for(i=0; i<mid.size(); i++) {
      p=mid.at(i);
      if (p.size())
        p=com+p;
      else
        p=comment;
      mid.replace(i,p);
    }
    *len=mid.join("a").size();
    return mid;
  }

  if (s.equals("plusline1"))
    t.fill('-',57);
  else
    t.fill('=',57);

  t=com + t;
  mid.prepend(t);
  *pos=*pos+1+t.size();
  *len=0;
  return mid;
}

// ---------------------------------------------------------------------
void Note::select_text(String s)
{
  int i,pos,len;
  String hdr,mid,ftr,txt;
  Nedit *e=editPage();
  config.filepos_set(e.fname,e.readtop());
  txt=e.readselect_text(&pos,&len);
  if (len==0) {
    info("Note","No text selected") ;
    return;
  }

  hdr=txt.mid(0,pos);
  mid=txt.mid(pos,len);
  ftr=txt.mid(pos+len);

  if (s.equals("lower"))
    mid=mid.toLower();
  else if (s.equals("upper"))
    mid=mid.toUpper();
  else if (s.equals("toggle")) {
    String old=mid;
    String lc=mid.toLower();
    mid=mid.toUpper();
    for (i=0; i<mid.size(); i++)
      if(mid[i]==old[i]) mid[i]=lc[i];
  }
  replacetext(e,hdr+mid+ftr);
  e.settop(config.filepos_get(e.fname));
  e.setselect(pos,0);
  siderefresh();
}

// ---------------------------------------------------------------------
void Note::setfont(QFont font)
{
  tabs.setfont(font);
}

// ---------------------------------------------------------------------
// ensure current project matches Note
void Note::setid()
{
  if (Id.size() && Id != project.Id)
    project.open(Id);
}

// ---------------------------------------------------------------------
void Note::setindex(int index)
{
  tabs.tabsetindex(index);
}

// ---------------------------------------------------------------------
void Note::setlinenos(boolean b)
{
  menuBar.viewlinenosAct.setChecked(b);
  tabs.setlinenos(b);
}

// ---------------------------------------------------------------------
void Note::setlinewrap(boolean b)
{
  menuBar.viewlinewrapAct.setChecked(b);
  tabs.setlinewrap(b);
}

// ---------------------------------------------------------------------
void Note::setmodified(int n, boolean b)
{
  tabs.setmodified(n,b);
}

// ---------------------------------------------------------------------
// for new note or second note
void Note::setpos()
{
  int x,y,w,h,wid;

  if (note==0) {
    x=config.EditPosX[0];
    y=config.EditPosX[1];
    w=config.EditPosX[2];
    h=config.EditPosX[3];
  } else {
    QDesktopWidget *d=qApp.desktop();
    QRect s=d.screenGeometry();
    wid=s.width();
    QPoint p=note.pos();
    QSize z=note.size();
    x=p.x();
    y=p.y();
    w=z.width();
    h=z.height();
    x=(wid > w + 2*x) ? wid-w : 0;
  }
  move(x,y);
  resize(w,h);
}

// ---------------------------------------------------------------------
void Note::settext(String s)
{
  tabs.tabsettext(s);
}

// ---------------------------------------------------------------------
void Note::settitle(String file, boolean mod)
{
  String f,n,s;

  if (file.isEmpty()) {
    s=EditText;
    if (project.Id.size())
      s="[" + project.Id + "] - " + s;
    setWindowTitle(s);
    return;
  }

  s=cfsname(file);
  if (project.Id.size()) n="[" + project.Id + "] - ";

  if (file == cpath("~" + project.Id + "/" + s))
    f = s;
  else
    f = project.projectname(file);
  setWindowTitle ("[*]" + f + " - " + n + EditText);
  setWindowModified(mod);
}

// ---------------------------------------------------------------------
void Note::settitle2(boolean edit2)
{
  String t=windowTitle();
  if (edit2) {
    EditText="Edit2";
    if ("2"!=t.right(1))
      setWindowTitle(t+"2");
  } else {
    EditText="Edit";
    if (t.right(1).equals("2"))
      setWindowTitle(t.remove(t.size()-1,1));
  }
}

// ---------------------------------------------------------------------
void Note::siderefresh()
{
  sideBar.refresh();
}

// ---------------------------------------------------------------------
void Note::tabclose(int index)
{
  tabs.tabclose(index);
  scriptenable();
}
