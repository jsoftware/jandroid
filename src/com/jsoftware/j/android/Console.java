package com.jsoftware.j.android;

import com.jsoftware.j.JInterface;

import android.content.Context;
import android.graphics.Rect;
import android.text.Editable;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;

import android.view.View;
import android.widget.TextView;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView.OnEditorActionListener;


public class Console extends FileEdit
{
  JConsoleApp theApp;
  JActivity jActivity;

  protected int backgroundColor = 0xffffffff;
  protected int inputColor = 0xff000000;
  protected int outputColor = 0xff000000;
  protected int errorColor = 0xff7f0000;
  protected int systemColor = 0xff00007f;
  protected int logColor = 0xff007f00;
  protected int fileColor = 0xff000000;

  public Console(Context context)
  {
    super(context);
    yinit();
  }

  public Console(Context context, AttributeSet attrs)
  {
    super(context, attrs);
    yinit();
  }

  public Console(Context context, AttributeSet attrs, int defStyle)
  {
    super(context, attrs, defStyle);
    yinit();
  }
  public void setJActivity(JActivity jActivity)
  {
    this.jActivity = jActivity;
  }

  final private  void yinit()
  {
    setText("", BufferType.EDITABLE);
    this.setBackgroundColor(backgroundColor);
    this.setTextColor(inputColor);

    /*
    this.setOnClickListener(new OnClickListener() {
    	String last = null;
    	public void onClick(View v) {
    		if (!isCursorOnBottomLine()) {
    			int n = getSelectionStart();
    			String line = getLineForPosition(n);
    			if(line != null && line.equals(last)) {
    //						appendSeq("\n");
    				handleEnter(line, false);
    				placeCursor();
    				last = null;
    			} else last = line;
    		}
    	}
    });
    */
    /*
    this.setOnKeyListener(new OnKeyListener() {

      public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
          int n = getSelectionStart();
          if (n > 0)
            --n;
          String line = getLineForPosition(n);
          boolean onLast = isCursorOnBottomLine();
    //		  appendSeq("\n");
          handleEnter(line, onLast);

          return true;
        }
        return false;
      }
    });
    */
    this.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == R.id.wsk || actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN) {
          int n = getSelectionStart();
          if (n > 0)
            --n;
          String line = getLineForPosition(n);
          boolean onLast = isCursorOnBottomLine();
          //		  appendSeq("\n");
          handleEnter(line, onLast);
          return true;
        }
        return false;
      }
    });

  }

  private boolean isCursorOnBottomLine()
  {
    String text = getText().toString();
    int n = getSelectionStart();
    boolean b = text.indexOf('\n', n) == -1 ? true : false;
    return b;
  }

  public void setApplication(JConsoleApp app)
  {
    theApp = app;
  }

  @Override
  public void onScrollChanged(int l, int t, int oldl, int oldt)
  {
    cancelLongPress();
    super.onScrollChanged(l, t, oldl, oldt);
  }


  public void handleEnter(String line, boolean last)
  {
    if (last) {
      appendSeq("\n");
      if (line != null && line.trim().length() > 0) {
        theApp.callWithHistory(line);
      } else {
        prompt();
      }
    } else {
      if (line != null) {
        Editable editable = getText();
//				int nn = getSelectionStart();
        if(editable.length() > 0 && editable.charAt(editable.length() -1) != '\n') {
          appendSeq("\n");
        }
        appendSeq(line);
        placeCursor();
      }
    }
  }

  public void placeCursor()
  {
    this.setSelection(getText().length());
  }

  public void replaceText(String s)
  {
    this.beginBatchEdit();
    setText(s, BufferType.EDITABLE);
    this.endBatchEdit();
  }

  protected void prompt()
  {
    appendSeq("  ");
  }

  public void appendSeq(CharSequence seq)
  {
    appendSeq(seq,getColorForType(-1));
  }

  public void appendSeq(CharSequence seq, int colour)
  {
    Editable sp = getText();
    beginBatchEdit();
    int s = sp.length();
    int e = seq.length() + s;
    sp.append(seq);

    sp.setSpan(new ForegroundColorSpan(colour), s, e,
               Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    setSelection(sp.length());
    endBatchEdit();

  }
  @Override
  public void onTextChanged(CharSequence seq,int start, int lengthBefore, int lengthAfter)
  {
//		this.ons
  }
  public void clear()
  {
    replaceText("");
    prompt();
  }
  public int getColorForType(int type)
  {
    int res = inputColor;
    switch (type) {
    case JInterface.MTYOFM:
      res = outputColor;
      break;
    case JInterface.MTYOER:
      res = errorColor;
      break;
    case JInterface.MTYOLOG:
      res = logColor;
      break;
    case JInterface.MTYOSYS:
      res = systemColor;
      break;
    case JInterface.MTYOFILE: // not sure where this should be routed
      res = fileColor;
      break;
    }
    return res;
  }

  public void quit()
  {
    jActivity.quit();
  }
  public void consoleOutput(int type,String s)
  {
    if(s!=null) appendSeq(s,getColorForType(type));
  }

}
