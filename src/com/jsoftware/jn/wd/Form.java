package com.jsoftware.jn.wd;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Child;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Font;
import com.jsoftware.jn.wd.Font;
import com.jsoftware.jn.wd.JwdActivity;
import com.jsoftware.jn.wd.JMb;
import com.jsoftware.jn.wd.Menus;
import com.jsoftware.jn.wd.Pane;
import com.jsoftware.jn.wd.Tabs;
import com.jsoftware.jn.wd.Wd;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
// import com.jsoftware.jn.base.Jsvr;
// import com.jsoftware.jn.base.State;
// import com.jsoftware.jn.base.Svr;
// import com.jsoftware.jn.base.Term;
// import com.jsoftware.jn.wd.Opengl2;

public class Form extends LinearLayout
{
  com.jsoftware.j.JInterface jInterface = null;
  public JMb dialog;
  public boolean closed;
  public boolean shown;
  public int seq;
  public String id;
  public String event="";
  public String lasttype="";
  public String locale;
  public String sysdata="";
  public String sysmodifiers="";

  public Child child;
  public Child evtchild;
  public Child opengl;
  public ArrayList<Child >children;
  public Menus menubar;
  public Pane pane;
  public ArrayList<Pane >panes;
//  public QTimer timer;
  public Tabs tab;
  public ArrayList<Tabs >tabs;
//  public QSignalMapper signalMapper;

// private
  boolean backButtonPressed;
  boolean closeok;
  boolean escclose;
  String fakeid;
  String lastfocus="";
  JwdActivity activity;

// ---------------------------------------------------------------------
  public Form(String s, String p, JwdActivity activity, Form parent)
  {
    super(activity);
    this.activity=activity;
    activity.form=this;
    jInterface = com.jsoftware.j.android.JConsoleApp.theApp.jInterface;
    id=s;
    locale=activity.jlocale;
    seq=JConsoleApp.theWd.FormSeq++;
    panes=new ArrayList<Pane >();
    children=new ArrayList<Child>();
    tabs=new ArrayList<Tabs>();
    dialog=new JMb(this);

    String[] m=Util.s2q(p).split(" ");     // SkipEmptyParts
    if (JConsoleApp.theWd.invalidopt(s,m,"escclose closeok dialog popup minbutton maxbutton closebutton ptop owner nosize")) return;
    escclose=Util.sacontains(m,"escclose");
    closeok=Util.sacontains(m,"closeok");
    setpn(s);

//   Qt::WindowFlags flags=0;
//   if (m.contains("dialog")) flags=Qt::Dialog|Qt::WindowTitleHint|Qt::WindowStaysOnTopHint|Qt::CustomizeWindowHint;
//   if (m.contains("popup")) flags=Qt::Popup;
//   if (m.contains("minbutton")) flags|=Qt::WindowMinimizeButtonHint;
//   if (m.contains("maxbutton")) flags|=Qt::WindowMaximizeButtonHint;
//   if (m.contains("closebutton")) flags|=Qt::WindowCloseButtonHint;
//   if (m.contains("ptop")) flags|=Qt::WindowStaysOnTopHint;
//   if (m.contains("owner")) {
//     flags|=Qt::Window;
//     setWindowModality(Qt::WindowModal);
//   }
//  if (JConsoleApp.theWd.fontdef) setfont(JConsoleApp.theWd.fontdef.font);
//  if (JConsoleApp.theWd.!fontdef) setFont(QApplication::font());
//  setWindowFlags(flags);

    setpadding(0,0,0,0);
    setId(JConsoleApp.theWd.nextId++);
    setOrientation(LinearLayout.VERTICAL);
    LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
    lp.setMargins(0,0,0,0);
    setLayoutParams(lp);
    addpane(0);
//   signalMapper=new QSignalMapper(this);
//   connect(signalMapper,SIGNAL(mapped(View )),
//           this,SLOT(buttonClicked(View )));
//   timer=new QTimer(this);
//   connect(timer, SIGNAL(timeout()),this,SLOT(systimer()));
  }

// ---------------------------------------------------------------------
  public void dispose()
  {
    for (Child c : children) c.dispose();
    if (this==JConsoleApp.theWd.form) JConsoleApp.theWd.form = null;
    if (this==JConsoleApp.theWd.evtform) JConsoleApp.theWd.evtform = null;
    JConsoleApp.theWd.Forms.remove(this);
    if (JConsoleApp.theWd.Forms.isEmpty()) JConsoleApp.theWd.form=null;
    if (!JConsoleApp.theWd.Forms.isEmpty()) {
      JConsoleApp.theWd.form=JConsoleApp.theWd.Forms.get(JConsoleApp.theWd.Forms.size()-1);
      JConsoleApp.theWd.wdactivateform();
    }
    if (JConsoleApp.theWd.Forms.isEmpty() && (!Util.ShowIde)) {
//   var_cmddo("(i.0 0)\"_ (2!:55)0");
//   state_quit();
//    QApplication::quit();
    }
  }

// ---------------------------------------------------------------------
  public void close()
  {
    closed=true;
    activity.finish();
  }

// ---------------------------------------------------------------------
  public void show()
  {

  }

// ---------------------------------------------------------------------
  public void raise()
  {

  }

// ---------------------------------------------------------------------
  public void activateWindow()
  {

  }

// ---------------------------------------------------------------------
  public void resize(int w, int h)
  {

  }

// ---------------------------------------------------------------------
  public void addchild(Child c)
  {
    child=c;
    children.add(c);
  }

// ---------------------------------------------------------------------
  public void addmenu()
  {
    menubar= new Menus("menu","",this,null);
    addchild((Child ) menubar);
    menubar.initmenu();
  }

// ---------------------------------------------------------------------
  public Pane addpane(int n)
  {
    pane=new Pane(n,this);
    panes.add(pane);
    return pane;
  }

// ---------------------------------------------------------------------
  public void backButtonTimer()
  {
    backButtonPressed=false;
    if (2>JConsoleApp.theWd.Forms.size()) return;
    JConsoleApp.theWd.Forms.remove(this);
    JConsoleApp.theWd.Forms.add(0,this);
    JConsoleApp.theWd.form=JConsoleApp.theWd.Forms.get(JConsoleApp.theWd.Forms.size()-1);
    JConsoleApp.theWd.wdactivateform();
  }

// // ---------------------------------------------------------------------
// void changeEvent(QEvent e)
// {
//   if(e.type()==QEvent::ActivationChange && isActiveWindow() && seq < JConsoleApp.theWd.FormSeq-1)
//     seq=JConsoleApp.theWd.FormSeq++;
//   View::changeEvent(e);
// }
//
// // ---------------------------------------------------------------------
// void closeEvent(QCloseEvent e)
// {
//   if (closeok || closed) {
//     e.accept();
//     return;
//   }
//   e.ignore();
//   event="close";
//   fakeid="";
//   JConsoleApp.theWd.form=this;
//   signalevent(null);
//   if (closed) {
//     e.accept();
//   } else e.ignore();
// }
//
// ---------------------------------------------------------------------
// close if not the main pane
  public void closepane()
  {
    if (panes.size()<=1) return;
    panes.remove(panes.size()-1);
    pane=panes.get(panes.size()-1);
  }

// ---------------------------------------------------------------------
  public byte[] get(String p,String v)
  {
    String r="";
    if ((!v.isEmpty()) && p!="extent") {
      JConsoleApp.theWd.error("extra parameters: " + p + " " + v);
      return new byte[] {};
    }
    if (p.equals("property")) {
      StringBuilder r1a=new StringBuilder();
      r1a.append(new String("caption")+"\012"+ "children"+"\012"+ "enable"+"\012"+ "extent"+"\012"+ "focus"+"\012");
      r1a.append(new String("focusable")+"\012"+ "font"+"\012"+ "hasfocus"+"\012"+ "hwnd"+"\012");
      r1a.append(new String("id")+"\012"+ "lastfocus"+"\012"+ "locale"+"\012");
      r1a.append(new String("maxwh")+"\012"+ "minwh"+"\012"+ "property"+"\012"+ "state"+"\012");
      r1a.append(new String("stylesheet")+"\012"+ "sysdata"+"\012"+ "sysmodifiers"+"\012");
      r1a.append(new String("tooltip")+"\012"+ "visible"+"\012"+ "wh"+"\012"+ "xywh"+"\012");
      r=r1a.toString();
    } else if (p.equals("caption")) {
//    r=Util.q2s(this.windowTitle());
    } else if (p.equals("children")) {
      StringBuilder r1a=new StringBuilder();
      for (int i=0; children.size()>i; i++)
        r1a.append(children.get(i).id+"\012");
      r=r1a.toString();
    } else if (p.equals("enable")) {
      r=Util.i2s((this.isEnabled())?1:0);
    } else if (p.equals("extent")) {
//    QFontMetrics fm = QFontMetrics(this.font());
//    r=Util.i2s(fm.width(Util.s2q(v)))+" "+Util.i2s(fm.height());
      r="1 1";
    } else if (p.equals("focus")) {
      r=this.getfocus();
    } else if (p.equals("focusable")) {
      r=Util.i2s(isFocusable()?1:0);
    } else if (p.equals("font")) {
      r=getfont();
    } else if (p.equals("hasfocus")) {
      r=Util.i2s(hasFocus()?1:0);
    } else if (p.equals("hwnd")) {
      r=this.hsform();
    } else if (p.equals("id")) {
      r=id;
    } else if (p.equals("lastfocus")) {
      r=lastfocus;
    } else if (p.equals("locale")) {
      r=locale;
    } else if (p.equals("minwh")) {
// need API 16
//    r=Util.i2s(getMinimumWidth())+" "+Util.i2s(getMinimumHeight());
      r=Util.i2s(0)+" "+Util.i2s(0);
    } else if (p.equals("state")) {
      r=Util.ba2s(this.state(0));
    } else if (p.equals("sysdata")) {
      r=sysdata;
    } else if (p.equals("sysmodifiers")) {
      r=getsysmodifiers();
    } else if (p.equals("visible")) {
      r=Util.i2s((View.VISIBLE==getVisibility())?1:0);
    } else if (p.equals("wh")) {
      r=Util.i2s(this.getWidth())+" "+Util.i2s(this.getHeight());
    } else if (p.equals("xywh")) {
      r=Util.i2s(this.getLeft())+" "+Util.i2s(this.getTop())+" "+Util.i2s(this.getWidth())+" "+Util.i2s(this.getHeight());
    } else
      JConsoleApp.theWd.error("get command not recognized: " + p + " " + v);
    return Util.s2ba(r);
  }

// ---------------------------------------------------------------------
  String getfont()
  {
// return Util.q2s(fontspec(this.font()));
    return"";
  }

// ---------------------------------------------------------------------
  public String getsysmodifiers()
  {
    return Util.i2s(0);
  }

// ---------------------------------------------------------------------
  public String getfocus()
  {
    View w=findFocus();
    if ((null==w) || (0==children.size())) return "";
    for (int i=this.children.size()-1; 0<=i; i--) {
      View c;
// TODO isAncestorOf not available
//    if ((c=this.children.get(i).widget) && (w==c || c.isAncestorOf(w)))
      if ((null!=(c=this.children.get(i).widget)) && (w==c))
        return this.children.get(i).id;
    }
    return "";
  }

// ---------------------------------------------------------------------
  public String hschild()
  {
    return Util.i2s((null!=child && (null!=child.widget))?child.widget.getId():0);
  }

// ---------------------------------------------------------------------
  public String hsform()
  {
    Log.d(JConsoleApp.LogTag,"form hwnd "+id+ " "+getId());
    return Util.i2s(getId());
  }

// ---------------------------------------------------------------------
  public byte[] qform()
  {
    return Util.s2ba(Util.i2s(this.getLeft())+" "+Util.i2s(this.getTop())+" "+Util.i2s(this.getWidth())+" "+Util.i2s(this.getHeight()));
  }

// ---------------------------------------------------------------------
  public Child id2child(String n)
  {
    for (int i=0; i<children.size(); i++)
      if ((!children.get(i).type.equals("menu")) && children.get(i).id.equals(n))
        return children.get(i);
    return (Child ) null;
  }

// ---------------------------------------------------------------------
  public boolean ischild(Child n)
  {
    return children.contains(n);
  }


// ---------------------------------------------------------------------
// void keyPressEvent(QKeyEvent e)
// {
//   int k=e.key();
//   if (ismodifier(k)) return;
//   if (k==KEYCODE_Back) {
//     View::keyPressEvent(e);
//     return;
//   }
//   if (k==KEYCODE_Escape) {
//     e.ignore();
//     if (closed) return;
//     if (escclose) {
//       if (closeok) {
//         closed=true;
//         close();
//       } else  {
//         event="close";
//         fakeid="";
//         JConsoleApp.theWd.form=this;
//         signalevent(null);
//       }
//     } else {
//       event="cancel";
//       fakeid="";
//       JConsoleApp.theWd.form=this;
//       signalevent(null);
//     }
//   } else if (k>=KEYCODE_F1 && k<=KEYCODE_F12) {
//     event="fkey";
//     JConsoleApp.theWd.form=this;
//     signalevent(null,e);
//   } else if (k>=KEYCODE_A && k<=KEYCODE_Z && (e.modifiers() & Qt::ControlModifier)) {
//     event="fkey";
//     JConsoleApp.theWd.form=this;
//     signalevent(null,e);
//   } else
//     View::keyPressEvent(e);
// }
//
// // ---------------------------------------------------------------------
// void keyReleaseEvent(QKeyEvent e)
// {
//   if (e.key()==KEYCODE_Back) {
//     if (!(backButtonPressed||(Qt::NonModal!=windowModality()))) {
//       backButtonPressed=true;
//       QTimer::singleShot(2000, this, SLOT(backButtonTimer()));
//     } else {
//       if (closed) return;
//       if (closeok) {
//         closed=true;
//         close();
//       } else {
//         event="close";
//         fakeid="";
//         JConsoleApp.theWd.form=this;
//         signalevent(null);
//       }
//     }
//   } else View::keyReleaseEvent(e);
// }
//
// ---------------------------------------------------------------------
  public void set(String p,String v)
  {
    if (p.equals("enable")) {
      setEnabled(Util.remquotes(v)!="0");
    } else if (p.equals("font")) {
      setfont(v);
    } else if (p.equals("invalid")) {
      invalidate();
    } else if (p.equals("show")||p.equals("visible")) {
      setVisibility((Util.remquotes(v)!="0")?View.VISIBLE:View.GONE);
    } else if (p.equals("wh")) {
      JConsoleApp.theWd.wdsetwh(this,v);
    } else
      JConsoleApp.theWd.error("set command not recognized: " + p + " " + v);
  }

// ---------------------------------------------------------------------
  void setfont(String p)
  {
//  this.setFont((Font(p)).font);
  }

// ---------------------------------------------------------------------
  public Child setmenuid(String id)
  {
    if (null!=menubar && 0!=menubar.getValue(Util.s2q(id)))
      return (Child ) menubar;
    else
      return null;
  }
//
// ---------------------------------------------------------------------
  public void setpadding(int l,int t,int r,int b)
  {
    setPadding(l,t,r,b);
  }

// ---------------------------------------------------------------------
  public void setpn(String p)
  {
//  setTitle(Util.s2q(p).toString());
  }

// ---------------------------------------------------------------------
  public void setpicon(String p)
  {
//  setWindowIcon(QIcon(Util.s2q(p)));
  }

// ---------------------------------------------------------------------
  public void settimer(String p)
  {
    int n=Util.c_strtoi(p);
//   if (n)
//     timer.start(n);
//   else
//     timer.stop();
  }
//
// ---------------------------------------------------------------------
  public void showit(String p)
  {
    if (!shown) {
// showide(false);
//      if (JConsoleApp.theWd.Forms.size()>1)
//        (JConsoleApp.theWd.Forms.get(JConsoleApp.theWd.Forms.size()-2)).setVisibility(View.GONE);
      for (int i=tabs.size()-1; i>=0; i--)
        tabs.get(i).tabend();
      for (int i=panes.size()-1; i>=0; i--)
        panes.get(i).fini();

      Log.d(JConsoleApp.LogTag,"Form showit addView");
      addView(pane);

//     Button w=new Button(activity);
//     w.setText("button");
//     LayoutParams lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//     w.setLayoutParams(lp);
//     addView(w);

      Log.d(JConsoleApp.LogTag,"Form setContentView");
      activity.setContentView(this);
//      requestLayout();
      shown=true;

//     if (p.equals("fullscreen"))
//       showFullScreen();
//     else if (p.equals("maximized"))
//       showMaximized();
//     else if (p.equals("minimized"))
//       showMinimized();
//     else if (p.equals("normal"))
//       showNormal();    // restore from maximized or minimized
//     else
//       show();          // show or hide
    }
  }

// ---------------------------------------------------------------------
  public void signalevent(Child c)
  {
    signalevent(c,null);
  }

// ---------------------------------------------------------------------
  public void signalevent(Child c, KeyEvent e)
  {
    if ((0!=Util.NoEvents) || closed) return;
    String loc = locale;
    JConsoleApp.theWd.evtform=this;
    if (null!=c) {
      evtchild=c;
      c.setform();
      sysmodifiers=c.sysmodifiers;
      if (sysmodifiers.isEmpty())
        sysmodifiers=getsysmodifiers();
      sysdata=c.sysdata;
      loc = (!c.locale.isEmpty())?c.locale:locale;
    } else {
      evtchild=null;
      if (event.equals("dialogpositive")) {
        fakeid="dialog";
        event="positive";
      } else if (event.equals("dialognegative")) {
        fakeid="dialog";
        event="negative";
      } else if (event.equals("dialogneutral")) {
        fakeid="dialog";
        event="neutral";
      } else if (event.equals("fkey")) {
        int k=e.getKeyCode();
        int meta=e.getMetaState();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
          if (k>=KeyEvent.KEYCODE_A && k<=KeyEvent.KEYCODE_Z && e.isCtrlPressed()) {
            fakeid=Character.toString((char)(k+32));  // lower case
            fakeid=fakeid + "ctrl" + new String( (e.isShiftPressed()) ? "shift" : "" );
          } else if (k>=KeyEvent.KEYCODE_F1 && k<=KeyEvent.KEYCODE_F12) {
            fakeid="f"+ Character.toString((char)(k+1-KeyEvent.KEYCODE_F1)) + new String((e.isCtrlPressed()) ? "ctrl" : "") + new String((e.isShiftPressed()) ? "shift" : "");
          }
        } else {
          if (k>=KeyEvent.KEYCODE_F1 && k<=KeyEvent.KEYCODE_F12) {
            fakeid="f"+ Character.toString((char)(k+1-KeyEvent.KEYCODE_F1)) + new String((e.isShiftPressed()) ? "shift" : "");
          }
        }
      }
    }
    String fc=getfocus();
    if (!fc.isEmpty()) lastfocus=fc;
    boolean jecallback=false;
    if (jecallback) {
//     term.removeprompt();
      jInterface.Jnicmd("wdhandlerx_jca_ '" + Util.s2q(loc) + "'");
    } else
      jInterface.Jnicmd("wdhandler_" + Util.s2q(loc) + "_$0");
  }

// ---------------------------------------------------------------------
  public byte[] state(int evt)
  {
    String c,c1,e,s,ec="";

    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (0!=evt) {
        if (null!=evtchild) {
          c=evtchild.eid;
          e=evtchild.event;
          ec=evtchild.locale;
        } else {
          c=fakeid;
          e=event;
        }
        c1=(c.isEmpty()) ? new String("") : (c+"_") ;
        r.write(Util.spair("syshandler",id+"_handler"));
        r.write(Util.spair("sysevent",id+"_"+c1+e));
        r.write(Util.spair("sysdefault",id+"_default"));
        r.write(Util.spair("sysparent",id));
        r.write(Util.spair("syschild",c));
        r.write(Util.spair("systype",e));
        r.write(Util.spair("syslocalec",ec));
        Log.d(JConsoleApp.LogTag,"event "+id+"_"+c1+e);
      }

      // need only syslocale (not syslocalep, syslocalec)?...  in isigraph
      r.write(Util.spair("syslocalep",locale));
      r.write(Util.spair("syshwndp",hsform()));
      r.write(Util.spair("syshwndc",hschild()));
      r.write(Util.spair("syslastfocus",lastfocus));
      r.write(Util.spair("sysfocus",getfocus()));
      r.write(Util.spair("sysmodifiers",sysmodifiers));
      r.write(Util.spair("sysdata",sysdata));

      for (int i=0; i<children.size(); i++)
        r.write(children.get(i).state());
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,"IOException");
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,"Exception");
    }

    return r.toByteArray();
  }

// ---------------------------------------------------------------------
// for debugging
  void status(String s)
  {
//  qDebug() << "form status: " << Util.s2q(s);
//  qDebug() << "current pane, panes: " << pane << panes;
//  qDebug() << "current tab, tabs: " << tab << tabs;
  }

// ---------------------------------------------------------------------
  public void systimer()
  {
    event="timer";
    fakeid="";
    signalevent(null);
  }

}
