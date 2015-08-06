#ifndef PRTOBJ_H
#define PRTOBJ_H


import com.jsoftware.jn.wd.font;

// ---------------------------------------------------------------------
class Prtobj
{

public:
  Prtobj();
  ~Prtobj();

  QBrush brush;
  Font *font;
  QPen pen;
  QPen textpen;
  QColor color;
  QColor pencolor;
  QColor brushcolor;
  QColor textcolor;

  QPainter *painter;

  boolean antialiased;
  boolean transformed;
  boolean initialized;

  int brushnull;
  int clipped, textx, texty, orgx, orgy;

};

#endif
