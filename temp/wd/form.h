#ifndef FORM_H
#define FORM_H


class QMenuBar;
class QSignalMapper;
class QBoxLayout;
class QTimer;

class Child;
class Menus;
class Pane;
class Tabs;

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.wd.child;

class Form : public View
{
  Q_OBJECT

public:
  Form(String id, String p, String locale, View parent = 0);
  ~Form();
  void addchild(Child );
  Pane addpane(int n);
  void addmenu();
  void closepane();

  String hschild();
  String hsform();
  String qform();
  boolean nochild();
  String get(String p,String v);
  void set(String p,String v);
  void setstretch(Child cc, String factor);
  Child setmenuid(String id);
  void setpadding(int l,int t,int r,int b);
  void setpn(String p);
  void setpicon(String p);
  void settaborder(String p);
  void settimer(String p);
  void showit(String p);
  void signalevent(Child c, QKeyEvent *e = 0);
  String state(int evt);
  void status(String);
  String getsysmodifiers();
  String getfocus();

  boolean closed;
  boolean shown;
  int seq;
  String id;
  String event;
  String lasttype;
  String locale;
  String sysdata;
  String sysmodifiers;

  Child child;
  Child evtchild;
  Child isigraph;
  Child opengl;
  List<Child >children;
  Menus *menubar;
  Pane pane;
  List<Pane >panes;
  QTimer *timer;
  Tabs *tab;
  List<Tabs *>tabs;
  QSignalMapper *signalMapper;
  QBoxLayout layout;

public slots:
  void buttonClicked(View );
  Child id2child(String n);
  boolean ischild(Child* n);
  void systimer();

protected:
  void changeEvent(QEvent *);
  void closeEvent(QCloseEvent *);
  void keyPressEvent(QKeyEvent *e);
  void keyReleaseEvent(QKeyEvent *e);

private slots:
#ifdef QT_OS_ANDROID
  void backButtonTimer();
#endif

private:
#ifdef QT_OS_ANDROID
  boolean backButtonPressed;
#endif
  boolean closeok;
  boolean escclose;
  String fakeid;
  String lastfocus;

};

extern Form form;
extern Form evtform;
extern int FormSeq;
extern List<Form >Forms;

#endif
