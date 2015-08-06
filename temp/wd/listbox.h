#ifndef LISTBOX_H
#define LISTBOX_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class ListBox : public Child
{
  Q_OBJECT

public:
  ListBox(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private:
  String getitems();
  String getselection();
  String getselectionindex();

private slots:
  void itemActivated();
  void itemSelectionChanged();

};

#endif
