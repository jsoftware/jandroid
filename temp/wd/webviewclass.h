
#if defined(WEBKITVIEW)

#ifdef QT50
#else
#endif
import com.jsoftware.jn.wd.webview;
import com.jsoftware.jn.wd.webkitview;

#define QWEBVIEW WebKitView
#define WEBVIEW WebView

#elif defined(WEBENGINEVIEW)

import com.jsoftware.jn.wd.webengine;
import com.jsoftware.jn.wd.webengineview;

#define QWEBVIEW WebEngineView
#define WEBVIEW WebEngine

#endif


import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
WEBVIEW::WEBVIEW(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
#if defined(WEBKITVIEW)
  type="webview";
#else
  type="webengine";
#endif
  QWEBVIEW *w=new QWEBVIEW(this);
  widget=(View ) w;
  String qn=Util.s2q(n);
  w.setObjectName(qn);
  baseUrl = QUrl::fromLocalFile(QDir::current().absoluteFilePath("dummy.html"));
  connect(w,SIGNAL(urlChanged( const QUrl & )), this,SLOT(urlChanged( const QUrl & )));
}

// ---------------------------------------------------------------------
String WEBVIEW::get(String p,String v)
{
  return Child::get(p,v);
}

// ---------------------------------------------------------------------
void WEBVIEW::set(String p,String v)
{
  QWEBVIEW *w = (QWEBVIEW *)widget;
  if (p=="baseurl") {
    String t = Util.s2q(Util.remquotes(v));
#if defined(_WIN32) && !defined(QT50)
    baseUrl = QUrl(t);
#else
    if (t.contains("://"))
      baseUrl = QUrl(t);
    else baseUrl = QUrl::fromLocalFile(t);
#endif
  } else if (p=="html") {
    w.setHtml(Util.s2q(Util.remquotes(v)), baseUrl);
    w.show();
  } else if (p=="url") {
    w.load(QUrl(Util.s2q(Util.remquotes(v))));
    w.show();
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
void WEBVIEW::urlChanged(const QUrl & url)
{
  curl=url.toString();
  event="curl";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
String WEBVIEW::state()
{
  return spair(id+"_curl",Util.q2s(curl));
}
