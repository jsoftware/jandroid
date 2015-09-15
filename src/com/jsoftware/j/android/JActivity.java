package com.jsoftware.j.android;

import java.io.File;

import com.jsoftware.j.ExecutionListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class JActivity extends AbstractActivity implements ExecutionListener
{
  Console console;
//	ViewGroup container = null;
  File root;
  JConsoleApp theApp;
  public static final String JANDROID = "J Android";

  private Runnable timerTask = null;
  private Handler timerHandler = new Handler();
  private long timerInterval = 0;
  private String timerVerb = "sys_timer_z_";

  public JActivity ()
  {
    timerTask = new Runnable() {
      public void run() {
        if (timerInterval > 0) {
          theApp.jInterface.Jnido( this, timerVerb, null, 0);
          timerHandler.postDelayed(this, timerInterval);
        }
      }
    };
  }

  public void setjtimer(int time)
  {
    timerHandler.removeCallbacks(timerTask);
    timerInterval  = Math.max (0l, (long)time);
    if (timerInterval > 0)
      timerHandler.postDelayed(timerTask, timerInterval);
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
//		container = (ViewGroup) findViewById(R.id.mainLayout);
    theApp = (JConsoleApp) this.getApplication();
    console = (Console) findViewById(R.id.ws);
    console.setJActivity(this);

    Intent ii = new Intent(getIntent());
    ii.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    theApp.addIntent(JANDROID, ii);
    theApp.setConsoleState(true);
    /*
    if(savedInstanceState != null) {
    	console.setText(savedInstanceState.getCharSequence("console"));
    	int n = savedInstanceState.getInt("cursor");
    	console.setSelection(n);
    }
    */
  }
  public void quit()
  {
    this.finish();
    int pid = android.os.Process.myPid();
    android.os.Process.killProcess(pid);
  }
  public void testQuit()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage("Are you sure you want to exit J?")
    .setCancelable(false)
    .setPositiveButton(android.R.string.yes,
    new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        quit();
      }
    })
    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.cancel();
      }
    });
    AlertDialog dialog = builder.create();
    dialog.show();

  }

  @Override
  public void finish()
  {
    super.finish();
  }
  @Override
  public void onPostCreate(Bundle savedInstanceState)
  {
    super.onPostCreate(savedInstanceState);
    theApp.setup(this,console);
  }
  @Override
  public void onBackPressed()
  {
    testQuit();
  }
  @Override
  public void onRestoreInstanceState(Bundle savedInstanceState)
  {
    console.setText(savedInstanceState.getCharSequence("console"));
    int n = savedInstanceState.getInt("cursor");
    console.setSelection(n);

  }
  @Override
  public void onSaveInstanceState(Bundle outState)
  {
    int pos = console.getSelectionStart();
    outState.putCharSequence("console",console.getText());
    outState.putInt("cursor",pos);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item)
  {
    boolean result = true;
    int itemId = item.getItemId();
    Log.d(JConsoleApp.LogTag,"selection " + itemId + ", " + getClass().getName());
    switch(itemId) {
    case R.id.clear:
      console.clear();
      break;
    case R.id.jbreak:
      theApp.jInterface.callBreak();
      break;
    case R.id.exit:
      testQuit();
      break;
    default :
      result = false;
    }
    if(!result) {
      result = super.onMenuItemSelected(featureId, item);
    }
    return result;
  }

  protected FileEdit getEditor()
  {
    return console;
  }
  @Override
  protected void onDestroy()
  {
    theApp.setConsoleState(false);
    if(isFinishing()) {
      theApp.getjInterface().destroyJ();
    }
    super.onDestroy();
  }

  public void onCommandComplete(int code)
  {
    Log.d(JConsoleApp.LogTag, "commandComplete receives " + code);
  }
}
