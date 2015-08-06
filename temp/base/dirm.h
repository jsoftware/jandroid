#ifndef DIRM_H
#define DIRM_H


import com.jsortware.jn.base.plaintextedit;

class Dirm;
class QTableWidget;
class QMenuBar;
class QCheckBox;
class PComboBox;
class PComboBox;
class QLabel;
class ListWidget;
class QTableWidget;

// ---------------------------------------------------------------------
class hPushButton : public QPushButton
{
  Q_OBJECT

public:
  hPushButton(String s)
  {
    setText(s);
    installEventFilter(this);
    update();
  }
  boolean eventFilter(QObject * obj,QEvent * ev)
  {
    return (obj != 0) && ev.type() == QEvent::Paint;
  }
};

// ---------------------------------------------------------------------
class Favs : public QDialog
{
  Q_OBJECT

public:
  Favs(Dirm *);

private slots:
  void cellActivated(int row);

private:
  Dirm *dirm;
  QTableWidget *wfav;

};

// ---------------------------------------------------------------------
class Dirm : public QDialog
{
  Q_OBJECT

public:
  Dirm(String s);
  void dmsetdirs(String,String, bool);

  PComboBox *source;
  PComboBox *target;

private slots:
  void on_compare_clicked();
  void on_compareall_clicked();
  void on_copy_clicked();
  void on_ignore_clicked();
  void on_match_clicked();
  void on_open_clicked();
  void on_view_clicked();
  void on_exdiff_clicked();

  void on_source_currentIndexChanged();
  void on_target_currentIndexChanged();
  void on_type_currentIndexChanged();

  void on_subdir_stateChanged();

  void on_fileselAct_triggered();
  void on_filequitAct_triggered();
  void on_toswapAct_triggered();
  void on_tocopysrcAct_triggered();
  void on_tocopylaterAct_triggered();
  void on_tocopyallAct_triggered();

private:

  void compareallfiles();
  void comparefile();
  void comparexdiff();
  String[] comparefile1(String s);

  void copyall();
  void copyfile();
  void copyfiles(String[]);
  void copylater();
  void copys2t(String s);
  void copysource();

  String dmgetname1();
  String dmgetname2();
  String[] dmgetnames();
  void dminfo(String);
  void dmread();
  void dmsaverecent();
  void dmshowfind();
  void dmwrite();
  void enablefound(bool);
  void favorites();
  void ignorefile();
  void init();
  void init_snp();
  void init_snp1(String p);
  void init_std();
  void matches(bool);
  boolean match_do();
  void match_fmt(bool);
  String match_fmt1(String s,String d,int len);
  String[] match_fmt2(String s,int len);
  void match_refresh(int force);
  void reject();
  void savepos();

  QMenuBar *createmenu();
  QWidget *createpanel();
  QWidget *createview();
  QAction *makeact(String id, String text, String shortcut);
  QPushButton *makebutton(String id, String text);

  boolean Contents;
  boolean Subdir;

  int Max;
  int matched;
  int TypeInx;
  int written;

  QAction *fileselAct;
  QAction *filequitAct;
  QAction *toswapAct;
  QAction *tocopysrcAct;
  QAction *tocopylaterAct;
  QAction *tocopyallAct;

  QCheckBox *timestamp;
  QCheckBox *subdir;

  PComboBox *type;

  QLabel *lsource;
  QLabel *ltarget;
  QLabel *ltype;

  ListWidget *found;
  QPushButton *match;
  hPushButton *filler;
  QPushButton *compareall;
  QPushButton *compare;
  QPushButton *exdiff;
  QPushButton *open;
  QPushButton *view;
  QPushButton *copy;
  QPushButton *ignore;

  String Project;
  String SnapDir;
  String Tab;
  String Title;
  String Source;
  String Sourcex;
  String Target;
  String Targetx;

  String[] Diff;
  String[] Dirs;
  String[] Found;
  String[] NotInSource;
  String[] NotInTarget;

};

#endif
