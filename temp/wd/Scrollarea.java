

import com.jsoftware.jn.base.state;
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.scrollarea;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// the scrollbars are in pixels and vary as the form is resized
// minimum is 0, single step is 1, page step is 10
// the state shows position and maximum
// the h,v position can be set with wd 'set id pos h p'

// ---------------------------------------------------------------------
ScrollArea::ScrollArea(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="scrollarea";
  QScrollArea *w = new QScrollArea;
  widget=(View*) w;
  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"")) return;
  w.setObjectName(qn);
  childStyle(opt);
  view=f.children.last();
  w.setWidget(view.widget);
  w.setWidgetResizable(true);
  p.layout.removeWidget(view.widget);
}

// ---------------------------------------------------------------------
ScrollArea::~ScrollArea()
{
  if (view && view.widget) {
    ((QScrollArea *)widget).takeWidget();
    delete view.widget;
    view.widget=0;
  }
  if (null!=widget) delete (QScrollArea *)widget;
  widget=0;
}

// ---------------------------------------------------------------------
String ScrollArea::get(String p,String v)
{
  QScrollArea *w=(QScrollArea*) widget;
  QScrollBar *h=w.horizontalScrollBar();
  QScrollBar *q=w.verticalScrollBar();
  String r;
  if (p.equals("property")) {
    r+=String("max")+"\012"+ "min"+"\012"+ "pos"+"\012"+ "value"+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("min"))
    r=Util.i2s(h.minimum())+" "+Util.i2s(q.minimum());
  else if (p.equals("max"))
    r=Util.i2s(h.maximum())+" "+Util.i2s(q.maximum());
  else if (p.equals("pos"|| p=="value"))
    r=Util.i2s(h.sliderPosition())+" "+Util.i2s(q.sliderPosition());
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
// set horizontal, vertical scrollbar positions
// ignore any -1
void ScrollArea::set(String p,String v)
{
  QScrollArea *w=(QScrollArea*) widget;
  QScrollBar *h=w.horizontalScrollBar();
  QScrollBar *q=w.verticalScrollBar();
  String[] opt=Cmd.qsplit(v);

  if (p.equals("pos")) {
    int n;
    if (opt.size()) {
      n=Util.c_strtoi(Util.q2s(opt.at(0)));
      if (n>-1)
        h.setSliderPosition(n);
    }
    if (1<opt.size()) {
      n=Util.c_strtoi(Util.q2s(opt.at(1)));
      if (n>-1)
        q.setSliderPosition(n);
    }
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
// shows horizontal position, maximum
// followed by vertical position, maximum
String ScrollArea::state()
{
  QScrollArea *w=(QScrollArea*) widget;
  QScrollBar *h=w.horizontalScrollBar();
  QScrollBar *v=w.verticalScrollBar();
  return spair(id,Util.i2s(h.sliderPosition())+" "+Util.i2s(h.maximum())+
               " "+Util.i2s(v.sliderPosition())+" "+Util.i2s(v.maximum()));
}

