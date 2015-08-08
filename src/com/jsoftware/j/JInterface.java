package com.jsoftware.j;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import android.util.Log;

public class JInterface
{

  /// output format constants from jlib.h
  public static final int MTYOFM	 =	1;	/* formatted result array output */
  public static final int MTYOER	 =	2;	/* error output */
  public static final int MTYOLOG	 =	3;	/* output log */
  public static final int MTYOSYS	 =	4;	/* system assertion failure */
  public static final int MTYOEXIT =	5;	/* exit */
  public static final int MTYOFILE =	6;	/* output 1!:2[2 */

  public static final String LOGTAG = "jandroid";
  public final boolean asyncj = false;

  private long nativeInstance = 0L;

  private List<ExecutionListener> execlist = new ArrayList<ExecutionListener>();
  private List<OutputListener> outputs = new ArrayList<OutputListener>();

  public int callJ(String []s)
  {
    int result = -1;
    try {
      for(String sentence : s) {
        result = callJ(sentence);
      }
    } catch(Throwable e) {
      Log.e(LOGTAG, "error executing sentence: " + s, e);
    } finally {
      for(ExecutionListener l : execlist) {
        l.onCommandComplete(result);
      }
    }
    return result;
  }


  public int callJ(String sentence)
  {
    int result = -1;
    try {
      if(nativeInstance == 0L) {
        synchronized(this) {
          if(nativeInstance == 0L) {
            nativeInstance = initializeJ(asyncj);
          }
        }
      }
      result = -1;
      Log.i(LOGTAG, "executing: " + sentence);
      result = callJNative(nativeInstance,sentence);
    } catch(Throwable e) {
      Log.e(LOGTAG, "error executing sentence: " + sentence, e);
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
            nativeInstance = initializeJ(asyncj);
          }
        }
      }
      Log.i(LOGTAG, "executing: " + sentence);
      result = dorsNative(nativeInstance,sentence);
    } catch(Throwable e) {
      Log.e(LOGTAG, "error executing sentence: " + sentence, e);
    }
    return result;
  }


  protected synchronized long initializeJ(boolean async)
  {
    return initializeJNative(async);
  }

  public synchronized void destroyJ()
  {
    if(nativeInstance != 0L) {
      destroyJNative(nativeInstance);
      nativeInstance = 0L;
    }
  }
  public Object getVariable(String ident)
  {
    return getVariableNative(nativeInstance, ident);
  }

  public String getLocale()
  {
    return getLocaleNative(nativeInstance);
  }

  public void reset()
  {
    if(nativeInstance != 0L) {
      synchronized(this) {
        if(nativeInstance != 0L) {
          nativeInstance = 0L;
          destroyJ();
        }
        nativeInstance = initializeJ(asyncj);
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

  /// not implemented, returns null
  native public Object getVariableNative(long inst,String name);
  /// not implemented
  native public void setVariableNative(long inst,String name, Object value);

  native public int callJNative(long inst,String s);
  native public String dorsNative(long inst,String s);
  native public void destroyJNative(long inst);
  native public long initializeJNative(boolean async);
  native public String getLocaleNative(long inst);
  native public void setEnvNative(String key,String value);

// to be called back from library
  public void output(int type,String s)
  {
    for(OutputListener oo : outputs) {
      oo.onOutput(type,s);
    }
  }

// callback to j
//cover for native method
  public synchronized Object Jnido(Object appobj,String verb,Object[] objref,int direct)
  {
    return jnido(nativeInstance,appobj,verb,objref,direct);
  }

//native method declaration
  native public Object jnido(long jt,Object appobj,String verb,Object[] objref,int direct);

  static
  {
    try {

      System.loadLibrary("j");
    } catch(Exception e) {
      Log.e(LOGTAG , "failed to load j shared object", e);
      throw new RuntimeException(e);
    }
  }

}
