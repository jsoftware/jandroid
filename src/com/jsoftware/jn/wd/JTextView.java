package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JTextView extends Child
{

// ---------------------------------------------------------------------
  JTextView(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="static";
    TextView w=new TextView(f.activity);
    widget=(View ) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"staticbox left right center")) return;
    if (1<(Util.sacontains(opt,"left")?1:0) + (Util.sacontains(opt,"right")?1:0) + (Util.sacontains(opt,"center")?1:0)) {
      JConsoleApp.theWd.error("conflicting child style: " + n + " " + Util.sajoinstr(opt," "));
      return;
    }
    childStyle(opt);
    if (!Util.sacontains(opt,"staticbox"))
      w.setText(qn);
    if (Util.sacontains(opt,"left"))
      w.setGravity(Gravity.LEFT);
    else if (Util.sacontains(opt,"right"))
      w.setGravity(Gravity.RIGHT);
    else if (Util.sacontains(opt,"center"))
      w.setGravity(Gravity.CENTER);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    TextView w=(TextView) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("alignment"+"\012"+ "caption"+"\012"+ "text"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("caption")||p.equals("text"))
        r.write(Util.s2ba(w.getText().toString()));
      else if (p.equals("alignment")) {
        if ((w.getGravity())==Gravity.RIGHT)
          r.write(Util.s2ba("right"));
        else if ((w.getGravity())==Gravity.CENTER)
          r.write(Util.s2ba("center"));
        else
          r.write(Util.s2ba("left"));
      } else
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
    TextView w=(TextView) widget;
    String[] opt=Cmd.qsplit(v);
    if (p.equals("caption") || p.equals("text"))
      w.setText(Util.remquotes(v));
    else if (p.equals("alignment")) {
      if (0==opt.length) {
        JConsoleApp.theWd.error("set alignment requires 1 argument: " + id + " " + p);
        return;
      }
      if (opt[0].equals("left"))
        w.setGravity(Gravity.LEFT);
      else if (opt[0].equals("right"))
        w.setGravity(Gravity.RIGHT);
      else if (opt[0].equals("center"))
        w.setGravity(Gravity.CENTER);
      else {
        JConsoleApp.theWd.error("set alignment requires left, right or center: " + id + " " + p);
        return;
      }
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    return new byte[0];
  }

}
