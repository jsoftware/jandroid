#ifndef BUTTONS_H
#define BUTTONS_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Button : public Child
{
  Q_OBJECT

public:
  Button(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();
  String iconFile;
};

#endif
