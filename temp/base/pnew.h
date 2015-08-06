#ifndef PNEW_H
#define PNEW_H


class QCheckBox;
class QDir;
class QLineEdit;
class QPushButton;

class Pnew : public QDialog
{
  Q_OBJECT

public:
  Pnew();
  boolean run();

private slots:
  void on_browse_clicked();
  void on_create_clicked();

private:
  QWidget *createfolderpanel();
  QWidget *createscriptspanel();
  QWidget *createotherpanel();
  QWidget *createbuttonpanel();

  QDir *Dir;
  String Path;
  String Title;

  QCheckBox *cbuild;
  QCheckBox *cinit;
  QCheckBox *crun;

  QLineEdit *folder;
  QLineEdit *other;
};


#endif
