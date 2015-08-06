package com.jsoftware.jn.wd;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Child;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Font;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Layout;
import com.jsoftware.jn.wd.Wd;
import java.util.ArrayList;
import java.util.List;
// import com.jsoftware.jn.wd.Button;
// import com.jsoftware.jn.wd.Checkbox;
// import com.jsoftware.jn.wd.Combobox;
// import com.jsoftware.jn.wd.Dateedit;
// import com.jsoftware.jn.wd.Dial;
// import com.jsoftware.jn.wd.Dspinbox;
// import com.jsoftware.jn.wd.Dummy;
// import com.jsoftware.jn.wd.Edit;
// import com.jsoftware.jn.wd.Edith;
// import com.jsoftware.jn.wd.Editm;
// import com.jsoftware.jn.wd.Image;
// import com.jsoftware.jn.wd.Isidraw;
// import com.jsoftware.jn.wd.Isigraph;
// import com.jsoftware.jn.wd.Isigrid;
// import com.jsoftware.jn.wd.Listbox;
// import com.jsoftware.jn.wd.Opengl;
// import com.jsoftware.jn.wd.Progressbar;
// import com.jsoftware.jn.wd.Qwidget;
// import com.jsoftware.jn.wd.Radiobutton;
// import com.jsoftware.jn.wd.Scrollarea;
// import com.jsoftware.jn.wd.Scrollbar;
// import com.jsoftware.jn.wd.Slider;
// import com.jsoftware.jn.wd.Spinbox;
// import com.jsoftware.jn.wd.Static;
// import com.jsoftware.jn.wd.Statusbar;
// import com.jsoftware.jn.wd.Table;
// import com.jsoftware.jn.wd.Tabs;
// import com.jsoftware.jn.wd.Timeedit;
// import com.jsoftware.jn.wd.Toolbar;
// import com.jsoftware.jn.wd.Webview;

public class Pane extends LinearLayout
{

  public Form pform;
  public String event;
  public String lasttype;
  public String locale;
  public String sysdata;
  public String sysmodifiers;
  public float weight=0f;
  public int sizew=-2;  // wrap_content
  public int sizeh=-2;
  public int minsizew;
  public int minsizeh;

//  public QButtonGroup *buttongroup;
  public Child child;
  public Child evtchild;
//  public QGroupBox *groupboxw;
//  public QSignalMapper *signalMapper;
  public Layout layout;
  public ArrayList<Layout >layouts;
//  public QSplitter *qsplitter;
//  public List<int> qsplitterp;

// ---------------------------------------------------------------------
  public Pane(int n,Form f)
  {
    super(f.activity);
    setOrientation(LinearLayout.VERTICAL);
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    setLayoutParams(lp);
    pform=f;
    layouts=new ArrayList<Layout >();
    if (n==1) {
      bin("v");
      layout.setContentsMargins(0,0,0,0);
      layout.setSpacing(0);
    }
  }

// ---------------------------------------------------------------------
// return if child valid
  boolean addchild(String n,String c,String p)
  {
    Child child=null;
    if (null==layout)
      bin("v");
    if (c.equals("button"))
      child=(Child ) new JButton(n,p,pform,this);
//   else if (c.equals("checkbox"))
//     child=(Child ) new CheckBox(n,p,pform,this);
//   else if (c.equals("combobox"))
//     child=(Child ) new ComboBox(n,p.length()?("edit " + p):"edit",pform,this);
//   else if (c.equals("combolist"))
//     child=(Child ) new ComboBox(n,p,pform,this);
//   else if (c.equals("dateedit"))
//     child=(Child ) new DateEdit(n,p,pform,this);
//   else if (c.equals("dial"))
//     child=(Child ) new Dial(n,p,pform,this);
//   else if (c.equals("dspinbox"))
//     child=(Child ) new DSpinBox(n,p,pform,this);
    else if (c.equals("edit"))
      child=(Child ) new JEditText(n,p,pform,this);
//   else if (c.equals("editm"))
//     child=(Child ) new Editm(n,p,pform,this);
//   else if (c.equals("edith"))
//     child=(Child ) new Edith(n,p,pform,this);
//   else if (c.equals("image"))
//     child=(Child ) new Image(n,p,pform,this);
    else if (c.equals("isidraw"))
      child=(Child ) new JIsigraph(n,p,pform,this,"isidraw");
    else if (c.equals("isigraph"))
      child=(Child ) new JIsigraph(n,p,pform,this,"isigraph");
//   else if (c.equals("isigrid"))
//     child=(Child ) new IsiGrid(n,p,pform,this);
//   else if (c.equals("listbox"))
//     child=(Child ) new ListBox(n,p,pform,this);
//   else if (c.equals("opengl"))
//     child=(Child ) new Opengl(n,p,pform,this);
//   else if (c.equals("progressbar"))
//     child=(Child ) new ProgressBar(n,p,pform,this);
//   else if (c.equals("qwidget"))
//     child=(Child ) new QWidgex(n,p,pform,this);
//   else if (c.equals("radiobutton"))
//     child=(Child ) new RadioButton(n,p,pform,this);
//   else if (c.equals("scrollarea"))
//     child=(Child ) new ScrollArea(n,p,pform,this);
//   else if (c.equals("scrollbar"))
//     child=(Child ) new ScrollBar(n,p,pform,this);
//   else if (c.equals("slider"))
//     child=(Child ) new Slider(n,p,pform,this);
//   else if (c.equals("spinbox"))
//     child=(Child ) new SpinBox(n,p,pform,this);
//   else if (c.equals("static"))
//     child=(Child ) new Static(n,p,pform,this);
//   else if (c.equals("statusbar"))
//     child=(Child ) new StatusBar(n,p,pform,this);
//   else if (c.equals("table"))
//     child=(Child ) new Table(n,p,pform,this);
//   else if (c.equals("tab"))
//     child=(Child ) new Tabs(n,p,pform,this);
//   else if (c.equals("timeedit"))
//     child=(Child ) new TimeEdit(n,p,pform,this);
//   else if (c.equals("toolbar"))
//     child=(Child ) new ToolBar(n,p,pform,this);
//   else if (c.equals("webview"))
//     child=(Child ) new WebView(n,p,pform,this);
    else {
      weight=0f;
      sizew=sizeh=-2;
      minsizew=minsizeh=0;
      JConsoleApp.theWd.error("child not supported: " + c + " " + p);
      return false;
    }
    if (JConsoleApp.theWd.rc==1) {
//    delete child;
      return false;
    }
//   if (fontdef && child.widget) child.widget.setFont(fontdef.font);
//   if (!fontdef && child.widget) child.widget.setFont(QApplication::font());
    if (null!=child.widget) child.widget.setId(JConsoleApp.theWd.nextId++);
    layout.addWidget(child.widget);   // must call even if widget==0
    child.setminwh(minsizew,minsizeh);
    child.setweight(weight);
    lasttype=child.type;
    String[] opt=Cmd.qsplit(p);
    if (Util.sacontains(opt,"flush")) {
      layout.setContentsMargins(0,0,0,0);
      layout.setSpacing(0);
    }
    child.setwh(sizew,sizeh);
    weight=0f;
    sizew=sizeh=-2;
    minsizew=minsizeh=0;
    this.child=child;
    pform.addchild(child);
    return true;
  }

// ---------------------------------------------------------------------
  void addlayout(Layout b)
  {
    layout=b;
    layouts.add(b);
  }

// ---------------------------------------------------------------------
  void bin(String s)
  {
    char c;
    int i,n;
    Layout b;
    String m;
    String[] p=Cmd.bsplit(s);
    String s1=Util.strless(s," 0123456789ghmpsvz");
    if (0!=s1.length()) {
      JConsoleApp.theWd.error("unrecognized bin type: " + s1);
      return;
    }
    for (i=0; i<p.length; i++) {
      m=p[i];
      c=m.charAt(0);
      n=Util.c_strtoi(Util.q2s(m.substring(1)));
      if (c=='h'||c=='v'||c=='g')
        addlayout(new Layout(c,n,this));
      else if (c=='m' && layout.type!='g') {
        layout.setContentsMargins(n,n,n,n);
        layout.setSpacing(n);
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
          n=layouts.get(layouts.size()-1).stretch;
          layouts.remove(layouts.size()-1);
          layout=layouts.get(layouts.size()-1);
          layout.addLayout(b);
        }
      }
    }
  }

// ---------------------------------------------------------------------
  void fini()
  {
    if (0!=layouts.size()) {
      while (layouts.size()>1)
        bin("z");
//    setLayout(layout.bin);
      Log.d(JConsoleApp.LogTag,"pane fini addView");
      addView(layout.bin);
    }
    pform.closepane();
  }

// ---------------------------------------------------------------------
  void grid(String p, String v)
  {
// // decommit the name size in the next release
//   if (p.equals("shape")||p.equals("size"))) {
//     int rmax=0,cmax=0;
//     String[] opt=Cmd.qsplit(v);
//     int n=opt.length;
//     if (1>n) {
//       JConsoleApp.theWd.error("missing grid size row_size or column_size: " + p + " " + v);
//       return;
//     }
//     if (null==layout)
//       bin("v");
//     if ('g'!=layout.type)
//       bin("g");
//     if (1==n) {
//       rmax=-1;
//       cmax=Util.c_strtoi(Util.q2s(opt[0]));
//       if (cmax<=0) {
//         JConsoleApp.theWd.error("grid size column_size must be positive: " + p + " " + v);
//         return;
//       }
//     } else {
//       rmax=Util.c_strtoi(Util.q2s(opt[0]));
//       cmax=Util.c_strtoi(Util.q2s(opt[1]));
//       if ((rmax<=0)||(cmax<=0)) {
//         JConsoleApp.theWd.error("grid size row_size and column_size must be positive: " + p + " " + v);
//         return;
//       }
//     }
//     layout.rmax=rmax;
//     layout.cmax=cmax;
//     layout.razed=true;
//   } else if (p.equals("cell")) {
//     int r,c,rs,cs,alignment=0;
//     String[] opt=Cmd.qsplit(v);
//     int n=opt.length;
//     if (!(2==n || 3==n || 4==n || 5==n)) {
//       JConsoleApp.theWd.error("not grid cell row, column [,row_span, column_span] [,alignment]: " + p + " " + v);
//       return;
//     }
//     if (null==layout)
//       bin("v");
//     if ('g'!=layout.type)
//       bin("g");
//     if (layout.razed) {
//       JConsoleApp.theWd.error("grid is raze: " + p + " " + v);
//       return;
//     }
//     if (4>n) {
//       r=Util.c_strtoi(Util.q2s(opt[0]));
//       c=Util.c_strtoi(Util.q2s(opt[1]));
//       rs=cs=1;
//       if (3==n) alignment=Util.c_strtoi(Util.q2s(opt[2]));
//     } else {
//       r=Util.c_strtoi(Util.q2s(opt[0]));
//       c=Util.c_strtoi(Util.q2s(opt[1]));
//       rs=Util.c_strtoi(Util.q2s(opt[2]));
//       cs=Util.c_strtoi(Util.q2s(opt[3]));
//       if (5==n) alignment=Util.c_strtoi(Util.q2s(opt[4]));
//     }
//     if ((r<0)||(c<0)) {
//       JConsoleApp.theWd.error("grid cell row and column must be non-negative: " + p + " " + v);
//       return;
//     }
//     if ((rs<=0)||(cs<=0)) {
//       JConsoleApp.theWd.error("grid cell row_span and column_span must be positiv: " + p + " " + v);
//       return;
//     }
//     if (alignment<0) {
//       JConsoleApp.theWd.error("grid cell alignment must be non-negativ: " + p + " " + v);
//       return;
//     }
//     layout.r=r;
//     layout.c=c;
//     layout.rs=rs;
//     layout.cs=cs;
//     layout.alignment=alignment;
//   } else if (p.equals("colwidth")) {
//     String[] opt=Cmd.qsplit(v);
//     int c,w=0;
//     int n=opt.length;
//     if ((2>n) || (0!=(n&1))) {
//       JConsoleApp.theWd.error("grid colwidth must specify column and width: " + p + " " + v);
//       return;
//     }
//     for (int i=0; i<n ; i+=2) {
//       c=Util.c_strtoi(Util.q2s(opt[i]));
//       w=Util.c_strtoi(Util.q2s(opt[i+1]));
//       if ((c<0)||(w<0)) {
//         JConsoleApp.theWd.error("grid colwidth column and width must be non-negative: " + p + " " + v);
//         return;
//       }
//       if (layout.razed && (layout.cmax<=c)) {
//         JConsoleApp.theWd.error("grid colwidth invalid colum: " + p + " " + v);
//         return;
//       }
//       ((GridLayout )(layout.bin)).setColumnMinimumWidth(c,w);
//     }
//   } else if (p.equals("colstretch")) {
//     String[] opt=Cmd.qsplit(v);
//     int c,s=0;
//     int n=opt.length;
//     if ((2>n) || (0!=(n&1))) {
//       JConsoleApp.theWd.error("grid colstretch must specify column and stretch: " + p + " " + v);
//       return;
//     }
//     for (int i=0; i<n ; i+=2) {
//       c=Util.c_strtoi(Util.q2s(opt[i]));
//       s=Util.c_strtoi(Util.q2s(opt[i+1]));
//       if ((c<0)||(s<0)) {
//         JConsoleApp.theWd.error("grid colstretch column and stretch must be non-negative: " + p + " " + v);
//         return;
//       }
//       if (layout.razed && (layout.cmax<=c)) {
//         JConsoleApp.theWd.error("grid colstretch invalid column: " + p + " " + v);
//         return;
//       }
//       ((GridLayout )(layout.bin)).setColumnStretch(c,s);
//     }
//   } else if (p.equals("rowheight")) {
//     String[] opt=Cmd.qsplit(v);
//     int r,h=0;
//     int n=opt.length;
//     if ((2>n) || (0!=(n&1))) {
//       JConsoleApp.theWd.error("grid row height must specify row and height: " + p + " " + v);
//       return;
//     }
//     for (int i=0; i<n ; i+=2) {
//       r=Util.c_strtoi(Util.q2s(opt[i]));
//       h=Util.c_strtoi(Util.q2s(opt[i+1]));
//       if ((r<0)||(h<0)) {
//         JConsoleApp.theWd.error("grid rowheight row and height must be non-negative: " + p + " " + v);
//         return;
//       }
//       if (layout.razed && (layout.rmax>=0) && (layout.rmax<=r)) {
//         JConsoleApp.theWd.error("grid rowheight invalid row: " + p + " " + v);
//         return;
//       }
//       ((GridLayout )(layout.bin)).setRowMinimumHeight(r,h);
//     }
//   } else if (p.equals("rowstretch")) {
//     String[] opt=Cmd.qsplit(v);
//     int r,s=0;
//     int n=opt.length;
//     if ((2>n) || (0!=(n&1))) {
//       JConsoleApp.theWd.error("grid row stretch must specify row and height: " + p + " " + v);
//       return;
//     }
//     for (int i=0; i<n ; i+=2) {
//       r=Util.c_strtoi(Util.q2s(opt[i]));
//       s=Util.c_strtoi(Util.q2s(opt[i+1]));
//       if ((r<0)||(s<0)) {
//         JConsoleApp.theWd.error("grid rowstretch row and stretch must be non-negative: " + p + " " + v);
//         return;
//       }
//       if (layout.razed && (layout.rmax>=0) && (layout.rmax<=r)) {
//         JConsoleApp.theWd.error("grid rowstretch invalid row: " + p + " " + v);
//         return;
//       }
//       ((GridLayout )(layout.bin)).setRowStretch(r,s);
//     }
//   } else
//     JConsoleApp.theWd.error("bad grid command: " + p + " " + v);
  }

// ---------------------------------------------------------------------
  boolean groupbox(String c, String s)
  {
//   String cmd=Util.s2q(c);
//   String id;
//
//   if (cmd.equals("groupbox")) {
//     if (null==layout)
//       bin("v");
//     String[] opt=Cmd.qsplit(s);
//     if (opt.length)
//       id=opt[0];
//     groupboxw=new QGroupBox(id);
//     if (fontdef) groupboxw.setFont(fontdef.font);
//     layout.addWidget(groupboxw);
//     QVBoxLayout vb=new QVBoxLayout;
//     vb.addWidget(pform.addpane(0));
//     groupboxw.setLayout(vb);
//     pform.pane.bin("v");
//     return true;
//   }
//
//   if (cmd.equals("groupboxend")) {
//     bin("z");
//     int n=pform.panes.size();
//     if (n>1) {
//       Pane p=pform.panes[n-2];
//       if (p.groupboxw) {
//         fini();
//         p.groupboxw=0;
//         return true;
//       }
//       JConsoleApp.theWd.error("no groupbox to end: " + c + " " + s);
//       return false;
//     }
//   }
    return false;
  }

// ---------------------------------------------------------------------
  boolean line(String p, String s)
  {
//   String cmd=Util.s2q(p);
//   if (!(cmd.equals("line") || cmd.equals("lineh") || cmd.equals("linev"))))
//     return false;
//   QFrame *f=new QFrame();
//   f.setFrameShape((cmd.equals("linev")) ? QFrame::VLine : QFrame::HLine);
//   f.setFrameShadow(QFrame::Sunken);
//   layout.addWidget(f);
    return true;
  }

// ---------------------------------------------------------------------
  void setstretch(Child cc, String factor)
  {
//  if (layout.type!='g')((QBoxLayout )(layout.bin)).setStretchFactor(cc.widget,atoi(factor.Util.c_str()));
  }

// ---------------------------------------------------------------------
  boolean split(String p, String s)
  {
//   if (p.equals("splith") || p.equals("splitv"))) {
//     if (null==layout)
//       bin("v");
//     qsplitter=new QSplitter((p.equals("splith"))?Qt::Horizontal : Qt::Vertical);
//     qsplitter.addWidget(pform.addpane(1));
//     qsplitterp=qs2intlist(Util.s2q(s));
//     return true;
//   }
//
//   if (!(p.equals("splitend") || p.equals("splitsep")))) return false;
//
//   fini();
//   Pane sp=pform.pane;
//
//   if (p.equals("splitend"))
//     sp.splitend();
//   else
//     sp.Cmd.qsplitter.addWidget(pform.addpane(1));
    return true;
  }

// ---------------------------------------------------------------------
  void splitend()
  {
//   if (qsplitterp.size()==4)
//     qsplitter.setSizes(qsplitterp.substring(2));
//   if (qsplitterp.size()>=2) {
//     qsplitter.setStretchFactor(0,qsplitterp[0]);
//     qsplitter.setStretchFactor(1,qsplitterp[1]);
//   }
//   layout.addWidget(qsplitter);
  }
}
