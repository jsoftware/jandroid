

import com.jsoftware.jn.base.pcombobox;
import com.jsoftware.jn.base.plaintextedit;
import com.jsoftware.jn.base.state;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.table;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.wd.font;

static QVector<int>CellAligns;
static QVector<int>CellTypes;
extern int rc;

// -------------------------------------------------------------------
QTableWidgex::QTableWidgex(Table *parent)
{
  p=parent;
}

// -------------------------------------------------------------------
void QTableWidgex::mousePressEvent(QMouseEvent* event)
{
  switch (event.button()) {
  case Qt::LeftButton:
    p.lmr = "l";
    break;
  case Qt::MidButton:
    p.lmr = "m";
    break;
  case Qt::RightButton:
    p.lmr = "r";
    break;
  default:
    ;
  }
  QTableWidget::mousePressEvent(event);
}

// -------------------------------------------------------------------
Table::Table(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="table";
  initglobals();
  ifhdr=false;
  rws=cls=len=0;
  row1=col1=0;
  row2=col2=-1;
  markrow=markcol=0;
  dblclick=QDateTime::currentDateTime();

  QTableWidgex *w=new QTableWidgex(this);
  widget=(View*) w;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"selectrows sortable")) return;
  String[] shape;

  if (opt.length>=2) {
    if ((isint(opt[0])) && ((isint(opt[1])))) {
      shape=Cmd.qsplit((opt[0])+" "+(opt[1]));
      setshape(shape);
    }
  }

  w.resizeColumnsToContents();
  w.resizeRowsToContents();
  w.horizontalHeader().setDefaultAlignment(Qt::AlignLeft);
  w.horizontalHeader().setHighlightSections(false);
  w.horizontalHeader().setStretchLastSection(true);
  w.horizontalHeader().setVisible(false);

  w.verticalHeader().setHighlightSections(false);
  w.verticalHeader().setVisible(false);
  QFontMetrics fm(w.font());
  w.verticalHeader().setDefaultSectionSize(fm.height() + 6);

  w.setSelectionMode(QAbstractItemView::ContiguousSelection);
  w.setAlternatingRowColors(true);

  if (opt.contains("selectrows")) {
    w.setSelectionBehavior(QAbstractItemView::SelectRows);
    w.selectRow(0);
  }

#ifndef QT50
  if (opt.contains("sortable")) {
    w.horizontalHeader().setClickable(true);
    connect(w.horizontalHeader(),SIGNAL(sectionDoubleClicked(int)),this,SLOT(on_headerClicked(int)));
  }
#endif

  connect(w,SIGNAL(cellChanged(int,int)),
          this,SLOT(on_cellChanged(int,int)));
  connect(w,SIGNAL(cellClicked(int,int)),
          this,SLOT(on_cellClicked(int,int)));
  connect(w,SIGNAL(cellDoubleClicked(int,int)),
          this,SLOT(on_cellDoubleClicked(int,int)));
  connect(w,SIGNAL(currentCellChanged(int,int,int,int)),
          this,SLOT(on_currentCellChanged(int,int,int,int)));
}

// ---------------------------------------------------------------------
void Table::applyhdralign()
{
  QTableWidget *w=(QTableWidget*) widget;
  if (hdralign.isEmpty() || !ifhdr) return;
  if (hdralign.length==1)
    w.horizontalHeader().setDefaultAlignment(getalign(hdralign[0]));
  else {
    for (int i=0; i<cls; i++)
      (w.horizontalHeaderItem(i)).setTextAlignment(getalign(hdralign[i]));
  }
}

// ---------------------------------------------------------------------
String Table::get(String p,String v)
{
  String[] opt;
  int r,c;

  if (p.equals("property")) {
    String r;
    r+=String("cell")+"\012"+ "col"+"\012"+ "row"+"\012"+ "table"+"\012";
    r+=super.get(p,v);
    return r;
  } else if (p.equals("cell")) {
    opt=Cmd.qsplit(v);
    if (!(opt.length==2)) {
      JConsoleApp.theWd.error("get cell must specify row, column: " + opt.join(" "));
      return"";
    }
    r=Util.c_strtoi(opt[0]);
    c=Util.c_strtoi(opt[1]);
    if (!(((r>=0) && (r<rws)) && ((c>=0) && (c<cls)))) {
      JConsoleApp.theWd.error("cell index out of bounds: " + opt.join(" "));
      return"";
    }
    rc=-1;
    return (readcellvalue(r,c));
  } else if (p.equals("row")) {
    opt=Cmd.qsplit(v);
    if (!(opt.length==1)) {
      JConsoleApp.theWd.error("get row must specify row: " + opt.join(" "));
      return"";
    }
    r=Util.c_strtoi(opt[0]);
    if (!((r>=0) && (r<rws))) {
      JConsoleApp.theWd.error("row index out of bounds: " + opt.join(" "));
      return"";
    }
    rc=-1;
    return (readrowvalue(r));
  } else if (p.equals("col")) {
    opt=Cmd.qsplit(v);
    if (!(opt.length==1)) {
      JConsoleApp.theWd.error("get col must specify column: " + opt.join(" "));
      return"";
    }
    c=Util.c_strtoi(opt[0]);
    if (!((c>=0)&&(c<cls))) {
      JConsoleApp.theWd.error("col index out of bounds: " + opt.join(" "));
      return"";
    }
    rc=-1;
    return (readcolvalue(c));
  } else if (p.equals("table")) {
    return(readtable(v));
  } else
    return super.get(p,v);
}

// ---------------------------------------------------------------------
Qt::Alignment Table::getalign(int i)
{
  switch (i) {
  case 0:
    return Qt::AlignLeft|Qt::AlignVCenter;
  case 1:
    return Qt::AlignHCenter|Qt::AlignVCenter;
  default :
    return Qt::AlignRight|Qt::AlignVCenter;
  }
}

// ---------------------------------------------------------------------
QVector<int> Table::getcellvec(QVector<int> v)
{
  if (len==v.length())
    return v;
  if (1==v.length())
    return QVector<int>(len,v[0]);
  QVector<int> r(len);
  for(int i=0; i<rws; i++)
    for(int j=0; j<cls; j++)
      r[j+i*cls]=v[j];
  return r;
}

// ---------------------------------------------------------------------
// get range for set block, set select
// r1 [r2] c1 [c2] or empty for all
boolean Table::getrange(String v,int &r1, int &r2, int &c1, int &c2)
{
  String[] arg=Cmd.qsplit(v);
  int n=arg.length;
  if (0==n) {
    r1= c1= 0;
    r2= c2= -1;
  } else if (2==n) {
    r1= r2= Util.c_strtoi(arg[0]);
    c1= c2= Util.c_strtoi(arg[1]);
  } else if (4==n) {
    r1= Util.c_strtoi(arg[0]);
    r2= Util.c_strtoi(arg[1]);
    c1= Util.c_strtoi(arg[2]);
    c2= Util.c_strtoi(arg[3]);
  } else {
    JConsoleApp.theWd.error("set range incorrect length: " + arg.join(" "));
    return false;
  }
  if (!(r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2))) {
    JConsoleApp.theWd.error("set range row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + " " + String::number(c1) + " " + String::number(c2));
    return false;
  }
  return true;
}

// ---------------------------------------------------------------------
void Table::initglobals()
{
  if (CellAligns.length()) return;
  CellAligns << 0 << 1 << 2;
  CellTypes << 0 << 10 << 100 << 200 << 300 << 400;
}

// ---------------------------------------------------------------------
QTableWidgetItem * Table::newitem(int r, int c,String s)
{
  int p= c + r*cls;
  QTableWidgetItem * m= new QTableWidgetItem(s);
  m.setTextAlignment(getalign(cellalign[p]));
  if (cellprotect[p]) {
    Qt::ItemFlags fdef=m.flags();
    Qt::ItemFlags fnoedit=fdef & ~(Qt::ItemIsEditable|Qt::ItemIsDragEnabled|Qt::ItemIsDropEnabled);
    m.setFlags(fnoedit);
  }
  return m;
}

// ---------------------------------------------------------------------
String Table::readcell(int row,int col)
{
  QTableWidget *w=(QTableWidget*) widget;
  QTableWidgetItem *m=w.item(row,col);
  int p=col+row*cls;
  View g=cellwidget[p];
  if (0==celltype[p])
    return (!m)?"":m.text();
  else if (10==celltype[p])
    return (!g)?"":((PlainTextEdit *)g).toPlainText();
  else if (100==celltype[p])
    return (!g)?"":((QCheckBox *)g).isChecked()?"1":"0";
  else if ((200==celltype[p]) || (300==celltype[p]))
    return (!g)?"":Util.i2s(((PComboBox *)g).currentIndex());
  else
    return "";
}

// ---------------------------------------------------------------------
String Table::readcellvalue(int row,int col)
{
  QTableWidget *w=(QTableWidget*) widget;
  QTableWidgetItem *m=w.item(row,col);
  int p=col+row*cls;
  View g=cellwidget[p];
  if (0==celltype[p])
    return (!m)?"":m.text();
  else if (10==celltype[p])
    return (!g)?"":((PlainTextEdit *)g).toPlainText();
  else if (100==celltype[p])
    return (!g)?"":((QCheckBox *)g).isChecked()?"1":"0";
  else if ((200==celltype[p]) || (300==celltype[p]))
    return (!g)?"":((PComboBox *)g).currentText();
  else
    return "";
}

// ---------------------------------------------------------------------
String Table::readcolvalue(int col)
{
  String colout="";
  int r;

  for (r=0; r<rws; r++)
    colout += readcellvalue(r,col) + "\177";
  return (colout);
}

// ---------------------------------------------------------------------
String Table::readrowvalue(int row)
{
  String rowout="";
  int c;

  for (c=0; c<cls; c++)
    rowout += readcellvalue(row,c) + "\177";
  return (rowout);
}

// ---------------------------------------------------------------------
String Table::readtable(String v)
{
  String tableout="";
  int r1,r2,c1,c2;

  String[] opt;
  opt=Cmd.qsplit(v);
  if (0==opt.length) {
    r1= 0;
    r2= rws-1;
    c1= 0;
    c2= cls-1;
  } else if (2==opt.length) {
    r1= r2= Util.c_strtoi(opt[0]);
    c1= c2= Util.c_strtoi(opt[1]);
  } else if (4==opt.length) {
    r1= Util.c_strtoi(opt[0]);
    r2= Util.c_strtoi(opt[1]);
    c1= Util.c_strtoi(opt[2]);
    c2= Util.c_strtoi(opt[3]);
  } else {
    JConsoleApp.theWd.error("get table incorrect number of arguments: " + opt.join(" "));
    return "";
  }

  if (!(r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2))) {
    JConsoleApp.theWd.error("get table row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + " " + String::number(c1) + " " + String::number(c2));
    return "";
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;

  for (int r=r1; r<=r2; r++) {
    for (int c=c1; c<=c2; c++) {
      tableout += readcellvalue(r,c) + "\177";
    }
  }
  rc=-1;
  return (tableout);
}

// ---------------------------------------------------------------------
void Table::resetlen(QVector<int> *v, QVector<int> def)
{
  v.resize(len);
  if (len==0) return;

  if (def.length()==1)
    v.fill(def[0]);
  else if (def.length()==cls) {
    int i,j;
    for (i=0; i<rws; i++)
      for (j=0; j<cls; j++)
        v.replace(j+i*cls,def[j]);
  } else if (def.length()==len) {
    for (int i=0; i<len; i++) v.replace(i,def[i]);
  } else
    v.fill(0);
}

// ---------------------------------------------------------------------
void Table::set(String p,String v)
{
  if (p.equals("align"))
    setalign(v);
  else if (p.equals("background"))
    setbackforeground(0,v);
  else if (p.equals("block"))
    setblock(v);
  else if (p.equals("color"))
    setbackforeground(2,v);
  else if (p.equals("colwidth"))
    setcolwidth(v);
  else if (p.equals("data"))
    setdata(v);
  else if (p.equals("font"))
    setfontstr(v);
  else if (p.equals("foreground"))
    setbackforeground(1,v);
  else if (p.equals("hdr"))
    sethdr(v);
  else if (p.equals("hdralign"))
    sethdralign(v);
  else if (p.equals("lab"))
    setlab(v);
  else if (p.equals("protect"))
    setprotect(v);
  else if (p.equals("resizecol"))
    setresizecol();
  else if (p.equals("resizerow"))
    setresizerow();
  else if (p.equals("rowheight"))
    setrowheight(v);
  else if (p.equals("scroll"))
    setscroll(v);
  else if (p.equals("select"))
    setselect(v);
  else if (p.equals("shape"))
    setshape(Cmd.qsplit(v));
  else if (p.equals("type"))
    settype(v);
  else if (p.equals("cell"))
    setcell(v);
  else if (p.equals("sort"))
    setsort(v);
  else super.set(p,v);
}

// ---------------------------------------------------------------------
void Table::setalign(String v)
{
  QVector<int> a=qs2intvector(v);
  int n=a.length();

  int len1,r1,r2,c1,c2;
  r1=row1;
  r2=row2;
  c1=col1;
  c2=col2;
  if (!((r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2)) || (0==rws && ((((c2==-1)?(cls-1):c2)-c1+1)==n || 1==n || 0==n)))) {
    JConsoleApp.theWd.error("set align row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + " " + String::number(c1) + " " + String::number(c2));
    return;
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;
  if (0==rws || 0==n) return;
  boolean colmode= (c2-c1+1)==n;

  if (!(n==1 || n== (len1=(r2-r1+1)*(c2-c1+1)) || colmode)) {
    String m="incorrect align length - ";
    m+= "given " + String::number(n);
    m+=" cells, require " + String::number(len1) + " cells";
    JConsoleApp.theWd.error(m);
    return;
  }
  if(!vecin(a,CellAligns,"align")) return;
  if (0==defcellalign.length) {
    defcellalign=QVector<int>(len,0);
    cellalign=QVector<int>(len,0);
  }

  QTableWidget *w=(QTableWidget*) widget;
  QTableWidgetItem *m;
  int q=0;
  for (int r=r1; r<=r2; r++) {
    for (int c=c1; c<=c2; c++) {
      int p=c + r*cls;
      if (colmode && c==c1) q=0;
      defcellalign[p]=a[q];
      cellalign[p]=a[q];
      if ((m=w.item(r,c))) m.setTextAlignment(getalign(cellalign[p]));
      if (n!=1) q++;
    }
  }
}

// ---------------------------------------------------------------------
void Table::setbackforeground(int colortype, String s)
{
  QTableWidget *w=(QTableWidget*) widget;

  String[] opt=Cmd.qsplit(s);
  int len1,r1,r2,c1,c2;
  int n=opt.length;

  if (2==colortype) {
    if (!(0==n%2)) {
      JConsoleApp.theWd.error("set color requires paired background and foreground values");
      return;
    }
    n=n/2;
  }
  r1=row1;
  r2=row2;
  c1=col1;
  c2=col2;
  if (!((r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2)) || (0==rws && ((((c2==-1)?(cls-1):c2)-c1+1)==n || 1==n || 0==n)))) {
    JConsoleApp.theWd.error("set back/foreground row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + " " + String::number(c1) + " " + String::number(c2));
    return;
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;
  if (0==rws || 0==n) return;
  boolean colmode= (c2-c1+1)==n;

  if (!(n==1 || n== (len1=(r2-r1+1)*(c2-c1+1)) || colmode)) {
    String m="incorrect back/foreground length - ";
    m+= "given " + String::number(n);
    m+=" cells, require " + String::number(len1) + " cells";
    JConsoleApp.theWd.error(m);
    return;
  }

  QTableWidgetItem *m;
  View g;
  QBrush brush,brush2;
  int q=0;
  for (int r=r1; r<=r2; r++) {
    for (int c=c1; c<=c2; c++) {
      int p= c + r*cls;
      if (colmode && c==c1) q=0;
      if (0==celltype[p]) {
        if (!(m=w.item(r,c))) {
          m= newitem(r,c,"");
          w.setItem(r,c,m);
        }
        brush = QBrush(QColor(opt[q]));
        if (0==colortype) m.setBackground(brush);
        else if (1==colortype) m.setForeground(brush);
        else if (2==colortype) {
          m.setBackground(brush);
          brush2 = QBrush(QColor(opt[q+1]));
          m.setForeground(brush2);
        }
      } else if ((g=w.cellWidget(r,c))) {
        if ((0==colortype) && (200>celltype[p])) g.setStyleSheet("background-color: " + opt[q]);
        else if (1==colortype) g.setStyleSheet("color: " + opt[q]);
        else if (2==colortype) {
          if (200>celltype[p]) g.setStyleSheet("background-color: " + opt[q] + "; color: " + opt[q+1]);
          else  g.setStyleSheet("color: " + opt[q+1]);
        }
      }
      if (n!=1) {
        q++;
        if (2==colortype) q++;
      }
    }
  }
}

// ---------------------------------------------------------------------
void Table::setblock(String v)
{
  int r1,r2,c1,c2;
  if (!getrange(v,r1,r2,c1,c2)) return;
  row1=r1;
  row2=r2;
  col1=c1;
  col2=c2;
}

// ---------------------------------------------------------------------
// called by setcell and setdata
void Table::set_cell(int r,int c,String v)
{
  int p=c+r*cls;
  QTableWidget *w=(QTableWidget*) widget;
  String[] dat;

  if (0==celltype[p]) {
    QTableWidgetItem *m=w.item(r,c);
    View g=w.cellWidget(r,c);
    if (g) {
      w.removeCellWidget(r,c);
      cellwidget[p]=0;
    }
    if (!m) {
      m= newitem(r,c,v);
      w.setItem(r,c,m);
    } else
      m.setText(v);
  } else if (10==celltype[p]) {
    if (w.item(r,c)) delete w.item(r,c);
    View g=cellwidget[p];
    if (!(g && String("PlainTextEdit")==g.metaObject().className())) {
      if (w.cellWidget(r,c)) w.removeCellWidget(r,c);
      PlainTextEdit *ed=new PlainTextEdit();
      g=cellwidget[p]=(View*) ed;
      View m=new View();
      QHBoxLayout y=new QHBoxLayout();
      y.setContentsMargins(0,0,0,0);
      y.setSpacing(0);
      y.addStretch(1);
      y.addWidget(ed);
      y.addStretch(1);
      m.setLayout(y);
      w.setCellWidget(r,c,m);
    }
    ((PlainTextEdit  *)cellwidget[p]).setPlainText(v);
  } else if (100==celltype[p]) {
    if (w.item(r,c)) delete w.item(r,c);
    View g=cellwidget[p];
    if (!(g && String("QCheckBox")==g.metaObject().className())) {
      if (w.cellWidget(r,c)) w.removeCellWidget(r,c);
      QCheckBox *cb=new QCheckBox();
#ifdef QT_OS_ANDROID
      cb.setStyleSheet(checkboxstyle(20*DM_density));
#endif
      g=cellwidget[p]=(View*) cb;
      View m=new View();
      QHBoxLayout y=new QHBoxLayout();
      y.setContentsMargins(0,0,0,0);
      y.setSpacing(0);
      y.addStretch(1);
      y.addWidget(cb);
      y.addStretch(1);
      m.setLayout(y);
      w.setCellWidget(r,c,m);
      connect(cb,SIGNAL(stateChanged(int)),
              this,SLOT(on_stateChanged(int)));
    }
    if (v.equals("1"))
      ((QCheckBox *)cellwidget[p]).setChecked(true);
    else
      ((QCheckBox *)cellwidget[p]).setChecked(false);
  } else if ((200==celltype[p]) || (300==celltype[p])) {
    if (w.item(r,c)) delete w.item(r,c);
    View g=cellwidget[p];
    if (!(g && String("PComboBox")==g.metaObject().className())) {
      if (w.cellWidget(r,c)) w.removeCellWidget(r,c);
      PComboBox *cm=new PComboBox();
      if (300==celltype[p])
        cm.setEditable(true);
      g=cellwidget[p]=(View*) cm;
      View m=new View();
      QHBoxLayout y=new QHBoxLayout();
      y.setContentsMargins(0,0,0,0);
      y.setSpacing(0);
      y.addWidget(cm);
      m.setLayout(y);
      w.setCellWidget(r,c,m);
      connect(cm,SIGNAL(currentIndexChanged(int)),
              this,SLOT(on_stateChanged(int)));
    }
    int cmind=0;
    dat= Cmd.qsplit(v);
    if (1==dat.length() && isint(dat[0])) {
      cmind=Util.c_strtoi(dat[0]);
      ((PComboBox *)cellwidget[p]).setCurrentIndex(cmind);
      return;
    }
    if (1<dat.length() && isint(dat[0])) {
      cmind=Util.c_strtoi(dat[0]);
      dat.removeFirst();
    }
    ((PComboBox *)cellwidget[p]).clear();
    ((PComboBox *)cellwidget[p]).addItems(dat);
    ((PComboBox *)cellwidget[p]).setCurrentIndex(cmind);
  } else if (400==celltype[p]) {
    if (w.item(r,c)) delete w.item(r,c);
    View g=cellwidget[p];
    if (!(g && String("QPushButton")==g.metaObject().className())) {
      if (w.cellWidget(r,c)) w.removeCellWidget(r,c);
      QPushButton *pb=new QPushButton(v);
      g=cellwidget[p]=(View*) pb;
      w.setCellWidget(r,c,pb);
      connect(pb,SIGNAL(clicked()),this,SLOT(on_cellClicked()));
    } else
      ((QPushButton *)g).setText(v);
  }
}

// ---------------------------------------------------------------------
void Table::setcell(String v)
{
  String[] opt;

  opt=Cmd.qsplit(v);
  if (!(opt.length==3)) {
    JConsoleApp.theWd.error("set cell must specify row, column, and data: " + opt.join(" "));
    return;
  }
  int r=Util.c_strtoi(opt[0]);
  int c=Util.c_strtoi(opt[1]);
  if (!(((r>=0) && (r<rws)) && ((c>=0) && (c<cls)))) {
    JConsoleApp.theWd.error("cell index out of bounds: " + opt.join(" "));
    return;
  }
  set_cell(r,c,opt[2]);
}

// ---------------------------------------------------------------------
void Table::setcolwidth(String s)
{
  QTableWidget *w=(QTableWidget*) widget;
  String[] opt;
  int c,c1,c2,cs,i,width;

  c1=col1;
  c2=col2;
  if (!((c1>=0 && c1<cls && c2>=-1 && c2<cls && (-1==c2 || c1<=c2)) || (0==cls))) {
    JConsoleApp.theWd.error("set colwidth col1 col2 out of bound: " + String::number(c1) + " " + String::number(c2));
    return;
  }
  if (c2==-1) c2=cls-1;
  if (0==cls) return;
  cs=1+(c2-c1);
  opt=Cmd.qsplit(s);
  if (!((opt.length==1) || opt.length==cs)) {
    JConsoleApp.theWd.error("set colwidth must specify a single width or one for each column in block: " + opt.join(" "));
    return;
  }
  boolean eqwid= (1==opt.length);
  i=0;
  for (c=c1; c<=c2; c++) {
    width= Util.c_strtoi(opt[i]);
    w.setColumnWidth(c,width);
    if (!eqwid) i++;
  }
  return;
}

// ---------------------------------------------------------------------
void Table::setdata(String s)
{
  QTableWidget *w=(QTableWidget*) widget;

  dat=Cmd.qsplit(s);
  int n=dat.length();

  int len1,r1,r2,c1,c2;
  r1=row1;
  r2=row2;
  c1=col1;
  c2=col2;
  if (!((r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2)) || (0==rws && ((((c2==-1)?(cls-1):c2)-c1+1)==n || 1==n || 0==n)))) {
    JConsoleApp.theWd.error("set data row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + String::number(c1) + " " + String::number(c2);
                            return;
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;
  if (0==rws || 0==n) return;
  boolean colmode= (c2-c1+1)==n;

  if (!(n==1 || n== (len1=(r2-r1+1)*(c2-c1+1)) || colmode)) {
    String m="incorrect data length - ";
    m+= "given " + String::number(n);
    m+=" cells, require " + String::number(len1) + " cells";
    JConsoleApp.theWd.error(m);
    return;
  }

  int q=0;
  for (int r=r1; r<=r2; r++) {
    for (int c=c1; c<=c2; c++) {
//    int p= c + r*cls;
      if (colmode && c==c1) q=0;
      set_cell(r,c,dat[q]);
      if (n!=1) q++;
    }
  }
  w.setVisible(false);
  w.resizeColumnsToContents();
  w.resizeRowsToContents();
  w.setVisible(true);
  w.horizontalHeader().setStretchLastSection(true);
}

// ---------------------------------------------------------------------
void Table::setfontstr(String s)
{
  int c,r;
  QTableWidget *w=(QTableWidget*) widget;

  int r1,r2,c1,c2;
  r1=row1;
  r2=row2;
  c1=col1;
  c2=col2;
  if (!((r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2)) || (0==rws))) {
    JConsoleApp.theWd.error("set font row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + String::number(c1) + " " + String::number(c2));
    return;
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;
  if (0==rws) return;

  QFont font= (Font(s)).font;

  QTableWidgetItem *m;
  View g;
  for (r=r1; r<=r2; r++) {
    for (c=c1; c<=c2; c++) {
      int p= c + r*cls;
      if (0==celltype[p]) {
        if (!(m=w.item(r,c))) {
          m= newitem(r,c,"");
          w.setItem(r,c,m);
        }
        m.setFont(font);
      } else if ((g=w.cellWidget(r,c))) {
        g.setFont(font);
      }
    }
  }
}

// ---------------------------------------------------------------------
void Table::sethdr(String v)
{
  QTableWidget *w=(QTableWidget*) widget;
  String[] s=Cmd.qsplit(v);
  if (s.length()!=cls) {
    String m=String::number(s.length());
    m+= " column headers do not match column count of ";
    m+= String::number(cls);
    JConsoleApp.theWd.error(m);
    return;
  }
  w.setHorizontalHeaderLabels(s);
  w.horizontalHeader().setVisible(true);
  ifhdr=true;
  applyhdralign();
}

// ---------------------------------------------------------------------
void Table::sethdralign(String v)
{
  QVector<int> a=qs2intvector(v);
  if (!(a.length()==1 || a.length()==cls)) {
    String m=String::number(a.length());
    m+= " column header alignments do not match column count of ";
    m+= String::number(cls);
    JConsoleApp.theWd.error(m);
    return;
  }
  hdralign=a;
  applyhdralign();
}

// ---------------------------------------------------------------------
void Table::setlab(String v)
{
  QTableWidget *w=(QTableWidget*) widget;
  String[] s=Cmd.qsplit(v);
  if (s.length()!=rws) {
    String m=String::number(s.length());
    m+= " row labels do not match row count of ";
    m+= String::number(rws);
    JConsoleApp.theWd.error(m);
    return;
  }
  w.setVerticalHeaderLabels(s);
  w.verticalHeader().setVisible(true);
}

// ---------------------------------------------------------------------
void Table::setprotect(String v)
{
  QVector<int> a=qs2intvector(v);
  int n=a.length();

  int len1,r1,r2,c1,c2;
  r1=row1;
  r2=row2;
  c1=col1;
  c2=col2;
  if (!((r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2)) || (0==rws && ((((c2==-1)?(cls-1):c2)-c1+1)==n || 1==n || 0==n)))) {
    JConsoleApp.theWd.error("set protect row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + " " + String::number(c1) + " " + String::number(c2));
    return;
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;
  if (0==rws || 0==n) return;
  boolean colmode= (c2-c1+1)==n;

  if (!(n==1 || n== (len1=(r2-r1+1)*(c2-c1+1)) || colmode)) {
    String m="incorrect protect length - ";
    m+= "given " + String::number(n);
    m+=" cells, require " + String::number(len1) + " cells";
    JConsoleApp.theWd.error(m);
    return;
  }
  if(!vecisbool(a,"protect")) return;
  if (0==defcellprotect.length()) {
    defcellprotect=QVector<int>(len,0);
    cellprotect=QVector<int>(len,0);
  }

  QTableWidget *w=(QTableWidget*) widget;
  QTableWidgetItem *m;
  int q=0;
  for (int r=r1; r<=r2; r++) {
    for (int c=c1; c<=c2; c++) {
      int p=c + r*cls;
      if (colmode && c==c1) q=0;
      defcellprotect[p]=a[q];
      cellprotect[p]=a[q];
      if ((m=w.item(r,c))) {
        Qt::ItemFlags fdef=m.flags();
        Qt::ItemFlags fnoedit;
        if (cellprotect[p])
          fnoedit=fdef & ~(Qt::ItemIsEditable|Qt::ItemIsDragEnabled|Qt::ItemIsDropEnabled);
        else
          fnoedit=fdef | (Qt::ItemIsEditable|Qt::ItemIsDragEnabled|Qt::ItemIsDropEnabled);
        m.setFlags(fnoedit);
      }
      if (n!=1) q++;
    }
  }
}

// ---------------------------------------------------------------------
void Table::setresizecol()
{
  QTableWidget *w=(QTableWidget*) widget;
  w.resizeColumnsToContents();
}

// ---------------------------------------------------------------------
void Table::setresizerow()
{
  QTableWidget *w=(QTableWidget*) widget;
  w.resizeRowsToContents();
}

// ---------------------------------------------------------------------
void Table::setrowheight(String s)
{
  QTableWidget *w=(QTableWidget*) widget;
  String[] opt;
  int r,r1,r2,rs,i,height;

  r1=row1;
  r2=row2;
  if (!((r1>=0 && r1<rws && r2>=-1 && r2<rws && (-1==r2 || r1<=r2)) || (0==rws))) {
    JConsoleApp.theWd.error("set rowheight row1 row2 out of bound: " + String::number(r1) + " " + String::number(r2));
    return;
  }
  if (r2==-1) r2=rws-1;
  if (0==rws) return;
  rs=1+(r2-r1);
  opt=Cmd.qsplit(s);
  if (!((opt.length==1) || opt.length==rs)) {
    JConsoleApp.theWd.error("set rowheight must specify a single height or one for each row in block: " + opt.join(" "));
    return;
  }
  boolean eqht= (1==opt.length);
  i=0;
  for (r=r1; r<=r2; r++) {
    height= Util.c_strtoi(opt[i]);
    w.setRowHeight(r,height);
    if (!eqht) i++;
  }
  return;
}

// ---------------------------------------------------------------------
void Table::setscroll(String v)
{
  String[] opt;
  QTableWidget *w=(QTableWidget*) widget;

  opt=Cmd.qsplit(v);
  if (!(opt.length==2)) {
    JConsoleApp.theWd.error("scroll must specify row and column: " + opt.join(" "));
    return;
  }
  int r=Util.c_strtoi(opt[0]);
  int c=Util.c_strtoi(opt[1]);
  if (!(((r>=0) && (r<rws)) && ((c>=0) && (c<cls)))) {
    JConsoleApp.theWd.error("scroll index out of bounds: " + opt.join(" "));
    return;
  }

  QModelIndex index = w.currentIndex();
  Q_UNUSED(index);
  w.scrollTo(w.model().index(r,cls-1), QAbstractItemView::PositionAtTop);
  w.scrollTo(w.model().index(r,c), QAbstractItemView::PositionAtTop);
  w.setFocus();
}

// ---------------------------------------------------------------------
void Table::setselect(String v)
{
  int r1,r2,c1,c2;
  if (!getrange(v,r1,r2,c1,c2)) return;
  QTableWidget *w=(QTableWidget*) widget;
  markrow=qMin(r1,r2);
  markcol=qMin(c1,c2);
  row=qMax(r1,r2);
  col=qMax(c1,c2);
  foreach(QTableWidgetSelectionRange r,w.selectedRanges())
    w.setRangeSelected(r,false);
  QTableWidgetSelectionRange r(markrow,markcol,row,col);
  w.setRangeSelected(r,true);
  w.scrollToItem(w.item(markrow,markcol),QAbstractItemView::EnsureVisible);
  w.scrollToItem(w.item(row,col),QAbstractItemView::EnsureVisible);
}

// ---------------------------------------------------------------------
void Table::setshape(String[] opt)
{
  if (opt.length<2) {
    JConsoleApp.theWd.error("table shape must have rows and columns: " + opt.join(" "));
    return;
  }
  int rws0=rws, cls0=cls;
  rws=Util.c_strtoi(opt[0]);
  cls=Util.c_strtoi(opt[1]);
  len=rws*cls;
  if (rws==rws0 && cls==cls0) return;

  QTableWidget *w=(QTableWidget*) widget;
  w.setRowCount(rws);
  w.setColumnCount(cls);

  cellalign=vecreshape(cellalign,rws,cls,rws0,cls0);
  defcellalign=vecreshape(defcellalign,rws,cls,rws0,cls0);
  cellprotect=vecreshape(cellprotect,rws,cls,rws0,cls0);
  defcellprotect=vecreshape(defcellprotect,rws,cls,rws0,cls0);
  celltype=vecreshape(celltype,rws,cls,rws0,cls0);
  defcelltype=vecreshape(defcelltype,rws,cls,rws0,cls0);

//  resetlen(&cellalign,defcellalign);
//  resetlen(&cellprotect,defcellprotect);
//  resetlen(&celltype,defcelltype);
//  cellwidget.resize(len);
  QVector<View > cw;
  cw.fill(0,rws*cls);
  for(int r=0; r<qMin(rws,rws0); r++)
    for(int c=0; c<qMin(cls,cls0); c++)
      cw[c + r*cls]=cellwidget[c + r*cls0];
  cellwidget=cw;
}

// ---------------------------------------------------------------------
void Table::setsort(String v)
{
  int c;
  QTableWidget *w=(QTableWidget*) widget;
  String[] opt;

  opt=Cmd.qsplit(v);
  if (!(opt.length>0)) {
    JConsoleApp.theWd.error("set sort must specify column: " + opt.join(" "));
    return;
  }
  c=Util.c_strtoi(opt[0]);
  if (opt.contains("descending"))
    w.sortItems(c,Qt::DescendingOrder);
  else
    w.sortItems(c,Qt::AscendingOrder);
}

// ---------------------------------------------------------------------
void Table::settype(String v)
{
  QVector<int> a=qs2intvector(v);
  int n=a.length();

  int len1,r1,r2,c1,c2;
  r1=row1;
  r2=row2;
  c1=col1;
  c2=col2;
  row=col=-1;
  if (!((r1>=0 && r1<rws && c1>=0 && c1<cls && r2>=-1 && r2<rws && c2>=-1 && c2<cls && (-1==r2 || r1<=r2) && (-1==c2 || c1<=c2)) || (0==rws && ((((c2==-1)?(cls-1):c2)-c1+1)==n || 1==n || 0==n)))) {
    JConsoleApp.theWd.error("set type row1 row2 col1 col2 out of bound: " + String::number(r1) + " " + String::number(r2) + " " + String::number(c1) + " " + String::number(c2));
    return;
  }
  if (r2==-1) r2=rws-1;
  if (c2==-1) c2=cls-1;
  if (0==rws || 0==n) return;
  boolean colmode= (c2-c1+1)==n;

  if (!(n==1 || n== (len1=(r2-r1+1)*(c2-c1+1)) || colmode)) {
    String m="incorrect type length - ";
    m+= "given " + String::number(n);
    m+=" cells, require " + String::number(len1) + " cells";
    JConsoleApp.theWd.error(m);
    return;
  }
  if(!vecin(a,CellTypes,"type")) return;
  if (0==defcelltype.length()) {
    defcelltype=QVector<int>(len,0);
    celltype=QVector<int>(len,0);
  }
  int q=0;
  for (int r=r1; r<=r2; r++) {
    for (int c=c1; c<=c2; c++) {
      int p=c + r*cls;
      if (colmode && c==c1) q=0;
      defcelltype[p]=a[q];
      celltype[p]=a[q];
      if (n!=1) q++;
    }
  }
}

// ---------------------------------------------------------------------
String Table::state()
{
  String r;

  if (this!=pform.evtchild || event.equals("mark" || event.substring(0,2)=="mb")) {
    r+=spair(id,Util.i2s(row)+" "+Util.i2s(col));
    QTableWidgex *w=(QTableWidgex*) widget;
    QModelIndexList ml=w.selectionModel().selectedIndexes();
    String mark="";
    if (!ml.isEmpty()) {
      int r0,r1,c0,c1;
      r0=rws;
      c0=cls;
      r1=c1=-1;
      foreach (QModelIndex m,ml) {
        r0=qMin(r0,m.row());
        r1=qMax(r1,m.row());
        c0=qMin(c0,m.column());
        c1=qMax(c1,m.column());
      }
      mark=Util.i2s(r0)+" "+Util.i2s(r1)+" "+Util.i2s(c0)+" "+Util.i2s(c1);
    }
    r+=spair(id+"_select",mark);
  } else  if (event.equals("change")) {
    r+=spair(id,readcell(row,col));
    r+=spair(id+"_cell",Util.i2s(row)+" "+Util.i2s(col));
    r+=spair(id+"_value",readcellvalue(row,col));
  } else if (event.equals("clicked")) {
    r+=spair(id+"_cell",Util.i2s(row)+" "+Util.i2s(col));
  }
  return r;
}

// ---------------------------------------------------------------------
boolean Table::vecin(QVector<int>vec,QVector<int>values,String id)
{
  for(int i=0; i<vec.length(); i++)
    if (!values.contains(vec[i])) {
      JConsoleApp.theWd.error(id + " invalid value: " + String::number(vec[i]));
      return false;
    }
  return true;
}

// ---------------------------------------------------------------------
boolean Table::vecisbool(QVector<int>vec,String id)
{
  for(int i=0; i<vec.length(); i++)
    if (!(vec[i]==0 || vec[i]==1)) {
      JConsoleApp.theWd.error(id + " invalid value: " + String::number(vec[i]));
      return false;
    }
  return true;
}

// ---------------------------------------------------------------------
QVector<int> Table::vecreshape(QVector<int> vec,int rws, int cls, int rws0, int cls0)
{
  QVector<int> vec1;
  vec1.fill(0,rws*cls);
  for(int r=0; r<qMin(rws,rws0); r++)
    for(int c=0; c<qMin(cls,cls0); c++)
      vec1[c + r*cls]=vec[c + r*cls0];
  return vec1;
}

// ---------------------------------------------------------------------
void Table::on_cellChanged (int r,int c)
{
  if (NoEvents) return;
  QTableWidget *w=(QTableWidget*) widget;
  w.resizeColumnsToContents();
  event="change";
  row=r;
  col=c;
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
// see on_cellDoubleClicked for event ignore code
void Table::on_cellClicked (int r, int c)
{
  if (NoEvents) return;
  if (QDateTime::currentDateTime() < dblclick) return;
  row=r;
  col=c;
  event="mb" + lmr + "down";
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
// for button
void Table::on_cellClicked_button ()
{
  if (NoEvents) return;
  event="clicked";
  int p=sender().objectName().toInt();
  row=p/cls;
  col=p-row*cls;
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
// a cellClicked event is given immediately after this, and
// ignored if within dblclick time
void Table::on_cellDoubleClicked (int r, int c)
{
  if (NoEvents) return;
  row=r;
  col=c;
  event="mb" + lmr + "dbl";
  dblclick=QDateTime::currentDateTime().addMSecs(250);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void Table::on_currentCellChanged (int r,int c, int pr, int pc)
{
  if (NoEvents) return;
  event="mark";
  row=r;
  col=c;
  lastrow=pr;
  lastcol=pc;
  if (!ifshift()) {
    markrow=r;
    markcol=c;
  }
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void Table::on_headerClicked (int c)
{
  QTableWidget *w=(QTableWidget*) widget;
  w.sortItems(c,Qt::AscendingOrder);
}

// ---------------------------------------------------------------------
// for checkbox and combolist
void Table::on_stateChanged (int n)
{
  Q_UNUSED(n);
  if (NoEvents) return;
  event="change";
  int p=sender().objectName().toInt();
  row=p/cls;
  col=p-row*cls;
  pform.signalevent(this);
}
