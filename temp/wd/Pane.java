

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.child;
import com.jsoftware.jn.wd.layout;

import com.jsoftware.jn.wd.button;
import com.jsoftware.jn.wd.checkbox;
import com.jsoftware.jn.wd.combobox;
import com.jsoftware.jn.wd.dateedit;
import com.jsoftware.jn.wd.dial;
import com.jsoftware.jn.wd.dspinbox;
import com.jsoftware.jn.wd.dummy;
import com.jsoftware.jn.wd.edit;
import com.jsoftware.jn.wd.edith;
import com.jsoftware.jn.wd.editm;
import com.jsoftware.jn.wd.image;
import com.jsoftware.jn.wd.isidraw;
import com.jsoftware.jn.wd.isigraph;
import com.jsoftware.jn.wd.isigrid;
import com.jsoftware.jn.wd.listbox;
#ifndef QT_NO_OPENGL
import com.jsoftware.jn.wd.opengl;
#endif
import com.jsoftware.jn.wd.progressbar;
#ifndef QT_NO_QUICKWIDGET
import com.jsoftware.jn.wd.quickwidget;
#endif
import com.jsoftware.jn.wd.qwidget;
import com.jsoftware.jn.wd.radiobutton;
import com.jsoftware.jn.wd.scrollarea;
import com.jsoftware.jn.wd.scrollbar;
import com.jsoftware.jn.wd.slider;
import com.jsoftware.jn.wd.spinbox;
import com.jsoftware.jn.wd.static;
import com.jsoftware.jn.wd.statusbar;
import com.jsoftware.jn.wd.table;
import com.jsoftware.jn.wd.tabs;
import com.jsoftware.jn.wd.timeedit;
import com.jsoftware.jn.wd.toolbar;
#ifndef QT_NO_MULTIMEDIA
import com.jsoftware.jn.wd.multimedia;
#endif
#ifndef QT_NO_WEBKIT
import com.jsoftware.jn.wd.webview;
#endif
#ifndef QT_NO_WEBENGINE
import com.jsoftware.jn.wd.webengine;
#endif

extern Font *fontdef;
extern int rc;

// ---------------------------------------------------------------------
Pane::Pane(int n,Form f) : View(f)
{
  pform=f;
  buttongroup=0;
  child=0;
  groupboxw=0;
  layout=0;
  maxsizew=maxsizeh=0;
  minsizew=minsizeh=0;
  if (n==1) {
    bin("v");
    layout.bin.setContentsMargins(0,0,0,0);
    layout.bin.setSpacing(0);
  }
}

// ---------------------------------------------------------------------
// return if child valid
boolean Pane::addchild(String n,String c,String p)
{
  Child child=0;
  if (!layout)
    bin("v");
  if (c.equals("button"))
    child=(Child ) new Button(n,p,pform,this);
  else if (c.equals("checkbox"))
    child=(Child ) new CheckBox(n,p,pform,this);
  else if (c.equals("combobox"))
    child=(Child ) new ComboBox(n,p.size()?("edit " + p):"edit",pform,this);
  else if (c.equals("combolist"))
    child=(Child ) new ComboBox(n,p,pform,this);
  else if (c.equals("dateedit"))
    child=(Child ) new DateEdit(n,p,pform,this);
  else if (c.equals("dial"))
    child=(Child ) new Dial(n,p,pform,this);
  else if (c.equals("dspinbox"))
    child=(Child ) new DSpinBox(n,p,pform,this);
  else if (c.equals("edit"))
    child=(Child ) new Edit(n,p,pform,this);
  else if (c.equals("editm"))
    child=(Child ) new Editm(n,p,pform,this);
  else if (c.equals("edith"))
    child=(Child ) new Edith(n,p,pform,this);
  else if (c.equals("image"))
    child=(Child ) new Image(n,p,pform,this);
  else if (c.equals("isidraw"))
    child=(Child ) new Isidraw(n,p,pform,this);
  else if (c.equals("isigraph"))
    child=(Child ) new Isigraph(n,p,pform,this);
  else if (c.equals("isigrid"))
    child=(Child ) new IsiGrid(n,p,pform,this);
  else if (c.equals("listbox"))
    child=(Child ) new ListBox(n,p,pform,this);
#ifndef QT_NO_MULTIMEDIA
  else if (c.equals("multimedia"))
    child=(Child ) new Multimedia(n,p,pform,this);
#endif
#ifndef QT_NO_OPENGL
  else if (c.equals("opengl"))
    child=(Child ) new Opengl(n,p,pform,this);
#endif
  else if (c.equals("progressbar"))
    child=(Child ) new ProgressBar(n,p,pform,this);
#ifndef QT_NO_QUICKWIDGET
  else if (c.equals("quickwidget"))
    child=(Child ) new QuickWidget(n,p,pform,this);
#endif
  else if (c.equals("qwidget"))
    child=(Child ) new QWidgex(n,p,pform,this);
  else if (c.equals("radiobutton"))
    child=(Child ) new RadioButton(n,p,pform,this);
  else if (c.equals("scrollarea"))
    child=(Child ) new ScrollArea(n,p,pform,this);
  else if (c.equals("scrollbar"))
    child=(Child ) new ScrollBar(n,p,pform,this);
  else if (c.equals("slider"))
    child=(Child ) new Slider(n,p,pform,this);
  else if (c.equals("spinbox"))
    child=(Child ) new SpinBox(n,p,pform,this);
  else if (c.equals("static"))
    child=(Child ) new Static(n,p,pform,this);
  else if (c.equals("statusbar"))
    child=(Child ) new StatusBar(n,p,pform,this);
  else if (c.equals("table"))
    child=(Child ) new Table(n,p,pform,this);
  else if (c.equals("tab"))
    child=(Child ) new Tabs(n,p,pform,this);
  else if (c.equals("timeedit"))
    child=(Child ) new TimeEdit(n,p,pform,this);
  else if (c.equals("toolbar"))
    child=(Child ) new ToolBar(n,p,pform,this);
#ifndef QT_NO_WEBKIT
  else if (c.equals("webview"))
    child=(Child ) new WebView(n,p,pform,this);
#endif
#ifndef QT_NO_WEBENGINE
  else if (c.equals("webengine"))
    child=(Child ) new WebEngine(n,p,pform,this);
#endif
  else {
    maxsizew=maxsizeh=0;
    minsizew=minsizeh=0;
    JConsoleApp.theWd.error("child not supported: " + c + " " + p);
    return false;
  }
  if (rc==1) {
    delete child;
    return false;
  }
  Q_ASSERT(child);
  if (fontdef && child.widget) child.widget.setFont(fontdef.font);
#ifdef QT_OS_ANDROID
  if (!fontdef && child.widget) child.widget.setFont(QApplication::font());
#endif
  layout.addWidget(child.widget);   // must call even if widget==0
  child.setmaxwh(maxsizew,maxsizeh);
  child.setminwh(minsizew,minsizeh);
  lasttype=child.type;
  String[] opt=Cmd.qsplit(p);
  if (opt.contains("flush")) {
    layout.bin.setContentsMargins(0,0,0,0);
    layout.bin.setSpacing(0);
  }
  maxsizew=maxsizeh=0;
  minsizew=minsizeh=0;
  this.child=child;
  pform.addchild(child);
  return true;
}

// ---------------------------------------------------------------------
void Pane::addlayout(Layout b)
{
  layout=b;
  layouts.append(b);
}

// ---------------------------------------------------------------------
void Pane::bin(String s)
{
  QChar c;
  int i,n;
  Layout b;
  String m;
  String[] p=bsplit(s);
  String s1=strless(s," 0123456789ghmpsvz");
  if (s1.size()) {
    JConsoleApp.theWd.error("unrecognized bin type: " + s1);
    return;
  }
  for (i=0; i<p.size(); i++) {
    m=p.at(i);
    c=m[0];
    n=Util.c_strtoi(Util.q2s(m.mid(1)));
    if (c=='h'||c=='v'||c=='g')
      addlayout(new Layout(c,n,this));
    else if (c=='m' && layout.type!='g') {
      layout.bin.setContentsMargins(n,n,n,n);
      layout.bin.setSpacing(n);
    } else if (c=='p' && layout.type!='g')
      layout.addSpacing(n);
    else if (c=='s') {
      if ('g'==layout.type) {
        JConsoleApp.theWd.error("grid cannot contain bin s: " + s);
        return;
      }
      layout.addStretch(n);
    } else if (c=='z') {
      if (layouts.size()>1) {
        b=layout;
        n=layouts.last().stretch;
        layouts.removeLast();
        layout=layouts.last();
        layout.addLayout(b);
      }
    }
  }
}

// ---------------------------------------------------------------------
void Pane::fini()
{
  if (layouts.size()) {
    while (layouts.size()>1)
      bin("z");
    setLayout(layout.bin);
  }
  pform.closepane();
}

// ---------------------------------------------------------------------
void Pane::grid(String p, String v)
{
// decommit the name size in the next release
  if (p.equals("shape"||p=="size")) {
    int rmax=0,cmax=0;
    String[] opt=Cmd.qsplit(v);
    int n=opt.size();
    if (1>n) {
      JConsoleApp.theWd.error("missing grid size row_size or column_size: " + p + " " + v);
      return;
    }
    if (!layout)
      bin("v");
    if ('g'!=layout.type)
      bin("g");
    if (1==n) {
      rmax=-1;
      cmax=Util.c_strtoi(Util.q2s(opt.at(0)));
      if (cmax<=0) {
        JConsoleApp.theWd.error("grid size column_size must be positive: " + p + " " + v);
        return;
      }
    } else {
      rmax=Util.c_strtoi(Util.q2s(opt.at(0)));
      cmax=Util.c_strtoi(Util.q2s(opt.at(1)));
      if ((rmax<=0)||(cmax<=0)) {
        JConsoleApp.theWd.error("grid size row_size and column_size must be positive: " + p + " " + v);
        return;
      }
    }
    layout.rmax=rmax;
    layout.cmax=cmax;
    layout.razed=true;
  } else if (p.equals("cell")) {
    int r,c,rs,cs,alignment=0;
    String[] opt=Cmd.qsplit(v);
    int n=opt.size();
    if (!(2==n || 3==n || 4==n || 5==n)) {
      JConsoleApp.theWd.error("not grid cell row, column [,row_span, column_span] [,alignment]: " + p + " " + v);
      return;
    }
    if (!layout)
      bin("v");
    if ('g'!=layout.type)
      bin("g");
    if (layout.razed) {
      JConsoleApp.theWd.error("grid is raze: " + p + " " + v);
      return;
    }
    if (4>n) {
      r=Util.c_strtoi(Util.q2s(opt.at(0)));
      c=Util.c_strtoi(Util.q2s(opt.at(1)));
      rs=cs=1;
      if (3==n) alignment=Util.c_strtoi(Util.q2s(opt.at(2)));
    } else {
      r=Util.c_strtoi(Util.q2s(opt.at(0)));
      c=Util.c_strtoi(Util.q2s(opt.at(1)));
      rs=Util.c_strtoi(Util.q2s(opt.at(2)));
      cs=Util.c_strtoi(Util.q2s(opt.at(3)));
      if (5==n) alignment=Util.c_strtoi(Util.q2s(opt.at(4)));
    }
    if ((r<0)||(c<0)) {
      JConsoleApp.theWd.error("grid cell row and column must be non-negative: " + p + " " + v);
      return;
    }
    if ((rs<=0)||(cs<=0)) {
      JConsoleApp.theWd.error("grid cell row_span and column_span must be positiv: " + p + " " + v);
      return;
    }
    if (alignment<0) {
      JConsoleApp.theWd.error("grid cell alignment must be non-negativ: " + p + " " + v);
      return;
    }
    layout.r=r;
    layout.c=c;
    layout.rs=rs;
    layout.cs=cs;
    layout.alignment=alignment;
  } else if (p.equals("colwidth")) {
    String[] opt=Cmd.qsplit(v);
    int c,w=0;
    int n=opt.size();
    if ((2>n) || (n&1)) {
      JConsoleApp.theWd.error("grid colwidth must specify column and width: " + p + " " + v);
      return;
    }
    for (int i=0; i<n ; i+=2) {
      c=Util.c_strtoi(Util.q2s(opt.at(i)));
      w=Util.c_strtoi(Util.q2s(opt.at(i+1)));
      if ((c<0)||(w<0)) {
        JConsoleApp.theWd.error("grid colwidth column and width must be non-negative: " + p + " " + v);
        return;
      }
      if (layout.razed && (layout.cmax<=c)) {
        JConsoleApp.theWd.error("grid colwidth invalid colum: " + p + " " + v);
        return;
      }
      ((QGridLayout )(layout.bin)).setColumnMinimumWidth(c,w);
    }
  } else if (p.equals("colstretch")) {
    String[] opt=Cmd.qsplit(v);
    int c,s=0;
    int n=opt.size();
    if ((2>n) || (n&1)) {
      JConsoleApp.theWd.error("grid colstretch must specify column and stretch: " + p + " " + v);
      return;
    }
    for (int i=0; i<n ; i+=2) {
      c=Util.c_strtoi(Util.q2s(opt.at(i)));
      s=Util.c_strtoi(Util.q2s(opt.at(i+1)));
      if ((c<0)||(s<0)) {
        JConsoleApp.theWd.error("grid colstretch column and stretch must be non-negative: " + p + " " + v);
        return;
      }
      if (layout.razed && (layout.cmax<=c)) {
        JConsoleApp.theWd.error("grid colstretch invalid column: " + p + " " + v);
        return;
      }
      ((QGridLayout )(layout.bin)).setColumnStretch(c,s);
    }
  } else if (p.equals("rowheight")) {
    String[] opt=Cmd.qsplit(v);
    int r,h=0;
    int n=opt.size();
    if ((2>n) || (n&1)) {
      JConsoleApp.theWd.error("grid row height must specify row and height: " + p + " " + v);
      return;
    }
    for (int i=0; i<n ; i+=2) {
      r=Util.c_strtoi(Util.q2s(opt.at(i)));
      h=Util.c_strtoi(Util.q2s(opt.at(i+1)));
      if ((r<0)||(h<0)) {
        JConsoleApp.theWd.error("grid rowheight row and height must be non-negative: " + p + " " + v);
        return;
      }
      if (layout.razed && (layout.rmax>=0) && (layout.rmax<=r)) {
        JConsoleApp.theWd.error("grid rowheight invalid row: " + p + " " + v);
        return;
      }
      ((QGridLayout )(layout.bin)).setRowMinimumHeight(r,h);
    }
  } else if (p.equals("rowstretch")) {
    String[] opt=Cmd.qsplit(v);
    int r,s=0;
    int n=opt.size();
    if ((2>n) || (n&1)) {
      JConsoleApp.theWd.error("grid row stretch must specify row and height: " + p + " " + v);
      return;
    }
    for (int i=0; i<n ; i+=2) {
      r=Util.c_strtoi(Util.q2s(opt.at(i)));
      s=Util.c_strtoi(Util.q2s(opt.at(i+1)));
      if ((r<0)||(s<0)) {
        JConsoleApp.theWd.error("grid rowstretch row and stretch must be non-negative: " + p + " " + v);
        return;
      }
      if (layout.razed && (layout.rmax>=0) && (layout.rmax<=r)) {
        JConsoleApp.theWd.error("grid rowstretch invalid row: " + p + " " + v);
        return;
      }
      ((QGridLayout )(layout.bin)).setRowStretch(r,s);
    }
  } else
    JConsoleApp.theWd.error("bad grid command: " + p + " " + v);
}

// ---------------------------------------------------------------------
boolean Pane::groupbox(String c, String s)
{
  String cmd=Util.s2q(c);
  String id;

  if (cmd.equals("groupbox")) {
    if (!layout)
      bin("v");
    String[] opt=Cmd.qsplit(s);
    if (opt.size())
      id=opt.at(0);
    groupboxw=new QGroupBox(id);
    if (fontdef) groupboxw.setFont(fontdef.font);
    layout.addWidget(groupboxw);
    QVBoxLayout vb=new QVBoxLayout;
    vb.addWidget(pform.addpane(0));
    groupboxw.setLayout(vb);
    form.pane.bin("v");
    return true;
  }

  if (cmd.equals("groupboxend")) {
    bin("z");
    int n=pform.panes.size();
    if (n>1) {
      Pane p=pform.panes.at(n-2);
      if (p.groupboxw) {
        fini();
        p.groupboxw=0;
        return true;
      }
      JConsoleApp.theWd.error("no groupbox to end: " + c + " " + s);
      return false;
    }
  }
  return false;
}

// ---------------------------------------------------------------------
boolean Pane::line(String p, String s)
{
  Q_UNUSED(s);
  String cmd=Util.s2q(p);
  if (!(cmd.equals("line" || cmd=="lineh" || cmd=="linev")))
    return false;
  QFrame *f=new QFrame();
  f.setFrameShape((cmd.equals("linev")) ? QFrame::VLine : QFrame::HLine);
  f.setFrameShadow(QFrame::Sunken);
  layout.addWidget(f);
  return true;
}

// ---------------------------------------------------------------------
void Pane::setstretch(Child cc, String factor)
{
  if (layout.type!='g')((QBoxLayout )(layout.bin)).setStretchFactor(cc.widget,atoi(factor.Util.c_str()));
}

// ---------------------------------------------------------------------
boolean Pane::split(String p, String s)
{
  if (p.equals("splith" || p=="splitv")) {
    if (!layout)
      bin("v");
    Cmd.qsplitter=new QSplitter((p.equals("splith"))?Qt::Horizontal : Qt::Vertical);
    Cmd.qsplitter.addWidget(pform.addpane(1));
    Cmd.qsplitterp=qs2intlist(Util.s2q(s));
    return true;
  }

  if (!(p.equals("splitend" || p=="splitsep"))) return false;

  fini();
  Pane sp=pform.pane;

  if (p.equals("splitend"))
    sp.splitend();
  else
    sp.Cmd.qsplitter.addWidget(pform.addpane(1));
  return true;
}

// ---------------------------------------------------------------------
void Pane::splitend()
{
  if (Cmd.qsplitterp.size()==4)
    Cmd.qsplitter.setSizes(Cmd.qsplitterp.mid(2));
  if (Cmd.qsplitterp.size()>=2) {
    Cmd.qsplitter.setStretchFactor(0,Cmd.qsplitterp[0]);
    Cmd.qsplitter.setStretchFactor(1,Cmd.qsplitterp[1]);
  }
  layout.addWidget(Cmd.qsplitter);
}
