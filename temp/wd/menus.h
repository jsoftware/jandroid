#ifndef MENUS_H
#define MENUS_H

import com.jsoftware.jn.wd.child;

class QMenu;
class Form;
class Pane;

class Menus : public Child
{
  Q_OBJECT

public:
  Menus(String n, String s, Form f, Pane p);
  QAction *makeact(String id, String parms);

  int menu(String c, String p);
  int menupop(String c);
  int menupopz();
  int menusep();
  String get(String p,String v);
  void set(String p,String v);
  String state();

  QHash<String,QAction*> items;

private slots:
  void menu_triggered(QAction *a);

private:
  QMenu *curMenu;
  List<QMenu*> menus;

  String selected;
};

#endif
