package com.jsoftware.jn.wd;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

public class JTextViewActivity extends android.app.Activity
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
    setContentView(com.jsoftware.j.android.R.layout.textview);
    TextView textview = (TextView) findViewById(com.jsoftware.j.android.R.id.textview);
    textview .setText(text);
  }
}
