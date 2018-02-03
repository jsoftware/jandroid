package com.jsoftware.jn.wd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.j.android.AndroidJInterface;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;

class JIsigraph extends Child
{

  Glcmds glcmds;
  public boolean nodblbuf=false;
  public boolean nopaintevent=false;
  public boolean paintx=false;

  Bitmap bitmap=null;
  Canvas canvas=null;
// sysdata
  private int cx,cy,andw,andh,button1,button2,control,shift,button3;

// ---------------------------------------------------------------------
  JIsigraph(String n, String s, Form f, Pane p, String type)
  {
    super(n,s,f,p);
    this.type=type;
    JView w= new JView(this, f.activity);
    widget=(View ) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
    childStyle(opt);

    w.setFocusable(true);
    glcmds=new Glcmds(this);

    if (!nodblbuf || type.equals("isidraw")) {
      bitmap=Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
      canvas=new Canvas(bitmap);
      canvas.drawARGB(255,255,255,255);
      glcmds.canvas=canvas;
      glcmds.bitmap=bitmap;
    }
    JConsoleApp.theWd.gltarget = 0;
    JConsoleApp.theWd.isigraph = this;

    glcmds.glclear2(false);
    w.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        return JIsigraph.this.onTouch(view, event);
      }
    });

  }

// ---------------------------------------------------------------------
  @Override
  void dispose()
  {
    if ((null!=bitmap) && !bitmap.isRecycled()) bitmap.recycle();
    if (this==JConsoleApp.theWd.isigraph) JConsoleApp.theWd.isigraph=null;
  }

// ---------------------------------------------------------------------
  void onPause ()
  {
    ((JView) widget).onPause();
  }

// ---------------------------------------------------------------------
  void onResume ()
  {
    ((JView) widget).onResume();
  }

// ---------------------------------------------------------------------
  void onDraw (Canvas canvas)
  {
    Log.d(JConsoleApp.LogTag,"JIsigraph onDraw");
    nopaintevent=true;
    if (type.equals("isidraw"))
      paintEvent_isidraw(canvas);
    else
      paintEvent_isigraph(canvas);
    nopaintevent=false;
  }

// ---------------------------------------------------------------------
  void onSizeChanged (int w, int h, int oldw, int oldh)
  {
    andw=w;
    andh=h;
    if (!nodblbuf || type.equals("isidraw")) {
      if (null!=bitmap) {
        if (w > bitmap.getWidth() || h > bitmap.getHeight()) {
          Bitmap p=Bitmap.createBitmap(w+128, h+128, Bitmap.Config.ARGB_8888);
          canvas=new Canvas(p);
          canvas.drawARGB(255,255,255,255);
          canvas.drawBitmap(bitmap,0f,0f,glcmds.paint);
          if (!bitmap.isRecycled()) bitmap.recycle();
          bitmap=p;
        }
      } else {
        bitmap=Bitmap.createBitmap(w+128, h+128, Bitmap.Config.ARGB_8888);
        canvas=new Canvas(bitmap);
        canvas.drawARGB(255,255,255,255);
      }
      glcmds.canvas=canvas;
      glcmds.bitmap=bitmap;
      if(type=="isidraw") {
        event="resize";
        pform.signalevent(this);
      } else
        widget.invalidate();  // isigraph paint event
    }
  }

// ---------------------------------------------------------------------
  boolean onTouch(View view, MotionEvent event)
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
    if (!(event.equals("paint") || event.equals("resize") || event.equals("mmove") || event.equals("mwheel") || event.equals("focus") || event.equals("lostfocus"))) super.setform();
    JConsoleApp.theWd.gltarget = 0;
    JConsoleApp.theWd.isigraph=this;
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

// Isigraph2
// ---------------------------------------------------------------------
  private void fill(int[] p)
  {
//   QColor c(*(p), *(p + 1), *(p + 2), *(p + 3));
//   if (bitmap)
//     bitmap.fill(c);
//   else
//     glcmds.painter.fillRect(0,0,width(),height(),c);
  }

// ---------------------------------------------------------------------
  private Bitmap getbitmap()
  {
    ((View)widget).setDrawingCacheEnabled(true);
    Bitmap bitmap = Bitmap.createBitmap(((View)widget).getDrawingCache());
    ((View)widget).setDrawingCacheEnabled(false);
    return bitmap;
  }

// ---------------------------------------------------------------------
  private void paintEvent_isidraw(Canvas canvas)
  {
    if (bitmap!=null)
      canvas.drawBitmap(bitmap,0f,0f,glcmds.paint);
  }
//
// ---------------------------------------------------------------------
  private void paintEvent_isigraph(Canvas canvas)
  {
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
      JConsoleApp.theWd.gltarget=0;
      JConsoleApp.theWd.isigraph=this;
      event="paint";
      pform.signalevent(this);
    } else
      paintx=false;
    if (!nodblbuf || type.equals("isidraw")) {
      if (bitmap!=null)
        canvas.drawBitmap(bitmap,0f,0f,glcmds.paint);
    }
    if(nodblbuf) {
      this.canvas = null;
      glcmds.canvas=null;
    }
  }
// // ---------------------------------------------------------------------
// private void buttonEvent(QEvent.Type type, QMouseEvent *event)
// {
//   JConsoleApp.theWd.isigraph=this;
//
//   String lmr = "";
//   switch (event.button()) {
//   case Qt.LeftButton:
//     lmr = "l";
//     break;
//   case Qt.MidButton:
//     lmr = "m";
//     break;
//   case Qt.RightButton:
//     lmr = "r";
//     break;
//   default:
//     break;
//   }
//
//   String evtname = "mmove";
//   switch (type) {
//   case QEvent.MouseButtonPress:
//     evtname = "mb" + lmr + "down";
//     break;
//   case QEvent.MouseButtonRelease:
//     evtname = "mb" + lmr + "up";
//     break;
//   case QEvent.MouseButtonDblClick:
//     evtname = "mb" + lmr + "dbl";
//     break;
//   case QEvent.MouseMove:
//     evtname = "mmove";
//     break;
//   default:
//     break;
//   }
//
//   // sysmodifiers = shift+2*control
//   // sysdata = mousex,mousey,gtkwh,button1,button2,control,shift,button3,0,0,wheel
//   char sysmodifiers[20];
//   sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & QtCTRL))) + (!!(event.modifiers() & Qt.SHIFT)));
//   char sysdata[200];
//   sprintf(sysdata , "%d %d %d %d %d %d %d %d %d %d %d %d", event.x(), event.y(), this.width(), this.height(), (!!(event.buttons() & Qt.LeftButton)), (!!(event.buttons() & Qt.MidButton)), (!!(event.modifiers() & Qt.CTRL)), (!!(event.modifiers() & Qt.SHIFT)), (!!(event.buttons() & QtrRightButton)), 0, 0, 0);
//
//   event=evtname;
//   sysmodifiers=String(sysmodifiers);
//   sysdata=String(sysdata);
//   pform.signalevent(this);
// }
//
// // ---------------------------------------------------------------------
// private void wheelEvent(QWheelEvent *event)
// {
//   JConsoleApp.theWd.isigraph=this;
//
//   char deltasign = ' ';
//   int delta = event.delta() / 8;  // degree
//   if (delta<0) {
//     delta = -delta;
//     deltasign = '_';
//   }
//
//   // sysmodifiers = shift+2*control
//   // sysdata = mousex,mousey,gtkwh,button1,button2,control,shift,button3,0,0,wheel
//   char sysmodifiers[20];
//   sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt.CTRL))) + (!!(event.modifiers() & Qt.SHIFT)));
//   char sysdata[200];
//   sprintf(sysdata , "%d %d %d %d %d %d %d %d %d %d %d %c%d", event.x(), event.y(), this.width(), this.height(), (!!(event.buttons() & Qt.LeftButton)), (!!(event.buttons() & Qt.MidButton)), (!!(event.modifiers() & Qt.CTRL)), (!!(event.modifiers() & Qt.SHIFT)), (!!(event.buttons() & Qt.RightButton)), 0, 0, deltasign, delta);
//
//   event=String("mwheel");
//   sysmodifiers=String(sysmodifiers);
//   sysdata=String(sysdata);
//   pform.signalevent(this);
// }
//
// // ---------------------------------------------------------------------
// private void mousePressEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseButtonPress, event);
// }
//
// // ---------------------------------------------------------------------
// private void mouseMoveEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseMove, event);
// }
//
// // ---------------------------------------------------------------------
// private void mouseDoubleClickEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseButtonDblClick, event);
// }
//
// // ---------------------------------------------------------------------
// private void mouseReleaseEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseButtonRelease, event);
// }
//
// // ---------------------------------------------------------------------
// private void focusInEvent(QFocusEvent *event)
// {
//   event="focus";
//   sysmodifiers="";
//   sysdata="";
//   pform.signalevent(this);
// }
//
// // ---------------------------------------------------------------------
// private void focusOutEvent(QFocusEvent *event)
// {
//   event="focuslost";
//   sysmodifiers="";
//   sysdata="";
//   pform.signalevent(this);
// }
//
// // ---------------------------------------------------------------------
// private void keyPressEvent(QKeyEvent *event)
// {
//   int key1=0;
//   int key=event.key();
//   if (ismodifier(key)) return;
// #ifdef QT_OS_ANDROID
//   if (key==KeyEvent.KEYCODE_Back) {
//     View.keyPressEvent(event);
//     return;
//   }
// #endif
//   if ((key>0x10000ff)||((key>=KeyEvent.KEYCODE_F1)&&(key<=KeyEvent.KEYCODE_F12))) {
//     View.keyPressEvent(event);
//     return;
//   } else
//     key1=translateqkey(key);
//   char sysmodifiers[20];
//   sprintf(sysmodifiers , "%d", (2*(!!(event.modifiers() & Qt.CTRL))) + (!!(event.modifiers() & Qt.SHIFT)));
//   char sysdata[20];
//   if (key==key1)
//     sprintf(sysdata , "%s", event.text().toUtf8().constData());
//   else sprintf(sysdata , "%s", String(QChar(key1)).toUtf8().constData());
//
//   event=String("char");
//   sysmodifiers=String(sysmodifiers);
//   sysdata=String(sysdata);
//   pform.signalevent(this);
//   View.keyPressEvent(event);
// }

}
