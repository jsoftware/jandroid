#ifndef PANE_H
#define PANE_H


class QGridLayout;
class QBoxLayout;
class QButtonGroup;
class QGroupBox;
class QSignalMapper;
class QSplitter;

class Child;
class Form;
class GroupBox;
class Layout;

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.font;
import com.jsoftware.jn.wd.child;
import com.jsoftware.jn.wd.layout;

class Pane : public View
{
  Q_OBJECT

public:
  Pane(int n,Form f);
  boolean addchild(String n,String c,String p);
  void addlayout(Layout b);
  void bin(String c);
  void fini();
  void grid(String c, String s);
  boolean groupbox(String c, String s);
  boolean line(String c, String s);
  String hschild();
  String hsform();
  boolean nochild();
  void setstretch(Child cc, String factor);
  Child setmenuid(String id);
  void setpn(String p);
  void showit();
  void signalevent(Child c);
  boolean split(String c, String s);
  void splitend();
  String state(int evt);

  Form pform;
  String event;
  String lasttype;
  String locale;
  String sysdata;
  String sysmodifiers;
  int maxsizew;
  int maxsizeh;
  int minsizew;
  int minsizeh;

  QButtonGroup *buttongroup;
  Child child;
  Child evtchild;
  QGroupBox *groupboxw;
  QSignalMapper *signalMapper;
  Layout layout;
  List<Layout >layouts;
  QSplitter *Cmd.qsplitter;
  List<int> Cmd.qsplitterp;

};

#endif
