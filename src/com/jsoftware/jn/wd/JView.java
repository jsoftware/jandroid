package com.jsoftware.jn.wd;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.View;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.wd.JIsigraph;

public class JView extends View
{
  JIsigraph child;
  int count;

// ---------------------------------------------------------------------
  public JView(JIsigraph child, Activity activity)
  {
    super(activity);
    this.child=child;
    if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB)
      setLayerType(View.LAYER_TYPE_SOFTWARE,null);
  }
// ---------------------------------------------------------------------
  @Override
  protected void onDraw (Canvas canvas)
  {
    Log.d(JConsoleApp.LogTag,"JView onDraw "+child.id+" "+ count++);
    child.onDraw (canvas);
  }

// ---------------------------------------------------------------------
  @Override
  protected void onSizeChanged (int w, int h, int oldw, int oldh)
  {
    super.onSizeChanged (w, h, oldw, oldh);
    child.onSizeChanged (w, h, oldw, oldh);
  }

}
