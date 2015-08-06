package com.jsoftware.j.android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.text.Editable;
import android.util.AttributeSet;
import android.widget.EditText;

public class FileEdit extends EditText
{

  boolean textChanged = false;
  String name;
  protected EditActivity editActivity;

  public void setActivity(EditActivity activity)
  {
    editActivity = activity;
  }
  public FileEdit(Context context, AttributeSet attrs, int defStyle)
  {
    super(context,attrs,defStyle);
  }

  public FileEdit(Context context, AttributeSet attrs)
  {
    super(context,attrs);
  }

  public FileEdit(Context jActivity)
  {
    super(jActivity);
  }

  public void setName(String s)
  {
    name =s ;
  }

  public String getName()
  {
    return name;
  }

  public String getLineForPosition(int n)
  {
    StringBuilder sb = new StringBuilder(getText());
    int start = sb.lastIndexOf("\n", n);
    int end = sb.indexOf("\n", start + 1);
    return sb.substring(start + 1, end == -1 ? sb.length() : end);
  }

  public boolean isTextChanged()
  {
    return textChanged;
  }
  public void setTextChanged(boolean b)
  {
    textChanged = b;
  }

  public void open(File f) throws IOException
  {
    ByteArrayOutputStream bb = new ByteArrayOutputStream();
    byte[] buf = new byte[4096];
    if(f.exists()) {
      InputStream in = new FileInputStream(f);
      int n;
      while((n = in.read(buf)) != -1) {
        bb.write(buf, 0, n);
      }
      in.close();
      setText(bb.toString());
    }
    if(getText().length() > 0) {
      this.setSelection(1);
    }
    this.setSelection(0);
    textChanged = false;
  }

  public void appendSeq(CharSequence seq)
  {
    Editable sp = getText();
    beginBatchEdit();
    sp.append(seq);
    endBatchEdit();
  }

  public void save(File file) throws IOException
  {
    if(textChanged) {
      OutputStream out = new FileOutputStream(file);
      out.write(getText().toString().getBytes());
      out.close();
      textChanged = false;
      editActivity.setFile(file);
      setName(file.getName());
      editActivity.setTitle(createTitle());
    }
  }

  public String createTitle()
  {
    String n = getName();
    if(n != null) {
      StringBuilder sb = new StringBuilder(getName());
      if(textChanged) {
        sb.append(" *");
      }
      return sb.toString();
    } else {
      return null;
    }
  }

  public boolean getTextChanged()
  {
    return textChanged;
  }

  @Override
  public void onTextChanged(CharSequence seq,int start, int lengthBefore, int lengthAfter)
  {
    if(textChanged == false) {
      textChanged = true;
      String tit = createTitle();
      if(tit != null) {
        editActivity.setTitle(tit);
      }
    }
  }
}
