#ifndef PROJ_H
#define PROJ_H


class Project : public QObject
{

  Q_OBJECT

public:
  Project() {};

  String Folder;
  boolean Git;
  String Id;
  String LastId;
  String Path;

  void close();
  boolean closeOK();
  String id2qproj(String);
  void init();
  void open(String id);
  void save(String[]);
  String[] source();

  String fullname(String s);
  String projfile();
  String projectname(String s);

private:
  String projectname1(String s,String[] k,String[] v);

};

extern Project project;

#endif
