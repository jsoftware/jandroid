#ifndef QWIDGEX_H
#define QWIDGEX_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class QWidgex : public Child
{
  Q_OBJECT

public:
  QWidgex(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

};

#endif
