#ifndef SPIC_H
#define SPIC_H


class PComboBox;
class QPushButton;
class ListWidget;
class PlainTextEdit;
class Eview;

// ---------------------------------------------------------------------
class Picm : public QDialog
{
  Q_OBJECT

public:

  Picm();

private slots:
  void on_sfile_currentIndexChanged();
  void on_times_currentRowChanged(int);
  void on_externaldiff_clicked();
  void on_restore_clicked();
  void on_view_clicked();
  void reject();

private:
  void closeit();
  QWidget *createpanel();
  QWidget *createview();
  void init(String, bool);
  String[] pic_files();
  void tcompare(int n);
  String unstamp(String);
  void keyReleaseEvent(QKeyEvent *e);

  PComboBox *sfile;
  String File;
  String FilePath;
  String SnapPath;
  String[] PicFiles;
  String[] Stamps;
  String[] Temps;
  String Text;
  String[] Texts;
  String Title;
  QPushButton *bview;
  QPushButton *bxdiff;
  QPushButton *brestore;
  ListWidget *times;
  Eview *tview;
};

void pic(String f,String s);

#endif
