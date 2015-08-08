
import com.jsoftware.jn.wd.opengl;
import com.jsoftware.jn.wd.opengl2;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Opengl::Opengl(String n, String s, Form f, Pane p) : Child(n,s,f,p)
{
  type="opengl";
  String qn=n;
  String[] opt=Cmd.qsplit(s);
  if (invalidoptn(n,opt,"version compatibility")) return;
#ifdef USE_QOpenGLWidget
  QSurfaceFormat qglFormat;
#else
  QGLFormat qglFormat;
  qglFormat.setSampleBuffers(true);
#endif
#ifdef QT47
  int l=opt.indexOf("version");
  if ((l!=-1) && (l<opt.length-1) && 0!=opt[l+1].toDouble()) {
    int ver1,ver2;
    String s=opt[l+1];
    int d=s.indexOf(".");
    if (d==-1) {
      ver1=s.toInt();
      ver2=0;
    } else {
      ver1=s.mid(0,d).toInt();
      ver2=s.mid(d+1).toInt();
    }
//    qDebug() << String::number(ver1) << String::number(ver2);
    qglFormat.setVersion(ver1,ver2);
  }
#ifdef USE_QOpenGLWidget
  if (opt.contains("compatibility")) qglFormat.setProfile(QSurfaceFormat::CompatibilityProfile);
  else qglFormat.setProfile(QSurfaceFormat::CoreProfile);
#else
  if (opt.contains("compatibility")) qglFormat.setProfile(QGLFormat::CompatibilityProfile);
  else qglFormat.setProfile(QGLFormat::CoreProfile);
#endif
#endif

  Opengl2 *w= new Opengl2(this, qglFormat);
  widget=(View ) w;
  childStyle(opt);
  f.opengl = this;
}

// ---------------------------------------------------------------------
Opengl::~Opengl()
{
  if (null!=widget) delete (Opengl2 *)widget;
  widget=0;
}

// ---------------------------------------------------------------------
void Opengl::setform()
{
  if (null==widget) return;
  if (!(event.equals("paint" || event=="paintz" || event=="resize" || event=="initialize" || event=="print"))) form=pform;
  form.opengl=this;
}

// ---------------------------------------------------------------------
String Opengl::get(String p,String v)
{
  return super.get(p,v);
}

// ---------------------------------------------------------------------
void Opengl::set(String p,String v)
{
  super.set(p,v);
}

// ---------------------------------------------------------------------
String Opengl::state()
{
  return "";
}
