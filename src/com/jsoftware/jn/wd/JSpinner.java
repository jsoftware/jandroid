package com.jsoftware.jn.wd;

import android.R;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JSpinner extends Child
{

// ---------------------------------------------------------------------
  JSpinner(String n, String s, Form f, Pane p, String type)
  {
    super(n,s,f,p);
    this.type=type;
    Spinner w=new Spinner(f.activity);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
    childStyle(opt);
    w.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        JSpinner.this.onItemSelected(position);
      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) {
        // sometimes you need nothing here
      }
    });
  }

// ---------------------------------------------------------------------
  private void onItemSelected(int position)
  {
    event="select";
    pform.signalevent(this);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    Spinner w=(Spinner) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("allitems"+"\012"+ "items"+"\012"+ "select"+"\012"+ "text"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("allitems"))
        r.write(Util.s2ba(getallitems()));
      else if (p.equals("text")||p.equals("select")) {
        int pos=w.getSelectedItemPosition();
        if (-1==pos) {
          if (p.equals("text"))
            r.write(new byte[0]);
          else
            r.write(Util.s2ba(Util.i2s(-1)));
        } else if (p.equals("text"))
          r.write(Util.s2ba((String)(w.getItemAtPosition(pos))));
        else
          r.write(Util.s2ba(Util.i2s(pos)));
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
  private String getallitems()
  {
    Spinner w=(Spinner) widget;
    StringBuilder s=new StringBuilder();

    for (int i=0; i<w.getAdapter().getCount(); i++) {
      s.append((String)w.getAdapter().getItem(i).toString());
      s.append("\012");
    }
    return s.toString();
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    Spinner w=(Spinner) widget;
    if (p.equals("items")) {
      addItems(Cmd.qsplit(v));
    } else if (p.equals("select")) {
      w.setSelection(Util.c_strtoi(v));
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  private void addItems(String[] v)
  {
    Spinner w=(Spinner) widget;
    ArrayAdapter<String> adapter=new ArrayAdapter<String>(pform.activity, R.layout.simple_spinner_item, v);
    adapter.setDropDownViewResource((type.equals("spinbox"))?R.layout.simple_spinner_item:R.layout.simple_spinner_dropdown_item);
    w.setAdapter(adapter);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    Spinner w=(Spinner) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      int  pos=w.getSelectedItemPosition();
      if (AdapterView.INVALID_ROW_ID==pos) {
        r.write(Util.spair(id,(String)""));
        r.write(Util.spair(id+"_select",(String)"_1"));
      } else {
        r.write(Util.spair(id,(String)(w.getItemAtPosition(pos))));
        r.write(Util.spair(id+"_select",Util.i2s(pos)));
      }
    } catch (IOException exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.e(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }
}
