package com.jsoftware.jn.wd;

import android.widget.TabHost;
import android.widget.TabHost;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Pane;
import com.jsoftware.jn.wd.Wd;
import java.util.ArrayList;
import java.util.List;

public class Tabs extends Child
{
  public  int index;

// ---------------------------------------------------------------------
  public Tabs(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="tabs";

    TabHost w=new TabHost(f.activity);
    widget=(TabHost) w;

    pform.tabs.add(this);
    pform.tab=this;
    String qn=Util.s2q(n);
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"documentmode movable closable east west south nobar")) return;
//  w.setObjectName(qn);
    childStyle(opt);
//  w.setUsesScrollButtons(true);

//   if (Util.sacontains(opt,"documentmode"))
//     w.setDocumentMode(true);
//
//   if (Util.sacontains("movable"))
//     w.setMovable(true);
//
//   if (Util.sacontains("closable"))
//     w.setTabsClosable(true);
//
//   if (Util.sacontains(opt,"east"))
//     w.setTabPosition(TabHost::East);
//   else if (Util.sacontains(opt,"west"))
//     w.setTabPosition(TabHost::West);
//   else if (Util.sacontains(opt,"south"))
//     w.setTabPosition(TabHost::South);
//
//   if (Util.sacontains(opt,"nobar"))
//     w.nobar(true);
//
//   connect(w,SIGNAL(currentChanged(int)),
//           this,SLOT(currentChanged(int)));
//
//   connect(w,SIGNAL(tabCloseRequested(int)),
//           this,SLOT(tabCloseRequested(int)));

  }

// ---------------------------------------------------------------------
  void currentChanged(int ndx)
  {
    event="select";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  byte[] get(String p,String v)
  {
//   TabHost w = (TabHost )widget;
//   String r;
//   if (p.equals("property")) {
//     r+=String("label")+"\012"+ "select"+"\012";
//     r+=super.get(p,v);
//   }
//   if (p.equals("label"||p=="select")) {
//     int n=w.currentIndex();
//     String s,t;
//     for (int i=0; i<w.count(); i++)
//       s+=Util.q2s(w.tabText(i)) + '\377';
//     t=(n>=0)?Util.i2s(n):String("_1");
//     if (p.equals("label"))
//       r=s;
//     else
//       r=t;
//   } else
//     r=super.get(p,v);
//   return r;
    return new byte[0];
  }

// ---------------------------------------------------------------------
  void set(String p,String v)
  {
//   TabHost w = (TabHost )widget;
//   String[] opt=Cmd.qsplit(v);
//   if (opt.isEmpty()) return;
//   int ndx=Util.c_strtoi(Util.q2s(opt.at(0)));
//   if (p.equals("active"))
//     w.setCurrentTab(ndx);
//   else if (p.equals("tabclose"))
//     w.removeTab(ndx);
//   else if (p.equals("label")) {
//     if (opt.size()<2) return;
//     w.setTabText(ndx,Util.s2q(Util.remquotes(Util.q2s(opt.at(1)))));
//   } else if (p.equals("tabenabled")) {
//     if (opt.size()<2) w.setTabEnabled(ndx,true);
//     w.setTabEnabled(ndx,Util.remquotes(Util.q2s(opt.at(1)))!="0");
//   } else if (p.equals("icon")) {
//     if (opt.size()<2) return;
//     String iconFile=Util.remquotes(Util.q2s(opt.at(1)));
//     QIcon image;
//     int spi;
//     if (iconFile.startsWith("qstyle::") && -1!=(spi=wdstandardicon(iconFile)))
//       w.setTabIcon(ndx,w.style().standardIcon((QStyle::StandardPixmap)spi));
//     else
//       w.setTabIcon(ndx,QIcon(Util.s2q(iconFile)));
//   } else if (p.equals("tooltip")) {
//     if (opt.size()<2) w.setTabToolTip(ndx,"");
//     w.setTabToolTip(ndx,Util.s2q(Util.remquotes(Util.q2s(opt.at(1)))));
//   } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  public byte[] state()
  {
    return new byte[0];
//   TabHost w=(TabHost) widget;
//   int n;
//   if (this==pform.evtchild && event.equals("tabclose"))
//     n=index;
//   else
//     n=w.currentIndex();
//   String r,s,t;
//   for (int i=0; i<w.count(); i++)
//     s+=Util.q2s(w.tabText(i)) + '\377';
//   t=(n>=0)?Util.i2s(n):String("_1");
//   r+=spair(id,s);
//   r+=spair(id+"_select",t);
//   return r;
  }

// ---------------------------------------------------------------------
  void tabCloseRequested(int ndx)
  {
    index=ndx;
    event="tabclose";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  void tabend()
  {
    TabHost w=(TabHost ) widget;
    pform.pane.fini();

    pform.tabs.remove(pform.tabs.size()-1);
    if (pform.tabs.isEmpty())
      pform.tab=null;
    else
      pform.tab=pform.tabs.get(pform.tabs.size()-1);

    if (0!=index)
      w.setCurrentTab(0);
  }

// ---------------------------------------------------------------------
  void tabnew(String p)
  {
    if (0!=index)
      pform.pane.fini();
    TabHost w=(TabHost ) widget;
    pform.addpane(1);
//  w.addTab(pform.pane, Util.s2q(p));
    index++;
  }
}
