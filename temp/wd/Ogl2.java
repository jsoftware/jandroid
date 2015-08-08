#define GLOPENGL
import com.jsoftware.jn.wd.gl2class;

// ---------------------------------------------------------------------
int gl_updategl(void *g)
{
  if (!g) return 1;
  Form f;
  for (int i=0; i<Forms.length(); i++) {
    f=Forms[i];
    if (f.ischild((Child )g)) {
      if ((((Child )g).type == "opengl") && ((Child )g).widget) {
        ((Opengl2 *)(((Opengl *) g).widget)).updateGL();
        return 0;
      }
    }
  }
  qDebug() << "gl_updategl failed " + String::number((SI)g);
  return 1;
}
