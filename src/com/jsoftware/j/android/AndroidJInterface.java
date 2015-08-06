package com.jsoftware.j.android;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.jsoftware.j.ExecutionListener;
import com.jsoftware.j.JInterface;
import com.jsoftware.j.OutputListener;

import android.os.Build;
import android.os.AsyncTask;
import android.util.Log;

public class AndroidJInterface extends JInterface
{

  JConsoleApp theApp;
  public static final String INTERFACE_VERSION="1.0";
  JRunner runner = null;
  Thread thread = null;
  LinkedList<String> commandBuffer = new LinkedList<String>();

  public AndroidJInterface(JConsoleApp theApp)
  {
    this.theApp = theApp;
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

  int downloadFile(String urlS, String fileS)
  {
    Log.d(LOGTAG,"downloading " + urlS + " to " + fileS);
    int result = -1;
    try {
      URL url = new URL(urlS);

      HttpGet get = new HttpGet(url.toURI());
      get.setHeader("User-Agent", "Mozilla/4.0 (compatible;) JConsoleForAndroid-v" + INTERFACE_VERSION);
      DefaultHttpClient client = new DefaultHttpClient();
      HttpResponse response = client.execute(get);
      HttpEntity entity = response.getEntity();
      InputStream in = null;
      OutputStream out = null;

      try {
        File file = new File(fileS);
        // assure parent folder exists
        if(file.exists()) {
          if(!file.canWrite()) {
            return -2;
          }
        } else {
          file.getParentFile().mkdirs();
          if(!file.getParentFile().canWrite()) {
            return -3;
          }
        }
        out = new FileOutputStream(file);
        in = entity.getContent();
        int rcode = response.getStatusLine().getStatusCode();
        if (rcode != 200) {
          if (rcode == 0) {
            return -99;
          }
          return -rcode;
        }
        byte[] buff = new byte[8092];
        int s;
        int counter = 0;
        while ((s = in.read(buff)) != -1) {
          out.write(buff, 0, s);
          counter+=s;
        }
        result = counter;
        long cl = entity.getContentLength();
        if(cl >= 0) {
          if(cl != result) {
            Log.w(LOGTAG,"content length reports difference while downloading " + fileS + ". cl = "
                  + cl + ", downloaded bytes=" + result);
          }
        }
      } finally {
        try {
          if (out != null)
            out.close();
          if (in != null)
            in.close();
        } catch (Exception e) {
          // quietly ignore
        }
      }
    } catch (MalformedURLException e) {
      Log.e(LOGTAG, "failed to download file due to malformed url: "
            + urlS);
      result = -5;
    } catch (IOException e) {
      Log.e(LOGTAG,
            "failed to download file due to IOException: "
            + e.getLocalizedMessage(), e);
      result = -6;
    } catch (Exception e) {
      Log.e(LOGTAG, "failed to download file.", e);
      result = -1;
    }

    return result;
  }

  public Process createProcess(String cmd)
  throws IOException
  {
    Runtime runtime = Runtime.getRuntime();
    return runtime.exec(cmd);
  }

  protected void callBreak()
  {
    callSuperJ(new String[] {"break_z_ ''"});
  }
  public int callSuperJ(String []sentence)
  {
    return super.callJ(sentence);
  }
  @Override
  public int callJ(String []sentence)
  {
    for(String s : sentence) {
      addLine(s);
    }
    return 0;
  }
  public Object call1J(String s)
  {
    addLine(s);
    return null;
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
          callSuperJ(new String[] {cmd});
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
      EngineOutput eo = new EngineOutput(AndroidJInterface.MTYOFM, "  ");
      publishProgress(eo);
    }

    public void onOutput(int type, String s)
    {
      EngineOutput eo = new EngineOutput(type, s);
      publishProgress(eo);
    }
  }
}
