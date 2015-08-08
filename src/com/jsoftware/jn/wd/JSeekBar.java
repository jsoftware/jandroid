package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JSeekBar extends Child
{

// ---------------------------------------------------------------------
  JSeekBar(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="seekbar";
    SeekBar w=new SeekBar(f.activity);
    String qn=n;
    widget=(View) w;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
    childStyle(opt);

    int i=0;
    if (i<opt.length) {
      w.incrementProgressBy(Util.c_strtoi(opt[i]));
      i++;
    }
    if (i<opt.length) {
      w.setMax(Util.c_strtoi(opt[i]));
      i++;
    }
    if (i<opt.length) {
      w.setProgress(Util.c_strtoi(opt[i]));
      i++;
    }
    w.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        JSeekBar.this.valueChanged();
      }
      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {
      }
      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {
      }
    });
  }

// ---------------------------------------------------------------------
  private void valueChanged()
  {
    event="changed";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    SeekBar w=(SeekBar) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("max"+"\012"+ "min"+"\012"+ "page"+"\012"+ "pos"+"\012"+ "step"+"\012"+ "tic"+"\012"+ "value"+"\012"));
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
    SeekBar w=(SeekBar) widget;
    String cmd=p;
    String[] arg=Cmd.qsplit(v);
    if (0==arg.length) {
      super.set(p,v);
      return;
    }
    if (cmd.equals("max"))
      w.setMax(Util.c_strtoi(arg[0]));
    else if (cmd.equals("step"))
      w.incrementProgressBy(Util.c_strtoi(arg[0]));
    else if (cmd.equals("pos")|| p.equals("value"))
      w.setProgress(Util.c_strtoi(v));
    else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    SeekBar w=(SeekBar) widget;
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
