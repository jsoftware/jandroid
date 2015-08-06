#ifndef EDIT_H
#define EDIT_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Edit : public Child
{
  Q_OBJECT

public:
  Edit(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:
  void returnPressed();

private:
  boolean focusSelect;

};

#endif
