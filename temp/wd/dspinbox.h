#ifndef DSPINBOX_H
#define DSPINBOX_H

import com.jsoftware.jn.wd.child;

class QLabel;
class Form;
class Pane;

// ---------------------------------------------------------------------
class DSpinBox : public Child
{
  Q_OBJECT

public:
  DSpinBox(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:
  void valueChanged();

private:
  double min;
  double max;
  int prec;

};

#endif
