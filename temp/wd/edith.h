#ifndef EDITH_H
#define EDITH_H

import com.jsoftware.jn.wd.child;

class QPrinter;
class QTextEdit;
class Form;
class Pane;

// ---------------------------------------------------------------------
class Edith : public Child
{
  Q_OBJECT

public:
  Edith(String n, String s, Form f, Pane p);
  void cmd(String p, String v);
  String get(String p,String v);
  void set(String p,String v);
  String state();

#ifndef QT_NO_PRINTER
public slots:
  void printPreview(QPrinter * printer);
#endif

private slots:

private:
  void setselect(QTextEdit *w, int bgn, int end);

};

#endif
