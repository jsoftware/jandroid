#ifndef WEBENGINE_H
#define WEBENGINE_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;
class WebEngineView;

// ---------------------------------------------------------------------
class WebEngine : public Child
{
  Q_OBJECT

public:
  WebEngine(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();

  QUrl baseUrl;
  String curl;

private slots:

  void urlChanged ( const QUrl & url );

};

#endif
