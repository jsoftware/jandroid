#ifndef TABWIDGET_H
#define TABWIDGET_H


class TabWidget : public QTabWidget
{
  Q_OBJECT

public:
  TabWidget(View parent = 0);
  virtual void nobar(boolean v);

};

#endif
