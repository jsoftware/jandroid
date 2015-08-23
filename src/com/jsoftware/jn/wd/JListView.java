package com.jsoftware.jn.wd;

import android.R;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JListView extends Child
{

  private int multiselect;
  private int simplelist;
  private int position=-1;

// ---------------------------------------------------------------------
  JListView(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="listbox";
    ListView w=new ListView(f.activity);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"none single multiple item multiplechoice singlechoice")) return;
    childStyle(opt);

    if (Util.sacontains(opt,"multiple"))
      multiselect=ListView.CHOICE_MODE_MULTIPLE;
    else if (Util.sacontains(opt,"single"))
      multiselect=ListView.CHOICE_MODE_SINGLE;
    else
      multiselect=ListView.CHOICE_MODE_NONE;

    if (Util.sacontains(opt,"item"))
      simplelist=android.R.layout.simple_list_item_1;
//     else if (Util.sacontains(opt,"activated"))
//       simplelist=android.R.layout.simple_list_item_activated_1;
    else if (Util.sacontains(opt,"singlechoice"))
      simplelist=android.R.layout.simple_list_item_single_choice;
    else if (Util.sacontains(opt,"mutiplechoice"))
      simplelist=android.R.layout.simple_list_item_multiple_choice;
    else {
      if ( multiselect==ListView.CHOICE_MODE_MULTIPLE)
        simplelist=android.R.layout.simple_list_item_multiple_choice;
      else if ( multiselect==ListView.CHOICE_MODE_SINGLE)
        simplelist=android.R.layout.simple_list_item_single_choice;
      else
        simplelist=android.R.layout.simple_list_item_1;
    }

    w.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        JListView.this.onItemClick(position);
      }
    });
    w.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
      @Override
      public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return JListView.this.onItemLongClick(position);
      }
    });
  }

// ---------------------------------------------------------------------
  private void onItemClick(int position)
  {
    this.position=position;
    JListView.this.event="select";
    pform.signalevent(JListView.this);
  }

// ---------------------------------------------------------------------
  private boolean onItemLongClick(int position)
  {
    this.position=position;
    JListView.this.event="button";
    pform.signalevent(JListView.this);
    return true;
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    ListView w=(ListView) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("allitems"+"\012"+ "items"+"\012"+ "select"+"\012"+ "text"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("allitems"))
        r.write(Util.s2ba(getallitems()));
      else if (p.equals("items"))
        r.write(Util.s2ba(getselection()));
      else if (p.equals("text"))
        r.write(Util.s2ba(getselection()));
      else if (p.equals("select"))
        r.write(Util.s2ba(getselectionindex()));
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
  private String getallitems()
  {
    ListView w=(ListView) widget;
    StringBuilder s=new StringBuilder();

    for (int i=0; i<w.getAdapter().getCount(); i++) {
      s.append((String)w.getAdapter().getItem(i).toString());
      s.append("\012");
    }
    return s.toString();
  }

// ---------------------------------------------------------------------
  private String getselection()
  {
    ListView w=(ListView) widget;
    if (multiselect==ListView.CHOICE_MODE_NONE) {
      if (-1==position) {
        return "";
      } else {
        return w.getAdapter().getItem(position).toString();
      }
    } else {
      StringBuilder s=new StringBuilder();
      SparseBooleanArray sp = w.getCheckedItemPositions();
      for (int i = 0; i < sp.size(); i++) {
        if(sp.valueAt(i)) {
          s.append((String)(w.getItemAtPosition(sp.keyAt(i))));
          s.append("\012");
        }
      }
      return s.toString();
    }
  }

// ---------------------------------------------------------------------
  private String getselectionindex()
  {
    ListView w=(ListView) widget;
    if (multiselect==ListView.CHOICE_MODE_NONE) {
      if (-1==position) {
        return Util.i2s(-1);
      } else {
        return Util.i2s(position);
      }
    } else {
      StringBuilder s=new StringBuilder();
      SparseBooleanArray sp = w.getCheckedItemPositions();
      if (0==sp.size()) {
        return Util.i2s(-1);
      } else {
        for (int i = 0; i < sp.size(); i++) {
          if(sp.valueAt(i)) {
            s.append(Util.i2s(sp.keyAt(i)));
            s.append(" ");
          }
        }
        return s.toString();
      }
    }
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    ListView w=(ListView) widget;
    if (p.equals("items")) {
      addItems(Cmd.qsplit(v));
    } else if (p.equals("select")) {
      String[] qs=Cmd.qsplit(v);
      if (0==qs.length || (multiselect!=ListView.CHOICE_MODE_MULTIPLE && 1<qs.length)) {
        JConsoleApp.theWd.error("set select requires 1 number: " + id + " " + v);
        return;
      }
      if (multiselect==ListView.CHOICE_MODE_NONE)
        w.setSelection(Util.c_strtoi(qs[0]));
      else if (multiselect==ListView.CHOICE_MODE_SINGLE)
        w.setItemChecked(Util.c_strtoi(qs[0]), true);
      else {
        for (String s : qs)
          w.setItemChecked(Util.c_strtoi(s), true);
      }
    } else
      super.set(p,v);
  }

// ---------------------------------------------------------------------
  private void addItems(String[] v)
  {
    ListView w=(ListView) widget;
    ArrayAdapter<String> adapter=new ArrayAdapter<String>(pform.activity, simplelist, v);
    w.setAdapter(adapter);
    w.setChoiceMode(multiselect);
    position=-1;
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    ListView w=(ListView) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(Util.spair(id,getselection()));
      r.write(Util.spair(id+"_select",getselectionindex()));
      r.write(Util.spair(id+"_click",Util.i2s(position)));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

}
