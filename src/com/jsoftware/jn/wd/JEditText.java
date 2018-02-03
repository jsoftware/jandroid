package com.jsoftware.jn.wd;

import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.inputmethod.EditorInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JEditText extends Child
{

// ---------------------------------------------------------------------
  JEditText(String n, String s, Form f, Pane p, String type)
  {
    super(n,s,f,p);
    this.type=type;
    EditText w=new EditText(f.activity);
    if (type.equals("edit"))
      w.setSingleLine(true);
    w.setFocusableInTouchMode(true);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"password readonly left right center")) return;
    if (1<(Util.sacontains(opt,"left")?1:0) + (Util.sacontains(opt,"right")?1:0) + (Util.sacontains(opt,"center")?1:0)) {
      JConsoleApp.theWd.error("conflicting child style: " + n + " " + Util.sajoinstr(opt," "));
      return;
    }
    childStyle(opt);

    if (Util.sacontains(opt,"password"))
      w.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    if (Util.sacontains(opt,"readonly"))
      setreadonly(true);

    if (Util.sacontains(opt,"left"))
      w.setGravity(Gravity.LEFT);
    else if (Util.sacontains(opt,"right"))
      w.setGravity(Gravity.RIGHT);
    else if (Util.sacontains(opt,"center"))
      w.setGravity(Gravity.CENTER);

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
  @Override
  byte[] get(String p,String v)
  {
    EditText w=(EditText) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba(("alignment"+"\012"+ "focusselect"+"\012"+ "inputmask"+"\012"+ "limit"+"\012"+ "select"+"\012"+ "text"+"\012")));
        r.write(super.get(p,v));
      } else if (p.equals("alignment")) {
        if ((w.getGravity())==Gravity.RIGHT)
          r.write(Util.s2ba("right"));
        else if ((w.getGravity())==Gravity.CENTER)
          r.write(Util.s2ba("center"));
        else
          r.write(Util.s2ba("left"));
//   } else if (p.equals("focusselect"))
//     r=Util.i2s(focusSelect);
//   else if (p.equals("inputmask"))
//     r=w.inputMask();
// } else if (p.equals("limit")) {
//     r=Util.i2s(w.maxLength());
      } else if (p.equals("text")) {
        r.write(Util.s2ba(w.getText().toString()));
      } else if (p.equals("select")) {
        int b,e;
        b=w.getSelectionStart();
        e=w.getSelectionEnd();
        r.write(Util.s2ba(Util.i2s(b)+" "+Util.i2s(e)));
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
    EditText w = (EditText )widget;
    String[] opt=Cmd.qsplit(v);
    int bgn,end;

    if (p.equals("text")) {
      w.setText(Util.remquotes(v));
    } else if (p.equals("cursorposition")) {
      if (0==opt.length) {
        JConsoleApp.theWd.error("set cursorposition requires 1 number: " + id + " " + p);
        return;
      }
      int pos=Util.c_strtoi(opt[0]);
      pos=Math.max(0,Math.min(pos,w.getText().length()));
      w.setSelection(pos);
    } else if (p.equals("limit")) {
      if (0==opt.length) {
        JConsoleApp.theWd.error("set limit requires 1 number: " + id + " " + p);
        return;
      }
      InputFilter[] filters = new InputFilter[1];
      filters[0] = new InputFilter.LengthFilter(Util.c_strtoi(opt[0]));
      w .setFilters(filters);
    } else if (p.equals("focusselect")) {
      w.setSelectAllOnFocus(!Util.remquotes(v).equals("0"));
    } else if (p.equals("focus")) {
      w.requestFocus();
    } else if (p.equals("readonly")) {
      setreadonly(!Util.remquotes(v).equals("0"));
    } else if (p.equals("select")) {
      if (0==opt.length) {
        w.selectAll();
      } else {
        bgn=end=Util.c_strtoi(opt[0]);
        if (opt.length>1)
          end=Util.c_strtoi(opt[1]);
        w.setSelection(bgn,end);
      }
    } else if (p.equals("alignment")) {
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
//   } else if (p.equals("inputmask")) {
//     if (0==opt.length) {
//       w.setInputMask("");
//     else
//       w.setInputMask(opt.at(0));
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  private void setreadonly(boolean readonly)
  {
    EditText w=(EditText) widget;
    w.setCursorVisible (!readonly) ; // hide the cursor
    w.setFocusable (!readonly) ; // loses focus
    w.setFocusableInTouchMode (!readonly) ; // virtual keyboard hidden If you need to
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    EditText w=(EditText) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    int b,e;
    b=e=0;
    b=w.getSelectionStart();
    e=w.getSelectionEnd();
    try {
      r.write(Util.spair(id,w.getText().toString()));
      r.write(Util.spair(id+"_select",Util.i2s(b)+" "+Util.i2s(e)));
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

}
