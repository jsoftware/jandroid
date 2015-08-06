#ifndef TABS_H
#define TABS_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;
class TabWidget;

// ---------------------------------------------------------------------
class Tabs : public Child
{
  Q_OBJECT

public:
  Tabs(String n, String s, Form f, Pane p);
  String state();

  String get(String p,String v);
  void set(String p,String v);
  void tabend();
  void tabnew(String p);

  int index;

private slots:
  void currentChanged(int index);
  void tabCloseRequested(int index);

};

#endif
