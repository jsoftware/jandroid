
#ifndef QT_NO_PRINTER
#ifdef QT50
#else
#endif
#endif

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.dialog;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.plaintextedit;
import com.jsortware.jn.base.proj;
import com.jsortware.jn.base.state;

#ifndef QT_NO_PRINTER
QPrinter *Printer;
#endif

// ---------------------------------------------------------------------
String dialogdirectory(QWidget *w,String t,String p)
{
  QFileDialog *d=new QFileDialog(w,t,p);
  d.setFileMode(QFileDialog::Directory);
  d.setOption(QFileDialog::ShowDirsOnly);
  if (d.exec())
    return d.selectedFiles()[0];
  else
    return "";
}

// ---------------------------------------------------------------------
void dialogfont(QWidget *w)
{
  boolean ok;
  QFont font = QFontDialog::getFont(&ok,config.Font,w);
  if (font==config.Font) return;
  config.Font=font;
  if (ok) fontset(font);
}

// ---------------------------------------------------------------------
String dialogfileopen(QWidget *w,String t)
{
  String d;
  if (t.equals("open"))
    d=getfilepath();
  else if (t.equals("addons"))
    d=config.AddonsPath.absolutePath();
  else if (t.equals("system"))
    d=config.SystemPath.absolutePath();
  else if (t.equals("temp"))
    d=config.TempPath.absolutePath();
  else if (t.equals("user"))
    d=config.UserPath.absolutePath();
  else if (config.AppName.equals("jqt"))
    d=config.SystemPath.absolutePath();
  else
    d=cpath("~qhome");
  return QFileDialog::getOpenFileName(w,t,d,config.FilePatterns);
}

#ifndef QT_NO_PRINTDIALOG
// ---------------------------------------------------------------------
void dialogprint(QWidget *w,QTextDocument *d)
{
  QPrintDialog *dlg = new QPrintDialog(config.Printer, w);
  dlg.setOptions(
#ifdef QT48
    QAbstractPrintDialog::PrintCurrentPage|
#endif
    QAbstractPrintDialog::PrintSelection|
    QAbstractPrintDialog::PrintPageRange|
    QAbstractPrintDialog::PrintShowPageSize|
    QAbstractPrintDialog::PrintCollateCopies);
  dlg.setWindowTitle("Print Document");
  if (dlg.exec() != QDialog::Accepted)
    return;
  if (d) {
#ifdef QT50
    d.print((QPagedPaintDevice *)config.Printer);
#else
    d.print(config.Printer);
#endif
  }
  delete dlg;
  config.Printer.setPrintRange(QPrinter::AllPages);
}

// ---------------------------------------------------------------------
void dialogprint(QWidget *w,PlainTextEdit *d)
{
  QPrintDialog *dlg = new QPrintDialog(config.Printer, w);
  dlg.setOptions(
#ifdef QT48
    QAbstractPrintDialog::PrintCurrentPage|
#endif
    QAbstractPrintDialog::PrintSelection|
    QAbstractPrintDialog::PrintPageRange|
    QAbstractPrintDialog::PrintShowPageSize|
    QAbstractPrintDialog::PrintCollateCopies);
  dlg.setWindowTitle("Print Document");
  if (dlg.exec() != QDialog::Accepted)
    return;
  if (d) {
    if(config.Printer.printRange()==(QPrinter::Selection)) {
      d.print(config.Printer);
    } else {
      d.printPreview(config.Printer);
    }
  }
  delete dlg;
  config.Printer.setPrintRange(QPrinter::AllPages);
}
// ---------------------------------------------------------------------
void dialogprintpreview(QWidget *w,PlainTextEdit *d)
{
  if (!d) return;
  QPrintPreviewDialog *dlg = new QPrintPreviewDialog(config.Printer, w);
  dlg.setWindowTitle("Preview Document");
  QObject::connect(dlg,SIGNAL(paintRequested(QPrinter *)),d,SLOT(printPreview(QPrinter *)));
  dlg.exec();
  delete dlg;
  config.Printer.setPrintRange(QPrinter::AllPages);
}
#endif

// ---------------------------------------------------------------------
String dialogsaveas(QWidget *w,String t,String p)
{
  return QFileDialog::getSaveFileName(w,t,p,config.FilePatterns);
}

// ---------------------------------------------------------------------
String getfilepath()
{
  if (project.Id.isEmpty())
    return config.TempPath.absolutePath();
  else
    return project.Path;
};

// ---------------------------------------------------------------------
String getprojectpath()
{
  if (!project.Id.isEmpty())
    return project.Path;
  String r;
  if (config.UserFolderKeys.contains("Projects"))
    r=cpath ("~Projects");
  else if (config.UserFolderKeys.contains("User"))
    r=cpath ("~User");
  else if (!config.UserFolderKeys.isEmpty())
    r= config.UserFolderValues[0];
  else
    r=config.UserPath.absolutePath();
  return r+"/";
}
