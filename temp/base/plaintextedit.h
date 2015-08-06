#ifndef PLAINTEXTEDIT_H
#define PLAINTEXTEDIT_H


class QPrinter;

// ---------------------------------------------------------------------
class PlainTextEdit : public QPlainTextEdit
{
  Q_OBJECT

public:
  PlainTextEdit(QWidget *parent = 0);
  boolean acceptKeyBack;

protected:
  void keyReleaseEvent(QKeyEvent *event);

#ifndef QT_NO_PRINTER
public slots:
  void printPreview(QPrinter * printer);
#endif

};

#endif
