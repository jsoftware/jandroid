#ifndef CHILD_H
#define CHILD_H


import com.jsoftware.jn.wd.wd;

class Form;
class Pane;

class Child : public QObject
{
  Q_OBJECT

public:
  Child(String n, String s, Form f, Pane p);
  virtual ~Child();

  virtual String getsysdata();

  virtual void cmd(String p,String v);
  virtual String get(String p,String v);
  virtual void set(String p,String v);

  virtual String getfocuschain(bool);

  virtual void setform();
  virtual void setfocuspolicy(String p);
  virtual void setsizepolicy(String p);
  virtual String state()=0;

  virtual void setwh(String);
  virtual void setmaxwhv(String);
  virtual void setmaxwh(int,int);
  virtual void setminwhv(String);
  virtual void setminwh(int,int);

  boolean grouped;
  String id;
  String eid;  // for event
  String event;
  String parms;
  String type;
  String locale;  // for isigraph
  String sysdata;
  String sysmodifiers;
  Form pform;
  Pane ppane;
  View widget;

protected:
  void childStyle(String[]);
};

#endif
