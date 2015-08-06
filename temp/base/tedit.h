#ifndef TEDIT_H
#define TEDIT_H

import com.jsortware.jn.base.bedit;

class Tedit : public Bedit
{
  Q_OBJECT

public:
  Tedit();

  void append(String s);
  void append_smoutput(String s);
  String getprompt();
  void insert(String s);
  void removeprompt();
  void setprompt();
  void setresized(int);
  void promptreplace(String t);

  QScrollBar *hScroll;
  int ifResized, Tw, Th;
  String prompt;
  String smprompt;

public slots:
  void docmdp(String t, boolean show, boolean same);
  void docmds(String t, boolean show);
  void docmdx(String t);
  void itemActivated(ListWidgetItem *);
  void loadscript(String s,boolean show);
  void runall(String s, boolean show=true);

private slots:
#ifdef QT_OS_ANDROID
  void backButtonTimer();
#endif

private:
  void docmd(String t);
  void keyPressEvent(QKeyEvent *);
  void keyReleaseEvent(QKeyEvent *);
  void enter();
#ifdef QT_OS_ANDROID
  int backButtonPressed;
#endif
  void togglemode();
};

extern Tedit *tedit;

#endif
