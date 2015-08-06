#ifndef CHECKBOX_H
#define CHECKBOX_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class CheckBox : public Child
{
  Q_OBJECT

public:
  CheckBox(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();
  String iconFile;

private slots:
  void stateChanged();

};

#endif
