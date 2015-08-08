/* J tedit */


import com.jsortware.jn.base.base;
import com.jsortware.jn.base.tedit;

import com.jsortware.jn.base.dlog;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.term;
import com.jsortware.jn.base.svr;

extern boolean runshow;

// ---------------------------------------------------------------------
Tedit::Tedit()
{
  smprompt="";
  type=0;
  ifResized=Tw=Th=0;
#ifdef QT_OS_ANDROID
  backButtonPressed=0;
#endif
  hScroll=horizontalScrollBar();
  ensureCursorVisible();
  setLineWrapMode(PlainTextEdit::NoWrap);
}

// ---------------------------------------------------------------------
// append at end of text
// (note appendPlainText appends to new line)
void Tedit::append(String s)
{
  appendPlainText(s);
}

// ---------------------------------------------------------------------
// append smoutput if called from jedo
void Tedit::append_smoutput(String s)
{
  removeprompt();
  if (s.length())
    appendPlainText(s + "\n" + getprompt());
  else
    appendPlainText(getprompt());
}

#ifdef QT_OS_ANDROID
// ---------------------------------------------------------------------
void Tedit::backButtonTimer()
{
  backButtonPressed=0;
}
#endif

// ---------------------------------------------------------------------
void Tedit::docmd(String t)
{
  runshow=false;
  dlog_add(t);
  var_run(t);
}

// ---------------------------------------------------------------------
void Tedit::docmdp(String t,boolean show,boolean same)
{
  boolean cmt=dlb(t).left(3).equals("NB.");
  runshow=same && t.length()>0 && !cmt;
  if (show)
    promptreplace(getprompt() + t);
  if (runshow) {
    dlog_add(t);
    var_run("output_jrx_=:i.0 0");
    var_run("output_jrx_=:"+t);
    var_run("output_jrx_");
  } else
    docmd(t);
}

// ---------------------------------------------------------------------
void Tedit::docmds(String t, boolean show)
{
  runshow=false;
  var_runs(t, show);
}

// ---------------------------------------------------------------------
void Tedit::docmdx(String t)
{
  promptreplace(t);
  docmd(t);
}

// ---------------------------------------------------------------------
void Tedit::enter()
{
  QTextCursor c = textCursor();
  String txt = c.block().text();
  int row = c.blockNumber();
  int len = blockCount();
  if (row < len - 1) {
    String p=getprompt();
    if (initialblanks(p) == p.length()) {
      int pad=qMax(0,p.length() - initialblanks(txt));
      String hdr(pad,' ');
      txt=hdr + txt;
    }
    promptreplace(txt);
  } else
    docmd(txt.trimmed());
}

// ---------------------------------------------------------------------
String Tedit::getprompt()
{
  if (smprompt.length()) {
    prompt=smprompt;
    smprompt="";
  }
  return prompt;
}

// ---------------------------------------------------------------------
// insert at end of text
// (note appendPlainText appends to new line)
void Tedit::insert(String s)
{
  moveCursor(QTextCursor::End);
  insertPlainText(s);
}

// ---------------------------------------------------------------------
void Tedit::itemActivated(ListWidgetItem *item)
{
  term.activateWindow();
  term.raise();
  promptreplace(getprompt() + item.text());
}

// ---------------------------------------------------------------------
void Tedit::keyPressEvent(QKeyEvent *e)
{
  Qt::KeyboardModifiers mod = QApplication::keyboardModifiers();
  boolean shift = mod.testFlag(Qt::ShiftModifier);
  boolean ctrl = mod.testFlag(Qt::ControlModifier);
  int key = e.key();

  if (ctrl>shift) {
    switch (key) {
    case KeyEvent.KEYCODE_D :
      new Slog();
      break;
    case KeyEvent.KEYCODE_Return:
      enter();
      break;
    default:
      Bedit::keyPressEvent(e);
    }
    return;
  }

  if (shift>ctrl) {
    Bedit::keyPressEvent(e);
    return;
  }

  if (shift) {
    switch (key) {
    case KeyEvent.KEYCODE_Up :
      promptreplace(dlog_scroll (-1));
      break;
    case KeyEvent.KEYCODE_Down :
      promptreplace(dlog_scroll (1));
      break;
    default:
      Bedit::keyPressEvent(e);
    }
    return;
  }

  switch (key) {
  case KeyEvent.KEYCODE_Home:
    home();
    break;
    return;
  case KeyEvent.KEYCODE_Return:
  case KeyEvent.KEYCODE_Enter:
    enter();
    break;
  case KeyEvent.KEYCODE_Escape:
    e.ignore();
    break;

  default:
    Bedit::keyPressEvent(e);
  }
}

// ---------------------------------------------------------------------
void Tedit::keyReleaseEvent(QKeyEvent *event)
{
// separate ANDROID code avoids compiler warnings
#ifdef QT_OS_ANDROID
  switch (event.key()) {
  case KeyEvent.KEYCODE_Back:
    if (2>backButtonPressed) {
      if (0==backButtonPressed) QTimer::singleShot(2000, this, SLOT(backButtonTimer()));
      backButtonPressed++;
    } else {
      if (!term.filequit(true))
        event.accept();
    }
    break;
  default:
    Bedit::keyReleaseEvent(event);
  }
#else
  Bedit::keyReleaseEvent(event);
#endif
}

// ---------------------------------------------------------------------
void Tedit::loadscript(String s,boolean show)
{
  tedit.docmdp(var_load(s,false),show, false);
}

// ---------------------------------------------------------------------
void Tedit::promptreplace(String t)
{
  if (t.length() == 0) return;
  moveCursor(QTextCursor::End, QTextCursor::MoveAnchor);
  moveCursor(QTextCursor::StartOfBlock, QTextCursor::KeepAnchor);
  moveCursor(QTextCursor::Left, QTextCursor::KeepAnchor);
  textCursor().removeSelectedText();
  append(t);
  moveCursor(QTextCursor::End);
}

// ---------------------------------------------------------------------
void Tedit::removeprompt()
{
  moveCursor(QTextCursor::End);
  QTextCursor c = textCursor();
  if (c.block().text().trimmed().isEmpty()) {
    c.select(QTextCursor::BlockUnderCursor);
    c.removeSelectedText();
  }
}

// ---------------------------------------------------------------------
// run all script
// implemented as loadd ...
void Tedit::runall(String s, boolean show)
{
  runshow=false;
  var_run(var_load(s,show));
}

// ---------------------------------------------------------------------
void Tedit::setprompt()
{
  append(getprompt());
  hScroll.triggerAction(QScrollBar::SliderToMinimum);
  moveCursor(QTextCursor::End);
}

// ---------------------------------------------------------------------
void Tedit::setresized(int s)
{
  this.ifResized = s;
}

