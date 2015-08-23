package com.jsoftware.jn.wd;

import android.os.Build;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Menus extends Child
{
  Menu curMenu;
  private ArrayList<Menu >menus;
  private Map<Integer, String> items;

  private ArrayList<String >cmds;
  private ArrayList<String >cmdsid;
  private ArrayList<String >cmdsp;

  private int ignored=0;
// ---------------------------------------------------------------------
  Menus(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="menu";
    id="";
    menus=new ArrayList<Menu>();
    items=new HashMap<Integer, String>();
    cmds=new ArrayList<String >();
    cmdsid=new ArrayList<String >();
    cmdsp=new ArrayList<String >();
  }

// ---------------------------------------------------------------------
  void initmenu()
  {
    for(int i=0; i<cmds.size(); i++) {
      if (cmds.get(i).equals("menu")) {
        menu(cmdsid.get(i),cmdsp.get(i));
      } else if (cmds.get(i).equals("menupop")) {
        menupop(cmdsp.get(i));
      } else if (cmds.get(i).equals("menupopz")) {
        menupopz();
      }
    }
  }

// ---------------------------------------------------------------------
  int menu(String id, String p)
  {
    if (null==curMenu) {
      cmds.add("menu");
      cmdsid.add(id);
      cmdsp.add(p);
      return 0;
    }
    int idn=JConsoleApp.theWd.nextId++;
    String[] s=Cmd.qsplit(p);
    String text=s[0];
    items.put(idn , id);
    MenuItem m=curMenu.add(Menu.NONE,idn,0,text.replaceAll("&", ""));
    if (s.length>1) {
      String shortcut=s[1];
      if (!shortcut.isEmpty()) {
        char ch=shortcut.charAt(shortcut.length()-1);
        if (Character.isDigit(ch))
          m.setNumericShortcut(ch);
        else
          m.setAlphabeticShortcut(ch);
      }
    }
    return 0;
  }

// ---------------------------------------------------------------------
  int menupop(String s)
  {
    if (null==curMenu) {
      cmds.add("menupop");
      cmdsid.add("");
      cmdsp.add(s);
      return 0;
    }
    menus.add(curMenu);
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
      curMenu=curMenu.addSubMenu(Menu.NONE,0,0,s.replaceAll("&", ""));
    else {
      if (0==menus.size())
        curMenu=curMenu.addSubMenu(Menu.NONE,0,0,s.replaceAll("&", ""));
      else
        ignored++;
    }
    return 0;
  }

// ---------------------------------------------------------------------
  int menupopz()
  {
    if (null==curMenu) {
      cmds.add("menupopz");
      cmdsid.add("");
      cmdsp.add("");
      return 0;
    }
    if (menus.isEmpty()) return 0;
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
      curMenu=menus.get(menus.size()-1);
      menus.remove(menus.size()-1);
    } else {
      if (0!=ignored) {
        curMenu=menus.get(menus.size()-1);
        menus.remove(menus.size()-1);
      } else
        ignored--;
    }
    return 0;
  }

// ---------------------------------------------------------------------
  int menusep()
  {
    return 0;
  }

// ---------------------------------------------------------------------
  boolean menu_triggered( MenuItem item)
  {
    int idn=item.getItemId();
    if (!items.containsKey(idn)) return false;
    event="button";
    eid=items.get(idn);
    pform.signalevent(this);
    return true;
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    return super.get(p,v);
  }

// ---------------------------------------------------------------------
  int getValue(String value)
  {
    if (!items.containsValue(value)) return 0;
    for (Map.Entry<Integer, String> e : items.entrySet()) {
      if (value.equals(e.getValue()))
        return e.getKey();
    }
    return 0;
  }


// TODO   set not yet implemented
// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    String id,m,parm,t;
    String[] sel,opt;

    sel=Cmd.qsplit(p);
    if (sel.length!=2) {
      JConsoleApp.theWd.error("invalid menu command: " + p + " " + v);
      return;
    }
    id=sel[0];
    m=sel[1];
    t=v;
    if (0==t.length()) {
      t=m;
      m="checked";
    }
    if (m.equals("checked") || m.equals("value")) {
      int idn=getValue(id);
//       a.setCheckable(true);
//       a.setChecked(t.equals("1"));
    } else if (m.equals("show")) {
//      items.value(id).setVisibilty(t.equals("1"));
    } else if (m.equals("enable")) {
//      items.value(id).setEnabled(t.equals("1"));
    } else
      super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    return new byte[0];
  }

}
