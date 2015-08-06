package com.jsoftware.jn.wd;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.wd.Child;
import com.jsoftware.jn.wd.Pane;
import com.jsoftware.jn.wd.Wd;
import java.util.ArrayList;
import java.util.List;
// add in API 14
// import android.widget.GridLayout;

public class Layout
{

  public Pane ppane;
  public ViewGroup bin;
  public char type;
  public int r,c,rs,cs;
  public int alignment;
  public int stretch;
  public int spacing;
  public boolean razed;
  public int rmax,cmax;

// ---------------------------------------------------------------------
  public Layout(char type, int stretch, Pane p)
  {
    ppane=p;
    this.type=type;
    rmax=cmax=1;
    rs=cs=1;
    this.stretch=stretch;
    LinearLayout.LayoutParams lp;
    if ('h'==type) {
      LinearLayout l=new LinearLayout(ppane.pform.activity);
      l.setOrientation (LinearLayout.HORIZONTAL);
      l.setPadding(0,0,0,0);
      if (null==p.layout)
        lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      else if ('v'==p.layout.type)
        lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
      else
        lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
      lp.setMargins(0,0,0,0);
      l.setLayoutParams(lp);
      bin= l;
    } else if ('v'==type) {
      LinearLayout l=new LinearLayout(ppane.pform.activity);
      l.setOrientation (LinearLayout.VERTICAL);
      l.setPadding(0,0,0,0);
      if (null==p.layout)
        lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      else if ('v'==p.layout.type)
        lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
      else
        lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
      lp.setMargins(0,0,0,0);
      l.setLayoutParams(lp);
      bin= l;
    }
//  } else if ('g'==type) bin= new GridLayout(ppane.pform.activity);
  }

// ---------------------------------------------------------------------
  void addWidget(View b)
  {
    Log.d(JConsoleApp.LogTag,"Layout addWidget addView");
    if (type!='g') {
      if (null!=b) ( bin).addView(b);
    } else {
//     if (razed) {
//       if ((0<=rmax && r>=rmax) || c>=cmax) {
//         error ("grid size exceeded");
//         return;
//       }
//       if (b) ((QGridLayout )bin).addview(b, r, c, (Qt::Alignment) alignment);
//       c=c+1;
//       if (c==cmax) {
//         c=0;
//         r=r+1;
//       }
//     } else {
//       if (b) {
//         if (rs==1 && cs==1) ((QGridLayout )bin).addview(b, r, c, (Qt::Alignment) alignment);
//         else ((QGridLayout )bin).addView(b, r, c, rs, cs, (Qt::Alignment) alignment);
//       }
//     }
    }
  }

// ---------------------------------------------------------------------
  void addLayout(Layout b)
  {
    if (type!='g')
      ( bin).addView(b.bin);
    else {
//     if (razed) {
//       ((QGridLayout )bin).addView(b.bin, r, c, (Qt::Alignment) alignment);
//       c=c+1;
//       if (c==cmax) {
//         c=0;
//         r=r+1;
//       }
//       if (r==rmax) {
//         c=0;
//         r=0;
//       }
//     } else if (rs==1 && cs==1) ((QGridLayout )bin).addView(b.bin, r, c, (Qt::Alignment) alignment);
//     else ((QGridLayout )bin).addLayout(b.bin, r, c, rs, cs, (Qt::Alignment) alignment);
    }
  }

// ---------------------------------------------------------------------
  void addSpacing(int n)
  {
//  if (type!='g') ( bin).addSpacing(n);
  }

// ---------------------------------------------------------------------
  void addStretch(int n)
  {
    if (type!='g') {
      View w=new View(ppane.pform.activity);
      w.setVisibility(View.INVISIBLE);
      LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(0,0,(0==n)?0.01f:(float)n);
      w.setLayoutParams(lp);
      ( bin).addView(w);
    }
  }

// ---------------------------------------------------------------------
  void removeWidget(View b)
  {
    ( bin).removeView(b);
  }

// ---------------------------------------------------------------------
  void setSpacing(int n)
  {
//
  }

// ---------------------------------------------------------------------
  void setContentsMargins(int left, int top, int right, int bottom)
  {
  }

}
