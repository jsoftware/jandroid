#ifndef RADIOBUTTON_H
#define RADIOBUTTON_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class RadioButton : public Child
{
  Q_OBJECT

public:
  RadioButton(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();
  String iconFile;

private slots:
  void toggled(boolean checked);

};

#endif
