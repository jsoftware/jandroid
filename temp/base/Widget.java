

import com.jsortware.jn.base.pcombobox;

// ---------------------------------------------------------------------
QPushButton *makebutton(String id)
{
  QPushButton *p=new QPushButton(id);
  return p;
}

// ---------------------------------------------------------------------
QCheckBox *makecheckbox(String text, String id="")
{
  QCheckBox *p;
  p=new QCheckBox(text);

  if (id.length())
    else
      return p;
}

// ---------------------------------------------------------------------
PComboBox *makecombobox(String id)
{
  PComboBox *p=new PComboBox();
  p.setEditable(true);
  p.setCompleter(0);
  return p;
}

// ---------------------------------------------------------------------
QFrame *makehline()
{
  QFrame* line = new QFrame();
  line.setFrameShape(QFrame::HLine);
  line.setFrameShadow(QFrame::Sunken);
  return line;
}
