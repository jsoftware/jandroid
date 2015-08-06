#ifndef VIEW_H
#define VIEW_H

import com.jsortware.jn.base.plaintextedit;

// ---------------------------------------------------------------------
class Eview : public PlainTextEdit
{
  Q_OBJECT

public:
  Eview(QWidget *parent = 0);

private slots:
  void highlightCurrentLine();

};

// ---------------------------------------------------------------------
class TextView : public QDialog
{

  Q_OBJECT

public:
  TextView(String t,String h,String s);

private slots:

private:
  void reject();
  void savepos();
  void keyPressEvent(QKeyEvent *e);
  void keyReleaseEvent(QKeyEvent *e);

  Eview *ev;

};

void textview(String t,String s);
void textview(String t,String[] s);
void textview(String t,String c,String s);
void textview(String s);
void textview(String[] s);


#endif

