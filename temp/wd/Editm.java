

import com.jsoftware.jn.base.plaintextedit;
import com.jsoftware.jn.base.state;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.editm;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;
#ifndef QT_NO_PRINTER
#ifdef QT50
#else
#endif
#endif

// ---------------------------------------------------------------------
Editm::Editm(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="editm";
  EditmPTE *w=new EditmPTE;
  w.pchild=this;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"readonly selectable")) return;
  w.setObjectName(qn);
  childStyle(opt);
  if (opt.contains("readonly")) {
    w.setReadOnly(true);
    if (opt.contains("selectable"))
      w.setTextInteractionFlags(Qt::TextSelectableByMouse | Qt::TextSelectableByKeyboard);
  }
}

// ---------------------------------------------------------------------
void Editm::cmd(String p,String v)
{
  String[] opt=Cmd.qsplit(v);
  if (p.equals("print")) {
#ifndef QT_NO_PRINTER
    ((EditmPTE*) widget).printPreview(config.Printer);
#endif
  } else if (p.equals("printpreview")) {
#ifndef QT_NO_PRINTER
    QPrintPreviewDialog *dlg = new QPrintPreviewDialog(config.Printer, pform);
    dlg.setWindowTitle("Preview Document");
    QObject::connect(dlg,SIGNAL(paintRequested(QPrinter *)),((EditmPTE*) widget),SLOT(printPreview(QPrinter *)));
    dlg.exec();
    delete dlg;
    config.Printer.setPrintRange(QPrinter::AllPages);
#endif
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Editm::get(String p,String v)
{
  EditmPTE *w=(EditmPTE*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("limit")+"\012"+ "readonly"+"\012"+ "scroll"+"\012"+ "select"+"\012"+ "text"+"\012"+ "wrap"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("text"))
    r=Util.q2s(w.toPlainText());
  else if (p.equals("select"||p=="scroll")) {
    QTextCursor c=w.textCursor();
    int b,e;
    b=c.selectionStart();
    e=c.selectionEnd();
    QScrollBar *vb=w.verticalScrollBar();
    if (p.equals("select"))
      r=Util.i2s(b)+" "+Util.i2s(e);
    else
      r=Util.i2s(vb.value());
  } else if (p.equals("limit"))
    r=Util.i2s(w.maximumBlockCount());
  else if (p.equals("readonly"))
    r=Util.i2s(w.isReadOnly());
  else if (p.equals("wrap"))
    r=Util.i2s(w.lineWrapMode());
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Editm::set(String p,String v)
{
  EditmPTE *w=(EditmPTE*) widget;
  String r;
  String[] opt=Cmd.qsplit(v);
  QScrollBar *sb;

  int bgn,end,pos=0;

  if (p.equals("limit")) {
    if (opt.isEmpty()) {
      JConsoleApp.theWd.error("set limit requires 1 number: " + id + " " + p);
      return;
    }
    w.setMaximumBlockCount(Util.c_strtoi(Util.q2s(opt.at(0))));
  } else if (p.equals("readonly"))
    w.setReadOnly(Util.remquotes(v)!="0");
  else if (p.equals("text"))
    w.setPlainText(Util.s2q(Util.remquotes(v)));
  else if (p.equals("select")) {
    if (opt.isEmpty())
      w.selectAll();
    else {
      bgn=end=Util.c_strtoi(Util.q2s(opt.at(0)));
      if (opt.size()>1)
        end=Util.c_strtoi(Util.q2s(opt.at(1)));
      setselect(w,bgn,end);
    }
  } else if (p.equals("scroll")) {
    if (opt.size()) {
      sb=w.verticalScrollBar();
      if (opt.at(0).equals("min"))
        pos=sb.minimum();
      else if (opt.at(0).equals("max"))
        pos=sb.maximum();
      else
        pos=Util.c_strtoi(Util.q2s(opt.at(0)));
      sb.setValue(pos);
    } else {
      JConsoleApp.theWd.error("set scroll requires additional parameters: " + id + " " + p);
      return;
    }
  } else if (p.equals("wrap")) {
    w.setLineWrapMode((Util.remquotes(v)!="0")?PlainTextEdit::WidgetWidth:PlainTextEdit::NoWrap);
  } else if (p.equals("find")) {
    w.find(opt.at(0));
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
void Editm::setselect(PlainTextEdit *w, int bgn, int end)
{
  QTextCursor c = w.textCursor();
  c.setPosition(end,QTextCursor::MoveAnchor);
  c.setPosition(bgn,QTextCursor::KeepAnchor);
  w.setTextCursor(c);
}

// ---------------------------------------------------------------------
String Editm::state()
{
  EditmPTE *w=(EditmPTE*) widget;
  QTextCursor c=w.textCursor();
  int b,e;
  b=c.selectionStart();
  e=c.selectionEnd();
  QScrollBar *v=w.verticalScrollBar();
  String r;
  r+=spair(id,Util.q2s(w.toPlainText()));
  r+=spair(id+"_select",Util.i2s(b)+" "+Util.i2s(e));
  r+=spair(id+"_scroll",Util.i2s(v.value()));
  return r;
}

// ---------------------------------------------------------------------
EditmPTE::EditmPTE(View parent) : PlainTextEdit(parent) {}

// ---------------------------------------------------------------------
void EditmPTE::keyPressEvent(QKeyEvent *event)
{
  int key=event.key();
  if (ismodifier(key)) return;
  if ((key==Qt::Key_Enter || key==Qt::Key_Return) && !(event.modifiers() & Qt::CTRL) && !(event.modifiers() & Qt::SHIFT)) {
    if (isReadOnly()) {
      char sysmodifiers[20];
      sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt::CTRL))) + (!!(event.modifiers() & Qt::SHIFT)));
      pchild.event=String("button");
      pchild.sysmodifiers=String(sysmodifiers);
      pchild.pform.signalevent(pchild);
    }
    PlainTextEdit::keyPressEvent(event);
    return;
  }
  int key1=0;
  if ((key>0x10000ff)||((key>=Qt::Key_F1)&&(key<=Qt::Key_F35))) {
    PlainTextEdit::keyPressEvent(event);
    return;
  } else
    key1=translateqkey(key);
  char sysmodifiers[20];
  sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt::CTRL))) + (!!(event.modifiers() & Qt::SHIFT)));
  char sysdata[20];
  if (key==key1)
    sprintf(sysdata , "%s", event.text().toUtf8().constData());
  else sprintf(sysdata , "%s", String(QChar(key1)).toUtf8().constData());

  pchild.event=String("char");
  pchild.sysmodifiers=String(sysmodifiers);
  pchild.sysdata=String(sysdata);
  pchild.pform.signalevent(pchild);
  PlainTextEdit::keyPressEvent(event);
}
