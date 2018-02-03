package com.jsoftware.j.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;
import java.lang.ThreadGroup;

import com.jsoftware.j.ExecutionListener;
import com.jsoftware.j.JInterface;
import com.jsoftware.j.OutputListener;

import android.os.Build;
import android.os.AsyncTask;
import android.util.Log;

public class AndroidJInterface extends JInterface
{

  public static final String INTERFACE_VERSION="1.0";
  Thread runner = null;
  Thread thread = null;
  LinkedList<String> commandBuffer = new LinkedList<String>();
  Boolean waiting = false;

  public AndroidJInterface(JConsoleApp theApp)
  {
    super.theApp = theApp;
    super.asyncj = theApp.asyncj;
    runner = new Thread(new ThreadGroup("j"), new JRunnable(), "j", theApp.stacksize);
  }

  public void stop()
  {
    intr();
  }

  public void start()
  {
    runner.start();
  }

  public void intr()
  {
    if(thread != null && thread.getState() == Thread.State.TIMED_WAITING) {
      thread.interrupt();
    }
  }

  public void addLine(String sentence, boolean addprompt)
  {
    LinkedList<String> linkedList = this.commandBuffer;
    synchronized (linkedList) {
      if (addprompt)
        commandBuffer.addFirst(promptkey + sentence);
      else
        commandBuffer.addFirst(sentence);
      intr();
    }
  }

  public int launchActivity(String action,String data, String type,int flags)
  {
    return theApp.launchActivity(action,data,type,flags);
  }

  public String nextLine(String s)
  {
    synchronized (waiting ) {
      waiting = true;
    }
    try {
      thread = Thread.currentThread();
      while(true) {
        synchronized(commandBuffer) {
          if(commandBuffer.size() >0) {
            synchronized (waiting ) {
              waiting = false;
            }
            return commandBuffer.removeLast();
          }
        }
        try {
          Thread.sleep(250);
        } catch(Exception e) {
          // ignore sleep/interrupted exceptions
        }
      }
    } catch(Exception e) {
      Log.e(LOGTAG,"error reading line",e);
    } finally {
      synchronized(commandBuffer) {
        thread = null;
      }
    }
    synchronized (waiting ) {
      waiting = false;
    }
    return null;
  }

  public String localnextLine()
  {
    synchronized (waiting ) {
      while (waiting) {
        try {
          Thread.sleep(250);
        } catch(Exception e) {
          // ignore sleep/interrupted exceptions
        }
      }
    }
    try {
      thread = Thread.currentThread();
      while(true) {
        synchronized(commandBuffer) {
          if(commandBuffer.size() >0) {
            return commandBuffer.removeLast();
          }
        }
        try {
          Thread.sleep(250);
        } catch(Exception e) {
          // ignore sleep/interrupted exceptions
        }
      }
    } catch(Exception e) {
      Log.e(LOGTAG,"error reading line",e);
    } finally {
      synchronized(commandBuffer) {
        thread = null;
      }
    }
    return null;
  }
  public void quit()
  {
    theApp.quit();
  }

  public Process createProcess(String cmd)
  throws IOException
  {
    Runtime runtime = Runtime.getRuntime();
    return runtime.exec(cmd);
  }

  public int callSuperJ(String sentence, boolean addprompt)
  {
    return super.callJ(sentence, addprompt);
  }
  @Override
  public int callJ(String sentence, boolean addprompt)
  {
    if (asyncj) {
      addLine(sentence, addprompt);
      return 0;
    } else
      return super.callJ(sentence, addprompt);
  }

  protected void callBreak()
  {
    callJ("(i.0 0)\"_ break_z_$0", false);
  }

  class JRunnable implements Runnable
  {
    @Override
    public void run()
    {
      while (true) {
        String cmd = localnextLine();
        if(cmd != null) {
          if (cmd.startsWith(promptkey))
            callSuperJ(cmd.substring(promptkey.length()), true);
          else
            callSuperJ(cmd, false);
        }
      }
    }
  }

}
