#ifndef WEBVIEW_H
#define WEBVIEW_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;
class WebKitView;

// ---------------------------------------------------------------------
class WebView : public Child
{
  Q_OBJECT

public:
  WebView(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

  QUrl baseUrl;
  String curl;

private slots:

  void urlChanged ( const QUrl & url );

};

#endif
