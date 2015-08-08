
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.recent;
import com.jsortware.jn.base.rsel;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.term;
import com.jsortware.jn.base.view;


Rsel::Rsel()
{
  int i;
  String[] s;

  recent.Files=qslexists(recent.Files);

  if (recent.Files.isEmpty()) {
    info("Recent Files","There are no recent files");
    return;
  }

  for (i=0; i<recent.Files.length(); i++)
    s.append(tofoldername(recent.Files[i]));

  QHBoxLayout h=new QHBoxLayout();
  h.setContentsMargins(0,0,0,0);

  flist=new ListWidget();
  flist.setAlternatingRowColors(true);
  flist.addItems(s);
  flist.setCurrentRow(0);
  h.addWidget(flist);

  QVBoxLayout v=new QVBoxLayout();
  v.setContentsMargins(0,4,2,0);
  v.setSpacing(0);
  open=makebutton(v,"Open");
  view=makebutton(v,"View");
  run=makebutton(v,"Run");
  rundisplay=makebutton(v,"Run Display");
  v.addStretch(1);
  h.addLayout(v);

  setLayout(h);
  setWindowTitle("Recent Files");
#ifdef SMALL_SCREEN
  move(0,0);
  resize(term.width(),term.height());
#else
  resize(450,400);
#endif
  QMetaObject::connectSlotsByName(this);
#ifdef QT_OS_ANDROID
  connect(flist, SIGNAL(itemClicked(ListWidgetItem*)),
          this,SLOT(itemActivated()));
#endif
  show();
}

// ---------------------------------------------------------------------
QPushButton *Rsel::makebutton(QVBoxLayout v,String id)
{
  QPushButton *p=new QPushButton(id);
  v.addWidget(p);
  return p;
}

// ---------------------------------------------------------------------
void Rsel::on_flist_itemActivated()
{
  on_open_clicked();
}

// ---------------------------------------------------------------------
void Rsel::on_open_clicked()
{
  String s=selected();
  if (s.isEmpty()) return;
  if (!ifshift())
    close();
  openfile1(s);
}

// ---------------------------------------------------------------------
void Rsel::on_run_clicked()
{
  String s=selected();
  if (s.isEmpty()) return;
  if (!ifshift())
    close();
  term.load(s,0);
}

// ---------------------------------------------------------------------
void Rsel::on_rundisplay_clicked()
{
  String s=selected();
  if (s.isEmpty()) return;
  if (!ifshift())
    close();
  term.load(s,1);
}

// ---------------------------------------------------------------------
void Rsel::on_view_clicked()
{
  String s=selected();
  if (s.isEmpty()) return;
  if (!ifshift())
    close();
  textview(cfread(s));
}

// ---------------------------------------------------------------------
String Rsel::selected()
{
  List<ListWidgetItem *> s=flist.selectedItems();
  if (s.length()) return cpath(s[0].text());
  else return "";
}
