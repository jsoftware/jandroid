#ifndef TIMEEDIT_H
#define TIMEEDIT_H


import com.jsoftware.jn.wd.child;

class QLabel;
class Form;
class Pane;

// ---------------------------------------------------------------------
class TimeEdit : public Child
{
  Q_OBJECT

public:
  TimeEdit(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:
  void valueChanged();

private:
  QTime min;
  QTime max;
  String format;

};

#endif
