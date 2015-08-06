
import com.jsortware.jn.base.plaintextedit;
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.nedit;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.tedit;
import com.jsortware.jn.base.term;


// ---------------------------------------------------------------------
void Note::runline(boolean advance, boolean show)
{
  note.saveall();
  Nedit *e = editPage();
  int len = e.blockCount();
  QTextCursor c = e.textCursor();
  String txt = c.block().text();
  int row = c.blockNumber();

// advance to next line, or if blank, to before next entry
  if (advance) {
    c.movePosition(QTextCursor::NextBlock,QTextCursor::MoveAnchor,1);
    c.movePosition(QTextCursor::StartOfBlock,QTextCursor::MoveAnchor,1);
    e.setTextCursor(c);
    if(c.block().text().trimmed().isEmpty()) {
      QTextCursor cnext=c;
      while (len>++row) {
        cnext.movePosition(QTextCursor::NextBlock,QTextCursor::MoveAnchor,1);
        if(cnext.block().text().trimmed().size()) break;
        c=cnext;
      }
    }
    c.movePosition(QTextCursor::EndOfBlock,QTextCursor::MoveAnchor,1);
    e.setTextCursor(c);
  }

  tedit.docmdp(txt,true,show);
}

// ---------------------------------------------------------------------
void Note::runlines(boolean all, boolean show)
{
  note.saveall();
  String txt;
  Nedit *e = editPage();
  if (all) {
    if (note.saveall())
      tedit.runall(e.fname, show);
  } else {
    txt=e.readselected();
    tedit.docmds(txt, show);
  }
}

// ---------------------------------------------------------------------
void Term::runlines()
{
  tedit.docmds(tedit.readselected(), true);
}
