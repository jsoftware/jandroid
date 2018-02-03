package com.jsoftware.jn.wd;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.util.ArrayList;

class Pane extends LinearLayout
{

  Form pform;
  private String event;
  private String lasttype;
  private String locale;
  private String sysdata;
  private String sysmodifiers;
  float weight=0f;
  int sizew=-2;  // wrap_content
  int sizeh=-2;
  int minsizew;
  int minsizeh;

  RadioGroup buttongroup;
  private Child child;
  private Child evtchild;
  Layout layout;
  ArrayList<Layout >layouts;

// ---------------------------------------------------------------------
  Pane(int n,Form f)
  {
    super(f.activity);
    setOrientation(LinearLayout.VERTICAL);
    ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
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
    else if (c.equals("checkbox"))
      child=(Child ) new JCheckBox(n,p,pform,this);
    else if (c.equals("combobox"))
      child=(Child ) new JSpinner(n,p,pform,this,"combobox");
    else if (c.equals("combolist"))
      child=(Child ) new JSpinner(n,p,pform,this,"combolist");
    else if (c.equals("datepicker"))
      child=(Child ) new JDatePicker(n,p,pform,this);
    else if (c.equals("edit"))
      child=(Child ) new JEditText(n,p,pform,this,"edit");
    else if (c.equals("editm"))
      child=(Child ) new JEditText(n,p,pform,this,"editm");
    else if (c.equals("image"))
      child=(Child ) new JImageView(n,p,pform,this);
    else if (c.equals("isidraw"))
      child=(Child ) new JIsigraph(n,p,pform,this,"isidraw");
    else if (c.equals("isigraph"))
      child=(Child ) new JIsigraph(n,p,pform,this,"isigraph");
//   else if (c.equals("isigrid"))
//     child=(Child ) new IsiGrid(n,p,pform,this);
    else if (c.equals("listbox"))
      child=(Child ) new JListView(n,p,pform,this);
    else if (!JConsoleApp.theApp.asyncj && c.equals("opengl"))
      child=(Child ) new JOpengl(n,p,pform,this);
    else if (c.equals("progressbar"))
      child=(Child ) new JProgressBar(n,p,pform,this);
    else if (c.equals("radiobutton"))
      child=(Child ) new JRadioButton(n,p,pform,this);
    else if (c.equals("seekbar"))
      child=(Child ) new JSeekBar(n,p,pform,this);
    else if (c.equals("spinbox"))
      child=(Child ) new JSpinner(n,p,pform,this,"spinbox");
    else if (c.equals("static"))
      child=(Child ) new JTextView(n,p,pform,this);
    else if (c.equals("switch")) {
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        child=(Child ) new JSwitch(n,p,pform,this);
      else
        child=(Child ) new JCheckBox(n,p,pform,this);
//   } else if (c.equals("table")) }
//     child=(Child ) new Table(n,p,pform,this);
    } else if (c.equals("timepicker"))
      child=(Child ) new JTimePicker(n,p,pform,this);
    else if (c.equals("togglebutton"))
      child=(Child ) new JToggleButton(n,p,pform,this);
//   else if (c.equals("toolbar"))
//     child=(Child ) new ToolBar(n,p,pform,this);
    else if (c.equals("videoview"))
      child=(Child ) new JVideoView(n,p,pform,this);
    else if (c.equals("webview"))
      child=(Child ) new JWebView(n,p,pform,this);
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
    if (child.type.equals("radiobutton")) {
      if (child.grouped)
        layout.addWidget(buttongroup);
    } else
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
  void addbin(Layout b)
  {
    layout=b;
    layouts.add(b);
  }

// ---------------------------------------------------------------------
  void bin(String s)
  {
    bin(s,"");
  }
// ---------------------------------------------------------------------
  void bin(String s, String id)
  {
    char c;
    int i,n;
    Layout b;
    String m;
    String[] p=Cmd.bsplit(s);
    String s1=Util.strless(s," 0123456789"+Wd.bintype);
    if (0!=s1.length()) {
      JConsoleApp.theWd.error("unrecognized bin type: " + s1);
      return;
    }
    if (0==p.length) {
      JConsoleApp.theWd.error("missing bin type: " + s);
      return;
    }
    for (i=0; i<p.length; i++) {
      m=p[i];
      c=m.charAt(0);
      n=Util.c_strtoi(m.substring(1));
      if (c=='h'||c=='v'||c=='u'||c=='l'||c=='g') {
        addbin(new Layout(c,n,this));
        if ((!id.isEmpty()) && 0==i) {
          layout.bin.setId(JConsoleApp.theWd.nextId++);
          pform.binx.put(id,layout.bin.getId());
        }
      } else if (c=='m' && layout.type!='g') {
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
//       cmax=Util.c_strtoi(opt[0]);
//       if (cmax<=0) {
//         JConsoleApp.theWd.error("grid size column_size must be positive: " + p + " " + v);
//         return;
//       }
//     } else {
//       rmax=Util.c_strtoi(opt[0]);
//       cmax=Util.c_strtoi(opt[1]);
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
//       r=Util.c_strtoi(opt[0]);
//       c=Util.c_strtoi(opt[1]);
//       rs=cs=1;
//       if (3==n) alignment=Util.c_strtoi(opt[2]);
//     } else {
//       r=Util.c_strtoi(opt[0]);
//       c=Util.c_strtoi(opt[1]);
//       rs=Util.c_strtoi(opt[2]);
//       cs=Util.c_strtoi(opt[3]);
//       if (5==n) alignment=Util.c_strtoi(opt[4]);
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
//       c=Util.c_strtoi(opt[i]);
//       w=Util.c_strtoi(opt[i+1]);
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
//       c=Util.c_strtoi(opt[i]);
//       s=Util.c_strtoi(opt[i+1]);
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
//       r=Util.c_strtoi(opt[i]);
//       h=Util.c_strtoi(opt[i+1]);
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
//       r=Util.c_strtoi(opt[i]);
//       s=Util.c_strtoi(opt[i+1]);
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
  @SuppressWarnings( "deprecation" )
  boolean line(String p, String s)
  {
    String cmd=p;
    if (!(cmd.equals("line") || cmd.equals("lineh") || cmd.equals("linev")))
      return false;
    View f = new View(pform.activity);
    ViewGroup.LayoutParams lp;
    boolean vertical = (cmd.equals("linev")) ? true : (cmd.equals("lineh")) ? false : (layout.type=='h');
    int wh = (int)(0.5f+JConsoleApp.theWd.dmdensity*1);
    if (vertical)
      lp=new ViewGroup.LayoutParams(wh, LayoutParams.MATCH_PARENT);
    else
      lp=new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, wh);
    f.setLayoutParams(lp);
    TypedArray array = pform.activity.getTheme().obtainStyledAttributes(new int[] {android.R.attr.listDivider});
    Drawable draw = array.getDrawable(0);
    array.recycle();
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
      f.setBackground(draw);
    else
      f.setBackgroundDrawable(draw);
    layout.addWidget(f);
    return true;
  }


// ---------------------------------------------------------------------
  void setstretch(Child cc, String factor)
  {
//  if (layout.type!='g')((QBoxLayout )(layout.bin)).setStretchFactor(cc.widget,atoi(factor.Util.c_str()));
  }

}
