

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.image;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Image::Image(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="image";

  String qn=Util.s2q(n);
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"transparent ignore keep expand")) return;

  lab=new Image2();
  imageFile="";

  if (opt.contains("ignore")) aspectRatio=0;
  else if (opt.contains("keep")) aspectRatio=1;
  else if (opt.contains("expand")) aspectRatio=2;
  else aspectRatio=-1; // notuse
  lab.aspectRatio=aspectRatio;
  lab.setContentsMargins(0,0,0,0);

  if (-1==aspectRatio) {
    lab.setBackgroundRole(QPalette::Base);
    lab.setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
    QScrollArea *w = new QScrollArea;
    widget=(View ) w;
    w.setObjectName(qn);
    if (opt.contains("transparent")) {
      w.setFrameShape(QFrame::NoFrame);
      w.setAttribute(Qt::WA_TranslucentBackground);
    } else
      w.setBackgroundRole(QPalette::Dark);
    w.setContentsMargins(0,0,0,0);
    w.setWidget(lab);
  } else {
    lab.setSizePolicy(QSizePolicy::Expanding, QSizePolicy::Expanding);
    widget=(View ) lab;
    lab.setObjectName(qn);
    if (opt.contains("transparent")) {
      lab.transparent=true;
      lab.setAttribute(Qt::WA_TranslucentBackground);
    } else {
      lab.transparent=false;
      lab.setBackgroundRole(QPalette::Dark);
    }
  }
  childStyle(opt);
}

// ---------------------------------------------------------------------
String Image::get(String p,String v)
{
  String r;
  if (p.equals("property")) {
    r+=String("image")+"\012";
    r+=Child::get(p,v);
  } else if (p.equals("image"))
    r=imageFile;
  else
    r=Child::get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Image::set(String p,String v)
{
  if (p.equals("image")) {

    String s=Util.s2q(Util.remquotes(v));
    if (s.isEmpty()) {
      JConsoleApp.theWd.error("set image needs image filename: " + id + " " + p + " " + v);
      return;
    }
    QImage image(s);
    if (image.isNull()) {
      JConsoleApp.theWd.error("set image cannot load image " + id + " " + p + " " + v);
      return;
    }
    imageFile=Util.q2s(s);
    QPixmap pix=QPixmap::fromImage(image);
    if (-1==aspectRatio) lab.resize(pix.size());
    lab.setPixmap(pix);
    lab.update();
  } else Child::set(p,v);
}

// ---------------------------------------------------------------------
String Image::state()
{
  return "";
}

// ---------------------------------------------------------------------
Image2::Image2(View parent) :
View(parent)
{
}

// ---------------------------------------------------------------------
void Image2::paintEvent(QPaintEvent *event)
{
  View::paintEvent(event);
  if (pix.isNull()) return;

  QPainter painter(this);
  painter.setRenderHint(QPainter::Antialiasing);
  if (-1!=aspectRatio) {
    QSize pixSize = pix.size();
    pixSize.scale(event.rect().size(), (Qt::AspectRatioMode)aspectRatio);
    QPixmap scaledPix = pix.scaled(pixSize, (Qt::AspectRatioMode)aspectRatio, Qt::SmoothTransformation );
    if (!transparent) painter.fillRect(rect(), QPalette::Dark);
    painter.drawPixmap(QPoint(0,0), scaledPix);
  } else {
    if (!transparent) painter.fillRect(rect(), QPalette::Dark);
    painter.drawPixmap(QPoint(0,0), pix);
  }
}

// ---------------------------------------------------------------------
const QPixmap* Image2::pixmap() const {
  return &pix;
}

// ---------------------------------------------------------------------
void Image2::setPixmap (const QPixmap &pixmap)
{
  pix = pixmap;
}

