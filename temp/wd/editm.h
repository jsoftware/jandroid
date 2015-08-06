#ifndef EDITM_H
#define EDITM_H

import com.jsoftware.jn.wd.child;
import com.jsoftware.jn.wd.../base/plaintextedit;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Editm : public Child
{
  Q_OBJECT

public:
  Editm(String n, String s, Form f, Pane p);
  void cmd(String p, String v);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

private:
  void setselect(PlainTextEdit *w, int bgn, int end);

};

// ---------------------------------------------------------------------
class EditmPTE : public PlainTextEdit
{
  Q_OBJECT

public:
  EditmPTE(View parent=0);

  Child pchild;

protected:
  void keyPressEvent(QKeyEvent *event);

};

#endif
