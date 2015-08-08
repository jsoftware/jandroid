package com.jsoftware.jn.wd;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

public class JBitmap
{

// ---------------------------------------------------------------------
  public static int wdreadimg(String s, Object[] res)
  {
    if (s.isEmpty()) return 1;
    Bitmap image=BitmapFactory.decodeFile(s);
    if (null!=image) {
      int w=image.getWidth();
      int h=image.getHeight();
      int[] pixels=new int[w*h];
      image.getPixels(pixels, 0, w, 0, 0, w, h);
      res[0]= pixels;
      res[1]= w;
      res[2]= h;
      return 0;
    } else return 1;
  }

// ---------------------------------------------------------------------
  public static int wdgetimg(byte[] data, Object[] res)
  {
    if (0==data.length) return 1;
    Bitmap image=BitmapFactory.decodeByteArray(data, 0, data.length);
    if (null!=image) {
      int w=image.getWidth();
      int h=image.getHeight();
      int[] pixels=new int[w*h];
      image.getPixels(pixels, 0, w, 0, 0, w, h);
      res[0]= pixels;
      res[1]= w;
      res[2]= h;
      return 0;
    } else return 1;
  }

// ---------------------------------------------------------------------
  public static int wdwriteimg(int[] p, int w, int h, String f, String format, int quality)
  {
    if (0==p.length) return 1;
    if (w*h!=p.length) return 1;
    Bitmap.CompressFormat fmt;
    if (format.equals("PNG"))
      fmt=Bitmap.CompressFormat.PNG;
    else if (format.equals("JPEG")||format.equals("JPG"))
      fmt=Bitmap.CompressFormat.JPEG;
    else
      return 1;
    Bitmap image=Bitmap.createBitmap(p, 0, w, w, h, Bitmap.Config.ARGB_8888);
    try {
      FileOutputStream stream = null;
      stream = new FileOutputStream(f);
      image.compress(fmt, quality, stream);
      if (stream != null) {
        stream.flush();
        stream.close();
      }
    } catch (FileNotFoundException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
      return 1;
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
      return 1;
    }
    return 0;
  }

// ---------------------------------------------------------------------
  public static int wdputimg(int[] p, int w, int h, String format, int quality, Object[] res)
  {
    if (0==p.length) return 1;
    if (w*h!=p.length) return 1;
    Bitmap.CompressFormat fmt;
    if (format.equals("PNG"))
      fmt=Bitmap.CompressFormat.PNG;
    else if (format.equals("JPEG")||format.equals("JPG"))
      fmt=Bitmap.CompressFormat.JPEG;
    else
      return 1;
    Bitmap image=Bitmap.createBitmap(p, 0, w, w, h, Bitmap.Config.ARGB_8888);
    ByteArrayOutputStream stream;
    stream = new ByteArrayOutputStream();
    image.compress(fmt, quality, stream);
    res[0]= stream.toByteArray();
    return 0;
  }

}
