package com.jsoftware.jn.wd;

import android.util.Log;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JImageView extends Child
{

  private String fit="";
  private String imageFile="";
  private int bitmapWidth;
  private int bitmapHeight;
  private int maxX;
  private int maxY;
  private int maxLeft;
  private int maxRight;
  private int maxTop;
  private int maxBottom;
  private int totalX, totalY;
  private float downX, downY;

// ---------------------------------------------------------------------
  JImageView(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="image";

    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"center centercrop centerinside fitcenter fitend fitstart fitxy")) return;

    ImageView w=new ImageView(f.activity);
    widget=(View ) w;
    childStyle(opt);

// android default scaletype is FIT_CENTER
    if (Util.sacontains(opt,"center")) {
      fit="center";
      w.setAdjustViewBounds(false);
      w.setScaleType(ImageView.ScaleType.CENTER);
    } else if (Util.sacontains(opt,"centercrop")) {
      fit="centercrop";
      w.setAdjustViewBounds(true);
      w.setScaleType(ImageView.ScaleType.CENTER_CROP);
    } else if (Util.sacontains(opt,"centerinside")) {
      fit="centerinside";
      w.setAdjustViewBounds(true);
      w.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    } else if (Util.sacontains(opt,"fitend")) {
      fit="fitend";
      w.setAdjustViewBounds(true);
      w.setScaleType(ImageView.ScaleType.FIT_END);
    } else if (Util.sacontains(opt,"fitstart")) {
      fit="fitstart";
      w.setAdjustViewBounds(true);
      w.setScaleType(ImageView.ScaleType.FIT_START);
    } else if (Util.sacontains(opt,"fitxy")) {
      fit="fitxy";
      w.setAdjustViewBounds(false);
      w.setScaleType(ImageView.ScaleType.FIT_XY);
    } else {
      fit="fitcenter";
      w.setAdjustViewBounds(true);
      w.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    if (!fit.equals("center")) return;

    // set touchlistener
    w.setOnTouchListener(new View.OnTouchListener() {

      @Override
      public boolean onTouch(View view, MotionEvent event) {
        JImageView.this.setmaxXT();
        int scrollByX, scrollByY;
        float currentX, currentY;
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          downX = event.getX();
          downY = event.getY();
          break;

        case MotionEvent.ACTION_MOVE:
          currentX = event.getX();
          currentY = event.getY();
          scrollByX = (int)(downX - currentX);
          scrollByY = (int)(downY - currentY);

          // scrolling to left side of image (pic moving to the right)
          if (currentX > downX) {
            if (totalX == maxLeft) scrollByX = 0;
            if (totalX > maxLeft) totalX += scrollByX;
            if (totalX < maxLeft) {
              scrollByX = maxLeft - (totalX - scrollByX);
              totalX = maxLeft;
            }
          }
          // scrolling to right side of image (pic moving to the left)
          if (currentX < downX) {
            if (totalX == maxRight) scrollByX = 0;
            if (totalX < maxRight) totalX +=  scrollByX;
            if (totalX > maxRight) {
              scrollByX = maxRight - (totalX - scrollByX);
              totalX = maxRight;
            }
          }
          // scrolling to top of image (pic moving to the bottom)
          if (currentY > downY) {
            if (totalY == maxTop) scrollByY = 0;
            if (totalY > maxTop) totalY += scrollByY;
            if (totalY < maxTop) {
              scrollByY = maxTop - (totalY - scrollByY);
              totalY = maxTop;
            }
          }
          // scrolling to bottom of image (pic moving to the top)
          if (currentY < downY) {
            if (totalY == maxBottom) scrollByY = 0;
            if (totalY < maxBottom) totalY +=  scrollByY;
            if (totalY > maxBottom) {
              scrollByY = maxBottom - (totalY - scrollByY);
              totalY = maxBottom;
            }
          }
          JImageView.this.widget.scrollBy(scrollByX, scrollByY);
          downX = currentX;
          downY = currentY;
          break;
        }
        return true;
      }
    });

  }

// ---------------------------------------------------------------------
  private void setmaxXT()
  {
    ImageView w=(ImageView) widget;

    // set maximum scroll amount (based on center of image)

    maxX =(bitmapWidth/2)- (w.getWidth() / 2);
    maxY = (bitmapHeight/2)- (w.getHeight() / 2);
    maxX=(maxX>0)?maxX:0;
    maxY=(maxY>0)?maxY:0;

    // set scroll limits
    maxLeft = (maxX * -1);
    maxRight = maxX;
    maxTop = (maxY * -1);
    maxBottom = maxY;
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    ImageView w=(ImageView) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("image"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("image"))
        r.write(Util.s2ba(imageFile));
      else
        r.write(super.get(p,v));
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    ImageView w=(ImageView) widget;
    if (p.equals("image")) {
      String s=Util.remquotes(v);
      if (s.isEmpty()) {
        JConsoleApp.theWd.error("set image needs image filename: " + id + " " + p + " " + v);
        return;
      }
      Bitmap image=BitmapFactory.decodeFile(s);
      if (null==image) {
        JConsoleApp.theWd.error("set image cannot load image " + id + " " + p + " " + v);
        return;
      }
      imageFile=s;
      w.setImageBitmap(image);
      if (fit.equals("center")) {
        bitmapWidth=image.getWidth();
        bitmapHeight=image.getHeight();
        totalX=0;
        totalY=0;
        downX=0f;
        downY=0f;
      }
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    return new byte[0];
  }

}

