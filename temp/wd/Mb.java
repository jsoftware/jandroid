
// the syntax for messages is:
//   wd 'mb type buttons title message'
//
// type specifies the icon and default behaviour:
//  about
//  info      (default OK button)
//  warn      (default OK button)
//  critical  (default OK button)
//  query     (requires two or three buttons)
//
// if one button, there is no result,
// otherwise the result is the button name (ok, cancel, ...)
//
// buttons are from the set, a button starts with = is the default:
//  mb_ok
//  mb_open
//  mb_save
//  mb_cancel
//  mb_close
//  mb_discard
//  mb_apply
//  mb_reset
//  mb_restoredefaults
//  mb_help
//  mb_saveall
//  mb_yes
//  mb_yestoall
//  mb_no
//  mb_notoall
//  mb_abort
//  mb_retry
//  mb_ignore


#ifndef QT_NO_PRINTER
#ifdef QT50
#else
#endif
#endif

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.base.dialog;
import com.jsoftware.jn.base.state;

String mb(String,String);
static String mbabout();
static String mbcolor();
static String mbdir();
static String mbfont();
static String mbinput();
static String mbmsg();
static String mbopen();
static String mbopen1();
static String mbprint(bool);
static String mbprintx(bool);
static String mbsave();

static String fixsep(String s);

static QMessageBox::StandardButton getdefaultbutton();
static QMessageBox::StandardButton getonebutton(boolean *);
static QMessageBox::StandardButtons getotherbuttons();
static String getname(int);

static String type;
static String[] arg;
static QMessageBox::StandardButton defbutton;

// ---------------------------------------------------------------------
// c is type, p is parameter, possibly preceded by *
String mb(String c,String p)
{
  type=c;
  if (type.size()==0) {
    JConsoleApp.theWd.error("missing mb type");
    return "";
  }

  arg=Cmd.qsplit(p,true);

  if (type.equals("about"))
    return mbabout();
  if (type.equals("color"))
    return mbcolor();
  if (type.equals("dir"))
    return mbdir();
  if (type.equals("font"))
    return mbfont();
  if (type.equals("input"))
    return mbinput();
  if (type.equals("open"))
    return mbopen();
  if (type.equals("open1"))
    return mbopen1();
  if (type.equals("print")) {
    String s=dlb(Util.s2q(p));
    return mbprint('*'==s.at(0));
  }
  if (type.equals("printx")) {
    String s=dlb(Util.s2q(p));
    return mbprintx('*'==s.at(0));
  }
  if (type.equals("save"))
    return mbsave();
  if (type.equals("info"||type=="query"||type=="warn"||type=="critical"))
    return mbmsg();
  JConsoleApp.theWd.error("invalid mb type: " + type);
  return "";
}

// ---------------------------------------------------------------------
String mbmsg()
{
  int r;
  String t,m;

  QMessageBox::StandardButton button1=getdefaultbutton();
  defbutton=button1;
  QMessageBox::StandardButtons buttons=getotherbuttons();

  if (arg.size()==1) {
    t="Message Box";
    m=arg.at(0);
  } else if (arg.size()==2) {
    t=arg.at(0);
    m=arg.at(1);
  } else {
    JConsoleApp.theWd.error("Need title and message: "+Util.q2s(arg.join(" ")));
    return "";
  }

  if (button1==-1) {
    button1=QMessageBox::Ok;
    if (type.equals("query"))
      buttons=QMessageBox::Cancel;
  }
  buttons|=button1;

  if (type.equals("query")) {
    r=QMessageBox::question(getmbparent(),t,m,buttons,defbutton);
    return getname(r);
  }
  if (type.equals("critical"))
    QMessageBox::critical(getmbparent(),t,m,buttons,button1);
  else if (type.equals("info"))
    QMessageBox::information(getmbparent(),t,m,buttons,button1);
  else if (type.equals("warn"))
    QMessageBox::warning(getmbparent(),t,m,buttons,button1);
  return "";
}

// ---------------------------------------------------------------------
String mbabout()
{
  if (arg.size()!=2) {
    JConsoleApp.theWd.error("about needs title and text");
    return "";
  }
  QMessageBox::about(getmbparent(),arg.at(0),arg.at(1));
  return "";

}

// ---------------------------------------------------------------------
String mbcolor()
{
  QColor c;
  int r,g,b;

  if (arg.size()==3) {
    r=Util.c_strtoi(Util.q2s(arg.at(0)));
    g=Util.c_strtoi(Util.q2s(arg.at(1)));
    b=Util.c_strtoi(Util.q2s(arg.at(2)));
    c=QColor(r,g,b);
  } else
    c=Qt::white;
  c=QColorDialog::getColor(c,getmbparent());
  if (!c.isValid()) return "";
  return Util.s2q(Util.i2s(c.red()) + " " + Util.i2s(c.green()) + " " + Util.i2s(c.blue()));
}

// ---------------------------------------------------------------------
String mbdir()
{
  String title,dir,fl;
  if (arg.size()!=2) {
    JConsoleApp.theWd.error("dir needs title and directory");
    return "";
  }
  title=arg.at(0);
  dir=arg.at(1);
  fl=QFileDialog::getExistingDirectory(
       getmbparent(),title,dir);
  return fl;
}

// ---------------------------------------------------------------------
String mbfont()
{
  boolean ok;
  QFont font, def;
  String s;
  def.setStrikeOut(false);
  def.setUnderline(false);
  if (arg.size())
    def.setFamily(arg.at(0));
  if (arg.size()>1)
    def.setPointSize(arg.at(1).toFloat());
  for (int i=2; i<arg.size(); i++) {
    s=arg.at(i);
    if (s.equals("bold"))
      def.setBold(true);
    if (s.equals("italic"))
      def.setItalic(true);
    if (s.equals("strikeout"))
      def.setStrikeOut(true);
    if (s.equals("underline"))
      def.setUnderline(true);
  }
#ifdef __MACH__
  font=QFontDialog::getFont(&ok,def,getmbparent(),String(), QFontDialog::DontUseNativeDialog);
#else
  font=QFontDialog::getFont(&ok,def,getmbparent());
#endif
  if (!ok) return "";
  return fontspec(font);
}

// ---------------------------------------------------------------------
String mbinput()
{
  String type,title,label,text;
  View w=getmbparent();
  String r;
  boolean ok=true;
  int s=arg.size();
  if (s<3) {
    JConsoleApp.theWd.error("input needs at least: type title label");
    return "";
  }
  type=arg.at(0);
  title=arg.at(1);
  label=arg.at(2);

// mb input double title label value min max decimals
  if (type.equals("double")) {
    if (s != 7) {
      JConsoleApp.theWd.error("input double needs 6 parameters");
      return "";
    }
    double dval=arg.at(3).toDouble();
    double dmin=arg.at(4).toDouble();
    double dmax=arg.at(5).toDouble();
    int ddec=arg.at(6).toInt();
    r= String::number(QInputDialog::getDouble(w, title, label, dval, dmin, dmax, ddec,&ok));
  }

// mb input int title label value min max step
  else if (type.equals("int")) {
    if (s != 7) {
      JConsoleApp.theWd.error("input int needs 6 parameters");
      return "";
    }
    int ival=arg.at(3).toInt();
    int imin=arg.at(4).toInt();
    int imax=arg.at(5).toInt();
    int istep=arg.at(6).toInt();
    r=String::number(QInputDialog::getInt(w, title, label, ival,imin,imax,istep,&ok));
  }

//mb input item title label index ifeditable items
  else if (type.equals("item")) {
    if (s != 6) {
      JConsoleApp.theWd.error("input item needs 5 parameters");
      return "";
    }
    int index=arg.at(3).toInt();
    boolean ifed=0 != arg.at(4).toInt();
    String[] items=Cmd.qsplit(Util.q2s(arg.at(5)));
    r=QInputDialog::getItem(w, title, label, items, index, ifed, &ok);
  }

// mb input text title label value
  else if (type.equals("text")) {
    if (s==3)
      text="";
    else  if (s==4)
      text=arg.at(3);
    else {
      JConsoleApp.theWd.error("input text needs 3 o4 4 parameters");
      return "";
    }
    r= QInputDialog::getText(w, title, label, QLineEdit::Normal, text,&ok);
  }

  else {
    JConsoleApp.theWd.error(Util.q2s("unsupported input type: " + type));
    return "";
  }

  return ok ? r : "";
}

// ---------------------------------------------------------------------
String mbopen()
{
  String title,dir,filter;
  String[] fl;
  if (arg.size()<2) {
    JConsoleApp.theWd.error("open needs title, directory, [filters]");
    return "";
  }
  title=arg.at(0);
  dir=arg.at(1);
  if (arg.size()==3)
    filter=fixsep(arg.at(2));
  fl=QFileDialog::getOpenFileNames(
       getmbparent(),title,dir,filter);
  if (fl.isEmpty())
    return "";
  else return fl.join("\012") + "\012";
}

// ---------------------------------------------------------------------
String mbopen1()
{
  String title,dir,filter,fl;
  if (arg.size()<2) {
    JConsoleApp.theWd.error("open1 needs title, directory, [filters]");
    return "";
  }
  title=arg.at(0);
  dir=arg.at(1);
  if (arg.size()==3)
    filter=fixsep(arg.at(2));
  fl=QFileDialog::getOpenFileName(
       getmbparent(),title,dir,filter);
  return fl;
}

// ---------------------------------------------------------------------
String mbprint(boolean iftext)
{
#ifdef QT_NO_PRINTER
  Q_UNUSED(iftext);
  return "";
#else
  String r="";
  if (arg.size()) {
    QTextDocument *d=0;
    String s=arg.at(0);
    if (!iftext) {
      if (!cfexist(s)) {
        JConsoleApp.theWd.error("File not found: " + Util.q2s(s));
        return "";
      }
      s=cfread(s);
    }
    d=new QTextDocument(s);
    d.setDefaultFont(config.Font);
#ifdef QT50
    d.documentLayout().setPaintDevice((QPagedPaintDevice *)config.Printer);
    d.setPageSize(QSizeF(config.Printer.pageRect().size()));
#else
    d.documentLayout().setPaintDevice(config.Printer);
    d.setPageSize(QSizeF(config.Printer.pageRect().size()));
#endif

#ifdef __MACH__
    QSysInfo qsi;
    if (qsi.MacintoshVersion < QSysInfo::MV_10_7) {
      if (!config.Printer.isValid()) {
        JConsoleApp.theWd.error("Invalid printer: " + Util.q2s(config.Printer.printerName()));
        delete d;
        return "";
      }
      d.print(config.Printer);
      delete d;
      return "";
    }
#endif
    dialogprint(getmbparent(),d);
    delete d;
  } else {
#ifdef __MACH__
    QSysInfo qsi;
    if (qsi.MacintoshVersion < QSysInfo::MV_10_7) {
      r=config.Printer.printerName();
      return r;
    }
#endif
    QPrintDialog *dlg = new QPrintDialog(config.Printer);
    if (dlg.exec() == QDialog::Accepted) {
      switch (config.Printer.outputFormat()) {
      case QPrinter::PdfFormat :
        r="_pdf:" + config.Printer.outputFileName();
        break;
#ifndef QT50
      case QPrinter::PostScriptFormat :
        r="_ps:" + config.Printer.outputFileName();
        break;
#endif
      default :
        r=config.Printer.printerName();
        break;
      }
    }
    delete dlg;
  }
  return r;
#endif
}

// ---------------------------------------------------------------------
// print with no dialog
String mbprintx(boolean iftext)
{
#ifdef QT_NO_PRINTER
  Q_UNUSED(iftext);
#else
  if (arg.size()==0) {
    JConsoleApp.theWd.error("No text given for printx");
    return "";
  }
  String s=arg.at(0);
  if (!iftext) {
    if (!cfexist(s)) {
      JConsoleApp.theWd.error("File not found: " + Util.q2s(s));
      return "";
    }
    s=cfread(s);
  }
  QTextDocument *d=new QTextDocument(s);
  d.setDefaultFont(config.Font);
  if (!config.Printer.isValid()) {
    JConsoleApp.theWd.error("Invalid printer: " + Util.q2s(config.Printer.printerName()));
    return "";
  }
#ifdef QT50
  d.documentLayout().setPaintDevice((QPagedPaintDevice *)config.Printer);
  d.setPageSize(QSizeF(config.Printer.pageRect().size()));
  d.print((QPagedPaintDevice *)config.Printer);
#else
  d.documentLayout().setPaintDevice(config.Printer);
  d.setPageSize(QSizeF(config.Printer.pageRect().size()));
  d.print(config.Printer);
#endif
  delete d;
#endif
  return "";
}

// ---------------------------------------------------------------------
String mbsave()
{
  String title,dir,filter,fl;
  if (arg.size()<2) {
    JConsoleApp.theWd.error("save needs title, directory, [filters]");
    return "";
  }
  title=arg.at(0);
  dir=arg.at(1);
  if (arg.size()==3)
    filter=fixsep(arg.at(2));
  fl=QFileDialog::getSaveFileName(
       getmbparent(),title,dir,filter);
  return fl;
}

// ---------------------------------------------------------------------
String fixsep(String s)
{
  return s.replace("|",";;");
}

// ---------------------------------------------------------------------
String getname(int b)
{
  if (b==QMessageBox::Ok)
    return "ok";
  if (b==QMessageBox::Open)
    return "open";
  if (b==QMessageBox::Save)
    return "save";
  if (b==QMessageBox::Cancel)
    return "cancel";
  if (b==QMessageBox::Close)
    return "close";
  if (b==QMessageBox::Discard)
    return "discard";
  if (b==QMessageBox::Apply)
    return "apply";
  if (b==QMessageBox::Reset)
    return "reset";
  if (b==QMessageBox::RestoreDefaults)
    return "restoredefaults";
  if (b==QMessageBox::Help)
    return "help";
  if (b==QMessageBox::SaveAll)
    return "saveall";
  if (b==QMessageBox::Yes)
    return "yes";
  if (b==QMessageBox::YesToAll)
    return "yestoall";
  if (b==QMessageBox::No)
    return "no";
  if (b==QMessageBox::NoToAll)
    return "notoall";
  if (b==QMessageBox::Abort)
    return "abort";
  if (b==QMessageBox::Retry)
    return "retry";
  if (b==QMessageBox::Ignore)
    return "ignore";
  return "unknown button";
}

// ---------------------------------------------------------------------
QMessageBox::StandardButton getonebutton(boolean *def)
{
  if (arg.isEmpty()) return QMessageBox::NoButton;
  String s=arg.first();
  if (s.startsWith("=")) {
    *def=true;
    s=s.mid(1);
  }
  if (s.equals("mb_ok"))
    return QMessageBox::Ok;
  if (s.equals("mb_open"))
    return QMessageBox::Open;
  if (s.equals("mb_save"))
    return QMessageBox::Save;
  if (s.equals("mb_cancel"))
    return QMessageBox::Cancel;
  if (s.equals("mb_close"))
    return QMessageBox::Close;
  if (s.equals("mb_discard"))
    return QMessageBox::Discard;
  if (s.equals("mb_apply"))
    return QMessageBox::Apply;
  if (s.equals("mb_reset"))
    return QMessageBox::Reset;
  if (s.equals("mb_restoredefaults"))
    return QMessageBox::RestoreDefaults;
  if (s.equals("mb_help"))
    return QMessageBox::Help;
  if (s.equals("mb_saveall"))
    return QMessageBox::SaveAll;
  if (s.equals("mb_yes"))
    return QMessageBox::Yes;
  if (s.equals("mb_yestoall"))
    return QMessageBox::YesToAll;
  if (s.equals("mb_no"))
    return QMessageBox::No;
  if (s.equals("mb_notoall"))
    return QMessageBox::NoToAll;
  if (s.equals("mb_abort"))
    return QMessageBox::Abort;
  if (s.equals("mb_retry"))
    return QMessageBox::Retry;
  if (s.equals("mb_ignore"))
    return QMessageBox::Ignore;
  return QMessageBox::NoButton;
}

// ---------------------------------------------------------------------
QMessageBox::StandardButton getdefaultbutton()
{
  boolean def=false;
  QMessageBox::StandardButton r=getonebutton(&def);
  if (r!=QMessageBox::NoButton)
    arg.removeFirst();
  return r;
}

// ---------------------------------------------------------------------
QMessageBox::StandardButtons getotherbuttons()
{
  QMessageBox::StandardButtons r=QMessageBox::NoButton;
  QMessageBox::StandardButton b;
  boolean def=false;
  while (arg.size()) {
    b=getonebutton(&def);
    if (b==QMessageBox::NoButton)
      return r;
    r|=b;
    if (def) defbutton=b;
    arg.removeFirst();
  }
  return r;
}

