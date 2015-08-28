package com.jsoftware.jn.wd;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JRadioButton extends Child
{

  private String iconFile="";

// ---------------------------------------------------------------------
  JRadioButton(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="radiobutton";
    RadioButton w=new RadioButton(f.activity);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"group horizontal")) return;
    childStyle(opt);
    w.setText(qn);
    iconFile="";

    if ((!Util.sacontains(opt,"group")) || (null==ppane.buttongroup)) {
      ppane.buttongroup=new RadioGroup(f.activity);
      if (Util.sacontains(opt,"horizontal"))
        ppane.buttongroup.setOrientation(RadioGroup.HORIZONTAL);
      grouped=true;
    } else
      grouped=false;
    ppane.buttongroup.addView(w);

    w.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        JRadioButton.this.event="button";
        pform.signalevent(JRadioButton.this);
      }
    });
  }

// ---------------------------------------------------------------------
  private void toggled(boolean checked)
  {
    if (grouped && checked==false) return;
    event="button";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    RadioButton w=(RadioButton) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("caption"+"\012"+ "icon"+"\012"+ "text"+"\012"+ "value"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("caption")||p.equals("text"))
        r.write(Util.s2ba(w.getText().toString()));
      else if (p.equals("icon"))
        r.write(Util.s2ba(iconFile));
      else if (p.equals("value"))
        r.write(Util.s2ba(w.isChecked()?(String)"1":(String)"0"));
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
  @SuppressWarnings( "deprecation" )
  void set(String p,String v)
  {
    RadioButton w=(RadioButton) widget;
    if (p.equals("caption") || p.equals("text"))
      w.setText(Util.remquotes(v));
    else if (p.equals("icon")) {
      iconFile=Util.remquotes(v);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        w.setBackground(Drawable.createFromPath(iconFile));
      else
        w.setBackgroundDrawable(Drawable.createFromPath(iconFile));
    } else if (p.equals("value"))
      w.setChecked(v.equals("1"));
    else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    RadioButton w=(RadioButton) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(Util.spair(id,w.isChecked()?(String)"1":(String)"0"));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

}
