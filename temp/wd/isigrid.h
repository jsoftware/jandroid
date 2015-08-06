#ifndef DGRID_H
#define DGRID_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;
class QGrid;

// ---------------------------------------------------------------------
class IsiGrid : public Child
{
  Q_OBJECT

public:
  IsiGrid(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

  String style;
};

#endif
