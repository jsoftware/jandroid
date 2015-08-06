#ifndef DUMMY_H
#define DUMMY_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Dummy : public Child
{
  Q_OBJECT

public:
  Dummy(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

};

#endif
