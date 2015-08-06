#ifndef LINEEDIT_H
#define LINEEDIT_H


import com.jsoftware.jn.wd.child;

class LineEdit : public QLineEdit
{
  Q_OBJECT

public:
  LineEdit(Child c, View parent = 0);

protected:
  void keyPressEvent(QKeyEvent *event);

private:
  Child pchild;

};

#endif
