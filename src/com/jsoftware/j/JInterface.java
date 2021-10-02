package com.jsoftware.j;

import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jsoftware.j.android.JConsoleApp;

public class JInterface
{

  /// output format constants from jlib.h
  public static final int MTYOFM	 =	1;	/* formatted result array output */
  public static final int MTYOER	 =	2;	/* error output */
  public static final int MTYOLOG	 =	3;	/* output log */
  public static final int MTYOSYS	 =	4;	/* system assertion failure */
  public static final int MTYOEXIT =	5;	/* exit */
  public static final int MTYOFILE =	6;	/* output 1!:2[2 */
  public static String promptkey = "\u00fe\u00fd\u00fc\u00fd\u00fe";

  public static boolean asyncj = true;

  private long nativeInstance = 0L;

  private List<ExecutionListener> execlist = new ArrayList<ExecutionListener>();
  private List<OutputListener> outputs = new ArrayList<OutputListener>();

  protected static JConsoleApp theApp;

  public int callJ(String sentence)
  {
    return callJ(sentence, false);
  }

  public int callJ(String sentence, boolean addprompt)
  {
    int result = -1;
    try {
      if(nativeInstance == 0L) {
        synchronized(this) {
          if(nativeInstance == 0L) {
            nativeInstance = initializeJ();
          }
        }
      }
      result = -1;
      Log.d(JConsoleApp.LogTag, "executing: " + sentence);
      result = JDo(sentence);
    } catch(Throwable e) {
      Log.e(JConsoleApp.LogTag, "error executing sentence: " + sentence, e);
    } finally {
      if (asyncj) {
        final boolean addp = addprompt;
        if (addp) {
          theApp.activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
              theApp.consoleOutput(MTYOFM, "   ");
            }
          });
        }
      }
    }
    return result;
  }


  public String dors(String sentence)
  {
    String result = "";
    try {
      if(nativeInstance == 0L) {
        synchronized(this) {
          if(nativeInstance == 0L) {
            nativeInstance = initializeJ();
          }
        }
      }
      Log.d(JConsoleApp.LogTag, "executing: " + sentence);
      result = JDoR(sentence);
    } catch(Throwable e) {
      Log.e(JConsoleApp.LogTag, "error dors sentence: " + sentence, e);
      throw new RuntimeException(e);
    }
    return result;
  }


  public String dorsm(String verb, String y)
  {
    String result = "";
    try {
      if(nativeInstance == 0L) {
        synchronized(this) {
          if(nativeInstance == 0L) {
            nativeInstance = initializeJ();
          }
        }
      }
      Log.d(JConsoleApp.LogTag, "executing: " + verb);
      JSetc("y_jrx_", y, y.length());
      result = JDoR(verb+"]y_jrx_");
    } catch(Throwable e) {
      Log.e(JConsoleApp.LogTag, "error dorsm verb: " + verb, e);
      throw new RuntimeException(e);
    }
    return result;
  }


  public String getc(String s)
  {
    String result = "";
    try {
      if(nativeInstance == 0L) {
        synchronized(this) {
          if(nativeInstance == 0L) {
            nativeInstance = initializeJ();
          }
        }
      }
      Log.d(JConsoleApp.LogTag, "getc: " + s);
      result = JGetc(s);
    } catch(Throwable e) {
      Log.e(JConsoleApp.LogTag, "error getc: " + s, e);
      throw new RuntimeException(e);
    }
    return result;
  }


  public void setc(String s, String v)
  {
    try {
      if(nativeInstance == 0L) {
        synchronized(this) {
          if(nativeInstance == 0L) {
            nativeInstance = initializeJ();
          }
        }
      }
      Log.d(JConsoleApp.LogTag, "setc: " + s);
      JSetc(s, v, v.length());
    } catch(Throwable e) {
      Log.e(JConsoleApp.LogTag, "error setc: " + s, e);
      throw new RuntimeException(e);
    }
  }


  protected synchronized long initializeJ()
  {
    return JInit(theApp.getApplicationInfo().nativeLibraryDir);
  }

  public synchronized void destroyJ()
  {
    if(nativeInstance != 0L) {
      JFree();
      nativeInstance = 0L;
    }
  }

  public String getLocale()
  {
    return JGetLocale();
  }

  public void reset()
  {
    if(nativeInstance != 0L) {
      synchronized(this) {
        if(nativeInstance != 0L) {
          nativeInstance = 0L;
          destroyJ();
        }
        nativeInstance = initializeJ();
      }
    }
  }

  public void addExecutionListener(ExecutionListener listener)
  {
    if(!execlist.contains(listener)) {
      execlist.add(listener);
    }
  }
  public void removeExecutionListener(ExecutionListener listener)
  {
    if(execlist.contains(listener)) {
      execlist.remove(listener);
    }
  }

  public void addOutputListener(OutputListener listener)
  {
    if(!outputs.contains(listener)) {
      outputs.add(listener);
    }
  }

  public void removeOutputListener(OutputListener listener)
  {
    if(outputs.contains(listener)) {
      outputs.remove(listener);
    }
  }

  native public static int JDo(String s);
  native public static String JDoR(String s);
  native public static String JGetc(String nam);
  native public static void JSetc(String nam,String value,long len);
  native public static void JFree();
  native public static long JInit(String libpath);
  native public static long JInit2(String libpath,String libj);
  native public static String JGetLocale();
  native public static void JSetEnv(String key,String value);

  public static String input(String s)
  {
    return JInterface.theApp.jInterface.nextLine(s);
  }

// to be called back from library
  public static void output(final int type,final String s)
  {
    if (!asyncj)
      theApp.consoleOutput(type,s);
    else {
      JInterface.theApp.activity.runOnUiThread(new Runnable() {
        @Override
        public void run() {
          JInterface.theApp.consoleOutput(type, s);
        }
      });
    }
  }

// call back from J
  public static int wd(int t, int[] inta, Object[] inarr, Object[] res, String loc)
  {
    return JConsoleApp.theWd.wd(t, inta, inarr, res, loc);
  }

  static
  {
    try {
      System.loadLibrary("jnative");
    } catch(Exception e) {
      Log.e(JConsoleApp.LogTag , "failed to load j shared object", e);
      throw new RuntimeException(e);
    }
  }

}
