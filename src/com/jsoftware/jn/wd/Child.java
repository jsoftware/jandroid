package com.jsoftware.jn.wd;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;

class Child
{

  boolean grouped;
  String id;
  String eid;  // for event
  String event;
  String parms;
  String type;
  String locale="";  // for isigraph
  String sysdata="";
  String sysmodifiers="";
  Form pform;
  Pane ppane;
  View widget;
  float weight=0f;
  int handle=0;
  boolean mPause = false;

  private JWdActivity activity;

// ---------------------------------------------------------------------
  Child(String n, String s, Form f, Pane p)
  {
    this.activity=f.activity;
    id=n;
    eid=n;
    parms=s;
    pform=f;
    ppane=p;
  }

// ---------------------------------------------------------------------
  void childStyle(String[] opt)
  {

  }

// ---------------------------------------------------------------------
  void dispose()
  {
  }

// ---------------------------------------------------------------------
  void onPause()
  {
    mPause = true;
  }

// ---------------------------------------------------------------------
  void onResume()
  {
    mPause = false;
  }

// ---------------------------------------------------------------------
  byte[] state()
  {
    return new byte[] {};
  }

// ---------------------------------------------------------------------
  void cmd(String p, String v)
  {
  }

// ---------------------------------------------------------------------
  byte[] get(String p,String v)
  {
    String r="";
    if (!v.isEmpty() && !p.equals("extent")) {
      JConsoleApp.theWd.error("extra parameters: " + id + " " + p + " " + v);
      return new byte[] {};
    }
    if (p.equals("property")) {
      StringBuilder r1=new StringBuilder();
      r1.append("enable"+"\012"+ "extent"+"\012");
      r1.append("focusable"+"\012"+ "font"+"\012"+ "hasfocus"+"\012"+ "hwnd"+"\012");
      r1.append("id"+"\012"+ "locale"+"\012");
      r1.append("minwh"+"\012"+ "parent"+"\012");
      r1.append("property"+"\012"+ "state"+"\012");
      r1.append("type"+"\012"+ "visible"+"\012"+ "wt"+"\012"+ "wh"+"\012"+ "wwh"+"\012"+ "xywh"+"\012");
      r=r1.toString();
    } else if (p.equals("enable")) {
      if (null!=widget) r=Util.i2s((widget.isEnabled())?1:0);
    } else if (p.equals("extent")) {
      if (null!=widget) {
//      QFontMetrics fm = QFontMetrics(widget.font());
//      r=Util.i2s(fm.width(v))+" "+Util.i2s(fm.height());
        r="1 1";
      }
    } else if (p.equals("focusable")) {
      if (null!=widget) {
        r=Util.i2s(widget.isFocusable()?1:0);
      }
    } else if (p.equals("font")) {
      if (null!=widget) r=getfont();
    } else if (p.equals("hasfocus")) {
      if (null!=widget) r=Util.i2s(widget.hasFocus()?1:0);
    } else if (p.equals("hwnd")) {
      r=Util.i2s((null!=widget)?widget.getId():0);
    } else if (p.equals("id")) {
      r=id;
    } else if (p.equals("locale")) {
      r=(!locale.isEmpty())?locale:pform.locale;
    } else if (p.equals("minwh")) {
      if (null!=widget) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
          r=Util.i2s(widget.getMinimumWidth())+" "+Util.i2s(widget.getMinimumHeight());
        else
          r=Util.i2s(0)+" "+Util.i2s(0);
      }
    } else if (p.equals("parent")) {
      r=pform.id;
    } else if (p.equals("state")) {
      r=Util.ba2s(state());
    } else if (p.equals("type")) {
      r=type;
    } else if (p.equals("visible")) {
      if (null!=widget) r=Util.i2s((View.VISIBLE==widget.getVisibility())?1:0);
    } else if (p.equals("wt")) {
      r=Util.d2s((double)weight);
    } else if (p.equals("wh")) {
      r=Util.i2s(widget.getWidth())+" "+Util.i2s(widget.getHeight());
    } else if (p.equals("wwh")) {
      r=Util.d2s((double)weight)+" "+Util.i2s(widget.getWidth())+" "+Util.i2s(widget.getHeight());
    } else if (p.equals("xywh")) {
      if (null!=widget) {
        r=Util.i2s(widget.getLeft())+" "+Util.i2s(widget.getTop())+" "+Util.i2s(widget.getWidth())+" "+Util.i2s(widget.getHeight());
      }
    } else
      JConsoleApp.theWd.error("get command not recognized: " + id + " " + p + " " + v);
    return Util.s2ba(r);
  }

// ---------------------------------------------------------------------
  String getfont()
  {
    if (widget instanceof TextView) {
//     return fontspec(((TextView)widget).getTypeface());
      return"";
    } else
      return"";
  }

// ---------------------------------------------------------------------
  String getsysdata()
  {
    return "";
  }

// ---------------------------------------------------------------------
  void set(String p,String v)
  {
    if (p.equals("enable")) {
      if (null!=widget) widget.setEnabled(!Util.remquotes(v).equals("0"));
    } else if (p.equals("locale")) {
      locale=Util.remquotes(v);
    } else if (p.equals("focus")) {
      if (null!=widget) widget.requestFocus();
    } else if (p.equals("focusable")) {
      if (null!=widget) widget.setFocusable(!Util.remquotes(v).equals("0"));
    } else if (p.equals("font")) {
      if (null!=widget) setfont(v);
    } else if (p.equals("invalid")) {
      if (null!=widget) widget.invalidate();
    } else if (p.equals("nofocus")) {
      if (null!=widget) widget.setFocusable(false);
    } else if (p.equals("show")||p.equals("visible")) {
      if (null!=widget) widget.setVisibility((!Util.remquotes(v).equals("0"))?View.VISIBLE:View.GONE);
    } else if (p.equals("wh")) {
      setwh(v);
    } else if (p.equals("wt")) {
      setweight(v);
    } else if (p.equals("wwh")) {
      setwwh(v);
    } else if (p.equals("minwh")) {
      setminwh(v);
    } else
      JConsoleApp.theWd.error("set command not recognized: " + id + " " + p + " " + v);
  }

// ---------------------------------------------------------------------
  void setfont(String p)
  {
    Font f=new Font(p);
    if (widget instanceof TextView) {
      ((TextView)widget).setTypeface(f.font);
      ((TextView)widget).setTextSize((float)f.fontsize);
    }
  }

// ---------------------------------------------------------------------
  void setform()
  {
    JConsoleApp.theWd.form=pform;
    JConsoleApp.theWd.form.child=this;
  }

// ---------------------------------------------------------------------
  void setweight(String p)
  {
    String[] n=Util.qsless(p.split(" "),new String[] {""});     // SkipEmptyParts
    if (n.length!=1) {
      JConsoleApp.theWd.error("set wt requires 1 number: " + id + " " + p);
    } else
      setweight((float)Util.c_strtod(n[0]));
  }

// ---------------------------------------------------------------------
  void setweight(float wt)
  {
    weight=wt;
  }

// ---------------------------------------------------------------------
  void setwh(String p)
  {
    if (null==widget) return;
    String[] n=Util.qsless(p.split(" "),new String[] {""});     // SkipEmptyParts
    if (n.length!=2) {
      JConsoleApp.theWd.error("set wh requires 2 numbers: " + id + " " + p);
    } else {
      int w=Util.c_strtoi(n[0]);
      int h=Util.c_strtoi(n[1]);
      setwh(w,h);
    }
  }

// ---------------------------------------------------------------------
  void setwh(int w, int h)
  {
    if ((null!=widget) && ((-3!=w) || (-3!=h)))
      widget.setLayoutParams(new LinearLayout.LayoutParams((-3==w)?widget.getLayoutParams().width:(w>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*w)):w,(-3==h)?widget.getLayoutParams().height:(h>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*h)):h,weight));
  }

// ---------------------------------------------------------------------
  void setwwh(String p)
  {
    if (null==widget) return;
    String[] n=Util.qsless(p.split(" "),new String[] {""});     // SkipEmptyParts
    if (n.length!=3) {
      JConsoleApp.theWd.error("set wwh requires 3 numbers: " + id + " " + p);
    } else {
      setweight((float)Util.c_strtod(n[0]));
      int w=Util.c_strtoi(n[1]);
      int h=Util.c_strtoi(n[2]);
      setwh(w,h);
    }
  }

// ---------------------------------------------------------------------
  void setwwh(float wt, int w, int h)
  {
    if (null==widget) return;
    setweight(wt);
    if ((-3!=w) || (-3!=h))
      widget.setLayoutParams(new LinearLayout.LayoutParams((-3==w)?widget.getLayoutParams().width:(w>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*w)):w,(-3==h)?widget.getLayoutParams().height:(h>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*h)):h,weight));
  }

// ---------------------------------------------------------------------
  void setminwh(String p)
  {
    String[] n=Util.qsless(p.split(" "),new String[] {""});     // SkipEmptyParts
    if (n.length!=2) {
      JConsoleApp.theWd.error("set minwh requires 2 numbers: " + id + " " + p);
    } else {
      int w=Util.c_strtoi(n[0]);
      int h=Util.c_strtoi(n[1]);
      setminwh(w,h);
    }
  }

// ---------------------------------------------------------------------
  void setminwh(int w, int h)
  {
    if (null!=widget) {
      if (-3!=w) widget.setMinimumWidth((w>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*w)):w);
      if (-3!=h) widget.setMinimumHeight((h>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*h)):h);
    }
  }

}
