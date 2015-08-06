package com.jsoftware.jn.wd;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Font;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Glcmds;
import com.jsoftware.jn.wd.JView;
import com.jsoftware.jn.wd.Pane;
import java.util.List;

public class JIsigraph extends Child
{

  Glcmds glcmds;

  Bitmap bitmap;
  Canvas canvas;
// sysdata
  int cx,cy,andw,andh,button1,button2,control,shift,button3;

// ---------------------------------------------------------------------
  public JIsigraph(String n, String s, Form f, Pane p, String type)
  {
    super(n,s,f,p);
    this.type=type;
    JView w= new JView(this, f.activity);
    widget=(View ) w;
    String qn=Util.s2q(n);
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
//  w.setObjectName(qn);
    childStyle(opt);

    w.setFocusable(true);
    glcmds=new Glcmds(w);

    if (type.equals("isidraw")) {
      bitmap=Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
      canvas=new Canvas(bitmap);
      canvas.drawARGB(255,255,255,255);
      glcmds.canvas=canvas;
    }
    glcmds.glclear2(false);
    JConsoleApp.theWd.isigraph = this;

    w.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View view, MotionEvent event) {
        return JIsigraph.this.onTounch(view, event);
      }
    });

  }

// ---------------------------------------------------------------------
  @Override
  public void dispose()
  {
    if ((null!=bitmap) && !bitmap.isRecycled()) bitmap.recycle();
    if (this==JConsoleApp.theWd.isigraph) JConsoleApp.theWd.isigraph=null;
  }

// ---------------------------------------------------------------------
  public void onDraw (Canvas canvas)
  {
    Log.d(JConsoleApp.LogTag,"JIsigraph onDraw");
    if (type.equals("isidraw"))
      paintEvent_isidraw(canvas);
    else
      paintEvent_isigraph(canvas);
  }

// ---------------------------------------------------------------------
  public void onSizeChanged (int w, int h, int oldw, int oldh)
  {
    andw=w;
    andh=h;
    if (type.equals("isidraw")) {
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
    }
    glcmds.canvas=canvas;
    event="resize";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  public boolean onTounch(View view, MotionEvent event)
  {
    int action=event.getAction();
    String name;
    if (action==MotionEvent.ACTION_DOWN) name="mbldown";
    else if (action==MotionEvent.ACTION_UP) name="mblup";
    else if (action==MotionEvent.ACTION_MOVE) name="mmove";
    else return true;
    int cx=(int)event.getX();
    int cy=(int)event.getY();
    long dt=event.getEventTime() - event.getDownTime();
    if ((dt>500) && (action==MotionEvent.ACTION_UP)) name="mbldbl";
    button1=1;
    sysmodifiers=Util.i2s(shift+2*control);
    this.event=name;
    pform.signalevent(this);
    return false;
  }

// ---------------------------------------------------------------------
  @Override
  byte[] getsysdata()
  {
//    (cx,cy,andwh,button1,button2,control,shift,button3,0,0,0)
    return Util.s2ba(Util.i2s(cx)+" "+Util.i2s(cy)+" "+Util.i2s(andw)+" "+Util.i2s(andh)+" "+Util.i2s(button1)+" "+Util.i2s(button2)+" "+Util.i2s(button3)+" 0 0 0");
  }

// ---------------------------------------------------------------------
  @Override
  public void setform()
  {
    if (!(event=="paint" || event=="resize")) super.setform();
    JConsoleApp.theWd.isigraph=this;
  }

// ---------------------------------------------------------------------
  public byte[] get(String p,String v)
  {
    return super.get(p,v);
  }

// ---------------------------------------------------------------------
  public void set(String p,String v)
  {
    super.set(p,v);
  }

// ---------------------------------------------------------------------
  public byte[] state()
  {
    return new byte[0];
  }

// Isigraph2
// ---------------------------------------------------------------------
  void fill(int[] p)
  {
//   QColor c(*(p), *(p + 1), *(p + 2), *(p + 3));
//   if (bitmap)
//     bitmap.fill(c);
//   else
//     glcmds.painter.fillRect(0,0,width(),height(),c);
  }
//
// // ---------------------------------------------------------------------
  Bitmap getbitmap()
  {
//   Bitmap m=new Bitmap();
//   if (null!=bitmap)
//     return bitmap.copy(0,0,widget.getWidth(),widget.getHeight());
//   if (null!=glcmds.canvas) return m;
//   Bitmap p=Bitmap.createBitmap(widget.getWidth(),widget.getHeight(), Bitmap.Config.ARGB_8888);
//  render(&p);
    return null;
  }
//
// ---------------------------------------------------------------------
  void paintEvent_isidraw(Canvas canvas)
  {
    if (null==bitmap) return;
    canvas.drawBitmap(bitmap,0f,0f,glcmds.paint);
  }
//
// ---------------------------------------------------------------------
  void paintEvent_isigraph(Canvas canvas)
  {
    JConsoleApp.theWd.isigraph=this;
    glcmds.canvas=canvas;
    event="paint";
    pform.signalevent(this);
    glcmds.canvas=null;
  }
// // ---------------------------------------------------------------------
// void buttonEvent(QEvent.Type type, QMouseEvent *event)
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
// void wheelEvent(QWheelEvent *event)
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
// void mousePressEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseButtonPress, event);
// }
//
// // ---------------------------------------------------------------------
// void mouseMoveEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseMove, event);
// }
//
// // ---------------------------------------------------------------------
// void mouseDoubleClickEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseButtonDblClick, event);
// }
//
// // ---------------------------------------------------------------------
// void mouseReleaseEvent(QMouseEvent *event)
// {
//   buttonEvent(QEvent.MouseButtonRelease, event);
// }
//
// // ---------------------------------------------------------------------
// void focusInEvent(QFocusEvent *event)
// {
//   event="focus";
//   sysmodifiers="";
//   sysdata="";
//   pform.signalevent(this);
// }
//
// // ---------------------------------------------------------------------
// void focusOutEvent(QFocusEvent *event)
// {
//   event="focuslost";
//   sysmodifiers="";
//   sysdata="";
//   pform.signalevent(this);
// }
//
// // ---------------------------------------------------------------------
// void keyPressEvent(QKeyEvent *event)
// {
//   int key1=0;
//   int key=event.key();
//   if (ismodifier(key)) return;
// #ifdef QT_OS_ANDROID
//   if (key==Qt.Key_Back) {
//     View.keyPressEvent(event);
//     return;
//   }
// #endif
//   if ((key>0x10000ff)||((key>=Qt.Key_F1)&&(key<=Qt.Key_F35))) {
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
