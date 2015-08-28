package com.jsoftware.jn.wd;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JButton extends Child
{

  private String iconFile="";

// ---------------------------------------------------------------------
  JButton(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="button";
    Button w=new Button(f.activity);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
    childStyle(opt);
    w.setText(qn);

    w.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        JButton.this.event="button";
        pform.signalevent(JButton.this);
      }
    });
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    Button w=(Button) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("caption"+"\012"+ "icon"+"\012"+ "text"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("caption")||p.equals("text"))
        r.write(Util.s2ba(w.getText().toString()));
      else if (p.equals("icon"))
        r.write(Util.s2ba(iconFile));
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
    Button w=(Button) widget;
    if (p.equals("caption") || p.equals("text"))
      w.setText(Util.remquotes(v));
    else if (p.equals("icon")) {
      iconFile=Util.remquotes(v);
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN)
        w.setBackground(Drawable.createFromPath(iconFile));
      else
        w.setBackgroundDrawable(Drawable.createFromPath(iconFile));
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    return new byte[0];
  }

}

