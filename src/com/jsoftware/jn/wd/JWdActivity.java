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
import com.jsoftware.j.android.AndroidJInterface;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.lang.Math;
import java.util.ArrayList;

public class JWdActivity extends Activity
{
  protected com.jsoftware.j.JInterface jInterface = null;
  public java.lang.String jlocale = "";
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
    jInterface = JConsoleApp.theApp.jInterface;
    jlocale = getIntent().getStringExtra("jlocale");
    Log.d(JConsoleApp.LogTag,"onCreate "+jlocale);
    backbuttonAct = getIntent().getStringExtra("backbuttonAct");
    fullscreen = getIntent().getStringExtra("fullscreen");
    // remove title
    if (fullscreen.equals("yes")) {
      if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
        getActionBar().hide();
      else
        requestWindowFeature(Window.FEATURE_NO_TITLE);
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
    if (null!=form) {
      JConsoleApp.theApp.addIntent("wd:"+jlocale, getIntent());
    } else
      finish();
  }
  @Override
  protected void onPause()
  {
    Log.d(JConsoleApp.LogTag,"onPause "+jlocale);
    super.onPause();
    if (null!=form) {
      form.onPause();
      jInterface.callJ( "(i.0 0)\"_ onPause_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onPause_" + jlocale + "_')$0");
    }
  }
  @Override
  protected void onResume()
  {
    Log.d(JConsoleApp.LogTag,"onResume "+jlocale);
    super.onResume();
    if (null!=form) {
      form.onResume();
      jInterface.callJ( "(i.0 0)\"_ onResume_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onResume_" + jlocale + "_')$0");
    }
  }
  @Override
  protected void onRestart()
  {
    Log.d(JConsoleApp.LogTag,"onRestart "+jlocale);
    super.onRestart();
    if (null!=form) {
      form.onRestart();
      jInterface.callJ( "(i.0 0)\"_ onRestart_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onRestart_" + jlocale + "_')$0");
    }
  }
  @Override
  protected void onStop()
  {
    Log.d(JConsoleApp.LogTag,"onStop "+jlocale);
    super.onStop();
    if (null!=form) {
      form.onStop();
      jInterface.callJ( "(i.0 0)\"_ onStop_" + jlocale + "_^:(3=(4!:0 ::_2:)<'onStop_" + jlocale + "_')$0");
    }
  }
  @Override protected void onDestroy()
  {
    Log.d(JConsoleApp.LogTag,"onDestroy "+jlocale);
    super.onDestroy();
    if(isFinishing()) {
      if (null!=form) {
        form.onDestroy();
        JConsoleApp.theApp.removeFile("wd:"+jlocale);
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
      JWdActivity.super.onBackPressed();
    }
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
