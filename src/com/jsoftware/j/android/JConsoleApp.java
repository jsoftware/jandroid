package com.jsoftware.j.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jsoftware.j.JInterface;
import com.jsoftware.jn.base.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.os.Message;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.jsoftware.j.JInterface;
import com.jsoftware.j.android.JActivity;
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
  public String mVersionName;
  public int mVersionCode;
  public boolean IF64=false;
  public boolean asyncj;          // run libj.so in its own thread
  public boolean asyncj0;         // shared preferences
  public int stacksize;
  protected File cfgDir;
  public String configpath = "";
  String home="";

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
  public final String jversion = "901";
  boolean localFile = false;

  List<EngineOutput> outputs = new LinkedList<EngineOutput>();
  public String smfont = "";
  public boolean started = false;

  boolean consoleState = false;

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
    loadPref();
    savePref();          // save defaults
    asyncj = asyncj0;
    JConsoleApp.theApp = (JConsoleApp) this;
    JConsoleApp.theWd = new Wd();
    try {
      PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
      mVersionName = pInfo.versionName;
      mVersionCode = pInfo.versionCode;
    } catch (NameNotFoundException e) {
      Log.d(LogTag, "Error: NameNotFoundException ");
      mVersionName = "";
      mVersionCode = -1;
    }
    if (Build.VERSION.SDK_INT < 21) {
      IF64 = false;  // always false
    } else if (Build.VERSION.SDK_INT < 23) {
      IF64 = Build.SUPPORTED_ABIS[0].contains("64");
    } else {
      IF64 = android.os.Process.is64Bit();
    }
//    jversion = getResources().getString(R.string.jversion);
  }

  public void setup(JActivity jActivity, Console console)
  {
    this.console = console;
    final Activity myact = activity = jActivity;
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
    console.setApplication(this);
    flushOutputs();
    if (!started) {
      started = true;

      console.setEnabled(false);
      if (false && Build.VERSION.SDK_INT < 28 && Build.VERSION.SDK_INT >= 23) {
        boolean bl = ContextCompat.checkSelfPermission((Context)jActivity, "android.permission.WRITE_EXTERNAL_STORAGE") == 0;
        if (!bl) {
          ActivityCompat.requestPermissions(jActivity, new String[] {"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        }
      }
      root = getDir("jandroid", Context.MODE_WORLD_READABLE
                    | Context.MODE_WORLD_WRITEABLE);

      File binDir = new File(root, "bin");
      binDir.mkdir();
      Runtime runtime = Runtime.getRuntime();
      setWorldReadable(runtime, binDir, true);
      File sysTmpDir = new File(root, "tmp");
      sysTmpDir.mkdir();
      setWorldReadable(runtime, sysTmpDir, true);
      jInterface = new AndroidJInterface(this);
      jInterface.start();
      jInterface.JSetEnv("TMPDIR", sysTmpDir.getAbsolutePath());
      String state = Environment.getExternalStorageState();
      if (false && Build.VERSION.SDK_INT < 28 && Environment.MEDIA_MOUNTED.equals(state)) {
        jInterface.JSetEnv("HOME", SDCARD);
        home = SDCARD;
        userDir = new File(SDCARD, "j"+jversion+"-user");
        installRoot = getExternalFilesDir(null);
        installRoot.mkdirs();
        currentExternDir = userDir;
        currentLocalDir = root;
      } else if (Environment.MEDIA_MOUNTED.equals(state)) {
        installRoot = getExternalFilesDir(null);
        installRoot.mkdirs();
        jInterface.JSetEnv("HOME", installRoot.getAbsolutePath());
        home = installRoot.getAbsolutePath();
        userDir = new File(installRoot, "j"+jversion+"-user");
        currentExternDir = userDir;
        currentLocalDir = root;
      } else {
        jInterface.JSetEnv("HOME", root.getAbsolutePath());
        home = root.getAbsolutePath();
        userDir = new File(root, "j"+jversion+"-user");
        installRoot = root;
        currentLocalDir = userDir;
      }
      currentExternDir = userDir;
      userDir.mkdirs();
      tmpDir = new File(userDir, "temp");
      cfgDir = new File(userDir, "config");
      tmpDir.mkdirs();
      cfgDir.mkdirs();
      installSystemFiles(jActivity, console, installRoot, false);
      if (!asyncj) {
        String string2 = jInterface.dors("jpath ::(''\"_) '~config'");
        configpath = string2.isEmpty() ? cfgDir.getAbsolutePath() : string2;
      }
      console.setFont();
    }
  }

  public void setWindow(Context context, String label)
  {
    Log.d(LogTag,"set Window: " + label);
    Intent intent = intentMap.get(label);
    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
    context.startActivity(intent);
  }

  public void addIntent(String path, Intent intent)
  {
    if (!JActivity.JANDROID.equals(path)) {
//      path = new File(path).getName();
    } else {

      intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

    }

//		intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY|Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS|Intent.FLAG_DEBUG_LOG_RESOLUTION);

    intentMap.put(path, intent);
  }

  public void removeFile(String path)
  {
    Log.d("jandroid", "removeFile "+path);
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
    activity = a;
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
    String[] ss = k.toArray(new String[k.size()]);
    Arrays.sort(ss);
    return ss;
  }

  public void callWithHistory(String line)
  {
    addHistory(line);
    callJ(line, true);
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
    File newf = newFileName();
    Intent intent = new Intent();
    intent.setClass(getApplicationContext(), EditActivity.class);
    intent.setData(Uri.fromFile(newf));
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
    intentMap.put(newf.getAbsolutePath(), intent);
    context.startActivity(intent);
  }

  public File newFileName()
  {
    int n = 1;
    File file = new File(tmpDir, Integer.toString((int)n) + ".ijs");
    while (hasEditor(file.getName()) || file.exists()) {
      File file2 = tmpDir;
      StringBuilder stringBuilder = new StringBuilder();
      file = new File(file2, stringBuilder.append(Integer.toString((int)(++n))).append(".ijs").toString());
    }
    return file;
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
      builder.setPositiveButton(android.R.string.yes,
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
      builder.setNegativeButton(android.R.string.no,
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
    .setPositiveButton(android.R.string.yes,
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

  public void callJ(String sentence, boolean addprompt)
  {
    callJ(sentence, addprompt, asyncj);
  }

  public void callJ(String sentence, boolean addprompt, boolean async)
  {
    if (async) {
      jInterface.callJ(sentence, addprompt);
    } else {
      jInterface.callSuperJ(sentence, addprompt);
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
    .append(" [ APILEVEL_ja_=:").append(""+Build.VERSION.SDK_INT)
    .append(" [ OSRELEASE_ja_=:'").append(Build.VERSION.RELEASE).append("'")
    .append(" [ LIBFILE_z_=: '").append(JConsoleApp.theApp.getApplicationInfo().nativeLibraryDir).append("/libj.so'")
    .append(" [ IFJA_z_=: 1")
    .append(" [ UNAME_z_=: 'Android'")
    .append(" [ 1!:44 ::0: '").append(home).append("'");
    Log.d(JConsoleApp.LogTag, "initialize engine");

    if (args.length > 1) {
      String[] ss = new String[args.length];
      ss[0] = sb.toString();
      // Log.d(JConsoleApp.LogTag,ss[0]);
      for (int i = 1; i < args.length; ++i) {
        ss[i] = args[i];
        Log.d(JConsoleApp.LogTag, ss[i]);
      }
      for (String s: ss)
        callJ(s, false);
    } else {
      // Log.d(JConsoleApp.LogTag,sb.toString());
      callJ(sb.toString(), false);
    }
    callJ("ExecIfExist_z_=: 3 : 'if. 3=(4!:0 ::_2:)<y do. y~$0 else. y end.'", false);
//    if (!asyncj) console.prompt();
    console.prompt();
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
    int ver = -1;
    try {
      File f = new File(base,"assets_version.txt");
      InputStream in = new FileInputStream(f);
      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
      String line = reader.readLine();
      reader.close();
      try {
        ver=Integer.valueOf(line);
      } catch (NumberFormatException e) {
        ver=mVersionCode-1;
      }
      if(ver==mVersionCode) {
        return true;
      }
    } catch(IOException e) {
      // so what? just install
    }
    try {
      File f = new File(base,"assets_version.txt");
      OutputStream out = new FileOutputStream(f);
      BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
      writer.write(Integer.toString(mVersionCode));
      writer.close();
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
//          File lo = new File(params[0],"j901-user");
//          if(lo.exists()) {
//            File ex = new File(Environment.getExternalStorageDirectory().getPath());
//            if(ex.canWrite()) {
//              String cmd = "i.0 0)\"_ [ 2!:0 'cp -r " + lo.getAbsolutePath()
//                           + " " + SDCARD + "'";
//              publishProgress("migrating user files to sdcard");
//              jInterface.callJ(cmd, false);
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
      try {
        copyFiles(new File(base, "system/config"), JConsoleApp.this.cfgDir);
      } catch (Exception exception) {
        Log.e("jandroid", "failed to copy config");
      }

//       publishProgress("installing help files");
//       installDirectory(base, "docs");

      publishProgress("installing addons");
      installDirectory(base, "addons");

      File root = getDir("jandroid", Context.MODE_WORLD_READABLE
                         | Context.MODE_WORLD_WRITEABLE);
      installDirectory(root , "libexec");
      final Runtime runtime = Runtime.getRuntime();
      setWorldReadable(runtime, new File(root, "libexec"), true);

      installFile(base, "assets_version.txt");
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

    protected void copyFiles(File file, File file2) throws IOException
    {
      if (file.isDirectory()) {
        if (!file2.exists()) {
          file2.mkdir();
        }
        for (File file3 : file.listFiles()) {
          int n;
          File file4 = new File((Object)file2 + "/" + file3.getName());
          if (!file4.isFile() && file4.exists()) continue;
          FileInputStream fileInputStream = new FileInputStream(file3);
          FileOutputStream fileOutputStream = new FileOutputStream((Object)file2 + "/" + file3.getName());
          byte[] arrby = new byte[1024];
          while ((n = fileInputStream.read(arrby)) > 0) {
            fileOutputStream.write(arrby, 0, n);
          }
          fileInputStream.close();
          fileOutputStream.close();
        }
      }
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

  public void loadPref()
  {
    SharedPreferences sp = getSharedPreferences("jpreferences", Context.MODE_PRIVATE);
    asyncj0 = sp.getBoolean("asyncj", true);         // default value
    stacksize = sp.getInt("stacksize", 5000000);
    smfont = sp.getString("smfont", "");
    configpath = sp.getString("configpath", "");
  }

  public void savePref()
  {
    SharedPreferences.Editor editor = getSharedPreferences("jpreferences", Context.MODE_PRIVATE).edit();
    editor.putBoolean("asyncj", asyncj0);
    editor.putInt("stacksize", stacksize);
    editor.putString("smfont", smfont);
    editor.putString("configpath", configpath);
    editor.commit();
  }

  public byte[] getpref(String p,String v)
  {
    String r="";
    if ((!v.isEmpty()) && !p.equals("extent")) {
      JConsoleApp.theWd.error("extra parameters: " + p + " " + v);
      return new byte[] {};
    }
    if (p.equals("property")) {
      StringBuilder r1a=new StringBuilder();
      r1a.append("asyncj\nconfigpath\nsmfont\nstacksize\n");
      r=r1a.toString();
    } else if (p.equals("asyncj")) {
      r=Util.i2s(asyncj?1:0);
    } else if (p.equals("stacksize")) {
      r=Util.i2s(stacksize);
    } else  if (p.equals("smfont")) {
      r=smfont;
    } else if (p.equals("configpath")) {
      r=configpath;
    } else
      theWd.error("get command not recognized: " + p + " " + v);
    return Util.s2ba(r);
  }

  public void setpref(String p,String v)
  {
    if (p.equals("asyncj")) {
      asyncj0 = !Util.remquotes(v).equals("0");   // effective after restart
      savePref();
    } else if (p.equals("stacksize")) {
      stacksize = Util.c_strtoi(v);
      savePref();
    } else if (p.equals("smfont")) {
      smfont = Util.remquotes(v).trim();
      savePref();
    } else  if (p.equals("configpath")) {
      configpath = Util.remquotes(v).trim();
      savePref();
    } else
      theWd.error("set command not recognized: " + p + " " + v);
  }

}
