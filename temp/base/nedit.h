#ifndef NEDIT_H
#define NEDIT_H

import com.jsortware.jn.base.bedit;

class QFile;

class Nedit : public Bedit

{
  Q_OBJECT

public:
  Nedit();
  String getcomment();
  QFile *file;
  String fname;
  boolean saved;
  String sname;
  String text;
  ~Nedit();

public slots:

private:
  void init_comments();
  void keyPressEvent(QKeyEvent *);
};

#endif
