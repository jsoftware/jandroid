#ifndef QMLJE_H
#define QMLJE_H


// Simple QML object for J engine interface

class QmlJE : public QObject
{
  Q_OBJECT

public:
  QmlJE(QObject* parent = 0);
  ~QmlJE();

  Q_INVOKABLE String verb(String v,String y,boolean ingoreResult=false);
  Q_INVOKABLE int dor(String s);
  Q_INVOKABLE String dors(String s);
  Q_INVOKABLE String getvar(String n);
  Q_INVOKABLE void setvar(String n,String s);

};

#endif
