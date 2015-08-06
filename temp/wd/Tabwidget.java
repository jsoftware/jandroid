
import com.jsoftware.jn.wd.tabwidget;

// ---------------------------------------------------------------------
TabWidget::TabWidget(View parent) : QTabWidget (parent)
{
  Q_UNUSED(parent);
}

// ---------------------------------------------------------------------
void TabWidget::nobar (boolean v)
{
  tabBar().setVisible(!v);
}
