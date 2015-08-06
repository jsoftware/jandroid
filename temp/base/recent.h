#ifndef RECENT_H
#define RECENT_H


class QFile;

class Recent : public QObject
{

  Q_OBJECT

public:
  Recent() {};

  String[] DirMatch;
  String[] recentFif;
  String[] Files;

  String ProjectFile;
  String RecentFile;

  boolean ProjectOpen;
  List<String[]> Projects;

  void filesadd(String s);
  void init();

  String[] projectget(String id);
  void projectset(String[] s);

  void save_project();
  void save_recent();

};

extern Recent recent;

#endif
