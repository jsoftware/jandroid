

import com.jsortware.jn.base.pcombobox;

// ---------------------------------------------------------------------
QPushButton *makebutton(String id)
{
  QPushButton *p=new QPushButton(id);
  p.setObjectName(id.remove(' ').remove('&').toLower());
  return p;
}

// ---------------------------------------------------------------------
QCheckBox *makecheckbox(String text, String id="")
{
  QCheckBox *p;
  p=new QCheckBox(text);

  if (id.size())
    p.setObjectName(id);
  else
    p.setObjectName(text.remove(' ').remove('&').toLower());
  return p;
}

// ---------------------------------------------------------------------
PComboBox *makecombobox(String id)
{
  PComboBox *p=new PComboBox();
  p.setEditable(true);
  p.setCompleter(0);
  p.setObjectName(id.remove(' ').remove('&').toLower());
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
