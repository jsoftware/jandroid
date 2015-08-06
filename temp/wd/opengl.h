#ifndef OPENGL_H
#define OPENGL_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Opengl : public Child
{
  Q_OBJECT

public:
  Opengl(String n, String s, Form f, Pane p);
  virtual ~Opengl();
  void setform();
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

};

#endif
