package com.jsoftware.jn.wd;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// java date month number is 0 base.

class JDatePicker extends Child
{

// ---------------------------------------------------------------------
  JDatePicker(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="datepicker";
    DatePicker w=new DatePicker(f.activity);
    String qn=n;
    widget=(View) w;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"c")) return;
    childStyle(opt);

    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
      w.setCalendarViewShown(false);
    int i=0;
    int v;
    int[] ymd;
    if ((i<opt.length) && (opt[i].equals("c"))) {
      i++;
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        w.setCalendarViewShown(true);
    }
    if (i<opt.length) {
      v=Util.c_strtoi(opt[i]);
      ymd=toymd(v);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        w.setMinDate(fromymd(ymd));
      i++;
    }
    if (i<opt.length) {
      v=Util.c_strtoi(opt[i]);
      ymd=toymd(v);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        w.setMaxDate(fromymd(ymd));
      i++;
    }
    if (i<opt.length) {
      v=Util.c_strtoi(opt[i]);
      ymd=toymd(v);
//      w.updateDate(ymd[0],ymd[1]-1,ymd[2]);
      i++;
    } else {
      Calendar cal = Calendar.getInstance();
      ymd= new int[] {cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH)};
    }

    w.init(ymd[0],ymd[1]-1,ymd[2], new DatePicker.OnDateChangedListener() {
      @Override
      public void onDateChanged (DatePicker view, int year, int month, int day) {
        JDatePicker.this.valueChanged();
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
    DatePicker w=(DatePicker) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("max"+"\012"+ "min"+"\012"+ "value"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("max")) {
        Calendar q = Calendar.getInstance();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
          q.setTimeInMillis(w.getMaxDate());
        r.write(Util.s2ba(Util.i2s((10000*q.get(Calendar.YEAR))+(100*(q.get(Calendar.MONTH)+1))+q.get(Calendar.DAY_OF_MONTH))));
      } else if (p.equals("min")) {
        Calendar q = Calendar.getInstance();
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
          q.setTimeInMillis(w.getMinDate());
        r.write(Util.s2ba(Util.i2s((10000*q.get(Calendar.YEAR))+(100*(q.get(Calendar.MONTH)+1))+q.get(Calendar.DAY_OF_MONTH))));
      } else if (p.equals("value"))
        r.write(Util.s2ba(Util.i2s((10000*w.getYear())+(100*(w.getMonth()+1))+w.getDayOfMonth())));
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
    DatePicker w=(DatePicker) widget;
    String[] qs=Cmd.qsplit(v);
    if (0==qs.length) {
      JConsoleApp.theWd.error("missing parameters: " + p + " " + v);
      return;
    }
    int i;
    int[] ymd;
    if (p.equals("min")) {
      i=Util.c_strtoi(qs[0]);
      ymd=toymd(i);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        w.setMinDate(fromymd(ymd));
    } else if (p.equals("max")) {
      i=Util.c_strtoi(qs[0]);
      ymd=toymd(i);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        w.setMaxDate(fromymd(ymd));
    } else if (p.equals("value")) {
      i=Util.c_strtoi(qs[0]);
      ymd=toymd(i);
      w.updateDate(ymd[0],ymd[1]-1,ymd[2]);
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    DatePicker w=(DatePicker) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(Util.spair(id,Util.i2s((10000*w.getYear())+(100*(w.getMonth()+1))+w.getDayOfMonth())));
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  private int[] toymd(int v)
  {
    int y=(int)Math.floor((float)v/10000);
    v=v%10000;
    int m=(int)Math.floor((float)v/100);
    int d=v%100;
    return new int[] {y,m,d};
  }

// return millisecond since 1970-01-01 00:00:00
// ---------------------------------------------------------------------
  private long fromymd(int[] ymd)
  {
    long startDate=0;
    try {
      String dateString = "" + ymd[2]+ "/" + ymd[1]+ "/" + ymd[0];
      SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
      Date date = sdf.parse(dateString);

      startDate = date.getTime();

    } catch (ParseException e) {
      e.printStackTrace();
    }
    return startDate;
  }
}
