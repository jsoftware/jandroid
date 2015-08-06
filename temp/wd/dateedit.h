#ifndef DATEEDIT_H
#define DATEEDIT_H


import com.jsoftware.jn.wd.child;

class QLabel;
class Form;
class Pane;

// ---------------------------------------------------------------------
class DateEdit : public Child
{
  Q_OBJECT

public:
  DateEdit(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:
  void valueChanged();

private:
  QDate min;
  QDate max;
  String format;

};

#endif
