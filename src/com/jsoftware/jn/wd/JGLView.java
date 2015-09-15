package com.jsoftware.jn.wd;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import com.jsoftware.j.android.JConsoleApp;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.egl.EGLSurface;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

class JGLView extends SurfaceView implements SurfaceHolder.Callback
{

  private static final int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
  private static final int EGL_CONTEXT_MAJOR_VERSION = 0x3098;
  private static final int EGL_CONTEXT_MINOR_VERSION = 0x30fb;
  private static final int EGL_OPENGL_ES2_BIT = 0x0004;
  private static final int EGL_OPENGL_ES3_BIT = 0x0040;

  private JOpengl pchild;
  private int[] version;
  private int width=0,height=0;

  private GL10 gl = null;
  private EglHelper mEglHelper;

  private SurfaceHolder mHolder;
  private boolean mPause=false;

// ---------------------------------------------------------------------
  JGLView (JOpengl pchild, Context context, int[] version)
  {
    super(context);
    this.pchild=pchild;
    this.version=version;
    init();
  }

// ---------------------------------------------------------------------
  public JGLView (JOpengl pchild, Context context, AttributeSet attrs, int[] version)
  {
    super(context, attrs);
    this.pchild=pchild;
    this.version=version;
    init();
  }

// ---------------------------------------------------------------------
  private void init()
  {
    // Install a SurfaceHolder.Callback so we get notified when the
    // underlying surface is created and destroyed
    mHolder = getHolder();
    mHolder.addCallback(this);
    mEglHelper = new EglHelper();
    /*
     * Specify a configuration for our opengl session
     * and grab the first configuration that matches is
     */
    mEglHelper.start();
    gl = null;
  }

// ---------------------------------------------------------------------
  private int[] getConfigSpec()
  {
    return new int[] {
             EGL10.EGL_RED_SIZE, 8,
             EGL10.EGL_GREEN_SIZE, 8,
             EGL10.EGL_BLUE_SIZE, 8,
             EGL10.EGL_ALPHA_SIZE, 8,
             EGL10.EGL_DEPTH_SIZE, EGL10.EGL_DONT_CARE,
             EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
             EGL10.EGL_CONFIG_CAVEAT, EGL10.EGL_NONE,
             EGL10.EGL_NONE /* marks end of list */
           };
  }

// ---------------------------------------------------------------------
  @Override
  protected void onDraw(Canvas canvas)
  {
    Log.d(JConsoleApp.LogTag,"JGLView onDraw "+pchild.id);
    if (mPause) return;
    mEglHelper.makecurrent();
    pchild.onDraw(canvas);
    swap();
    mEglHelper.makenocurrent();
  }

// ---------------------------------------------------------------------
  @Override
  public void surfaceCreated(SurfaceHolder holder)
  {
    setWillNotDraw(false);
    gl = (GL10) mEglHelper.createSurface(holder);
    pchild.onInitialize();
    mEglHelper.makenocurrent();
  }

// ---------------------------------------------------------------------
  @Override
  public void surfaceDestroyed(SurfaceHolder holder)
  {
    // Surface will be destroyed when we return
    mEglHelper.finish();
  }

// ---------------------------------------------------------------------
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h)
  {
    // Surface size or format has changed
    pchild.onSizeChanged(w, h);
  }

// ---------------------------------------------------------------------
  /**
   * Inform the view that the activity is paused.
   */
  public void onPause()
  {
    mPause=true;
  }

// ---------------------------------------------------------------------
  /**
   * Inform the view that the activity is resumed.
   */
  public void onResume()
  {
    mPause=false;
  }


// ----------------------------------------------------------------------
  public void swap()
  {
    if (null!=mEglHelper)
      mEglHelper.swap();
  }

// ----------------------------------------------------------------------
  public void eglWaitGL()
  {
    mEglHelper.eglWaitGL();
  }

// ----------------------------------------------------------------------
  public void eglWaitNative()
  {
    mEglHelper.eglWaitNative();
  }

// ----------------------------------------------------------------------
  /**
   * An EGL helper class.
   */
  private class EglHelper
  {

    EGL10 mEgl;
    EGLDisplay mEglDisplay;
    EGLSurface mEglSurface;
    EGLConfig mEglConfig;
    EGLContext mEglContext;

// ----------------------------------------------------------------------
    public EglHelper()
    {
    }

// ----------------------------------------------------------------------
    public void makenocurrent()
    {
      if (null!=mEgl)
        mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                            EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT);
    }

// ----------------------------------------------------------------------
    public void makecurrent()
    {
      if (null!=mEgl)
        mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface,
                            mEglContext);
    }

// ----------------------------------------------------------------------
    public void eglWaitNative()
    {
      if (null!=mEgl)
        mEgl.eglWaitNative(EGL10.EGL_CORE_NATIVE_ENGINE, mHolder);
    }

// ----------------------------------------------------------------------
    public void eglWaitGL()
    {
      if (null!=mEgl)
        mEgl.eglWaitGL();
    }

// ----------------------------------------------------------------------
    /**
     * Initialize EGL for a given configuration spec.
     * @param configSpec
     * @param version
     */
    public void start()
    {
      /*
       * Get an EGL instance
       */
      mEgl = (EGL10) EGLContext.getEGL();

      /*
       * Get to the default display.
       */
      mEglDisplay = mEgl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
      if (mEglDisplay == EGL10.EGL_NO_DISPLAY) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglGetDisplay");
      }

      /*
       * We can now initialize EGL for that display
       */
      int[] eglversion = new int[2];
      if (!mEgl.eglInitialize(mEglDisplay, eglversion)) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglInitialize");
      }

      EGLConfig[] configs = new EGLConfig[1];
      int[] num_config = new int[1];
      int[] configSpec = getConfigSpec();
      mEgl.eglChooseConfig(mEglDisplay, configSpec, configs, 1, num_config);
      mEglConfig = configs[0];
      if (null==mEglConfig) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglChooseConfig");
      }

      /*
      * Create an OpenGL ES context. This must be done only once, an
      * OpenGL context is a somewhat heavy object.
      */

      int[] attrib_list;
      if (version[0]==3 && version[1]>0) {
// ES 3.1 format
        attrib_list = new int[] {
          EGL_CONTEXT_MAJOR_VERSION, version[0],
          EGL_CONTEXT_MINOR_VERSION, version[1],
          EGL10.EGL_NONE
        };
      } else {
// ES 3.0 and below
        attrib_list = new int[] {
          EGL_CONTEXT_CLIENT_VERSION, version[0],
          EGL10.EGL_NONE
        };
      }

      mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
                                          EGL10.EGL_NO_CONTEXT, attrib_list);
      if (EGL10.EGL_NO_CONTEXT==mEglContext && 3==version[0] && version[1]>0) {
        version[1] = 0;  // retry for ES 3.0
        attrib_list = new int[] {
          EGL_CONTEXT_CLIENT_VERSION, version[0],
          EGL10.EGL_NONE
        };
        mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
                                            EGL10.EGL_NO_CONTEXT, attrib_list);
      }
      if (EGL10.EGL_NO_CONTEXT==mEglContext && 3==version[0]) {
        version[0] = 2;  // retry for ES 2
        attrib_list[1] = version[0];
        mEglContext = mEgl.eglCreateContext(mEglDisplay, mEglConfig,
                                            EGL10.EGL_NO_CONTEXT, attrib_list);
      }
      if (EGL10.EGL_NO_CONTEXT==mEglContext ) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglCreateContext");
      }

      mEglSurface = null;
    }

// ----------------------------------------------------------------------
    /*
     * Create and return an OpenGL surface
     */
    public GL createSurface(SurfaceHolder holder)
    {
      /*
       *  The window size has changed, so we need to create a new
       *  surface.
       */
      if (mEglSurface != null) {

        /*
         * Unbind and destroy the old EGL surface, if
         * there is one.
         */
        if (!mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                                 EGL10.EGL_NO_SURFACE, EGL10.EGL_NO_CONTEXT)) {
          Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglMakeCurrent");
        };
        if (  !mEgl.eglDestroySurface(mEglDisplay, mEglSurface)) {
          Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglDestroySurface");
        };

      }

      /*
       * Create an EGL surface we can render into.
       */
      mEglSurface = mEgl.eglCreateWindowSurface(mEglDisplay,
                    mEglConfig, holder, null);
      if (null==mEglSurface ) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglCreateWindowSurface");
      };

      /*
       * Before we can issue GL commands, we need to make sure
       * the context is current and bound to a surface.
       */
      if(!mEgl.eglMakeCurrent(mEglDisplay, mEglSurface, mEglSurface,
                              mEglContext)) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglMakeCurrent");
      };

      GL gl = mEglContext.getGL();
      return gl;
    }

// ----------------------------------------------------------------------
    /**
     * Display the current render surface.
     * @return false if the context has been lost.
     */
    public boolean swap()
    {
      if (!mEgl.eglSwapBuffers(mEglDisplay, mEglSurface)) {
        Log.d(JConsoleApp.LogTag,"JGLView failed: "+"eglSwapBuffers");
      };

      /*
       * Always check for EGL_CONTEXT_LOST, which means the context
       * and all associated data were lost (For instance because
       * the device went to sleep). We need to sleep until we
       * get a new surface.
       */
      return mEgl.eglGetError() != EGL11.EGL_CONTEXT_LOST;
    }

// ----------------------------------------------------------------------
    public void finish()
    {
      if (mEglSurface != null) {
        mEgl.eglMakeCurrent(mEglDisplay, EGL10.EGL_NO_SURFACE,
                            EGL10.EGL_NO_SURFACE,
                            EGL10.EGL_NO_CONTEXT);
        mEgl.eglDestroySurface(mEglDisplay, mEglSurface);
        mEglSurface = null;
      }
      if (mEglContext != null) {
        mEgl.eglDestroyContext(mEglDisplay, mEglContext);
        mEglContext = null;
      }
      if (mEglDisplay != null) {
        mEgl.eglTerminate(mEglDisplay);
        mEglDisplay = null;
      }
    }

  }
}

