package com.jsoftware.jn.wd;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JToggleButton extends Child
{

  private String iconFile="";


// ---------------------------------------------------------------------
  JToggleButton(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="togglebutton";
    ToggleButton w=new ToggleButton(f.activity);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
    childStyle(opt);
    iconFile="";
    w.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        JToggleButton.this.stateChanged();
      }
    });
  }

// ---------------------------------------------------------------------
  private void stateChanged()
  {
    event="button";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    ToggleButton w=(ToggleButton) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("caption"+"\012"+ "icon"+"\012"+ "text"+"\012"+ "text"+"\012"+ "textoff"+"\012"+ "texton"+"\012"+ "value"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("caption")||p.equals("text"))
        r.write(Util.s2ba(w.getText().toString()));
      else if (p.equals("icon"))
        r.write(Util.s2ba(iconFile));
      else if (p.equals("textoff"))
        r.write(Util.s2ba(w.getTextOff().toString()));
      else if (p.equals("texton"))
        r.write(Util.s2ba(w.getTextOn().toString()));
      else if (p.equals("value"))
        r.write(Util.s2ba(w.isChecked()?(String)"1":(String)"0"));
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
  @SuppressWarnings( "deprecation" )
  void set(String p,String v)
  {
    ToggleButton w=(ToggleButton) widget;
    if (p.equals("caption") || p.equals("text"))
      w.setText(Util.remquotes(v));
    else if (p.equals("icon")) {
      iconFile=Util.remquotes(v);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        w.setBackground(Drawable.createFromPath(iconFile));
      else
        w.setBackgroundDrawable(Drawable.createFromPath(iconFile));
    } else if (p.equals("texts")) {
      String[] qs=Cmd.qsplit(v);
      if (0==qs.length) {
        w.setTextOff(null);
        w.setTextOn(null);
      } else if (1==qs.length) {
        w.setText(Util.remquotes(qs[0]));
        w.setTextOff(Util.remquotes(qs[0]));
        w.setTextOn(Util.remquotes(qs[0]));
      } else if (2==qs.length) {
        w.setText(Util.remquotes(qs[0]));
        w.setTextOff(Util.remquotes(qs[0]));
        w.setTextOn(Util.remquotes(qs[1]));
      } else {
        w.setText(Util.remquotes(qs[0]));
        w.setTextOff(Util.remquotes(qs[1]));
        w.setTextOn(Util.remquotes(qs[2]));
      }
    } else if (p.equals("textoff"))
      w.setTextOff(Util.remquotes(v));
    else if (p.equals("texton"))
      w.setTextOn(Util.remquotes(v));
    else if (p.equals("toggle"))
      w.toggle();
    else if (p.equals("value"))
      w.setChecked(!Util.remquotes(v).equals("0"));
    else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    ToggleButton w=(ToggleButton) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(Util.spair(id,w.isChecked()?(String)"1":(String)"0"));
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

}
