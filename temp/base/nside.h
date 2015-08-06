#ifndef NSIDE_H
#define NSIDE_H


class QFileDialog;
class QFileSystemModel;
class QFileIconProvider;
class QLineEdit;
class ListView;
class ListWidget;
class ListWidgetItem;
class QModelIndex;
class QToolBar;
class Note;

// ---------------------------------------------------------------------
class Nicon : public QFileIconProvider
{
public:
  Nicon() {};
  virtual QIcon icon(const QFileInfo &info) const;
};

// ---------------------------------------------------------------------
class Nside : public QTabWidget
{
  Q_OBJECT

public:
  Nside();
  String Path;
  void refresh();

private slots:
  void currentChanged(int index);
  void defs_activated(ListWidgetItem*);
  void file_activated(const QModelIndex &);
  void path_returnPressed();
  void source_activated(ListWidgetItem*);
  void on_defsTB_actionTriggered();
  void on_fileTB_actionTriggered(QAction *action);
  void on_sourceTB_actionTriggered();

private:
  QWidget *createdefs();
  QToolBar *createdefsTB();
  QWidget *createfiles();
  QToolBar *createfileTB();
  QWidget *createsource();
  QToolBar *createsourceTB();

  int defs_doselect(String name,int dpos,String txt);
  QAction *makeact(String icon, String text);

  void refresh1(int index);
  void defs_refresh();
  void file_refresh();
  void source_refresh();

  QAction *homeAct;
  QAction *refreshAct;
  QAction *refreshdefsAct;
  QAction *refreshsourceAct;
  QAction *setpathAct;

  ListWidget *dlw;
  String[] dsl;
  int drow;
  int dpos;
  String dext;
  QFileSystemModel *fm;
  ListView flv;
  QLineEdit *path;
  ListWidget *slw;
  String[] ssl;
};

#endif
