#ifndef PROGRESSBAR_H
#define PROGRESSBAR_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class ProgressBar : public Child
{
  Q_OBJECT

public:
  ProgressBar(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private slots:

private:
  int min;
  int max;

};

#endif
