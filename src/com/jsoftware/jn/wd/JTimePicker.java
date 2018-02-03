package com.jsoftware.jn.wd;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JTimePicker extends Child
{

// ---------------------------------------------------------------------
  JTimePicker(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="timepicker";
    TimePicker w=new TimePicker(f.activity);
    String qn=n;
    widget=(View) w;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"am")) return;
    childStyle(opt);

    int i=0;
    int v;
    w.setIs24HourView(true);
    if ((i<opt.length) && (opt[i].equals("am"))) {
      w.setIs24HourView(false);
      i++;
    }
    if (i<opt.length) {
      v=Util.c_strtoi(opt[i]);
      int[] hms=totime(v);
      if (Build.VERSION.SDK_INT < 23) {
        w.setCurrentHour(hms[0]);
        w.setCurrentMinute(hms[1]);
      } else {
        w.setHour(hms[0]);
        w.setMinute(hms[1]);
      }
      i++;
    }
    w.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
      @Override
      public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        JTimePicker.this.valueChanged();
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
    TimePicker w=(TimePicker) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("value"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("value"))
        if (Build.VERSION.SDK_INT < 23)
          r.write(Util.s2ba(Util.i2s((10000*w.getCurrentHour())+(100*w.getCurrentMinute())+0)));
        else
          r.write(Util.s2ba(Util.i2s((10000*w.getHour())+(100*w.getMinute())+0)));
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
    TimePicker w=(TimePicker) widget;
    String[] qs=Cmd.qsplit(v);
    if (0==qs.length) {
      JConsoleApp.theWd.error("missing parameters: " + p + " " + v);
      return;
    }
    int i;
    int[] hms;
    if (p.equals("value")) {
      i=Util.c_strtoi(qs[0]);
      hms=totime(i);
      if (Build.VERSION.SDK_INT < 23) {
        w.setCurrentHour(hms[0]);
        w.setCurrentMinute(hms[1]);
      } else {
        w.setHour(hms[0]);
        w.setMinute(hms[1]);
      }
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    TimePicker w=(TimePicker) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (Build.VERSION.SDK_INT < 23)
        r.write(Util.spair(id,Util.i2s((10000*w.getCurrentHour())+(100*w.getCurrentMinute())+0)));
      else
        r.write(Util.spair(id,Util.i2s((10000*w.getHour())+(100*w.getMinute())+0)));
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  private int[] totime(int v)
  {
    int y=(int)Math.floor((float)v/10000);
    v=v%10000;
    int m=(int)Math.floor((float)v/100);
    int d=v%100;
    return new int[] {y,m,d};
  }
}
