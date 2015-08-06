#ifndef LAYOUT_H
#define LAYOUT_H


class Child;
class Pane;

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.child;

class Layout : public QObject
{
  Q_OBJECT

public:
  Layout(QChar type, int stretch, Pane p);
  void addWidget(View b);
  void addLayout(Layout b);
  void addSpacing(int n);
  void addStretch(int n);
  void removeWidget(View b);

  Pane ppane;
  QLayout  bin;
  QChar type;
  int r,c,rs,cs;
  int alignment;
  int stretch;
  int spacing;
  boolean razed;
  int rmax,cmax;

};

#endif
