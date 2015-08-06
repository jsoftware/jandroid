#ifndef NTABS_H
#define NTABS_H


class Nedit;
class Note;

class Ntabs : public QTabWidget

{
  Q_OBJECT

public:
  Ntabs(Note *);
  void projectopen(bool);
  String[] gettablist();
  std::String gettabstate();
  void setfont(QFont font);
  void setlinenos(boolean b);
  void setlinewrap(boolean b);
  void setmodified(int index,boolean b);

  void tabclose(int index);
  void tabcloseall();
  void tabclosefile(String f);

  boolean tabopen(String s,int line);
  boolean tabreplace(String s,int line);
  void tabrestore(int index);
  boolean tabsave(int index);
  boolean tabsaveall();
#ifndef QT_NO_PRINTER
  boolean tabprint(int index);
  boolean tabprintall();
#endif
  void tabsaveas(int index);
  int tabsaveOK(int index);
  void tabsetindex(int index);
  void tabsettext(String s);

private slots:
  void currentChanged(int index);
  void fileChanged(const String &);
  void modificationChanged(boolean b);
  void tabCloseRequested(int index);

private:
  int getfileindex(String f);
  void tabsetcolor(int index,boolean ifmod);
  int tabopen1(String s,int line);

  Note *pnote;
  QFileSystemWatcher *watcher;
};

#endif
