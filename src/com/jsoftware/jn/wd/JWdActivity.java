package com.jsoftware.jn.wd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.jsoftware.j.android.AndroidJInterface;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.lang.Math;
import java.util.ArrayList;

public class JWdActivity extends android.app.Activity
{
  protected com.jsoftware.j.JInterface jInterface = null;
  public java.lang.String jlocale = null;
  protected java.lang.String japparg = null;

  Form form;

  public JWdActivity()
  {
    super();
    JConsoleApp.theWd.activity=this;
  }
  @Override
  protected void onCreate(android.os.Bundle bundle)
  {
    Log.d(JConsoleApp.LogTag,"onCreate");
    super.onCreate(bundle);
    jlocale = getIntent().getStringExtra("jlocale");
    jInterface = com.jsoftware.j.android.JConsoleApp.theApp.jInterface;
    if (null==bundle)
      jInterface.callJ( "(i.0 0)\"_ onCreate_" + jlocale + "_$0");
  }
  @Override
  protected void onResume()
  {
    Log.d(JConsoleApp.LogTag,"onResume");
    super.onResume();
    jInterface.callJ( "(i.0 0)\"_ onResume_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onResume_" + jlocale + "_')$0");
  }
  @Override protected void onDestroy()
  {
    Log.d(JConsoleApp.LogTag,"onDestroy");
    jInterface.callJ( "(i.0 0)\"_ onDestroy_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onDestroy_" + jlocale + "_')$0");
    if (null!=form) form.dispose();
    super.onDestroy();
  }
  @Override
  public boolean onKeyDown(int arg1,android.view.KeyEvent arg2)
  {
    return super.onKeyDown( arg1, arg2);
  }
  @Override
  public boolean onKeyLongPress(int arg1,android.view.KeyEvent arg2)
  {
    return super.onKeyLongPress( arg1, arg2);
  }
  @Override
  public boolean onKeyUp(int arg1,android.view.KeyEvent arg2)
  {
    return super.onKeyUp( arg1, arg2);
  }
  @Override
  public boolean onKeyMultiple(int arg1,int arg2,android.view.KeyEvent arg3)
  {
    return super.onKeyMultiple( arg1, arg2, arg3);
  }
  @Override
  public void onBackPressed()
  {
    super.onBackPressed();
    return;
  }
  @Override
  public boolean onCreateOptionsMenu( Menu menu)
  {
    if (null==form || null==form.menubar) return super.onCreateOptionsMenu(menu);
    form.menubar.curMenu=menu;
    form.menubar.initmenu();
    jInterface.callJ( "(i.0 0)\"_ onCreateOptionsMenu_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onCreateOptionsMenu_" + jlocale + "_')$0");
    return true;
  }
  @Override
  public boolean onOptionsItemSelected( MenuItem item)
  {
    if (null==form || null==form.menubar) return super.onOptionsItemSelected(item);
    if (form.menubar.menu_triggered(item)) return true;
    return super.onOptionsItemSelected(item);
  }
  @Override
  public boolean onTouchEvent(android.view.MotionEvent arg1)
  {
    return super.onTouchEvent( arg1);
  }
}
