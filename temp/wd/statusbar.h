#ifndef STATUSBAR_H
#define STATUSBAR_H

import com.jsoftware.jn.wd.child;

class QLabel;
class Form;
class Pane;

// ---------------------------------------------------------------------
class StatusBar : public Child
{
  Q_OBJECT

public:
  StatusBar(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

private:
  List<QLabel *> labelw;
  List<String> labels;

};

#endif
