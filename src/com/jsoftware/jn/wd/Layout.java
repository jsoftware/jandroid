package com.jsoftware.jn.wd;

import android.graphics.Canvas;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import com.jsoftware.j.android.JConsoleApp;
import java.util.ArrayList;

class Layout
{

  private Pane ppane;
  ViewGroup bin;
  char type;
  private int r,c,rs,cs;
  private int alignment;
  int stretch;
  private int spacing;
  private boolean razed;
  private int rmax,cmax;

// ---------------------------------------------------------------------
  Layout(char type, int stretch, Pane p)
  {
    ppane=p;
    this.type=type;
    rmax=cmax=1;
    rs=cs=1;
    this.stretch=stretch;
    LinearLayout.LayoutParams lp;
    LinearLayout.LayoutParams lp0;
    if ('h'==type) {
      LinearLayout l=new LinearLayout(ppane.pform.activity);
      l.setOrientation (LinearLayout.HORIZONTAL);
      l.setPadding(0,0,0,0);
      if (null==p.layout)
        lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      else if ('v'==p.layout.type||'u'==p.layout.type)
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
      else if ('v'==p.layout.type||'u'==p.layout.type)
        lp=new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
      else
        lp=new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
      lp.setMargins(0,0,0,0);
      l.setLayoutParams(lp);
      bin= l;
    } else if ('u'==type) {
      ScrollView l=new ScrollView(ppane.pform.activity);
      l.setPadding(0,0,0,0);
      if (null==p.layout)
        lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      else if ('v'==p.layout.type||'u'==p.layout.type)
        lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
      else
        lp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
      lp.setMargins(0,0,0,0);
      l.setLayoutParams(lp);

// vertical layout frame
      LinearLayout l0=new LinearLayout(ppane.pform.activity);
      l0.setOrientation (LinearLayout.VERTICAL);
      l0.setPadding(0,0,0,0);
      lp0=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
      lp0.setMargins(0,0,0,0);
      l0.setLayoutParams(lp0);
      l.addView(l0);

      bin= l;
    } else if ('l'==type) {
      HorizontalScrollView l=new HorizontalScrollView(ppane.pform.activity);
      l.setPadding(0,0,0,0);
      if (null==p.layout)
        lp=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
      else if ('v'==p.layout.type||'u'==p.layout.type)
        lp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
      else
        lp=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
      lp.setMargins(0,0,0,0);
      l.setLayoutParams(lp);

// horiztonal layout frame
      LinearLayout l0=new LinearLayout(ppane.pform.activity);
      l0.setOrientation (LinearLayout.HORIZONTAL);
      l0.setPadding(0,0,0,0);
      lp0=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
      lp0.setMargins(0,0,0,0);
      l0.setLayoutParams(lp0);
      l.addView(l0);

      bin= l;
    }
//  } else if ('g'==type) bin= new GridLayout(ppane.pform.activity);
  }

// ---------------------------------------------------------------------
  void addWidget(View b)
  {
    if (type!='g') {
      if (null!=b) {
        if (type=='u'||type=='l') {
          if (1==bin.getChildCount())
            ((ViewGroup)( bin).getChildAt(0)).addView(b);
        } else
          ( bin).addView(b);
      }
    } else {
//     if (razed) {
//       if ((0<=rmax && r>=rmax) || c>=cmax) {
//         error ("grid size exceeded");
//         return;
//       }
//       if (b) ((TableLayout )bin).addview(b, r, c, (Qt::Alignment) alignment);
//       c=c+1;
//       if (c==cmax) {
//         c=0;
//         r=r+1;
//       }
//     } else {
//       if (b) {
//         if (rs==1 && cs==1) ((TableLayout )bin).addview(b, r, c, (Qt::Alignment) alignment);
//         else ((TableLayout )bin).addView(b, r, c, rs, cs, (Qt::Alignment) alignment);
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
//       ((TableLayout )bin).addView(b.bin, r, c, (Qt::Alignment) alignment);
//       c=c+1;
//       if (c==cmax) {
//         c=0;
//         r=r+1;
//       }
//       if (r==rmax) {
//         c=0;
//         r=0;
//       }
//     } else if (rs==1 && cs==1) ((TableLayout )bin).addView(b.bin, r, c, (Qt::Alignment) alignment);
//     else ((TableLayout )bin).addLayout(b.bin, r, c, rs, cs, (Qt::Alignment) alignment);
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
