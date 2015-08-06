

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.base.bedit;
import com.jsoftware.jn.base.jsvr;
import com.jsoftware.jn.base.note;
import com.jsoftware.jn.base.state;
import com.jsoftware.jn.base.tedit;
import com.jsoftware.jn.base.term;

// sm does its own parsing of the wd command
extern Cmd cmd;
extern int rc;

static String smact();
static String smactive();
static String smclose();
static String smJConsoleApp.theWd.error(String p);
static String smfocus();
static String smfont();
static String smget();
static String smgetactive();
static String smgetscript(String);
static String smgettabs(String);
static String smgetwin(String);
static String smgetwin1(Bedit *);
static String smgetwin2(Note *n);
static String smgetxywh();
static String smgetxywh1(View );
static String smopen();
static String smprompt();
static String smreplace();
static String smrun();
static String smsave();
static String smsaveactive();
static String smsaveall();
static String smset();
static String smsetselect(Bedit *,String);
static String smsettext(String,String);
static String smsetxywh(String,String);

// ---------------------------------------------------------------------
String sm(String c)
{
  rc=0;
  if (c.equals("act"))
    return smact();
  if (c.equals("active"))
    return smactive();
  if (c.equals("close"))
    return smclose();
  if (c.equals("focus"))
    return smfocus();
  if (c.equals("font"))
    return smfont();
  if (c.equals("get"))
    return smget();
  if (c.equals("new"))
    return smopen();
  if (c.equals("open"))
    return smopen();
  if (c.equals("replace"))
    return smreplace();
  if (c.equals("save"))
    return smsave();
  if (c.equals("set"))
    return smset();
  else if (c.equals("prompt"))
    return smprompt();
  cmd.getparms();
  return smJConsoleApp.theWd.error("unrecognized sm command: " + c);
}

// ---------------------------------------------------------------------
String smact()
{
  cmd.getparms();
  term.smact();
  return"";
}

// ---------------------------------------------------------------------
String smactive()
{
  String p=cmd.getparms();
  String[] opt=Cmd.qsplit(p);
  if (note==0 || note.editIndex()<0)
    return smerror ("No active edit window");
  if (opt[0]!="tab")
    return smerror ("unrecognized sm command parameters: " + p);
  int ndx=opt[1].toInt();
  if (ndx<0 || ndx>=note.count())
    return smerror ("invalid tab index: " + p);
  note.setindex(ndx);
  return "";
}

// ---------------------------------------------------------------------
String smclose()
{
  String c=cmd.getid();
  String p=cmd.getparms();
  if (c.equals("tab")) {
    if (note==0 || note.editIndex()<0)
      return smerror ("No active edit window");
    int ndx=Util.s2q(p).toInt();
    if (ndx<0 || ndx>=note.count())
      return smerror ("invalid tab index: " + p);
    note.tabclose(ndx);
  } else if (c.equals("edit")) {
    if (note==0)
      return smerror ("No edit window");
    note.close();
  } else if (c.equals("edit2")) {
    if (note2==0)
      return smerror ("No edit2 window");
    note2.close();
  } else
    return smerror ("unrecognized sm command parameters: " + p);
  return "";
}

// ---------------------------------------------------------------------
String smJConsoleApp.theWd.error(String p)
{
  rc=1;
  return p;
}

// ---------------------------------------------------------------------
String smfocus()
{
  String p=cmd.getparms();
  if (p.empty())
    return smJConsoleApp.theWd.error("sm focus needs additional parameters");
  if (p.equals("term"))
    term.smact();
  else if (p.equals("edit")) {
    if (note==0 || note.editIndex()==-1)
      return smJConsoleApp.theWd.error("No active edit window");
    note.activateWindow();
    note.raise();
    note.repaint();
  } else if (p.equals("edit2")) {
    if (note2==0 || note2.editIndex()==-1)
      return smJConsoleApp.theWd.error("No active edit2 window");
    setnote(note2);
    note.activateWindow();
    note.raise();
    note.repaint();
  } else
    return smJConsoleApp.theWd.error("unrecognized sm command: focus " + p);
  return "";
}

// ---------------------------------------------------------------------
String smfont()
{
  String p=cmd.getparms();
  if (!p.empty()) {
    Font *fnt = new Font(p);
    if (fnt.error) {
      delete fnt;
      return smJConsoleApp.theWd.error("unrecognized sm command: font " + p);
    } else {
      config.Font=fnt.font;
      delete fnt;
    }
  }
  fontset(config.Font);
  return "";
}

// ---------------------------------------------------------------------
String smget()
{
  String p=cmd.getparms();
  if (p.size()==0)
    return smJConsoleApp.theWd.error("sm get needs additional parameters");
  if (p.equals("active"))
    return smgetactive();
  if (p.equals("term" || p=="edit" || p=="edit2"))
    return smgetwin(p);
  if (p.equals("xywh"))
    return smgetxywh();
  String[] s=Cmd.qsplit(p);
  if (s[0].equals("tabs")) {
    if(s.size()<=1)
      return smJConsoleApp.theWd.error("sm command requires another parameter: get tabs");
    else
      return smgettabs(s[1]);
  }
  return smJConsoleApp.theWd.error("unrecognized sm command: get " + p);
}

// ---------------------------------------------------------------------
String smgetactive()
{
  rc=-1;
  return (note && ActiveWindows.indexOf(note)<ActiveWindows.indexOf(term))
         ? "edit" : "term";
}

// ---------------------------------------------------------------------
String smgetscript(String f)
{
  return dors(">{.getscripts_j_ '" + f + "'");
}

// ---------------------------------------------------------------------
String smgettabs(String p)
{
  Note *n;
  if (p.equals("edit")) {
    if (note==0)
      return smJConsoleApp.theWd.error("No active edit window");
    n=note;
  } else if (p.equals("edit2")) {
    if (note2==0)
      return smJConsoleApp.theWd.error("No active edit2 window");
    n=note2;
  } else
    return smJConsoleApp.theWd.error("sm get tabs needs edit or edit2 parameter");
  rc=-2;
  return n.gettabstate();
}

// ---------------------------------------------------------------------
String smgetwin(String p)
{
  rc=-2;
  if (p.equals("term"))
    return smgetwin1(tedit);
  if (p.equals("edit")) {
    if (note==0)
      return smJConsoleApp.theWd.error("No active edit window");
    return smgetwin2(note);
  }
  if (note2==0)
    return smJConsoleApp.theWd.error("No active edit2 window");
  return smgetwin2(note2);
}

// ---------------------------------------------------------------------
String smgetwin1(Bedit *t)
{
  String r;
  if (t==0) {
    r+=spair("text",(String)"");
    r+=spair("select",(String)"");
  } else {
    QTextCursor c=t.textCursor();
    int b=c.selectionStart();
    int e=c.selectionEnd();
    r+=spair("text",t.toPlainText());
    r+=spair("select",String::number(b)+" "+String::number(e));
  }
  return r;
}

// ---------------------------------------------------------------------
String smgetwin2(Note *n)
{
  if (n.editIndex()==-1)
    return smgetwin1((Bedit *)0);
  String r=smgetwin1((Bedit *)n.editPage());
  r+=spair("file",n.editFile());
  return r;
}

// ---------------------------------------------------------------------
String smgetxywh()
{
  rc=-2;
  String r;
  r+=spair("text",smgetxywh1(term));
  if (note)
    r+=spair("edit",smgetxywh1(note));
  if (note2)
    r+=spair("edit2",smgetxywh1(note2));
  return r;
}

// ---------------------------------------------------------------------
String smgetxywh1(View w)
{
  QPoint p=w.pos();
  QSize z=w.size();
  return Util.q2s(String::number(p.rx())+" "+String::number(p.ry())+" "+
                  String::number(z.width())+" "+String::number(z.height()));
}

// ---------------------------------------------------------------------
String smopen()
{
  String c=cmd.getid();
  String p=cmd.getparms();

  if (c.equals("edit"))
    term.vieweditor();
  if (c.equals("edit2")) {
    if (note==0)
      return smJConsoleApp.theWd.error("no edit window open");
    note.on_winotherAct_triggered();
  }
  if (c.equals("edit" || c=="edit2"))
    return "";
  if (c!="tab") {
    return smJConsoleApp.theWd.error("unrecognized sm command: open " + c);
  }
  term.vieweditor();
  if (p.empty())
    note.newtemp();
  else {
    String f=Util.s2q(smgetscript(p));
    if (!cfexist(f))
      return smJConsoleApp.theWd.error("file not found: " + Util.q2s(f));
    note.fileopen(f);
  }
  rc=-1;
  return Util.i2s(note.editIndex());
}

// ---------------------------------------------------------------------
String smprompt()
{
  String p=cmd.getparms();
  term.smprompt(Util.s2q(p));
  return"";
}

// ---------------------------------------------------------------------
String smreplace()
{
  String c=cmd.getid();
  String p=cmd.getparms();
  if (note==0 || note.editIndex()<0)
    return smerror ("No active edit window");
  if (c!="edit")
    return smJConsoleApp.theWd.error("unrecognized sm command: replace " + c);
  if (p.empty())
    return smJConsoleApp.theWd.error("replace needs 2 parameters: edit filename");
  String f=Util.s2q(smgetscript(p));
  if (!cfexist(f))
    return smJConsoleApp.theWd.error("file not found: " + Util.q2s(f));
  note.filereplace(f);
  return"";
}

// ---------------------------------------------------------------------
// String smrun()
// {
//   String p=cmd.getparms();
//   if (p!="edit")
//     return smJConsoleApp.theWd.error("unrecognized sm command: run " + p);
//   if (note==0 || note.editIndex()<0)
//     return smJConsoleApp.theWd.error("No active edit window");
//   note.runlines(true,false);  // all lines, no display
//   return"";
// }
//
// ---------------------------------------------------------------------
String smsave()
{
  String p=cmd.getparms();
  if (note==0)
    return smJConsoleApp.theWd.error("No active edit window");
  if (p.empty())
    return smJConsoleApp.theWd.error("sm save parameter not given");
  if (p.equals("edit"))
    return smsaveactive();
  if (p.equals("tabs"))
    return smsaveall();
  return smJConsoleApp.theWd.error("sm save parameter should be 'edit' or 'tabs': " + p);
}

// ---------------------------------------------------------------------
String smsaveactive()
{
  note.savecurrent();
  return "";
}

// ---------------------------------------------------------------------
String smsaveall()
{
  note.saveall();
  return "";
}

// ---------------------------------------------------------------------
String smset()
{
  String p=cmd.getid();
  if (p.empty())
    return smJConsoleApp.theWd.error("sm set parameters not given");
  String c=cmd.getid();
  if (c.empty())
    return smJConsoleApp.theWd.error("sm set " + p + " parameters not given");
  String q=cmd.getparms();
  Bedit *e;

  if (p.equals("term")) {
    e=tedit;
  } else if (p.equals("edit")) {
    if (note==0)
      return smJConsoleApp.theWd.error("No active edit window");
    e=(Bedit *)note.editPage();
  } else if (p.equals("edit2")) {
    if (note2==0)
      return smJConsoleApp.theWd.error("No active edit2 window");
    e=(Bedit *)note2.editPage();
  } else
    return smJConsoleApp.theWd.error("unrecognized sm command: set " + p);

  if (e==0 && (q.equals("select" || q=="text")))
    return smJConsoleApp.theWd.error("no edit window for sm command: set " + q);

  if (c.equals("select"))
    return smsetselect(e,q);
  if (c.equals("text"))
    return smsettext(p,q);
  if (c.equals("xywh"))
    return smsetxywh(p,q);

  return smJConsoleApp.theWd.error("unrecognized sm command: set " + p + " " + q);
}

// ---------------------------------------------------------------------
String smsetselect(Bedit *e, String q)
{
  List<int> s=qsl2intlist(Cmd.qsplit(q));
  if (s.size()!= 2)
    return smJConsoleApp.theWd.error("sm set select should have begin and end parameters");
  int m=e.toPlainText().size();
  if (s[1]==-1) s[1]=m;
  s[1]=qMin(m,s[1]);
  s[0]=qMin(s[0],s[1]);
  e.setselect(s[0],s[1]-s[0]);
  return"";
}

// ---------------------------------------------------------------------
String smsettext(String p,String s)
{
  String t=Util.s2q(s);
  if (p.equals("term"))
    tedit.setPlainText(t);
  else if (p.equals("edit"))
    note.settext(t);
  else
    note2.settext(t);
  return"";
}

// ---------------------------------------------------------------------
String smsetxywh(String m,String q)
{
  View w;
  if (m.equals("term"))
    w=term;
  else if (m.equals("edit"))
    w=note;
  else
    w=note2;
  List<int> s=qsl2intlist(Cmd.qsplit(q));
  QPoint p=w.pos();
  QSize z=w.size();
  if (s[0]==-1) s[0]=p.rx();
  if (s[1]==-1) s[1]=p.ry();
  if (s[2]==-1) s[2]=z.width();
  if (s[3]==-1) s[3]=z.height();
  w.move(s[0],s[1]);
  w.resize(s[2],s[3]);
  return"";
}

