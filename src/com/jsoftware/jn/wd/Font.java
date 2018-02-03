package com.jsoftware.jn.wd;

import android.graphics.Typeface;
import android.util.Log;
import java.io.File;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;

public class Font
{

  public Typeface font;
  public float fontsize;
  public int angle;
  public boolean error;

// ---------------------------------------------------------------------
  public Font(String s)
  {
    this(s,-1f);
  }

// ---------------------------------------------------------------------
  Font(String s, float pointsize)
  {
    if (s.equals("fixfont")) {
      font=Typeface.create(Typeface.MONOSPACE,0);
      fontsize=(0f>=pointsize)?14f:pointsize;
      return;
    } else if (s.equals("profont")) {
      font=Typeface.create(Typeface.DEFAULT,0);
      fontsize=(0f>=pointsize)?14f:pointsize;
      return;
    }
    String face, sizestyle;
    face="";
    int bold=0,italic=0,strikeout=0,underline=0;
    float size=-1f;
    String[] ss=Cmd.qsplit(s);
    if (ss.length>0) {
      face = ss[0];
      if (ss.length>1) {
        for (int j = 1; j < ss.length; j++) {
          face = ss[0];
          if (ss[j].equals("bold")) bold = 1;
          else if (ss[j].equals("italic")) italic = 1;
          else if (ss[j].equals("underline")) underline = 1;
          else if (ss[j].equals("strikeout")) strikeout = 1;
          else if (ss[j].startsWith("angle")) angle = Util.c_strtoi(ss[j].substring(5));
          else if (Util.isNumber(ss[j])) size = (float) Util.c_strtod(ss[j]);
          else {
            error=true;
            break;
          }
        }
      }
    }
    if (0f<pointsize) size=pointsize;
    if (0f<size) fontsize=size;
    else fontsize=14f;
    Log.d(JConsoleApp.LogTag, "font: " + face + " size=" + fontsize + " bold=" + bold + " italic=" + italic + " strikeout=" + strikeout + " underline=" + underline + " angle=" + angle);
    if (!face.startsWith("@")) {
      font = Typeface.create(face,bold*(int)Typeface.BOLD+italic*(int)Typeface.ITALIC);
    } else {
      File file;
      font = null;
      String fontfile = "";
      if (!JConsoleApp.theApp.configpath.isEmpty()) {
        fontfile = JConsoleApp.theApp.configpath + "/" + face.substring(1) + ".ttf";
      }
      if (!fontfile.isEmpty() && (file = new File(fontfile)).exists()) {
        font = Typeface.createFromFile(file);
      }
      if (font != null) return;
      font = Typeface.create((Typeface)Typeface.MONOSPACE, bold*(int)Typeface.BOLD+italic*(int)Typeface.ITALIC);
    }
  }

// ---------------------------------------------------------------------
  Font(String s,int size10, int bold, int italic, int strikeout, int underline, int angle10)
  {
    angle=angle10;
    String face = Util.remquotes(s);
    fontsize=size10/10f;
    Log.d(JConsoleApp.LogTag, "font: " + face + " size=" + fontsize + " bold=" + bold + " italic=" + italic + " strikeout=" + strikeout + " underline=" + underline + " angle=" + angle);
    if (!face.startsWith("@")) {
      font = Typeface.create(face,bold*(int)Typeface.BOLD+italic*(int)Typeface.ITALIC);
    } else {
      File file;
      font = null;
      String fontfile = "";
      if (!JConsoleApp.theApp.configpath.isEmpty()) {
        fontfile = JConsoleApp.theApp.configpath + "/" + face.substring(1) + ".ttf";
      }
      if (!fontfile.isEmpty() && (file = new File(fontfile)).exists()) {
        font = Typeface.createFromFile(file);
      }
      if (font != null) return;
      font = Typeface.create((Typeface)Typeface.MONOSPACE, bold*(int)Typeface.BOLD+italic*(int)Typeface.ITALIC);
    }
  }

}
