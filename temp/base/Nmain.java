
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.nmain;
import com.jsortware.jn.base.nedit;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.ntabs;
import com.jsortware.jn.base.state;

#ifdef QT_OS_ANDROID
extern float DM_density;
#endif


// ---------------------------------------------------------------------
Nmain::Nmain(Note *n)
{
  QVBoxLayout layout=new QVBoxLayout;
  layout.setContentsMargins(layout.contentsMargins());
  layout.setSpacing(0);
  tabBar = new Ntabs(n);
  n.tabs = tabBar;
  toolBar = new QToolBar(0);
  layout.addWidget(toolBar,0,0);
  layout.addWidget(tabBar,1,0);
  createActions();
  createToolBar();
  setLayout(layout);
}

// ---------------------------------------------------------------------
void Nmain::createActions()
{
  lastprojectAct = makeact("lastprojectAct","undo.png", "Open Last Project");
#ifdef QT_OS_ANDROID
  openprojectAct = makeact("openprojectAct","folder.png","Open Project");
  runallAct = makeact("runallAct","run.png","Run All Lines");
  xeditAct = makeact("xeditAct","regular.png","External editor");
  markCursorAct = makeact("markCursorAct","jump-to.png","Mark cursor");
#else
  openprojectAct = makeact("openprojectAct","folder.svg","Open Project");
  runallAct = makeact("runallAct","run.svg","Run All Lines");
#endif
}

// ---------------------------------------------------------------------
void Nmain::createToolBar()
{
#ifdef QT_OS_ANDROID
  toolBar.setIconSize(QSize((int)DM_density*(5.0/3)*16,(int)DM_density*(5.0/3)*16));
#else
  toolBar.setIconSize(QSize(16,16));
#endif
  toolBar.addAction(lastprojectAct);
  toolBar.addAction(openprojectAct);
  toolBar.addAction(runallAct);
#ifdef QT_OS_ANDROID
  toolBar.addSeparator();
  toolBar.addAction(xeditAct);
  toolBar.addAction(markCursorAct);
#endif
}

// ---------------------------------------------------------------------
QAction *Nmain::makeact(String id, String icon, String text)
{
  QAction *r = new QAction(QIcon(":/images/" + icon),text,this);
  r.setObjectName(id);
  return r;
}
