#ifndef COMBOBOX_H
#define COMBOBOX_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class ComboBox : public Child
{
  Q_OBJECT

public:
  ComboBox(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private:
  String getitems();

private slots:
  void activated();

};

#endif
