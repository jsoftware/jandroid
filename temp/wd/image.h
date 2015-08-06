#ifndef IMAGE_H
#define IMAGE_H

import com.jsoftware.jn.wd.child;

class View;
class QPixmap;
class Form;
class Pane;
class Image2;

// ---------------------------------------------------------------------
class Image : public Child
{
  Q_OBJECT

public:
  Image(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

private:
  Image2 *lab;
  String imageFile;
  int aspectRatio;
};

// ---------------------------------------------------------------------
class Image2 : public View
{
  Q_OBJECT

public:
  explicit Image2(View parent = 0);
  const QPixmap* pixmap() const;
  int aspectRatio;
  boolean transparent;

public slots:
  void setPixmap(const QPixmap&);

protected:
  void paintEvent(QPaintEvent *);

private:
  QPixmap pix;
};

#endif
