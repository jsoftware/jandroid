package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JProgressBar extends Child
{

// ---------------------------------------------------------------------
  JProgressBar(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="progressbar";
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"horizontal inverse large large_inverse small small_inverse")) return;
    childStyle(opt);

    int style=android.R.attr.progressBarStyleLarge;
    int i=0;
    if ((i<opt.length) && (!Util.isNumber(opt[i]))) {
      if (opt[i].equals("horizontal"))
        style=android.R.attr.progressBarStyleHorizontal;
      if (opt[i].equals("inverse"))
        style=android.R.attr.progressBarStyleInverse;
      if (opt[i].equals("large"))
        style=android.R.attr.progressBarStyleLarge;
      if (opt[i].equals("large_inverse"))
        style=android.R.attr.progressBarStyleLargeInverse;
      if (opt[i].equals("small"))
        style=android.R.attr.progressBarStyleSmall;
      if (opt[i].equals("small_inverse"))
        style=android.R.attr.progressBarStyleSmallInverse;
      i++;
    }

    ProgressBar w=new ProgressBar(f.activity,null,style);
    widget=(View) w;

    if (i<opt.length) {
      w.setMax(Util.c_strtoi(opt[i]));
      i++;
    }
    if (i<opt.length) {
      w.setProgress(Util.c_strtoi(opt[i]));
      i++;
    }
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    ProgressBar w=(ProgressBar) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("max"+"\012"+ "pos"+"\012"+ "value"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("max"))
        r.write(Util.s2ba(Util.i2s(w.getMax())));
      else if (p.equals("pos")|| p.equals("value"))
        r.write(Util.s2ba(Util.i2s(w.getProgress())));
      else
        r.write(super.get(p,v));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    ProgressBar w=(ProgressBar) widget;
    String cmd=p;
    String[] arg=Cmd.qsplit(v);
    if (0==arg.length) {
      super.set(p,v);
      return;
    }
    if (cmd.equals("max"))
      w.setMax(Util.c_strtoi(arg[0]));
    else if (cmd.equals("pos") || cmd.equals("value"))
      w.setProgress(Util.c_strtoi((v)));
    else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    ProgressBar w=(ProgressBar) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(Util.spair(id,Util.i2s(w.getProgress())));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }
}
