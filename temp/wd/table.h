#ifndef TABLE_H
#define TABLE_H


import com.jsoftware.jn.wd.child;

class QTableWidgetItem;
class Form;
class Pane;
class Table;

// ---------------------------------------------------------------------
class QTableWidgex : public QTableWidget
{
  Q_OBJECT

public:
  QTableWidgex(Table *parent);
  Table *p;

protected:
  void mousePressEvent(QMouseEvent *event);
};

// ---------------------------------------------------------------------
class Table : public Child
{
  Q_OBJECT

public:
  Table(String n, String s, Form f, Pane p);

  String get(String p,String v);
  void set(String p,String v);
  String state();
  String lmr;
  QDateTime dblclick;

private slots:
  void on_cellChanged(int,int);
  void on_currentCellChanged(int,int,int,int);
  void on_stateChanged(int);
  void on_headerClicked(int);
  void on_cellClicked(int,int);
  void on_cellDoubleClicked(int,int);
  void on_cellClicked_button();

private:
  void applyhdralign();
  Qt::Alignment getalign(int i);
  QVector<int> getcellvec(QVector<int>);
  boolean getrange(String,int&, int&, int&, int&);
  void initglobals();

  QTableWidgetItem * newitem(int r, int c,String s);
  String readcell(int row,int col);
  String readcellvalue(int row,int col);
  String readcolvalue(int col);
  String readrowvalue(int row);
  String readtable(String v);
  void resetlen(QVector<int> *v, QVector<int> def);
  void set_cell(int r,int c,String v);

  void setalign(String v);
  void setbackforeground(int colortype, String s);
  void setblock(String v);
  void setcell(String v);
  void setcolwidth(String v);
  void setdata(String s);
  void setfontstr(String s);
  void sethdr(String v);
  void sethdralign(String v);
  void setlab(String v);
  void setprotect(String v);
  void setresizecol();
  void setresizerow();
  void setrowheight(String v);
  void setscroll(String v);
  void setselect(String v);
  void setshape(String[]);
  void setsort(String v);
  void settype(String v);

  boolean vecin(QVector<int>vec,QVector<int>values,String id);
  boolean vecisbool(QVector<int>vec,String id);
  QVector<int> vecreshape(QVector<int> vec,int rws,int cls,int rws0,int cls0);

  int cls;
  int len;
  int rws;

  int col;
  int row;
  int lastcol;
  int lastrow;
  int markcol;
  int markrow;

  int row1,row2,col1,col2;
  boolean ifhdr;

  QVector<int> defcellalign;
  QVector<int> defcellprotect;
  QVector<int> defcelltype;

  QVector<int> cellalign;
  QVector<int> cellprotect;
  QVector<int> celltype;
  QVector<View > cellwidget;
  QVector<int> hdralign;

  String[] dat;
};

#endif
