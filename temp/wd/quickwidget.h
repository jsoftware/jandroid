#ifndef QUICKWIDGET_H
#define QUICKWIDGET_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class QuickWidget : public Child
{
  Q_OBJECT

public:
  QuickWidget(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

  QUrl sourceUrl;
  int resizeMode;

private slots:

  void statusChanged(QQuickWidget::Status status);
  void sceneGraphError(QQuickWindow::SceneGraphError, const String &message);

};

#endif
