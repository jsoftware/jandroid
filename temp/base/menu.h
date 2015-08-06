#ifndef MENU_H
#define MENU_H


class QAction;
class QMenu;

class Menu : public QMenuBar
{
  Q_OBJECT

public:
  Menu() {};

  void createActions();
  void createhelpActions();
  void createMenus(String);
  void createMenus_fini(String s);
  void createuserkeyMenu();

  QAction *projectcloseAct;
  QAction *rundebugAct;
  QAction *runlineadvanceshowAct;
  QAction *runlineshowAct;
  QAction *runprojectAct;
  QAction *viewasciiAct;
  QAction *viewlinenosAct;
  QAction *viewlinewrapAct;

  QMenu *selMenu;
  QMenu *viewMenu;

  List<QAction *> GitEnable;
  List<QAction *> ProjectEnable;
  List<QAction *> ScriptEnable;

private slots:
  void on_userAct_triggered();

private:
  void createcfgMenu();
  void createeditMenu(String s);
  void createfileMenu(String s);
  void createfkeyMenu(String s);
  void createhelpMenu();
  void createlaunchMenu(String s);
  void createprojectMenu(String s);
  void createrunMenu(String s);
  void createscriptMenu();
  void createselMenu();
  void createtoolsMenu(String s);
  void createviewMenu(String s);
  void createwindowMenu(String s);

  QAction *makeact(String id, String text, String shortcut);
  QAction *makeuseract(String text, String shortcut);

  QMenu *cfgMenu;
  QMenu *editMenu;
  QMenu *fileMenu;
  QMenu *fkeyMenu;
  QMenu *helpMenu;
  QMenu *lpadMenu;
  QMenu *projMenu;
  QMenu *runMenu;
  QMenu *scriptMenu;
  QMenu *toolsMenu;
  QMenu *userkeyMenu;
  QMenu *winMenu;

  QAction *cfgbaseAct;
  QAction *cfgdirmAct;
  QAction *cfgfoldersAct;
  QAction *cfglaunchpadAct;
  QAction *cfgopenallAct;
  QAction *cfgqtideAct;
  QAction *cfgstartupAct;
  QAction *cfguserkeysAct;
  QAction *cfgstyleAct;
  QAction *cleartermAct;
  QAction *clipcopyAct;
  QAction *clipcutAct;
  QAction *clippasteAct;
  QAction *editfifAct;
  QAction *editfiwAct;
  QAction *editfontAct;
  QAction *editinputlogAct;
#ifdef QT_OS_ANDROID
  QAction *editwdformAct;
#endif
  QAction *editredoAct;
  QAction *editundoAct;
  QAction *filecloseAct;
  QAction *filecloseallAct;
  QAction *filecloseotherAct;
  QAction *filedeleteAct;
  QAction *filenewAct;
  QAction *filenewtempAct;
  QAction *fileopenAct;
  QAction *fileopenaddonsAct;
  QAction *fileopenallAct;
  QAction *fileopensystemAct;
  QAction *fileopentempAct;
  QAction *fileopenuserAct;
#ifndef QT_NO_PRINTER
  QAction *fileprintAct;
  QAction *fileprintpreviewAct;
  QAction *fileprintallAct;
#endif
  QAction *filequitAct;
  QAction *filerecentAct;
  QAction *filereloadAct;
  QAction *filesaveAct;
  QAction *filesaveallAct;
  QAction *filesaveasAct;
  QAction *projectbuildAct;
  QAction *projectgitguiAct;
  QAction *projectgitstatusAct;
  QAction *projectlastAct;
  QAction *projectnewAct;
  QAction *projectopenAct;
  QAction *projectsnapAct;
  QAction *projectsnapmakeAct;
  QAction *projectterminalAct;
  QAction *runalllines1Act;
  QAction *runalllines2Act;
  QAction *runalllinesAct;
  QAction *runclipAct;
  QAction *runlineAct;
  QAction *runlineadvanceAct;
  QAction *runscriptAct;
  QAction *runselectAct;
  QAction *runtestAct;
  QAction *scriptformatAct;
  QAction *scriptglobalsAct;
  QAction *scriptrestoreAct;
  QAction *scriptsnapAct;
  QAction *toolsdirmAct;
  QAction *toolsfkeysAct;
  QAction *toolslaunchpadAct;
  QAction *toolspacmanAct;
  QAction *tosellowerAct;
  QAction *toselminusAct;
  QAction *toselplusAct;
  QAction *toselplusline1Act;
  QAction *toselplusline2Act;
  QAction *toselsortAct;
  QAction *toseltoggleAct;
  QAction *toselupperAct;
  QAction *toselviewlinewrapAct;
  QAction *vieweditorAct;
  QAction *viewfontminusAct;
  QAction *viewfontplusAct;
  QAction *viewsidebarAct;
  QAction *viewterminalAct;
  QAction *winfileclosexAct;
  QAction *winotherAct;
  QAction *winprojAct;
  QAction *winscriptsAct;
  QAction *winsourceAct;
  QAction *wintextAct;
  QAction *winthrowAct;

  static QAction *sharedHelpAboutAct; // QTBUG-17941
  QAction *helpaboutAct;
  QAction *helpcontextAct;
  QAction *helpcontextnuvocAct;

#ifdef JQT
  QAction *helpconstantsAct;
  QAction *helpcontrolsAct;
  QAction *helpdemoqtAct;
  QAction *helpdemowdAct;
  QAction *helpdictionaryAct;
  QAction *helpforeignsAct;
  QAction *helpgeneralAct;
  QAction *helphelpAct;
  QAction *helplabsAct;
  QAction *helplabsadvanceAct;
  QAction *helplabschaptersAct;
  QAction *helpreleaseAct;
  QAction *helprelnotesAct;
  QAction *helpvocabAct;
  QAction *helpvocabnuvocAct;
#else
  QAction *helpbriefAct;
  QAction *helpintercAct;
  QAction *helpreferenceAct;
  QAction *helpwikiAct;
#endif

};

#endif
