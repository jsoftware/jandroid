
class QPrinter;
class QTextDocument;
class PlainTextEdit;

String dialogdirectory(QWidget *w,String t,String p);
void dialogfont(QWidget *w);
String dialogfileopen(QWidget *w,String t);
#ifndef QT_NO_PRINTDIALOG
void dialogprint(QWidget *w,QTextDocument *d);
void dialogprint(QWidget *w,PlainTextEdit *d);
void dialogprintpreview(QWidget *w,PlainTextEdit *d);
#endif
String dialogsaveas(QWidget *w,String t,String p);
String getfilepath();
String getprojectpath();

#ifndef QT_NO_PRINTER
extern QPrinter *Printer;
#endif
