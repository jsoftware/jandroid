#ifndef RSEL_H
#define RSEL_H


class ListWidget;
class QVBoxLayout;

class Rsel : public QDialog
{

  Q_OBJECT

public:
  Rsel();

private slots:
  void on_flist_itemActivated();
  void on_open_clicked();
  void on_view_clicked();
  void on_run_clicked();
  void on_rundisplay_clicked();

private:
  QPushButton *makebutton(QVBoxLayout v,String id);
  String selected();

  ListWidget *flist;
  QPushButton *open;
  QPushButton *view;
  QPushButton *run;
  QPushButton *rundisplay;

};

#endif
