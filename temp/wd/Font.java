package com.jsoftware.jn.wd;

import android.graphics.Typeface;

import com.jsoftware.jn.wd.Wd;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.base.State;

Font *FontExtent=0;

public class Font
{

  Typeface font;
  int angle;
  boolean error;

// ---------------------------------------------------------------------
  Font(String s, float pointsize)
  {
    error=false;
    angle=0;
    if (s.equals("fixfont")) {
      font=State.config.Font;
      return;
    } else if (s.equals("profont")) {
      font=QApplication::font();
      return;
    }
    String face, sizestyle;
    int bold=0,italic=0,strikeout=0,underline=0;
    float size=0;
    String[] ss=Cmd.qsplit(s);
    if (ss.length()>0) {
      face = ss[0];
      if (ss.length()>1) {
        for (int j = 1; j < ss.length(); j++) {
          if (ss[j].equals("bold")) bold = 1;
          else if (ss[j].equals("italic")) italic = 1;
          else if (ss[j].equals("underline")) underline = 1;
          else if (ss[j].equals("strikeout")) strikeout = 1;
          else if (ss[j].mid(0,5).equals("angle")) angle = Util.c_strtoi(Util.q2s(ss[j].mid(5)));
          else {
            size = (float) Util.Util.c_strtod(Util.q2s(ss[j]));
            if (0==size) {
              error=true;
              break;
            };
          }
        }
      }
    }
    if (-1.0!=pointsize) size=pointsize;
//  qDebug() << "font: " + face + ",size=" + String::number(size) + ",bold=" + String::number(bold) + ",italic=" + String::number(italic) + ",strikeout=" + String::number(strikeout) + ",underline=" + String::number(underline) + ",angle=" + String::number(angle) ;
    font = new Typeface(face);
    if (0.0!=size) font.setPointSizeF((size>0.0)?size:-size);
    font.setBold(bold);
    font.setItalic(italic);
    font.setStrikeOut(strikeout);
    font.setUnderline(underline);
  }

// ---------------------------------------------------------------------
  Font(String s,int size10, boolean bold, boolean italic, boolean strikeout, boolean underline, int angle10)
  {
    error=false;
    angle=angle10;
    String face = Util.Util.s2q(Util.remquotes(s));
    font = new Typeface(face);
    font.setPointSizeF(size10/10.0);
    font.setBold(bold);
    font.setItalic(italic);
    font.setStrikeOut(strikeout);
    font.setUnderline(underline);
  }
}
