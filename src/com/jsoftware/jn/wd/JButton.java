package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.View;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Pane;
import com.jsoftware.jn.wd.Wd;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class JButton extends Child
{

  String iconFile="";

// ---------------------------------------------------------------------
  public JButton(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="button";
    Button w=new Button(f.activity);
    widget=(View) w;
    String qn=Util.s2q(n);
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"default")) return;
//  w.setObjectName(qn);
    childStyle(opt);
    w.setText(qn);
//   if (Util.sacontains(opt,"default"))
//     w.setDefault(true);

    w.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        JButton.this.event="button";
        pform.signalevent(JButton.this);
      }
    });
  }

// ---------------------------------------------------------------------
  public byte[] get(String p,String v)
  {
    Button w=(Button) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba(new String("caption")+"\012"+ "icon"+"\012"+ "text"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("caption")||p.equals("text"))
        r.write(Util.s2ba(Util.q2s(w.getText().toString())));
      else if (p.equals("icon"))
        r.write(Util.s2ba(iconFile));
      else
        r.write(super.get(p,v));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,"IOException");
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,"Exception");
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  public void set(String p,String v)
  {
    Button w=(Button) widget;
    if (p.equals("caption") || p.equals("text"))
      w.setText(Util.s2q(Util.remquotes(v)));
    else if (p.equals("icon")) {
      String[] qs=Cmd.qsplit(v);
      String[] sizes;
      if (0==qs.length) {
        JConsoleApp.theWd.error("missing parameters: " + p + " " + v);
        return;
      }
      if (qs.length==2) {
        String t=qs[1];
        if (Util.qshasonly(t,"0123456789x")) {
          sizes=t.split("x");
          if (sizes.length<2) {
            JConsoleApp.theWd.error("invalid icon width, height: " + p + " " + v);
            return;
          }
        } else {
          JConsoleApp.theWd.error("invalid icon width, height: " + p + " " + v);
          return;
        }
      }  else if (qs.length>2) {
        JConsoleApp.theWd.error("extra parameters: " + p + " " + v);
        return;
      }
      iconFile=Util.remquotes(Util.q2s(qs[0]));
      int spi;
//    w.setIcon(QIcon(Util.s2q(iconFile)));
//     if (qs.length==2)
//       w.setIconSize(QSize(Util.c_strtoi(Util.q2s(sizes[0])),Util.c_strtoi(Util.q2s(sizes[1]))));
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  public byte[] state()
  {
    return new byte[0];
  }

}

