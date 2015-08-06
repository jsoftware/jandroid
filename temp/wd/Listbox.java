

import com.jsoftware.jn.base.state;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.listbox;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

extern int rc;

// ---------------------------------------------------------------------
ListBox::ListBox(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="listbox";
  ListWidget *w=new ListWidget;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"multiple")) return;
  w.setObjectName(qn);
  childStyle(opt);
  if (opt.contains("multiple"))
    w.setSelectionMode(QAbstractItemView::ExtendedSelection);

  connect(w,SIGNAL(itemActivated(ListWidgetItem*)),
          this,SLOT(itemActivated()));
  connect(w,SIGNAL(itemSelectionChanged()),
          this,SLOT(itemSelectionChanged()));
}

// ---------------------------------------------------------------------
String ListBox::get(String p,String v)
{
  ListWidget *w=(ListWidget*) widget;
  String r;
  if (p.equals("property")) {
    r+=String("allitems")+"\012"+ "items"+"\012"+ "select"+"\012"+ "text"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("allitems"))
    r=getitems();
  else if (p.equals("items"))
    r=getselection();
  else if (p.equals("text"||p=="select")) {
    List <ListWidgetItem*> list = w.selectedItems();
    if (0==list.count()) {
      if (p.equals("text"))
        r="";
      else
        r=Util.i2s(-1);
    } else {
      if ((w.selectionMode()) == QAbstractItemView::ExtendedSelection) {
        if (p.equals("text"))
          r=getselection();
        else
          r=getselectionindex();
      } else {
        int n=w.currentRow();
        if (p.equals("text"))
          r=Util.q2s(w.item(n).text());
        else
          r=Util.i2s(n);
      }
    }
  } else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
String ListBox::getitems()
{
  ListWidget *w=(ListWidget*) widget;
  String s="";

  for (int i=0; i<w.count(); i++) {
    s += Util.q2s(w.item(i).text());
    s += "\012";
  }
  return(s);
}

// ---------------------------------------------------------------------
String ListBox::getselection()
{
  ListWidget *w=(ListWidget*) widget;
  List <ListWidgetItem*> list = w.selectedItems();
  String s="";

  for (int i=0; i<list.count(); i++) {
    s += Util.q2s(((ListWidgetItem*) list.at(i)).text());
    s += "\012";
  }
  return(s);
}

// ---------------------------------------------------------------------
String ListBox::getselectionindex()
{
  ListWidget *w=(ListWidget*) widget;
  QModelIndexList list = ((QItemSelectionModel *)w.selectionModel()).selectedIndexes();
  String s="";

  for (int i=0; i<list.size(); i++) {
    s += Util.i2s(list[i].row());
    s += " ";
  }
  return(s);
}

// ---------------------------------------------------------------------
void ListBox::itemActivated()
{
  event="button";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void ListBox::itemSelectionChanged()
{
  event="select";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void ListBox::set(String p,String v)
{
  ListWidget *w=(ListWidget*) widget;
  if (p.equals("items")) {
    w.clear();
    w.addItems(Cmd.qsplit(v));
  } else if (p.equals("select")) {
    w.setCurrentRow(Util.c_strtoi(v));
  } else
    Child::set(p,v);
}

// ---------------------------------------------------------------------
String ListBox::state()
{
  ListWidget *w=(ListWidget*) widget;
  List <ListWidgetItem*> list = w.selectedItems();
  String r;
  if (0==list.count()) {
    r+=spair(id,(String)"");
    r+=spair(id+"_select",(String)"_1");
  } else {
    if ((w.selectionMode()) == QAbstractItemView::ExtendedSelection) {
      r+=spair(id,getselection());
      r+=spair(id+"_select",getselectionindex());
    } else {
      int n=w.currentRow();
      r+=spair(id,Util.q2s(w.item(n).text()));
      r+=spair(id+"_select",Util.i2s(n));
    }
  }
  return r;
}
