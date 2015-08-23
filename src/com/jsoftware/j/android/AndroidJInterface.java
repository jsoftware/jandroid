package com.jsoftware.j.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import com.jsoftware.j.ExecutionListener;
import com.jsoftware.j.JInterface;
import com.jsoftware.j.OutputListener;

import android.os.Build;
import android.os.AsyncTask;
import android.util.Log;

public class AndroidJInterface extends JInterface
{

  public static final String INTERFACE_VERSION="1.0";
  JRunner runner = null;
  Thread thread = null;
  LinkedList<String> commandBuffer = new LinkedList<String>();

  public AndroidJInterface(JConsoleApp theApp)
  {
    super.theApp = theApp;
    runner = new JRunner();
  }

  public void stop()
  {
    runner.stop();
    intr();
  }

  public void start()
  {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      runner.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[] {});
    }  else {
      runner.execute(new String[] {});
    }
  }

  public void intr()
  {
    if(thread != null && thread.getState() == Thread.State.TIMED_WAITING) {
      thread.interrupt();
    }
  }

  public void addLine(String sentence)
  {
    synchronized (commandBuffer) {
      commandBuffer.addFirst(sentence);
      intr();
    }
  }

  public int launchActivity(String action,String data, String type,int flags)
  {
    return theApp.launchActivity(action,data,type,flags);
  }

  public String nextLine()
  {
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

  protected void callBreak()
  {
    callJ("break_z_ ''");
  }

  class JRunner extends AsyncTask<String, Object, Integer>
    implements ExecutionListener, OutputListener
  {
    LinkedList<String> cms = new LinkedList<String>();
    boolean running = true;

    public void stop()
    {
      running = false;
      //		intr();
    }
    @Override
    protected void onProgressUpdate(Object... objs)
    {
      for(Object o : objs) {
        if(o instanceof Boolean) {
          theApp.setEnableConsole((Boolean)o);
        } else if(o instanceof EngineOutput) {
          theApp.consoleOutput((EngineOutput)o);
        }
      }
    }

    @Override
    protected Integer doInBackground(String... params)
    {
      AndroidJInterface.this.addOutputListener(this);
      thread = Thread.currentThread();
      while (running) {
        String cmd = nextLine();
        if(cmd != null) {
          // publishProgress(false);
          StringBuilder sb = new StringBuilder();
          AndroidJInterface.this.addExecutionListener(this);
          callJ(cmd);
          AndroidJInterface.this.removeExecutionListener(this);
          synchronized (commandBuffer) {
            if(commandBuffer.size() == 0) {
              publishProgress(true);
            }
          }
        }
      }
      AndroidJInterface.this.removeOutputListener(this);
      return 0;
    }

    public void onCommandComplete(int resultCode)
    {
      EngineOutput eo = new EngineOutput(AndroidJInterface.MTYOFM, "   ");
      publishProgress(eo);
    }

    public void onOutput(int type, String s)
    {
      EngineOutput eo = new EngineOutput(type, s);
      publishProgress(eo);
    }
  }
}
