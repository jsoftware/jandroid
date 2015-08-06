

import com.jsoftware.jn.base.state;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.edith;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;
#ifndef QT_NO_PRINTER
#ifdef QT50
#else
#endif
#endif
// ---------------------------------------------------------------------
Edith::Edith(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="edith";
  QTextEdit *w=new QTextEdit;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);
  w.setReadOnly(true);
}

// ---------------------------------------------------------------------
void Edith::cmd(String p,String v)
{
  String[] opt=Cmd.qsplit(v);
  if (p.equals("print")) {
#ifndef QT_NO_PRINTER
    printPreview(config.Printer);
#endif
  } else if (p.equals("printpreview")) {
#ifndef QT_NO_PRINTER
    QPrintPreviewDialog *dlg = new QPrintPreviewDialog(config.Printer, pform);
    dlg.setWindowTitle("Preview Document");
    QObject::connect(dlg,SIGNAL(paintRequested(QPrinter *)),this,SLOT(printPreview(QPrinter *)));
    dlg.exec();
    delete dlg;
    config.Printer.setPrintRange(QPrinter::AllPages);
#endif
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Edith::get(String p,String v)
{
  QTextEdit *w=(QTextEdit*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("readonly")+"\012"+ "scroll"+"\012"+ "select"+"\012"+ "text"+"\012"+ "wrap"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("text"))
    r=Util.q2s(w.toHtml());
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
  } else if (p.equals("readonly"))
    r=Util.i2s(w.isReadOnly());
  else if (p.equals("wrap"))
    r=Util.i2s(w.lineWrapMode());
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Edith::set(String p,String v)
{
  QTextEdit *w=(QTextEdit*) widget;
  String[] opt=Cmd.qsplit(v);
  QScrollBar *sb;

  int bgn,end,pos=0;

  if (p.equals("edit")) {
    int s;
    if (opt.isEmpty())
      s=1;
    else
      s=Util.c_strtoi(Util.q2s(opt.at(0)));
    if (0==s) {
      if (!w.isReadOnly())  {
        String t=w.toPlainText();
        w.setHtml(t);
        w.setReadOnly(1);
      }
    } else {
      if (w.isReadOnly()) {
        String t=w.toHtml();
        w.setPlainText(t);
        w.setReadOnly(0);
      }
    }
  } else if (p.equals("text")) {
    w.setHtml(Util.s2q(Util.remquotes(v)));
    w.setReadOnly(1);
  } else if (p.equals("select")) {
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
    w.setLineWrapMode((Util.remquotes(v)!="0")?QTextEdit::WidgetWidth:QTextEdit::NoWrap);
  } else if (p.equals("find")) {
    w.find(opt.at(0));
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
void Edith::setselect(QTextEdit *w, int bgn, int end)
{
  QTextCursor c = w.textCursor();
  c.setPosition(end,QTextCursor::MoveAnchor);
  c.setPosition(bgn,QTextCursor::KeepAnchor);
  w.setTextCursor(c);
}

// ---------------------------------------------------------------------
String Edith::state()
{
  QTextEdit *w=(QTextEdit*) widget;
  QTextCursor c=w.textCursor();
  int b,e;
  b=c.selectionStart();
  e=c.selectionEnd();
  QScrollBar *v=w.verticalScrollBar();
  String r;
  r+=spair(id,Util.q2s(w.toHtml()));
  r+=spair(id+"_select",Util.i2s(b)+" "+Util.i2s(e));
  r+=spair(id+"_scroll",Util.i2s(v.value()));
  return r;
}

#ifndef QT_NO_PRINTER
// ---------------------------------------------------------------------
void Edith::printPreview(QPrinter * printer)
{
  QTextEdit *w=(QTextEdit*) widget;
  QTextDocument *d=w.document().clone();
#ifdef QT50
  d.documentLayout().setPaintDevice((QPagedPaintDevice *)printer);
  d.setPageSize(QSizeF(printer.pageRect().size()));
  d.print((QPagedPaintDevice *)printer);
#else
  d.documentLayout().setPaintDevice(printer);
  d.setPageSize(QSizeF(printer.pageRect().size()));
  d.print(printer);
#endif
  delete d;
}
#endif
