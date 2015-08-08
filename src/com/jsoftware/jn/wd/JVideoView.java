package com.jsoftware.jn.wd;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JVideoView extends Child
{

  private String mediaFile="";

// ---------------------------------------------------------------------
  JVideoView(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="videoview";
    VideoView w=new VideoView(f.activity);
    MediaController mc=new MediaController(f.activity);
    w.setMediaController(mc);
    widget=(View) w;
    String qn=n;
    String[] opt=Cmd.qsplit(s);
    if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
    childStyle(opt);

    w.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
      @Override
      public void onCompletion(MediaPlayer mp) {
        JVideoView.this.event="completion";
        pform.signalevent(JVideoView.this);
      }
    });
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    VideoView w=(VideoView) widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      if (p.equals("property")) {
        r.write(Util.s2ba("media"+"\012"));
        r.write(super.get(p,v));
      } else if (p.equals("media"))
        r.write(Util.s2ba(mediaFile));
      else
        r.write(super.get(p,v));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    VideoView w=(VideoView) widget;
    if (p.equals("media")) {
      mediaFile=Util.remquotes(v);
      w.setVideoURI(Uri.parse(mediaFile));
    } else if (p.equals("play"))
      w.start();
    else if (p.equals("stop"))
      w.stopPlayback();
    else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    return new byte[0];
  }

}

