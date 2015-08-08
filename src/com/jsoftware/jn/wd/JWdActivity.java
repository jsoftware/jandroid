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
  protected void onCreate(android.os.Bundle arg1)
  {
    Log.d(JConsoleApp.LogTag,"onCreate");
    super.onCreate( arg1);
    jlocale = getIntent().getStringExtra("jlocale");
    jInterface = com.jsoftware.j.android.JConsoleApp.theApp.jInterface;
    jInterface.Jnido( this, "onCreate_" + jlocale + "_", new java.lang.Object[] { arg1 } , 0);
  }
  @Override
  protected void onResume()
  {
    Log.d(JConsoleApp.LogTag,"onResume");
    super.onResume();
    jInterface.Jnido( this, "onResume_" + jlocale + "_", null , 0);
  }
  @Override protected void onDestroy()
  {
    Log.d(JConsoleApp.LogTag,"onDestroy");
    jInterface.Jnido( this, "onDestroy_" + jlocale + "_", null , 0);
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
    jInterface.Jnido( this, "onCreateOptionsMenu_" + jlocale + "_", null , 0);
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
