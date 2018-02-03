package com.jsoftware.j.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class HelpActivity extends AbstractActivity
{

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.help);
    TextView wv = (TextView) findViewById(R.id.help);
    Bundle b = getIntent().getExtras();
    String s = b.getString("file");
    setTitle(s);
    String base = b.getString("base");
    File f = new File(base,s);
    try {
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(f)));
      StringBuilder sb = new StringBuilder();
      String line;
      while((line = reader.readLine())!=null) {
        sb.append(line).append("\n");
      }
      wv.setText(sb.toString());
    } catch(IOException e) {
      wv.setText("The was an error reading the requested file " + s);
      Log.e(JConsoleApp.LogTag,"error loading file",e);
    }

  }
  public String buildTitle(File f)  throws IOException
  {
    return "help";
  }
  protected FileEdit getEditor()
  {
    return null;
  }
  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.help, menu);
    return true;
  }
  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    boolean result = true;
    int itemId = item.getItemId();
    Log.d(JConsoleApp.LogTag,"onOptionsItemSelected selection " + itemId + ", " + getClass().getName());
    switch(itemId) {
    default:
      result = false;
    }
    if(!result) {
      result = super.onOptionsItemSelected(item);
    }

    return result;
  }
  @Override
  public boolean shouldEnableDrawer()
  {
    return true;
  }

  @Override
  protected void addIntent()
  {
  }

}
