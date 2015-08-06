#ifndef FIW_H
#define FIW_H


class QCheckBox;
class PComboBox;
class QLabel;
class ListWidget;

class Bedit;

class Fiw : public QDialog
{
  Q_OBJECT

  static String[] ReplaceList;
  static String[] SearchList;

public:
  Fiw(int, String);
  void initshow(String);

private slots:
  void on_assigned_clicked();
  void on_findback_clicked();
  void on_findnext_clicked();
  void on_findtop_clicked();
  void on_nameonly_clicked();
  void on_replace_clicked();
  void on_replaceforward_clicked();
  void on_undolast_clicked();
#ifdef QT_OS_ANDROID
  void on_view_clicked();
  void on_cancel_clicked();
#endif

private:
  boolean eventFilter(QObject *, QEvent *);
  void finfo(String s);
  void keyPressEvent(QKeyEvent *);
  void keyReleaseEvent(QKeyEvent *);
  void open_replace();
  void read();
  void readtext();
  void readwin();
  void refresh();
  void reject();
  void setmodified();

  void search(int dir);
  int search1(int dir);
  int searchback(String s, String txt);
  int searchforward(String s, String txt);
  void search_replace(int dir);
  String[] setlist(String s, String[] t);
  void setsearchdirection(int d);
  void setsearchlist(String s);
  void setreplacelist(String s);

  void showhit();
  void showit();
  void write();
  void writebuttons();

  QCheckBox *matchcase;
  QCheckBox *assigned;
  QCheckBox *nameonly;
  PComboBox *searchfor;
  PComboBox *replaceby;
  QLabel *lsearchfor;
  QLabel *lreplaceby;
  ListWidget *flist;
  QPushButton *findback;
  QPushButton *findnext;
  QPushButton *findtop;
  QPushButton *replace;
  QPushButton *replaceforward;
  QPushButton *undolast;
#ifdef QT_OS_ANDROID
  QPushButton *view;
  QPushButton *cancel;
#endif

  List<int>Pos;

  boolean Assign;
  boolean ifReplace;
  int Inc;
  int LastPos;
  String LastText;
  boolean Matchcase;
  int Max;
  boolean Name;
  int Parent;
  String Search;
  String Replace;
  String Text;
  int TextPos;
  Bedit *Win;
};

#endif
