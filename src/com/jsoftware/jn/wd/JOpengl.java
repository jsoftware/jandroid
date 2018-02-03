package com.jsoftware.jn.wd;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;

class JOpengl extends Child
{

  Glcmds glcmds;
  public boolean nodblbuf=true;
  public boolean nopaintevent=false;
  public boolean paintx=false;

  Bitmap bitmap=null;
  Canvas canvas=null;
// sysdata
  private int cx,cy,andw,andh,button1,button2,control,shift,button3;

// ---------------------------------------------------------------------
  JOpengl(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="opengl";
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"version")) return;

    int ver1=2,ver2=0;
    if (1<opt.length && opt[0].equals("version")) {
      String t=opt[1];
      int d=t.indexOf(".");
      if (d==-1) {
        ver1=Util.c_strtoi(t);
        ver2=0;
      } else {
        ver1=Util.c_strtoi(t.substring(0,d));
        ver2=Util.c_strtoi(t.substring(d+1));
      }
    }

    if (ver1<2 || ver1>3) {
      JConsoleApp.theWd.error("not opengles version 2 or 3");
      return;
    }

    JGLView w= new JGLView(this, f.activity, new int[] {ver1,ver2});
    if (!w.initOK) {
      JConsoleApp.theWd.error(type);
      return;
    }
    widget=(View ) w;
    childStyle(opt);

    w.setFocusable(true);
    glcmds=new Glcmds(this);

    glcmds.glclear2(false);
    JConsoleApp.theWd.gltarget = 1;
    JConsoleApp.theWd.opengl = this;

    w.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        return JOpengl.this.onTounch(view, event);
      }
    });

  }

// ---------------------------------------------------------------------
  @Override
  void dispose()
  {
    if (this==JConsoleApp.theWd.opengl) JConsoleApp.theWd.opengl=null;
  }

// ---------------------------------------------------------------------
  void onPause ()
  {
    ((JGLView) widget).onPause();
  }

// ---------------------------------------------------------------------
  void onResume ()
  {
    ((JGLView) widget).onResume();
  }

// ---------------------------------------------------------------------
  void glwaitgl ()
  {
    ((JGLView) widget).eglWaitGL();
  }

// ---------------------------------------------------------------------
  void glwaitnative ()
  {
    ((JGLView) widget).eglWaitNative();
  }

// ---------------------------------------------------------------------
  void onDraw (Canvas canvas)
  {
    Log.d(JConsoleApp.LogTag,"JOpengl onDraw");
    nopaintevent=true;
    if(nodblbuf) {
      this.canvas = canvas;
      glcmds.canvas=canvas;
    }
    if (0<glcmds.sbuf.length) {
      glcmds.commitsbuf();
    }
    if(nodblbuf && JConsoleApp.theApp.asyncj) {
      this.canvas = null;
      glcmds.canvas=null;
    }
    if (!paintx) {         // suppress paint event if called from glpaintx
      JConsoleApp.theWd.gltarget = 1;
      JConsoleApp.theWd.opengl=this;
      event="paint";
      pform.signalevent(this);
    } else
      paintx=false;
    if(nodblbuf) {
      this.canvas = null;
      glcmds.canvas=null;
    }
    nopaintevent=false;
  }

// ---------------------------------------------------------------------
  void onSizeChanged (int w, int h)
  {
    andw=w;
    andh=h;
    glcmds.canvas=canvas;
    event="resize";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  void onInitialize()
  {
    event="initialize";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  boolean onTounch(View view, MotionEvent event)
  {
    String name;
    int action=event.getAction();
    if (action==MotionEvent.ACTION_DOWN) name="mbldown";
    else if (action==MotionEvent.ACTION_UP) name="mblup";
    else if (action==MotionEvent.ACTION_MOVE) name="mmove";
    else return false;
    long dt=event.getEventTime() - event.getDownTime();
    if ((dt>500) && (action==MotionEvent.ACTION_UP)) name="mbldbl";
    cx=(int)event.getX();
    cy=(int)event.getY();
    button1=1;
    sysmodifiers=Util.i2s(shift+2*control);
    this.sysdata=getsysdata();
    this.event=name;
    pform.signalevent(this);
    return true;
  }

// ---------------------------------------------------------------------
  @Override
  String getsysdata()
  {
//    (cx,cy,andwh,button1,button2,control,shift,button3,0,0,0)
    return Util.i2s(cx)+" "+Util.i2s(cy)+" "+Util.i2s(andw)+" "+Util.i2s(andh)+" "+Util.i2s(button1)+" "+Util.i2s(button2)+" "+Util.i2s(button3)+" 0 0 0";
  }

// ---------------------------------------------------------------------
  @Override
  void setform()
  {
    if (null==widget) return;
    if (!(event.equals("paint") || event.equals("resize"))) super.setform();
    JConsoleApp.theWd.gltarget = 1;
    JConsoleApp.theWd.opengl=this;
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    return super.get(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    return new byte[0];
  }

}
