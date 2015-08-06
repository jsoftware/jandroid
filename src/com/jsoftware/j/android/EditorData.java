package com.jsoftware.j.android;

import android.text.Editable;

class EditorData
{
  int cursorPosition;
  String name;
  Editable text;
  String path;
  boolean changed;

  EditorData(String name,String path,Editable text,int cp, boolean changed)
  {
    this.cursorPosition = cp;
    this.name = name;
    this.text = text;
    this.path = path;
    this.changed = changed;
  }
  public void placeCursor()
  {
    cursorPosition = text.length();
  }
}