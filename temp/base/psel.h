#ifndef PSEL_H
#define PSEL_H

class ListWidget;

class Psel : public QDialog
{

  Q_OBJECT

public:
  Psel();

private slots:
  void fsel_changed(int row);
  void psel_changed(int row);
  void rsel_changed(int row);
  void itemActivated();

private:
  void getids(int);
  String[] getrecent();
  void getrecentx();

  void init();
  void initsel();
  void initwrite();
  void prefresh();
  void reject();
  void keyReleaseEvent(QKeyEvent *e);

  int Ftx,Rtx;
  ListWidget *panel(String s);
  ListWidget *f,*p,*r;
  String[] Folders,Paths,Projects;
  String[] Recent,RecentFolders,RecentProjects;
  List<String[]> Ids;
  List<int> Idx;
};

#endif
