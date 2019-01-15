package com.jsoftware.jn.wd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import com.jsoftware.j.android.AbstractActivity;
import com.jsoftware.j.android.AndroidJInterface;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.j.android.FileEdit;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.File;
import java.lang.Math;
import java.util.ArrayList;

public class JWdActivity extends AbstractActivity
{
  protected com.jsoftware.j.android.AndroidJInterface jInterface = null;
  public String jlocale = "";
  protected java.lang.String japparg = null;

  Form form;
  String backbuttonAct="default";
  String fullscreen="no";

  public JWdActivity()
  {
    super();
    JConsoleApp.theWd.activity=this;
  }
  @Override
  protected void onCreate(Bundle bundle)
  {
    super.onCreate(bundle);
    jInterface = theApp.jInterface;
    jlocale = getIntent().getStringExtra("jlocale");
    Log.d(JConsoleApp.LogTag,"onCreate "+jlocale);
    backbuttonAct = getIntent().getStringExtra("backbuttonAct");
    fullscreen = getIntent().getStringExtra("fullscreen");
    // remove title
    if (fullscreen.equals("yes")) {
      getSupportActionBar().hide();
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                           WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
  }
  @Override
  protected void onStart()
  {
    Log.d(JConsoleApp.LogTag,"onStart "+jlocale);
    super.onStart();
    if (null!=form)
      form.onStart();
    else
      jInterface.callJ( "(i.0 0)\"_ onStart_" + jlocale + "_$0");
  }
  @Override
  protected void onPause()
  {
    Log.d(JConsoleApp.LogTag,"onPause "+jlocale);
    super.onPause();
    if (null!=form) {
      form.onPause();
      jInterface.callJ( "(i.0 0)\"_ ExecIfExist 'onPause_" + jlocale + "_'");
    }
  }
  @Override
  protected void onResume()
  {
    Log.d(JConsoleApp.LogTag,"onResume "+jlocale);
    super.onResume();
    if (null!=form) {
      form.onResume();
      jInterface.callJ( "(i.0 0)\"_ ExecIfExist 'onResume_" + jlocale + "_'");
    }
  }
  @Override
  protected void onRestart()
  {
    Log.d(JConsoleApp.LogTag,"onRestart "+jlocale);
    super.onRestart();
    if (null!=form) {
      form.onRestart();
      jInterface.callJ( "(i.0 0)\"_ ExecIfExist 'onRestart_" + jlocale + "_'");
    }
  }
  @Override
  protected void onStop()
  {
    Log.d(JConsoleApp.LogTag,"onStop "+jlocale);
    super.onStop();
    if (null!=form) {
      form.onStop();
      jInterface.callJ( "(i.0 0)\"_ ExecIfExist 'onStop_" + jlocale + "_'");
    }
  }
  @Override protected void onDestroy()
  {
    Log.d(JConsoleApp.LogTag,"onDestroy "+jlocale);
    super.onDestroy();
    if(isFinishing()) {
      theApp.removeFile("wd:"+jlocale);
      if (null!=form) {
        if (!theApp.asyncj)form.onDestroy(); // not working for asyncj
      }
    }
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
    if (null==form) {
      theApp.removeFile("wd:"+jlocale);
      JWdActivity.super.onBackPressed();
      return;
    }
    if (backbuttonAct.equals("never"))
      moveTaskToBack(true);
    else if (backbuttonAct.equals("ask")) {
      new AlertDialog.Builder(this)
      .setTitle("")
      .setMessage("Close this window?")
      .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          JWdActivity.super.onBackPressed();
        }
      })
      .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          // no action
        }
      })
      .setNeutralButton("Hide", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface arg0, int arg1) {
          moveTaskToBack(true);
        }
      }).create().show();
    } else {
      theApp.removeFile("wd:"+jlocale);
      JWdActivity.super.onBackPressed();
    }
  }
  @Override
  public boolean onCreateOptionsMenu( Menu menu)
  {
    Log.d(JConsoleApp.LogTag,"onCreateOptionsMenu "+jlocale);
    if (null==form || null==form.menubar) return super.onCreateOptionsMenu(menu);
    form.menubar.curMenu=menu;
    form.menubar.initmenu();
    jInterface.callJ( "(i.0 0)\"_ onCreateOptionsMenu_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onCreateOptionsMenu_" + jlocale + "_')$0");
    return true;
  }
  @Override
  public boolean onOptionsItemSelected( MenuItem item)
  {
    Log.d(JConsoleApp.LogTag,"onOptionsItemSelected "+jlocale);
    if (null==form || null==form.menubar) return super.onOptionsItemSelected(item);
    if (form.menubar.menu_triggered(item)) return true;
    return super.onOptionsItemSelected(item);
  }
  @Override
  public boolean onTouchEvent(android.view.MotionEvent arg1)
  {
    return super.onTouchEvent( arg1);
  }

  @Override
  public boolean shouldEnableDrawer()
  {
    return true;
  }

  @Override
  protected void addIntent()
  {
    String jlocale = getIntent().getStringExtra("jlocale");
    Log.d(JConsoleApp.LogTag,"wd addIntent "+jlocale);
    theApp.addIntent("wd:"+jlocale, getIntent());
  }

  @Override
  protected FileEdit getEditor()
  {
    return null;
  }

  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState)
  {
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState)
  {
    super.onRestoreInstanceState(savedInstanceState);
  }

}
