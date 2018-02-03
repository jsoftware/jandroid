package com.jsoftware.jn.wd;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.view.LayoutInflater;
import com.jsoftware.j.android.AbstractActivity;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.j.android.FileEdit;
import java.io.File;

public class JTextViewActivity extends AbstractActivity
{

  private String title;
  private String header;
  private String text;

  public JTextViewActivity ()
  {
    super();
  }
  @Override
  protected void onCreate(android.os.Bundle arg1)
  {
    super.onCreate( arg1);
    title = getIntent().getStringExtra("title");
    header = getIntent().getStringExtra("header");
    text = getIntent().getStringExtra("text");
    setTitle(title);
    LayoutInflater.from(this).inflate(com.jsoftware.j.android.R.layout.textview, getFrame());
//    setContentView(com.jsoftware.j.android.R.layout.textview);
    TextView textview = (TextView) findViewById(com.jsoftware.j.android.R.id.textview);
    textview.setText(text);
  }

  @Override protected void onDestroy()
  {
    Log.d(JConsoleApp.LogTag,"onDestroy wd:tv:"+getIntent().getStringExtra("title"));
    super.onDestroy();
    if(isFinishing()) {
      theApp.removeFile("wd:tv:"+getIntent().getStringExtra("title"));
    }
  }
  @Override
  public void onBackPressed()
  {
    theApp.removeFile("wd:tv:"+getIntent().getStringExtra("title"));
    JTextViewActivity.super.onBackPressed();
  }
  @Override
  public boolean shouldEnableDrawer()
  {
    return true;
  }

  @Override
  protected void addIntent()
  {
    String title = getIntent().getStringExtra("title");
    Log.d(JConsoleApp.LogTag,"wd addIntent "+title);
    theApp.addIntent("wd:tv:"+title, getIntent());
  }

  @Override
  protected FileEdit getEditor()
  {
    return null;
  }

}
