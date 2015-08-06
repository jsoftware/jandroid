#ifndef COMP_H
#define COMP_H


class Compare : public QObject
{
  Q_OBJECT

public:
  Compare() {};
  String comp(String[] s,String[] t);

private:
  String[] XY;
  QVector<int> AX,AY,NX,NY,SX,SY,X,Y;
  boolean compend();
  void complcs();
  QVector<int> seqlcs(QVector<int>,QVector<int>);
};

String compare(String[],String[]);
String fcompare(String,String);

#endif
