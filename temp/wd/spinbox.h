#ifndef SPINBOX_H
#define SPINBOX_H

import com.jsoftware.jn.wd.child;

class QLabel;
class Form;
class Pane;

// ---------------------------------------------------------------------
class SpinBox : public Child
{
  Q_OBJECT

public:
  SpinBox(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:
  void valueChanged();

private:
  int min;
  int max;

};

#endif
