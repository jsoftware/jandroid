#ifndef DRAWOBJ_H
#define DRAWOBJ_H


import com.jsoftware.jn.wd.font;

// ---------------------------------------------------------------------
class Drawobj
{

public:
  Drawobj();
  ~Drawobj();

  QBrush brush;
  Font *font;
  QPen pen;
  QPen textpen;
  QColor color;
  QColor pencolor;
  QColor brushcolor;
  QColor textcolor;

  QPainter *painter;
  QPixmap *pixmap;

  boolean antialiased;
  boolean transformed;

  int brushnull;
  int clipped, textx, texty, orgx, orgy;

  int height();
  int width();
  void fill(const int *p);
  QPixmap getpixmap();
  void resize(int w, int h);
  void freepixmap();

private:
  int m_height;
  int m_width;

};

#endif
