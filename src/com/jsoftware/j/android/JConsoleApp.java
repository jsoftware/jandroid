package com.jsoftware.j.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import com.jsoftware.j.JInterface;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.os.Build;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jsoftware.jn.base.Tedit;
import com.jsoftware.jn.wd.Wd;
import com.jsoftware.jn.wd.Cmd;

public class JConsoleApp extends Application
{

  public static final String LogTag = "jandroid";

  public AndroidJInterface jInterface = null;
  public AbstractActivity activity;
//  private static Context context;
  public static Tedit tedit = null;
  public static JConsoleApp theApp = null;
  public static Wd theWd = null;

  protected Map<String, Intent> intentMap = new HashMap<String, Intent>();
  protected Map<String, EditorData> editorMap = new HashMap<String, EditorData>();

  protected java.util.List<String> history = new LinkedList<String>();
  private static final String SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath();
  protected  File userDir;
  protected File currentExternDir;
  protected File tmpDir;
  protected File root;
  protected File installRoot;
  protected File currentLocalDir = null;
  private Console console;
  boolean localFile = false;

  List<EngineOutput> outputs = new LinkedList<EngineOutput>();

  boolean consoleState = false;
  boolean started = false;

  public Handler handler;

  /*
    public Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
     // get the bundle and extract data by key
     Bundle b = msg.getData();

     Intent intent = new Intent();
     ClassLoader clz = JConsoleApp.class.getClassLoader();
     try {
     intent.setClass(activity.getApplicationContext(), clz.loadClass(b.getString("class")));
     intent.putExtra("locale", b.getString("locale"));
     intent.putExtra("jargx", b.getString("jargx"));
     intent.putExtra("jargy", b.getString("jargy"));
     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
     activity.startActivity(intent);
     } catch (ClassNotFoundException e) {
  	   Log.e(LogTag, "failed to launch class " + b.getString("class"),e);
     }
    }
   };
  */
  @Override
  public void onCreate()
  {

    super.onCreate();
    JConsoleApp.theApp = (JConsoleApp) this;
    JConsoleApp.theWd = new Wd();
  }

  public void setup(JActivity activity, Console console)
  {
    this.console = console;
    final Activity myact = this.activity = activity;
    final Context mycontext = myact.getApplicationContext();
    handler = new Handler() {
      @Override
      public void handleMessage(Message msg) {
        // get the bundle and extract data by key
        Bundle b = msg.getData();

        Intent intent = new Intent();
        ClassLoader clz = JConsoleApp.class.getClassLoader();
        try {
          intent.setClass(mycontext, clz.loadClass(b.getString("class")));
          intent.putExtra("locale", b.getString("locale"));
          intent.putExtra("jargx", b.getString("jargx"));
          intent.putExtra("jargy", b.getString("jargy"));
          myact.startActivity(intent);
        } catch (ClassNotFoundException e) {
          Log.e(LogTag, "failed to launch class " + b.getString("class"),e);
        }
      }
    };
    this.console.setApplication(this);
    flushOutputs();
    if (!started) {
      StringBuilder sb = new StringBuilder();
      started = true;

      console.setEnabled(false);
      root = getDir("jandroid", Context.MODE_WORLD_READABLE
                    | Context.MODE_WORLD_WRITEABLE);

      File binDir = new File(root, "bin");
      binDir.mkdir();
      File sysTmpDir = new File(root, "tmp");
      sysTmpDir.mkdir();
      jInterface = new AndroidJInterface(this);
      jInterface.setEnvNative("TMPDIR", sysTmpDir.getAbsolutePath());
      String home;
      String state = Environment.getExternalStorageState();
      if(Environment.MEDIA_MOUNTED.equals(state)) {
        jInterface.setEnvNative("HOME", SDCARD);
        home = SDCARD;
        userDir = new File(SDCARD, "j804-user");
        installRoot = getExternalFilesDir(null);
        installRoot.mkdirs();
        currentExternDir = userDir;
        currentLocalDir = root;
      } else {
        jInterface.setEnvNative("HOME", root.getAbsolutePath());
        home = root.getAbsolutePath();
        userDir = new File(root, "j804-user");
        installRoot = root;
        currentLocalDir = userDir;
      }
      tmpDir = new File(userDir, "temp");
      currentExternDir = userDir;
      userDir.mkdir();
      sb.append("(i.0 0)\"_ [ 1!:44 ::0: '").append(home).append("'");
      jInterface.callJ(new String[] {sb.toString()}); // need this side effect to initialize J
      jInterface.start();
      installSystemFiles(activity, console, installRoot, false);
      sb.setLength(0);
    }
  }

  public void setWindow(Context context, String label)
  {
    Log.d(LogTag,"set Window: " + label);
    Intent intent = intentMap.get(label);
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    context.startActivity(intent);
  }

  protected void addIntent(String path, Intent intent)
  {
    if (!JActivity.JANDROID.equals(path)) {
      path = new File(path).getName();
    } else {

      intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

    }

//		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS|Intent.FLAG_DEBUG_LOG_RESOLUTION);

    intentMap.put(path, intent);
  }

  protected void removeFile(String path)
  {
    intentMap.remove(path);
  }

  public boolean isLocalFile()
  {
    return localFile;
  }

  public void setLocalFile(boolean localFile)
  {
    this.localFile = localFile;
  }

  protected void flushOutputs()
  {
    for (EngineOutput eo : outputs) {
      console.consoleOutput(eo.type, eo.content);
    }
    outputs.clear();
  }

  public synchronized void consoleOutput(int type, String content)
  {
    consoleOutput(new EngineOutput(type, content));
  }

  public synchronized void consoleOutput(EngineOutput output)
  {
    if (!consoleState) {
      outputs.add(output);
    } else {
      flushOutputs();
      console.consoleOutput(output.type, output.content);
    }
  }

  public void setWorldReadable(Runtime runtime, File f, boolean recurse)
  {
    if (recurse && f.isDirectory()) {
      File[] ff = f.listFiles();
      for (File fi : ff) {
        setWorldReadable(runtime, fi, recurse);
      }
    }

    try {
      runtime.exec("/system/bin/chmod 0777 " + f.getAbsolutePath());
    } catch (IOException e) {
      Log.e(LogTag,
            "ioexception changing permissions on "
            + f.getAbsolutePath(), e);
    }
  }

  public void setWorldReadable(final File f, final boolean recurse)
  {
    final Runtime runtime = Runtime.getRuntime();
    AsyncTask<String, String, String> task = new AsyncTask<String, String, String>() {
      @Override
      protected void onPreExecute() {
        Toast.makeText(activity,
                       "resetting file permission to world-readable",
                       Toast.LENGTH_LONG).show();
      }

      @Override
      protected String doInBackground(String... params) {
        setWorldReadable(runtime, f, recurse);

        return null;
      }

      @Override
      protected void onPostExecute(String s) {
        Toast.makeText(activity, "file permissions reset",
                       Toast.LENGTH_LONG).show();
      }

    };
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
      task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }  else {
      task.execute();
    }
  }

  public void stop()
  {
    jInterface.stop();
  }

  public void quit()
  {
//		for(Intent intent: this.intentMap.values()) {
//		this.startActivity(intent);
//		}
    console.quit();
  }

  public void setEnableConsole(boolean b)
  {
    console.setEnabled(b);
  }

  public void setActivity(AbstractActivity a)
  {
    this.activity = a;
  }

  public int launchActivity(String action, String data, String type,int flags)
  {
    int result = 0;
    Intent intent = new Intent(action);
    if(data!=null && data.length() >0) {
      if(type!=null&&type.length()>0) {
        intent.setDataAndType(Uri.parse(data), type);
      } else {
        intent.setData(Uri.parse(data));
      }
    } else if(type!=null&&type.length()>0) {
      intent.setType(type);
    }
    if(flags!=0) {
      intent.setFlags(flags);
    }
    try {
      activity.startActivity(intent);
    } catch (ActivityNotFoundException e) {
      Log.e(LogTag, "activity not found for action=" + action + ", data="
            + data + ", type=" + type);
      result = -1;
    }
    return result;
  }

  public void setConsoleState(boolean n)
  {
    consoleState = n;
  }

  protected String[] getHistoryAsArray()
  {
    return history.toArray(new String[history.size()]);
  }

  protected String[] getWindowsAsArray()
  {
    Set<String> k = intentMap.keySet();
    return k.toArray(new String[k.size()]);
  }

  public void callWithHistory(String line)
  {
    addHistory(line);
    //jInterface.callSuperJ(new String[] {line});
    callJ(new String[] {line});
  }

  public void addHistory(String line)
  {
    if (line != null && line.trim().length() > 0) {
      line = line.trim();
      if (history.size() >= 100) {
        history.remove(history.size() - 1);
      }
      if (history.contains(line)) {
        history.remove(line);
      }
      history.add(0, line);
    }
  }

  public void reset()
  {
    Toast.makeText(this, "resetting console", Toast.LENGTH_SHORT).show();
    getjInterface().reset();
    console.replaceText("");
    bootStrapSession(null, "''");
    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
    imm.restartInput(console);
  }

  public void newFile(Context context)
  {
    int i = 1;
    File newf = new File(tmpDir, Integer.toString(i) + ".ijs");
    while (hasEditor(newf.getName()) || newf.exists()) {
      newf = new File(tmpDir, Integer.toString(++i) + ".ijs");
    }
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), EditActivity.class);
    intent.setData(Uri.fromFile(newf));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
    intentMap.put(newf.getName(), intent);
    context.startActivity(intent);
  }

  private void _saveas(Activity _activity, final FileEdit fe, final File f)
  throws IOException
  {
    fe.setTextChanged(true);
    fe.save(f);
    _activity.setTitle(fe.createTitle());
  }

  public void saveAs(final FileEdit fe, final File f) throws IOException
  {

    if (f.exists()) {
      AlertDialog.Builder builder = new AlertDialog.Builder(activity);
      builder.setMessage("Do you want to overwrite " + f.getName() + "?");
      builder.setPositiveButton("OK",
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          try {
            _saveas(activity, fe, f);
          } catch (IOException e) {
            Toast.makeText(
              activity,
              "there was an error overwriting file "
              + f.getName() + ": "
              + e.getLocalizedMessage(),
              Toast.LENGTH_LONG).show();
            Log.e(JConsoleApp.LogTag,
                  "there was an error overwriting file "
                  + f.getName(), e);
          }
        }
      });
      builder.setNegativeButton("Cancel",
      new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          dialog.cancel();
        }
      });
    } else {
      _saveas(activity, fe, f);
    }
  }

  protected void promptSaveWithAction(final FileEdit fe, final File file,
                                      final ResponseAction action) throws IOException
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setMessage("Save " + fe.getName() + "?")
    .setPositiveButton("Yes",
    new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        try {
          fe.save(file);
          if (action != null)
            action.action(true);
          dialog.dismiss();
        } catch (IOException e) {
          Log.e(JConsoleApp.LogTag,
                "error saving file", e);
        }
      }
    })
    .setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        if (action != null)
          action.action(false);
        dialog.cancel();
      }
    });
    AlertDialog dd = builder.create();
    dd.show();
  }

  public boolean hasEditor(String name)
  {
    return intentMap.keySet().contains(name);
  }

  public void callJ(String... sentences)
  {
    callJ(sentences, true);
  }

  public void callJ(String[] sentences, boolean async)
  {
    if (async && jInterface.asyncj) {
      jInterface.callJ(sentences);
    } else {
      jInterface.callSuperJ(sentences);
    }
  }

  protected void bootstrap()
  {
    Toast.makeText(activity, "booting session", Toast.LENGTH_SHORT).show();
    bootStrapSession(activity, "''");
  }

  protected void bootStrapSession(Activity apctivity, String... args)
  {
    String argv = "''";
    if (args.length > 0) {
      argv = args[0];
    }
    StringBuilder sb = new StringBuilder();
    sb.append("(3 : '0!:0 y')<INSTALLROOT,'/bin/profile.ijs' [ ARGV_z_=: ")
    .append(argv).append(" [ BINPATH_z_ =: '").append(root.getAbsolutePath())
    .append("/bin").append("'")
    .append(" [ INSTALLROOT_z_=:'").append(installRoot.getAbsolutePath()).append("'")
    .append(" [ AndroidPackage_z_=:'").append(activity.getApplicationContext().getPackageName()).append("'")
    .append(" [ IFJCA_z_=: 1")
    .append(" [ UNAME_z_=: 'Android'");
    Log.d(JConsoleApp.LogTag, "initialize engine: " + sb.toString());

    if (args.length > 1) {
      String[] ss = new String[args.length];
      ss[0] = sb.toString();
      // Log.d(JConsoleApp.LogTag,ss[0]);
      for (int i = 1; i < args.length; ++i) {
        ss[i] = args[i];
        Log.d(JConsoleApp.LogTag, ss[i]);
      }
      callJ(ss);
    } else {
      // Log.d(JConsoleApp.LogTag,sb.toString());
      callJ(new String[] { sb.toString() });
    }
    console.setEnabled(true);
  }

  protected void installSystemFiles(JActivity activity, Console console,
                                    File base, boolean force)
  {
    if (force || !checkInstall(base)) {
      InstallationTask task = new InstallationTask(activity, console);
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, base);
      }  else {
        task.execute(base);
      }
    } else {
      Log.d(JConsoleApp.LogTag, "bootstraping session");
      bootstrap();
    }
  }

  protected boolean checkInstall(File base)
  {
    try {
      File f = new File(base,"android-version.txt");
      InputStream in = new FileInputStream(f);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line = reader.readLine();
      reader.close();
      String version = getResources().getString(R.string.version);
      if(line!=null && line.equals(version)) {
        return true;
      }
    } catch(IOException e) {
      // so what? just install
    }
    return false;
  }

  protected void showToast(String message)
  {
    showToast(message, false);
  }

  protected void showToast(String message, boolean islong)
  {
    Toast toast = Toast.makeText(this, message, islong ? Toast.LENGTH_LONG
                                 : Toast.LENGTH_SHORT);
    toast.show();
  }

  interface ResponseAction
  {
    public void action(boolean state);
  }

  class InstallationTask extends AsyncTask<File, String, String>
  {

    Activity activity;
    Console console;

    InstallationTask(Activity activity, Console console)
    {
      this.activity = activity;
      this.console = console;
    }

    @Override
    public void onPreExecute()
    {
      console.setEnabled(false);
    }

    @Override
    public void onPostExecute(String s)
    {
      bootstrap();
      // console.setEnabled(true);
    }

    @Override
    protected void onProgressUpdate(String... i)
    {
      String limit = i[0];
      showToast(limit);
    }

    @Override
    protected String doInBackground(File... params)
    {
      try {
//        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//          File lo = new File(params[0],"j804-user");
//          if(lo.exists()) {
//            File ex = new File(Environment.getExternalStorageDirectory().getPath());
//            if(ex.canWrite()) {
//              String cmd = "i.0 0)\"_ [ 2!:0 'cp -r " + lo.getAbsolutePath()
//                           + " " + SDCARD + "'";
//              publishProgress("migrating user files to sdcard");
//              //jInterface.callSuperJ(new String[] {cmd});
//              jInterface.callJ(new String[] {cmd});
//            }
//          }
//        }
        doInstall(params[0]);
      } catch (IOException e) {
        publishProgress("error during installation: "
                        + e.getLocalizedMessage());
      }
      return "OK";
    }

    protected boolean doInstall(File base) throws IOException
    {
      publishProgress("performing first-boot installation");
      publishProgress("installing system files");
      installDirectory(base, "system");
      installDirectory(base, "bin");

      publishProgress("installing help files");
      installDirectory(base, "docs");

      publishProgress("installing addons");
      installDirectory(base, "addons");

      installFile(base, "android-version.txt");
      publishProgress("installation complete");

      return true;
    }

    protected boolean _installFile(File base, String path)
    throws IOException
    {
      byte buff[] = new byte[8092];
      InputStream in = getAssets().open(path);
      File f = new File(base, path);
      if (f.exists()) f.delete();
      OutputStream out = new FileOutputStream(new File(base, path));
      int n;
      while ((n = in.read(buff)) != -1) {
        out.write(buff, 0, n);
      }
      out.close();
      in.close();
      return true;
    }

    protected boolean installFile(File base, String path)
    {
      try {
        return _installFile(base, path);
      } catch (Exception e) {
        Log.e(JConsoleApp.LogTag, "failed to install " + path);
      }
      return false;
    }

    protected File createDirectory(File base, String d)
    {
      File f = new File(base, d);
      f.mkdirs();
      return f;
    }

    protected boolean installDirectory(File base, String directory)
    throws IOException
    {
      AssetManager am = getAssets();
      boolean res = true;
      String[] tests = am.list(directory);
      createDirectory(base, directory);
      for (String t : tests) {
        try {
          res &= _installFile(base, directory + "/" + t);
        } catch (FileNotFoundException e) {
          Log.i(JConsoleApp.LogTag, "recursing to " + directory + "/"
                + t);
          installDirectory(base, directory + "/" + t);
        }
      }
      return res;
    }
  }

  public AndroidJInterface getjInterface()
  {
    return jInterface;
  }

  public java.util.List<String> getHistory()
  {
    return history;
  }

  public void setHistory(java.util.List<String> history)
  {
    this.history = history;
  }

  public File getRoot()
  {
    return root;
  }

  public void setRoot(File root)
  {
    this.root = root;
  }

  public File getCurrentLocalDir()
  {
    return currentLocalDir;
  }

  public void setCurrentLocalDir(File currentDir)
  {
    currentLocalDir = currentDir;
  }

  public File getCurrentExternDir()
  {
    return currentExternDir;
  }

  public void setCurrentExternDir(File currentDir)
  {
    currentExternDir = currentDir;
  }

  public void setCurrentDirectory(File dir)
  {
    if (localFile)
      setCurrentLocalDir(dir);
    else
      setCurrentExternDir(dir);
  }

  public File getCurrentDirectory()
  {
    File f = null;
    if (localFile) {
      f = getCurrentLocalDir();
    } else {
      f = getCurrentExternDir();
    }
    return f;
  }

  public int gl2(int[] buf, Object[] r)
  {
    return theWd.gl2(buf, r);
  }

  public int wd(String s, Object[] r)
  {
    return theWd.wd(s, r);
//    Log.d(LogTag,"wd: " + s);
//     Cmd cmd=new Cmd();
//     cmd.init(s);
//     while (cmd.more()) {
//     String c1=cmd.getid();
//     String c2=cmd.getparms();
//     Log.d(LogTag,"wd id: " + c1);
//     Log.d(LogTag,"wd parms: " + c2);
//     String[] list = Cmd.qsplit(c2,false);
//     for(String t : list) {
//       Log.d(LogTag,"wd split: " + t);
//    }
//   }
//   return 0;
  }

}
