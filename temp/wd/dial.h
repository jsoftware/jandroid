#ifndef DIAL_H
#define DIAL_H

import com.jsoftware.jn.wd.child;

class QLabel;
class Form;
class Pane;

// ---------------------------------------------------------------------
class Dial : public Child
{
  Q_OBJECT

public:
  Dial(String n, String s, Form f, Pane p);
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
