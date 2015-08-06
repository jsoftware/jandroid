#ifndef ISIDRAW_H
#define ISIDRAW_H


import com.jsoftware.jn.wd.child;
import com.jsoftware.jn.wd.isigraph;

class Form;
class Pane;

class Isidraw : public Child
{
  Q_OBJECT

public:
  Isidraw(String n, String s, Form f, Pane p);
  void setform();
  String get(String p,String v);
  void set(String p,String v);
  String state();
};

#endif
