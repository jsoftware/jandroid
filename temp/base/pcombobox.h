#ifndef PCOMBOBOX_H
#define PCOMBOBOX_H


// ---------------------------------------------------------------------
class PComboBox: public QComboBox
{
  Q_OBJECT

public:
  PComboBox(QWidget *parent = 0);
  boolean acceptKeyBack;

protected:
  void keyReleaseEvent(QKeyEvent *event);

};

#endif
