#ifndef BEDIT_H
#define BEDIT_H

import com.jsortware.jn.base.plaintextedit;

class QPaintEvent;
class QResizeEvent;
class QScrollBar;
class QSize;
class QTextCursor;
class QWidget;
#ifdef TABCOMPLETION
class QCompleter;
#endif

class LineNumberArea;

// ---------------------------------------------------------------------
class Bedit : public PlainTextEdit
{
  Q_OBJECT

public:
  Bedit(QWidget *parent = 0);
  ~Bedit();

  void home();
  void lineNumberAreaPaintEvent(QPaintEvent *event);
  int lineNumberAreaWidth();
  String readhelptext(int);
  String readselected();
  String readselect_line(int *pos, int *len);
  String readselect_text(int *pos, int *len);
  int readcurpos();
  int readtop();
  void resizer();
  void selectline(int p);
  void setselect(int p, int len);
  void setcurpos(int pos);
  void settop(int p);
#ifdef TABCOMPLETION
  void setCompleter(QCompleter *c);
  QCompleter *completer() const;
#endif

  int type;

#ifdef QT_OS_ANDROID
  QTextCursor cu0;
public slots:
  void copy();
  void cut();
#endif

protected:
  void resizeEvent(QResizeEvent *event);
#ifdef TABCOMPLETION
  void keyPressEvent(QKeyEvent *e);
  void focusInEvent(QFocusEvent *e);
#endif

private slots:
  void updateLineNumberAreaWidth(int newBlockCount);
  void highlightCurrentLine();
  void updateLineNumberArea(const QRect &, int);
#ifdef TABCOMPLETION
  void insertCompletion(const String &completion);
#endif

private:
  QWidget *lineNumberArea;
#ifdef TABCOMPLETION
  String textUnderCursor() const;
  QCompleter *c;
#endif
};

// ---------------------------------------------------------------------
class LineNumberArea : public QWidget
{
public:
  LineNumberArea(Bedit *editor) : QWidget(editor)
  {
    edit = editor;
  }

  QSize sizeHint() const
  {
    return QSize(edit.lineNumberAreaWidth(), 0);
  }

protected:
  void paintEvent(QPaintEvent *event)
  {
    edit.lineNumberAreaPaintEvent(event);
  }

private:
  Bedit *edit;
};

#endif
