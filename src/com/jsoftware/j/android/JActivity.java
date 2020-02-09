package com.jsoftware.j.android;

import java.io.File;

import com.jsoftware.j.ExecutionListener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class JActivity extends AbstractActivity implements ExecutionListener
{
  Console console;
//	ViewGroup container = null;
  File root;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    LayoutInflater.from(this).inflate(R.layout.main, getFrame());

    console = (Console) findViewById(R.id.ws);
    console.setJActivity(this);

    theApp.setConsoleState(true);
    /*
    if(savedInstanceState != null) {
    	console.setText(savedInstanceState.getCharSequence("console"));
    	int n = savedInstanceState.getInt("cursor");
    	console.setSelection(n);
    }
    */
  }

  @Override
  public boolean shouldEnableDrawer()
  {
    return true;
  }

  @Override
  protected void addIntent()
  {
    Intent ii = new Intent(getIntent());
    ii.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    theApp.addIntent(JANDROID, ii);
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
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    boolean result = true;
    int itemId = item.getItemId();
    Log.d(JConsoleApp.LogTag,"onOptionstemSelected selection " + itemId + ", " + getClass().getName());
    switch(itemId) {
    case R.id.clearterm:
      console.clear();
      break;
    case R.id.jbreak:
      theApp.jInterface.callBreak();
      break;
    case R.id.exit:
      testQuit();
      break;
//    case android.R.id.home:
//      testQuit();
//      break;

    default :
      result = false;
    }
    if(!result) {
      result = super.onOptionsItemSelected(item);
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
