
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.nedit;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.term;


QMap<String,String> Comments;

// ---------------------------------------------------------------------
Nedit::Nedit()
{
  type=1;
  ensureCursorVisible();
  setLineWrapMode(config.LineWrap ?
                  PlainTextEdit::WidgetWidth : PlainTextEdit::NoWrap);
  setFont(config.Font);
  QPalette p = palette();
  p.setColor(QPalette::Active, QPalette::Base, config.EditBack.color);
  p.setColor(QPalette::Inactive, QPalette::Base, config.EditBack.color);
  p.setColor(QPalette::Text, config.EditFore.color);
  setPalette(p);
  highlight(document());
}

// ---------------------------------------------------------------------
String Nedit::getcomment()
{
  if (Comments.isEmpty()) init_comments();
  String f=file.fileName();
  int n=f.lastIndexOf('.');
  if (n==-1) return "";
  return Comments[f.mid(n+1)];
}

// ---------------------------------------------------------------------
void Nedit::init_comments()
{
  Comments["ijs"]="NB.";
  Comments["ijt"]="NB.";
  Comments["jproj"]="NB.";
  Comments["k"]="/";
  Comments["q"]="/";
  Comments["R"]="#";
  Comments["sh"]="#";
  Comments["tex"]="#";
}

// ---------------------------------------------------------------------
void Nedit::keyPressEvent(QKeyEvent *e)
{
  Qt::KeyboardModifiers mod = QApplication::keyboardModifiers();
  boolean shift = mod.testFlag(Qt::ShiftModifier);
  boolean ctrl = mod.testFlag(Qt::ControlModifier);
  int key = e.key();
  if (key==Qt::Key_Home && (ctrl==false) && shift==false)
    home();
  else
    Bedit::keyPressEvent(e);
}

// ---------------------------------------------------------------------
Nedit::~Nedit()
{
  delete file;
}
