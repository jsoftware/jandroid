#ifndef STATIC_H
#define STATIC_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Static : public Child
{
  Q_OBJECT

public:
  Static(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

};

#endif
