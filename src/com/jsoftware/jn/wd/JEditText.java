package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Pane;
import com.jsoftware.jn.wd.Wd;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.IOException;
import java.util.List;

public class JEditText extends Child
{

  boolean focusSelect;

// ---------------------------------------------------------------------
  public JEditText(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="edit";
    EditText w=new EditText(f.activity);
    w.setSingleLine(true);
    w.setFocusableInTouchMode(true);
    widget=(View) w;
    String qn=Util.s2q(n);
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"password readonly left right center")) return;
    if (1<(Util.sacontains(opt,"left")?1:0) + (Util.sacontains(opt,"right")?1:0) + (Util.sacontains(opt,"center")?1:0)) {
      JConsoleApp.theWd.error("conflicting child style: " + n + " " + Util.q2s(Util.sajoinstr(opt," ")));
      return;
    }
//  w.setObjectName(qn);
    childStyle(opt);

//   if (opt.contains("password"))
//     w.setEchoMode(LinePassword);
//
//   if (opt.contains("readonly"))
//     w.setReadOnly(true);
//
//   if (opt.contains("left"))
//     w.setAlignment(Qt::AlignVCenter|Qt::AlignLeft);
//   else if (opt.contains("right"))
//     w.setAlignment(Qt::AlignVCenter|Qt::AlignRight);
//   else if (opt.contains("center"))
//     w.setAlignment(Qt::AlignVCenter|Qt::AlignHCenter);

//     w.setOnKeyListener(new OnKeyListener() {
//       @Override
//       public boolean onKey(View v, int keyCode, KeyEvent event) {
//         // If the event is a key-down event on the "enter" button
//         if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//             (keyCode == KeyEvent.KEYCODE_ENTER)) {
//           // Perform action on key press
//           event="button";
//           pform.signalevent(JEditText.this);
//           return true;
//         }
//         return false;
//       }
//     });

    w.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (event==null) {
          if (actionId==EditorInfo.IME_ACTION_DONE);
          // Capture soft enters in a singleLine EditText that is the last EditText.
          else if (actionId==EditorInfo.IME_ACTION_NEXT);
          // Capture soft enters in other singleLine EditTexts
          else return false;  // Let system handle all other null KeyEvents
        } else if (actionId==EditorInfo.IME_NULL) {
          // Capture most soft enters in multi-line EditTexts and all hard enters.
          // They supply a zero actionId and a valid KeyEvent rather than
          // a non-zero actionId and a null event like the previous cases.
          if (event.getAction()==KeyEvent.ACTION_DOWN);
          // We capture the event when key is first pressed.
          else  return true;   // We consume the event when the key is released.
        } else  return false;
        // We let the system handle it when the listener
        // is triggered by something that wasn't an enter.


        // Code from this point on will execute whenever the user
        // presses enter in an attached view, regardless of position,
        // keyboard, or singleLine status.

        JEditText.this.event="button";
        pform.signalevent(JEditText.this);
        return true;   // Consume the event
      }
    });

  }

// ---------------------------------------------------------------------
  void returnPressed()
  {
    event="button";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  public byte[] get(String p,String v)
  {
    EditText w=(EditText) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba((new String("alignment")+"\012"+ "focusselect"+"\012"+ "inputmask"+"\012"+ "limit"+"\012"+ "readonly"+"\012"+ "select"+"\012"+ "text"+"\012")));
        r.write(super.get(p,v));
//   } else if (p.equals("alignment")) {
//     if ((w.alignment())&Qt::AlignRight)
//       r=String("right");
//     else if ((w.alignment())&Qt::AlignHCenter)
//       r=String("center");
//     else
//       r=String("left");
//   } else if (p.equals("focusselect"))
//     r=Util.i2s(focusSelect);
//   else if (p.equals("inputmask"))
//     r=Util.q2s(w.inputMask());
//   else if (p.equals("limit"))
//     r=Util.i2s(w.maxLength());
//   else if (p.equals("readonly")) {
//     r=Util.i2s(w.isReadOnly());
      } else if (p.equals("text")) {
        r.write(Util.s2ba(Util.q2s(w.getText().toString())));
      } else if (p.equals("select")) {
        int b,e;
        b=w.getSelectionStart();
        e=w.getSelectionEnd();
        r.write(Util.s2ba(Util.i2s(b)+" "+Util.i2s(e)));
      } else
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
    EditText w = (EditText )widget;
    String[] opt=Cmd.qsplit(v);

    if (p.equals("text")) {
      w.setText(Util.s2q(Util.remquotes(v)));
//   } else if (p.equals("cursorposition")) {
//     if (opt.isEmpty()) {
//       JConsoleApp.theWd.error("set cursorposition requires 1 number: " + id + " " + p);
//       return;
//     }
//     int p=Util.c_strtoi(Util.q2s(opt.at(0)));
//     p=qMax(0,qMin(p,w.text().length()));
//     w.setCursorPosition(p);
//   } else if (p.equals("limit")) {
//     if (opt.isEmpty()) {
//       JConsoleApp.theWd.error("set limit requires 1 number: " + id + " " + p);
//       return;
//     }
//     w.setMaxLength(Util.c_strtoi(Util.q2s(opt.at(0))));
//   } else if (p.equals("focusselect")) {
//     focusSelect=Util.remquotes(v)!="0";
    } else if (p.equals("focus")) {
      w.requestFocus();
//     if (focusSelect) w.selectAll();
//   } else if (p.equals("readonly")) {
//     w.setReadOnly(Util.remquotes(v)!="0");
//   } else if (p.equals("select")) {
//     w.selectAll();
//   } else if (p.equals("alignment")) {
//     if (opt.isEmpty()) {
//       JConsoleApp.theWd.error("set alignment requires 1 argument: " + id + " " + p);
//       return;
//     }
//     if (opt.at(0).equals("left"))
//       w.setAlignment(Qt::AlignVCenter|Qt::AlignLeft);
//     else if (opt.at(0).equals("right"))
//       w.setAlignment(Qt::AlignVCenter|Qt::AlignRight);
//     else if (opt.at(0).equals("center"))
//       w.setAlignment(Qt::AlignVCenter|Qt::AlignHCenter);
//     else {
//       JConsoleApp.theWd.error("set alignment requires left, right or center: " + id + " " + p);
//       return;
//     }
//   } else if (p.equals("inputmask")) {
//     if (opt.isEmpty())
//       w.setInputMask("");
//     else
//       w.setInputMask(opt.at(0));
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  public byte[] state()
  {
    EditText w=(EditText) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    int b,e;
    b=e=0;
    b=w.getSelectionStart();
    e=w.getSelectionEnd();
    try {
      r.write(Util.spair(id,Util.q2s(w.getText().toString())));
      r.write(Util.spair(id+"_select",Util.i2s(b)+" "+Util.i2s(e)));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,"IOException");
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,"Exception");
    }
    return r.toByteArray();
  }

}
