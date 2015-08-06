#ifndef OPENGL2_H
#define OPENGL2_H

// regression of Android QOpenGLWidget in Qt 5.4.1
#ifdef QT54
#ifndef QT_OS_ANDROID
#define USE_QOpenGLWidget
#endif
#endif

#ifdef USE_QOpenGLWidget
#else
#endif
//#include <QEvent>

import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
#ifdef USE_QOpenGLWidget
class Opengl2 : public QOpenGLWidget
#else
class Opengl2 : public QGLWidget
#endif
{
  Q_OBJECT

public:
#ifdef USE_QOpenGLWidget
  Opengl2(Child c, const QSurfaceFormat& format, View parent = 0);
#else
  Opengl2(Child c, const QGLFormat& format, View parent = 0);
#endif
  ~Opengl2();

  void fill(const int *);
  QPixmap getpixmap();
  void paintend();
#ifdef USE_QOpenGLWidget
  void updateGL();
#endif

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

  int brushnull;
  int clipped, textx, texty, orgx, orgy;

protected:
  void paintGL();
  void resizeGL(int,int);
  void initializeGL();

  void mousePressEvent(QMouseEvent *event);
  void mouseReleaseEvent(QMouseEvent *event);
  void mouseDoubleClickEvent(QMouseEvent *event);
  void mouseMoveEvent(QMouseEvent *event);
  void focusInEvent(QFocusEvent *event);
  void focusOutEvent(QFocusEvent *event);
  void keyPressEvent(QKeyEvent *event);
  void mouseWheelEvent(QWheelEvent *event);

private slots:

private:
  void buttonEvent(QEvent::Type type, QMouseEvent *event);
  void wheelEvent(QWheelEvent *event);
  Child pchild;
  boolean initialized;

};

#endif
