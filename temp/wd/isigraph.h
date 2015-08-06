#ifndef ISIGRAPH_H
#define ISIGRAPH_H


import com.jsoftware.jn.wd.child;

class Form;
class Pane;

class Isigraph : public Child
{
  Q_OBJECT

public:
  Isigraph(String n, String s, Form f, Pane p);
  void setform();
  String get(String p,String v);
  void set(String p,String v);
  String state();
};

#endif

