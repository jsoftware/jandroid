package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Font;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Pane;
import com.jsoftware.jn.wd.Wd;
import java.util.List;

public class Child
{

  public boolean grouped;
  public String id;
  public String eid;  // for event
  public String event;
  public String parms;
  public String type;
  public String locale="";  // for isigraph
  public String sysdata="";
  public String sysmodifiers="";
  public Form pform;
  public Pane ppane;
  public View widget;
  public float weight=0f;

// private
  JwdActivity activity;

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
  public void dispose()
  {
  }

// ---------------------------------------------------------------------
  public byte[] state()
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
    if (!v.isEmpty() && p!="extent") {
      JConsoleApp.theWd.error("extra parameters: " + id + " " + p + " " + v);
      return new byte[] {};
    }
    if (p.equals("property")) {
      StringBuilder r1=new StringBuilder();
      r1.append(new String("enable")+"\012"+ "extent"+"\012");
      r1.append(new String("focusable")+"\012"+ "font"+"\012"+ "hasfocus"+"\012"+ "hwnd"+"\012");
      r1.append(new String("id")+"\012"+ "locale"+"\012");
      r1.append(new String("minwh")+"\012"+ "parent"+"\012");
      r1.append(new String("property")+"\012"+ "state"+"\012");
      r1.append(new String("type")+"\012"+ "visible"+"\012"+ "weight"+"\012"+ "wh"+"\012"+ "xywh"+"\012");
      r=r1.toString();
    } else if (p.equals("enable")) {
      if (null!=widget) r=Util.i2s((widget.isEnabled())?1:0);
    } else if (p.equals("extent")) {
      if (null!=widget) {
//      QFontMetrics fm = QFontMetrics(widget.font());
//      r=Util.i2s(fm.width(Util.s2q(v)))+" "+Util.i2s(fm.height());
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
      r=(locale!="")?locale:pform.locale;
    } else if (p.equals("minwh")) {
      if (null!=widget) {
// need API 16
//      r=Util.i2s(widget.getMinimumWidth())+" "+Util.i2s(widget.getMinimumHeight());
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
    } else if (p.equals("weight")) {
      r=Util.d2s((double)weight);
    } else if (p.equals("wh")) {
      if (null!=widget) {
        r=Util.i2s(widget.getWidth())+" "+Util.i2s(widget.getHeight());
      }
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
//     return Util.q2s(fontspec(((TextView)widget).getTypeface()));
      return"";
    } else
      return"";
  }

// ---------------------------------------------------------------------
  byte[] getsysdata()
  {
    return new byte[0];
  }

// ---------------------------------------------------------------------
  void set(String p,String v)
  {
    if (p.equals("enable")) {
      if (null!=widget) widget.setEnabled(Util.remquotes(v)!="0");
    } else if (p.equals("locale")) {
      locale=Util.remquotes(v);
    } else if (p.equals("focus")) {
      if (null!=widget) widget.requestFocus();
    } else if (p.equals("focusable")) {
      if (null!=widget) widget.setFocusable(Util.remquotes(v)!="0");
    } else if (p.equals("font")) {
      if (null!=widget) setfont(v);
    } else if (p.equals("invalid")) {
      if (null!=widget) widget.invalidate();
    } else if (p.equals("nofocus")) {
      if (null!=widget) widget.setFocusable(false);
    } else if (p.equals("show")||p.equals("visible")) {
      if (null!=widget) widget.setVisibility((Util.remquotes(v)!="0")?View.VISIBLE:View.GONE);
    } else if (p.equals("wh")) {
      setwh(v);
    } else if (p.equals("minwh")) {
      setminwhv(v);
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
  void setweightv(String p)
  {
    String[] n=Util.s2q(p).split(" ");     // SkipEmptyParts
    if (n.length!=1) {
      JConsoleApp.theWd.error("set weight requires 1 number: " + id + " " + p);
    } else {
      float wt=(float)Util.c_strtod(Util.q2s(n[0]));
      setweight(wt);
    }
  }

// ---------------------------------------------------------------------
  void setweight(float wt)
  {
    weight=wt;
  }

// ---------------------------------------------------------------------
  void setwh(String p)
  {
    if (null!=widget) JConsoleApp.theWd.wdsetwh(widget,p);
  }

// ---------------------------------------------------------------------
  void setwhv(String p)
  {
    String[] n=Util.s2q(p).split(" ");     // SkipEmptyParts
    if (n.length!=2) {
      JConsoleApp.theWd.error("set wh requires 2 numbers: " + id + " " + p);
    } else {
      int w=Util.c_strtoi(Util.q2s(n[0]));
      int h=Util.c_strtoi(Util.q2s(n[1]));
      setwh(w,h);
    }
  }

// ---------------------------------------------------------------------
  void setwh(int w, int h)
  {
    if ((null!=widget) && (-9!=w) && (-9!=h)) {
      LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams((-9==w)?widget.getLayoutParams().width:(w>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*w)):w,(-9==h)?widget.getLayoutParams().height:(h>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*h)):h,weight);
      widget.setLayoutParams(lp);
    }
  }

// ---------------------------------------------------------------------
  void setminwhv(String p)
  {
    String[] n=Util.s2q(p).split(" ");     // SkipEmptyParts
    if (n.length!=2) {
      JConsoleApp.theWd.error("set minwh requires 2 numbers: " + id + " " + p);
    } else {
      int w=Util.c_strtoi(Util.q2s(n[0]));
      int h=Util.c_strtoi(Util.q2s(n[1]));
      setminwh(w,h);
    }
  }

// ---------------------------------------------------------------------
  void setminwh(int w, int h)
  {
    if (null!=widget) {
      if (-9!=w) widget.setMinimumWidth((w>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*w)):w);
      if (-9!=h) widget.setMinimumHeight((h>0)?((int)(0.5f+JConsoleApp.theWd.dmdensity*h)):h);
    }
  }

}
