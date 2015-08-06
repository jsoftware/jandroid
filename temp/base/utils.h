#ifndef UTILS_H
#define UTILS_H

class QFont;
class Note;
#ifdef TABCOMPLETION
class QAbstractItemModel;
class QCompleter;
#endif

String cpath(String);
String defext(String s);
int fkeynum(int key,boolean c,boolean s);
String[] folder_tree(String b,String f,boolean subdir);
String[] folder_tree1(String b,String s,String[] f);
void fontdiff(int n);
void fontset(QFont font);
extern "C" Dllexport void fontsetsize(int n);
String fontspec(QFont font);
String getcmd(String,String);
#ifdef TABCOMPLETION
QAbstractItemModel *getcompletermodel(QCompleter *,const String&);
#endif
QWidget* getmbparent();
String getsha1(String);
std::String getversion();
boolean gitavailable();
void gitgui(String path);
String gitstatus(String path);
String[] globalassigns(String s,String ext);
int globalassign1(String s,String t,int p);
void helpabout();
boolean ismodifier(int key);
void newfile(QWidget*);
String newtempscript();
void openfile(QWidget*,String s);
void openfile1(String f);
void projectclose();
void projectenable();
String[] project_tree(String b);
String[] project_tree1(String b,String s);
void projectterminal();
String rxassign(String ext, boolean ifglobal);
void setwh(QWidget *w, String s);
void setxywh(QWidget *w, String s);
String[] shell(String cmd, String dir);
void setnote(Note *);
void showide(bool);
String tofoldername(String s);
String toprojectname(String s);
void userkey(int mode, String cmd);
void utils_init();
List<int> winpos_get(QWidget *w);
void winpos_set(QWidget *w,List<int>p);
void xdiff(String s,String t);

extern boolean ShowIde;

#endif
