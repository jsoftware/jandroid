#ifndef TOOLBAR_H
#define TOOLBAR_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;
class QAction;
class QToolBar;

// ---------------------------------------------------------------------
class ToolBar : public Child
{
  Q_OBJECT

public:
  ToolBar(String n, String s, Form f, Pane p);

  void makeact(String[] opt);
  String get(String p,String v);
  void set(String p,String v);
  QAction * getaction(String id);
  void setbutton(String p, String[] opt);
  String state();

private slots:
  void actionTriggered(QAction *action);

private:
  List<QAction *>acts;

};

#endif
