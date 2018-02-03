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
  public static int glreadimg(int t, String s, Object[] res)
  {
    if (s.isEmpty()) return 1;
    Bitmap image=BitmapFactory.decodeFile(s);
    if (null!=image) {
      int w=image.getWidth();
      int h=image.getHeight();
      int[] pixels=new int[w*h];
      image.getPixels(pixels, 0, w, 0, 0, w, h);
      res[0]= pixels;
      res[1]= new int[] {4,h,w};
      return -1;
    } else return 1;
  }

// ---------------------------------------------------------------------
  public static int glgetimg(int t, byte[] data, Object[] res)
  {
    if (0==data.length) return 1;
    Bitmap image=BitmapFactory.decodeByteArray(data, 0, data.length);
    if (null!=image) {
      int w=image.getWidth();
      int h=image.getHeight();
      int[] pixels=new int[w*h];
      image.getPixels(pixels, 0, w, 0, 0, w, h);
      res[0]= pixels;
      res[1]= new int[] {4,h,w};
      return -1;
    } else return 1;
  }

// ---------------------------------------------------------------------
  public static int glwriteimg(int t, int[] p, int w, int h, String f, String format, int quality)
  {
    if (0==p.length) return 1;
    if (w*h!=p.length) return 1;
    if (format=="") format= JBitmap.fextension(f);
    Bitmap.CompressFormat fmt;
    if (format.equalsIgnoreCase("PNG"))
      fmt=Bitmap.CompressFormat.PNG;
    else if (format.equalsIgnoreCase("JPEG")||format.equalsIgnoreCase("JPG"))
      fmt=Bitmap.CompressFormat.JPEG;
    else
      return 1;
    if (quality<0) quality=75;
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
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
      return 1;
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
      return 1;
    }
    return 0;
  }

// ---------------------------------------------------------------------
  public static int glputimg(int t, int[] p, int w, int h, String format, int quality, Object[] res)
  {
    if (0==p.length) return 1;
    if (w*h!=p.length) return 1;
    Bitmap.CompressFormat fmt;
    if (format.equalsIgnoreCase("PNG"))
      fmt=Bitmap.CompressFormat.PNG;
    else if (format.equalsIgnoreCase("JPEG")||format.equalsIgnoreCase("JPG"))
      fmt=Bitmap.CompressFormat.JPEG;
    else
      return 1;
    if (quality<0) quality=75;
    Bitmap image=Bitmap.createBitmap(p, 0, w, w, h, Bitmap.Config.ARGB_8888);
    ByteArrayOutputStream stream;
    stream = new ByteArrayOutputStream();
    image.compress(fmt, quality, stream);
    res[0]= stream.toByteArray();
    res[1]= new int[] {2,-1,-1};
    return -1;
  }

// ---------------------------------------------------------------------
  private static String fextension(String s)
  {
    String separator = System.getProperty("file.separator");
    int indexOfLastSeparator = s.lastIndexOf(separator);
    String filename = s.substring(indexOfLastSeparator + 1);
    int extensionIndex = filename.lastIndexOf(".");
    return filename.substring(extensionIndex + 1);
  }

}
