#ifndef FIF_H
#define FIF_H


class QCheckBox;
class PComboBox;
class QLabel;
class ListWidget;
class ListWidgetItem;

class Fif : public QDialog
{

  Q_OBJECT

public:
  Fif(String s, boolean b);
  void initshow(String s, boolean b);

private slots:
  void on_assigned_clicked();
  void on_fileonly_clicked();
  void on_find_clicked();
  void on_found_itemActivated(ListWidgetItem * item);
  void on_matchcase_clicked();
  void on_nameonly_clicked();
  void on_regex_clicked();
  void on_subdir_clicked();
#ifdef QT_OS_ANDROID
  void on_view_clicked();
  void on_cancel_clicked();
#endif

private:

  void findit();
  void finfo(String s);
  void init(String s, boolean b);
  List<int> lineindex(List<int> hit,const String txt);
  List<int> lineends(const String txt);
  void read();
  void refresh();
  void reject();
  void keyReleaseEvent(QKeyEvent *e);
  List<int> removedups(List<int> n);
  void research();
  void search();
  String[] searchdo();
  String[] searchfile(String file);
  List<int> searchfilex(String txt);
  String[] searchformat(String f,List<int>hit,String txt);
  void setenable();
  void setpathlist(String p);
  void setsearchlist(String s);
  void setsearchmaxlength();
  void settypelist(String t);
  void write();
  void writebuttons();

  QCheckBox *assigned;
  QCheckBox *fileonly;
  QCheckBox *matchcase;
  QCheckBox *nameonly;
  QCheckBox *regex;
  QCheckBox *subdir;

  PComboBox *searchfor;
  PComboBox *infolder;
  PComboBox *filetypes;

  ListWidget *found;
  QPushButton *find;
#ifdef QT_OS_ANDROID
  QPushButton *view;
  QPushButton *cancel;
#endif

  List<int>Pos;

  boolean Assign;
  boolean Fileonly;
  String Filetypes;
  boolean Matchcase;
  int Max;
  boolean Name;
  String Path;
  String[] PathList;
  boolean Regex;
  String Search;
  String[] SearchList;
  boolean Subdir;
  String Title;
  String Type;
  String[] Types;

  boolean ifRegex;
  boolean ifResults;
  String what;
  QRegExp pat;

};

#endif
