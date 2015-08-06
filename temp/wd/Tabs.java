
import com.jsoftware.jn.wd.tabwidget;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.tabs;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

public  int index;

// ---------------------------------------------------------------------
Tabs::Tabs(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="tabs";
  index=0;

  TabWidget *w=new TabWidget;
  widget=(TabWidget*) w;

  form.tabs.append(this);
  form.tab=this;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"documentmode movable closable east west south nobar")) return;
  w.setObjectName(qn);
  childStyle(opt);
  w.setUsesScrollButtons(true);

  if (opt.contains("documentmode"))
    w.setDocumentMode(true);

  if (opt.contains("movable"))
    w.setMovable(true);

  if (opt.contains("closable"))
    w.setTabsClosable(true);

  if (opt.contains("east"))
    w.setTabPosition(QTabWidget::East);
  else if (opt.contains("west"))
    w.setTabPosition(QTabWidget::West);
  else if (opt.contains("south"))
    w.setTabPosition(QTabWidget::South);

  if (opt.contains("nobar"))
    w.nobar(true);

  connect(w,SIGNAL(currentChanged(int)),
          this,SLOT(currentChanged(int)));

  connect(w,SIGNAL(tabCloseRequested(int)),
          this,SLOT(tabCloseRequested(int)));

}

// ---------------------------------------------------------------------
void Tabs::currentChanged(int ndx)
{
  Q_UNUSED(ndx);
  event="select";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String Tabs::get(String p,String v)
{
  QTabWidget *w = (QTabWidget *)widget;
  String r;
  if (p.equals("property")) {
    r+=String("label")+"\012"+ "select"+"\012";
    r+=Child::get(p,v);
  }
  if (p.equals("label"||p=="select")) {
    int n=w.currentIndex();
    String s,t;
    for (int i=0; i<w.count(); i++)
      s+=Util.q2s(w.tabText(i)) + '\377';
    t=(n>=0)?Util.i2s(n):String("_1");
    if (p.equals("label"))
      r=s;
    else
      r=t;
  } else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Tabs::set(String p,String v)
{
  QTabWidget *w = (QTabWidget *)widget;
  String[] opt=Cmd.qsplit(v);
  if (opt.isEmpty()) return;
  int ndx=Util.c_strtoi(Util.q2s(opt.at(0)));
  if (p.equals("active"))
    w.setCurrentIndex(ndx);
  else if (p.equals("tabclose"))
    w.removeTab(ndx);
  else if (p.equals("label")) {
    if (opt.size()<2) return;
    w.setTabText(ndx,Util.s2q(Util.remquotes(Util.q2s(opt.at(1)))));
  } else if (p.equals("tabenabled")) {
    if (opt.size()<2) w.setTabEnabled(ndx,true);
    w.setTabEnabled(ndx,Util.remquotes(Util.q2s(opt.at(1)))!="0");
  } else if (p.equals("icon")) {
    if (opt.size()<2) return;
    String iconFile=Util.remquotes(Util.q2s(opt.at(1)));
    QIcon image;
    int spi;
    if (iconFile.substring(0,8).equals("qstyle::") && -1!=(spi=wdstandardicon(iconFile)))
      w.setTabIcon(ndx,w.style().standardIcon((QStyle::StandardPixmap)spi));
    else
      w.setTabIcon(ndx,QIcon(Util.s2q(iconFile)));
  } else if (p.equals("tooltip")) {
    if (opt.size()<2) w.setTabToolTip(ndx,"");
    w.setTabToolTip(ndx,Util.s2q(Util.remquotes(Util.q2s(opt.at(1)))));
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Tabs::state()
{
  QTabWidget *w=(QTabWidget*) widget;
  int n;
  if (this==pform.evtchild && event.equals("tabclose"))
    n=index;
  else
    n=w.currentIndex();
  String r,s,t;
  for (int i=0; i<w.count(); i++)
    s+=Util.q2s(w.tabText(i)) + '\377';
  t=(n>=0)?Util.i2s(n):String("_1");
  r+=spair(id,s);
  r+=spair(id+"_select",t);
  return r;
}

// ---------------------------------------------------------------------
void Tabs::tabCloseRequested(int ndx)
{
  index=ndx;
  event="tabclose";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void Tabs::tabend()
{
  QTabWidget *w=(QTabWidget *) widget;
  pform.pane.fini();

  form.tabs.removeLast();
  if (form.tabs.isEmpty())
    form.tab=0;
  else
    form.tab=form.tabs.last();

  if (index)
    w.setCurrentIndex(0);
}

// ---------------------------------------------------------------------
void Tabs::tabnew(String p)
{
  if (index)
    pform.pane.fini();
  QTabWidget *w=(QTabWidget *) widget;
  pform.addpane(1);
  w.addTab(pform.pane, Util.s2q(p));
  index++;
}
