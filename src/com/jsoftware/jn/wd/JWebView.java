package com.jsoftware.jn.wd;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

class JWebView extends Child
{

  private String baseUrl="";
  private String curl="";

// ---------------------------------------------------------------------
  JWebView(String n, String s, Form f, Pane p)
  {
    super(n,s,f,p);
    type="webview";
    WebView w=new WebView(f.activity);
    widget=(View ) w;
    String qn=n;
    baseUrl = "dummy.html";
  }

// ---------------------------------------------------------------------
  @Override
  byte[] get(String p,String v)
  {
    return super.get(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  void set(String p,String v)
  {
    WebView w = (WebView )widget;
    if (p.equals("baseurl")) {
      baseUrl = Util.remquotes(v);
    } else if (p.equals("html")) {
      w.loadDataWithBaseURL(baseUrl,Util.remquotes(v), "text/html", "UTF-8", "");
    } else if (p.equals("url")) {
      w.loadUrl(Util.remquotes(v));
    } else super.set(p,v);
  }

// ---------------------------------------------------------------------
  @Override
  byte[] state()
  {
    WebView w = (WebView )widget;
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(Util.spair(id+"_curl",Util.q2s(curl)));
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }
}
