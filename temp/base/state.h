#ifndef STATE_H
#define STATE_H

import com.jsortware.jn.base.base;
import com.jsortware.jn.base.style;

#ifndef QT_NO_PRINTER
class QPrinter;
#endif
class QSyntaxHighlighter;
class QTextDocument;

class Config : public QObject
{

  Q_OBJECT

public:
  Config() {};

  String config_path(String);
  void config_init();
  void dirmatch_init();
  int filepos_get(String f);
  void filepos_set(String f, int p);
  void fkeys_init();
  void folder_init();
  String[] getuserkeys();
  void ini0();
  void init();
  void initide();
  List<int> initposX(List<int>);
  boolean initide1(String f);
  void initstyle();
  void launch_init();
  void noprofile();
  void setfolders();
  void toggleascii();
  void togglelinenos();
  void togglelinewrap();
  void userkeys_init();
  String[] userkeys_split(String s);
  void winpos_init();
  List<int> winpos_read(String id);
  void winpos_save(QWidget *w,String id);
  void winpos_save1(List<int>d,String id);

  QDir AddonsPath;
  String[] AllFolderKeys;
  String[] AllFolderValues;
  String AppName;
  boolean Ascii;
#ifdef QT_OS_ANDROID
  String FontFile;
#endif
  QDir BinPath;
  int BoxForm;
#ifdef TABCOMPLETION
  boolean Completion;
  String CompletionFile;
#endif
  QDir ConfigPath;
  boolean ConfirmClose;
  boolean ConfirmSave;
  List<int> DebugPos;
  List<int> DebugPosX;
  String DefCmt;
  String DefCCmt;
  String DefExt;
  String DefIndent;
  String[] DefTypes;
  String[] DirTreeX;
  String FilePatterns;
  String[] DMFavorites;
  String DMType;
  int DMTypeIndex;
  String[] DMTypes;
  String[] DMTypex;
  List<int>EditPos;
  List<int>EditPosX;
  boolean EscClose;
  String[] FifExt;
  QMap<String,int> FilePos;
  List<int> FKeyKeys;
  List<String[]> FKeyValues;
  QFont Font;
  String Host;
  boolean ifGit;
  String Lang;
  String[] LaunchPadKeys;
  String[] LaunchPadValues;
  String LaunchPadPrefix;
  boolean LineNos;
  boolean LineWrap;
  int MaxInputLog;
  int MaxRecent;
  boolean NoProfile;
  int OpenTabAt;
  String Pass;
  int Port;
#ifndef QT_NO_PRINTER
  QPrinter *Printer;
#endif
  String ProjExt;
  boolean ProjInit;
  String Rxnna;
  String Rxnnz;
  String RunQ;
  int ScreenHeight;
  int ScreenWidth;
  String ScriptFilter;
  boolean SingleWin;
  QDir SnapPath;
  int Snapshots;
  String Snapshotx;
  QDir SystemPath;
  QDir TempPath;
  String Terminal;
  List<int> TermPos;
  List<int> TermPosX;
  boolean TermSyntaxHighlight;
  boolean TrimTrailingWS;
  String User;
  QDir UserPath;
  String[] UserFolderKeys;
  String[] UserFolderValues;
  List<String[]> UserKeys;
  QMap<String,List<int> >WinPos;
  String XDiff;

  Style EditFore;
  Style EditBack;
  Style EditHigh;
  Style TermFore;
  Style TermBack;
  Style TermHigh;

  Style adverbStyle;
  Style commentStyle;
  Style conjunctionStyle;
  Style controlStyle;
  Style functionStyle;
  Style keywordStyle;
  Style nounStyle;
  Style noundefStyle;
  Style numberStyle;
  Style StringStyle;
  Style verbStyle;
};

extern List<QWidget*> ActiveWindows;
extern Config config;
#ifdef QT_OS_ANDROID
extern int androidVfuncPos;
#endif

QSyntaxHighlighter *highlight(QTextDocument *);

void delactivewindow(QWidget*);
QWidget* getactivewindow();
void setactivewindow(QWidget*);

void state_appname();
String[] state_about();
int state_fini();
boolean state_init(int argc, char *argv[], void *jproc);
void state_init_args(int *,char *argv[]);

void state_init_resource();
void state_quit();
void state_reinit();

void var_cmd(String s);
void var_cmddo(String s);
String var_cmdr(String s);
String var_load(String s, boolean d);
void var_run(String s);
void var_runs(String s, boolean show);
void var_set(String s, String t);

extern "C" {
#ifdef JQT
  Dllexport int state_run(int argc, char *argv[],char *lib,boolean fhs, void *jproc, void *jt);
  Dllexport void immexj(const char *s);
#else
  Dllexport int state_run(int argc, char *argv[],char *lib,boolean fhs);
#endif
}

#endif
