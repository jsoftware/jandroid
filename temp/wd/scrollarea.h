#ifndef SCROLLAREA_H
#define SCROLLAREA_H


import com.jsoftware.jn.wd.child;

class Form;
class Pane;

class ScrollArea : public Child
{
  Q_OBJECT

public:
  ScrollArea(String n, String s, Form f, Pane p);
  ~ScrollArea();
  String get(String p,String v);
  void set(String p,String v);
  String state();

protected:

private:
  Child view;

};

#endif

