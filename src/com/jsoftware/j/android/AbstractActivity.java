package com.jsoftware.j.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.jsoftware.j.JInterface;
import com.jsoftware.jn.base.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.lang.NumberFormatException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractActivity extends AppCompatActivity
{

  FrameLayout contentFrame;
  LinearLayout drawerView;
  DrawerLayout leftDrawer;
  private ActionBarDrawerToggle mDrawerToggle;

  public static final String EMPTY = " -- empty -- ";
  public JConsoleApp theApp=null;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    theApp = (JConsoleApp) this.getApplication();
    if (getClass().getSimpleName().equals("JActivity")) theApp.setActivity(this);
    addIntent();
    ActionBar actionbar = getSupportActionBar();
    if (actionbar != null) {
      Log.d(JConsoleApp.LogTag, "Setting up action bar home button.");
      actionbar.setDisplayHomeAsUpEnabled(true);
      actionbar.setHomeButtonEnabled(true);
    } else {
      Log.d(JConsoleApp.LogTag, "action bar is null");
    }
    setContentView(shouldEnableDrawer() ? R.layout.activity_base_with_drawer : R.layout.activity_base_without_drawer);

    contentFrame = (FrameLayout) findViewById(R.id.content_frame);
    Log.d(JConsoleApp.LogTag, "drawer enabled: " + shouldEnableDrawer());
    if (shouldEnableDrawer()) {
      drawerView = (LinearLayout) findViewById(R.id.drawer);
      leftDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

      mDrawerToggle = new ActionBarDrawerToggle(
        AbstractActivity.this,
        leftDrawer,
        R.string.app_name,
        R.string.app_name
      ) {

        @Override
        public void onDrawerClosed(View drawerView) {
          super.onDrawerClosed(drawerView);
        }
      };


      leftDrawer.setDrawerListener(mDrawerToggle);

    }
  }

  @Override
  protected void onResume()
  {
    super.onResume();
    if (shouldEnableDrawer()) {
      //Populate the drawer
      final List<SliderMenuItem> sliderMenuItems = new ArrayList<SliderMenuItem>();
      SliderMenuItem item1 = new SliderMenuItem();
      item1.setId(1);
      item1.setTitle(getResources().getString(R.string.app_name));
      item1.setClickHandler(new SliderMenuItem.MenuItemClickHandler() {
        @Override
        public void handleMenuClick() {
          startDrawerActivity(JActivity.class);
        }
      });
      sliderMenuItems.add(item1);

      int i=1;
      for (String s : theApp.getWindowsAsArray()) {
        if (!s.equals(getResources().getString(R.string.app_name))) {
          SliderMenuItem item2 = new SliderMenuItem();
          item2.setId(++i);
          item2.setTitle(s);
          final String ss=s;
          item2.setClickHandler(new SliderMenuItem.MenuItemClickHandler() {
            @Override
            public void handleMenuClick() {
              startDrawerActivity(ss);
            }
          });
          sliderMenuItems.add(item2);
        }
      }

      SliderMenuItem item3 = new SliderMenuItem();
      item3.setId(++i);
      item3.setTitle("Editor");
      item3.setClickHandler(new SliderMenuItem.MenuItemClickHandler() {
        @Override
        public void handleMenuClick() {
          startDrawerActivity(EditActivity.class);
        }
      });
      sliderMenuItems.add(item3);

      drawerView.removeAllViews();
      for (SliderMenuItem item : sliderMenuItems) {
        SliderMenuItemView view = new SliderMenuItemView(this);
        view.setItem(item);
        drawerView.addView(view);
      }
    }
  }

  private void startDrawerActivity(final Class cclass)
  {
    leftDrawer.closeDrawer(GravityCompat.START);
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (cclass.getSimpleName().equals("JActivity")) {
          startActivity(new Intent(AbstractActivity.this, cclass));
        } else {
          Intent intent = new Intent();
          intent.setClass(getApplicationContext(), EditActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
          startActivity(intent);
        }
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }
    }, 200);
  }

  private void startDrawerActivity(final String ss)
  {
    leftDrawer.closeDrawer(GravityCompat.START);
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        theApp.setWindow(AbstractActivity.this,ss);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }
    }, 200);
  }

  @Override
  protected void onPostCreate(@Nullable Bundle savedInstanceState)
  {
    super.onPostCreate(savedInstanceState);
    mDrawerToggle.syncState();
  }

  public abstract boolean shouldEnableDrawer();

  protected abstract void addIntent();

  public ViewGroup getFrame()
  {
    return contentFrame;
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig)
  {
    super.onConfigurationChanged(newConfig);
    mDrawerToggle.onConfigurationChanged(newConfig);
  }

  public void alert(String message)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message);
    builder.setPositiveButton(android.R.string.yes, new AlertDialog.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();
      }
    });
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    boolean result = true;

    if (mDrawerToggle.onOptionsItemSelected(item)) {
      return true;
    }
    int itemId = item.getItemId();
    switch(itemId) {
    case R.id.newf:
      theApp.newFile(this);
      break;
    case R.id.open:
      requestFileOpen();
      break;
    case R.id.window:
      requestWindowSelect();
      break;
    case R.id.pacman:
      theApp.callJ("(i.0 0)\"_ "+getResources().getString(R.string.runpacman), false);
      break;
    case R.id.log:
      showHistoryDialog();
      break;
    case R.id.runl:
      runCurrentLine();
      break;
    case R.id.runf:
      requestFileRun();
      break;
    case R.id.runcb:
      runClipboard();
      break;
    case R.id.vocab:
      showHelp(R.string.help_start);
      break;
    case R.id.learning:
      showHelp(R.string.learning);
      break;
    case R.id.labs:
      theApp.callJ("(i.0 0)\"_ "+getResources().getString(R.string.runlabs), false);
      break;
    case R.id.labsadvance:
      theApp.callJ("(i.0 0)\"_ "+getResources().getString(R.string.runlabsadvance), false);
      break;
    case R.id.demos:
      theApp.callJ("(i.0 0)\"_ "+getResources().getString(R.string.rundemos), false);
      break;
    case R.id.showcase:
      theApp.callJ("(i.0 0)\"_ "+getResources().getString(R.string.runshowcase), false);
      break;
    case R.id.aboutj:
//      showTextFile(R.string.aboutj);
      Toast.makeText(AbstractActivity.this, com.jsoftware.j.android.JConsoleApp.theApp.jInterface.dors("JVERSION"), Toast.LENGTH_LONG).show();
      break;
    case R.id.upgrade:
      updateJ();
      break;
    case R.id.checknewver:
      new GetHttpTask().execute("http://www.jsoftware.com/download/j" + this.theApp.jversion + "/install/AndroidManifest.xml");
      break;
    default:
      result = false;
    }
    return result;
  }

  protected abstract FileEdit getEditor();

  protected void runCurrentLine()
  {
    final FileEdit editor = getEditor();

    int n = editor.getSelectionStart();
    if (n > 0)
      --n;
    String line = editor.getLineForPosition(n);
    theApp.consoleOutput(JInterface.MTYOFM, line + "\n");
    theApp.callJ(line, true);
  }

  protected void runClipboard()
  {
    String m=Util.clipread(this.getApplicationContext());
    if (0!=m.length()) {
      String m1=m.replace("\u00a0"," ");
// strip last LF
      if((m1.length()-1) == m1.lastIndexOf("\n")) m1 = m1.substring(0,m1.length()-1);
      if(m1.contains("\n")) {
// run as script if multiple lines
        theApp.consoleOutput(JInterface.MTYOFM, "\n");
        theApp.callJ("(i.0 0)\"_ (0!:100)'"+ m1.replace("'","''")+"'", true);
      } else {
// run as sentence if only 1 line
        theApp.consoleOutput(JInterface.MTYOFM, m1 + "\n");
        theApp.callJ(m1, true);
      }
    }
  }

  protected void updateJ()
  {
    theApp.consoleOutput(JInterface.MTYOFM, "Updating J...\n");
    theApp.callJ("(i.0 0)\"_ 'upgrade'jpkg'all' [ 'upgrade'jpkg'all' [ require 'pacman'", false);
  }

  public void requestWindowSelect()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    ArrayAdapter<String> add = new ArrayAdapter<String>(this,
        R.layout.list_item, theApp.getWindowsAsArray());
    final ListView lv = new ListView(this);
    lv.setAdapter(add);
    builder.setView(lv);
    final AlertDialog ad = builder.create();

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      public void onItemClick(AdapterView<?> arg0, View vv, int index,
                              long id) {
        TextView ttv = ((TextView) vv);
        String ss = new StringBuilder(ttv.getText()).toString();

        theApp.setWindow(AbstractActivity.this,ss);
        ad.dismiss();
      }
    });

    ad.show();

  }

  protected void showHistoryDialog()
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    String [] his = theApp.getHistoryAsArray();
    AlertDialog myadd;
    if(his.length > 0) {
      ArrayAdapter<String> add = new ArrayAdapter<String>(this, R.layout.list_item, his);
      final ListView lv = new ListView(this);

      lv.setAdapter(add);
      builder.setView(lv);

      final FileEdit editor = getEditor();

      final AlertDialog ad = myadd = builder.create();
      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> v, View vv, int index,
                                long id) {
          TextView tv = (TextView) vv;
          String text = new StringBuilder(tv.getText()).toString();
          if(editor instanceof Console) {
            theApp.consoleOutput(JInterface.MTYOFM, text);
          } else {
            Editable ed = editor.getEditableText();
            int n = editor.getSelectionStart();
            ed.insert(n, text);
            editor.setSelection(n+text.length());
          }
          ad.dismiss();
        }
      });
    } else {
      builder.setView(emptyView());
      myadd = builder.create();
    }
    myadd.show();
  }

  protected View emptyView()
  {
    TextView view = new TextView(this);
    view.setPadding(4, 4, 4, 4);
    view.setBackgroundColor(0xff000000);
    view.setTextColor(0xffffffff);
    view.setText(EMPTY);
    return view;
  }


  protected String[] filterFilelist(File dir, List<String> files, int nfiles)
  {
    List<String> res = files;
    if(nfiles == 0) {
      files.add(EMPTY);
    }
    res = new ArrayList<String>(files.size());
    for (String s : files) {
      File f = new File(dir, s);
      if (s.startsWith("..") || f.isDirectory()) {
        res.add(s + "/");
      } else {
        res.add(s);
      }
    }
    String[] r = res.toArray(new String[res.size()]);
    if(nfiles > 0) Arrays.sort(r, new FileComparator(dir));
    return r;
  }

  protected AlertDialog fileListDialog(File dir,ListView lv,boolean dirsOnly)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    try {
      if(dir.exists()) {
        ArrayAdapter<String> add = createDirAdapter(dir,dirsOnly);
        lv.setAdapter(add);
        String title =  filePath(dir.getCanonicalPath());
        if(title == null || title.length() == 0 || title.equals("/mnt/sdcard")
           || title.equals(Environment.getExternalStorageDirectory().getPath())) {
          title = "/";
        }
        builder.setTitle(title);
        builder.setView(lv);
      } else {
        theApp.setLocalFile(false);
        theApp.setCurrentDirectory(theApp.userDir);
        builder.setMessage(dir.getAbsolutePath() + " does not exist.");
      }
      return builder.create();
    } catch(IOException e) {
      Log.e(JConsoleApp.LogTag,"error creating file list",e);
    }
    return null;
  }

  public void requestFileOpen()
  {

    File f = theApp.getCurrentDirectory();
    FileAction fs = new OpenEditorAction();
    requestFileSelect(f,fs);
  }




  public void requestFileRun()
  {

    File f = theApp.getCurrentDirectory();

    FileAction fs = new RunFileAction();
    requestFileSelect(f,fs);
  }

  interface  FileAction
  {
    public void useFile(File f);
  }

  class OpenEditorAction implements FileAction
  {
    public void useFile(File f)
    {
      Log.d(JConsoleApp.LogTag,"OpenEditorAction.useFile()");
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), EditActivity.class);
      intent.setData(Uri.parse("file://" + f.getAbsolutePath()));
      intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

//			intent.putExtra("file", f.getAbsolutePath());
      theApp.setCurrentDirectory(f.getParentFile());


      startActivity(intent);
    }
  }

  protected void runFile(File f)
  {
    StringBuilder sb = new StringBuilder("load '");
    sb.append(f.getAbsolutePath()).append("'");
    theApp.consoleOutput(JInterface.MTYOFM, sb.toString() + "\n");
    theApp.callJ(sb.toString(), false);
  }

  class RunFileAction implements FileAction
  {
    public void useFile(File f)
    {
      runFile(f);
    }

  }

  class SaveAsAction implements FileAction
  {

    EditText et;
    public SaveAsAction(EditText et)
    {
      this.et = et;
    }

    public void useFile(File f)
    {
      et.setText(f.getName());
    }

  }

  public void requestFileSaveAs(File ff)
  {
    final EditText et = new EditText(AbstractActivity.this);
    et.setLayoutParams(new ViewGroup.LayoutParams(
                         ViewGroup.LayoutParams.MATCH_PARENT,
                         ViewGroup.LayoutParams.WRAP_CONTENT));
    et.setSingleLine();
    et.setText(ff.getName());
    et.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

    FileAction fs = new SaveAsAction(et);
    File dir = ff.getParentFile();
    theApp.setCurrentDirectory(dir);
    requestFileSelect(dir,fs,et);
  }

  public void requestFileSelect(File dir, final FileAction ffa)
  {
    requestFileSelect(dir, ffa, null);
  }


  @Override
  public boolean onSearchRequested()
  {
    showHistoryDialog();
    return true;
  }
  public void showHelp(int resId)
  {
    File file = new File(JConsoleApp.theApp.jInterface.dors("jpath'~addons/docs/help/"+getResources().getString(resId)+"'"));
    Intent myIntent = new Intent(Intent.ACTION_VIEW);
    myIntent.addCategory(Intent.CATEGORY_BROWSABLE);
    if(file.exists())
      myIntent.setDataAndType(Uri.fromFile(file), "text/html");
    else
      myIntent.setData(Uri.parse("http://www.jsoftware.com/help/"+getResources().getString(resId)));
    startActivity(myIntent);
  }
  public void showTextFile(int resId)
  {
    Intent intent = new Intent();
    intent.setClass(this.getApplicationContext(), HelpActivity.class);

    intent.putExtra("file", getResources().getString(resId));
    intent.putExtra("base", theApp.getRoot().getAbsolutePath());
    this.startActivity(intent);

  }



  protected FileEdit openFileEditor(File f)
  {
    FileEdit ef = new FileEdit(this);
    try {
      ef.setName(f.getName());
      ef.open(f);
    } catch(IOException e) {
      Log.e(JConsoleApp.LogTag,"error opening file",e);
    }
    return ef;
  }


  protected String filePath(String path)
  {
    String rp = theApp.getRoot().getAbsolutePath();
    if (path.startsWith(rp)) {
      path = path.substring(rp.length());
    }
    return path;
  }

  public String buildTitle(File f)  throws IOException
  {
    String title =  filePath(f.getCanonicalPath());
    if(title == null || title.length() == 0 || title.equals(Environment.getExternalStorageDirectory().getPath())) {
      title = "/";
    }
    return (theApp.isLocalFile() ? "system: " : "user: ") + title;
  }

//	public abstract String buildTitle(File f)  throws IOException;

  public void requestFileSelect(final File dir, final FileAction ffa,
                                final TextView textView)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    final LinearLayout ll = new LinearLayout(AbstractActivity.this);
    ll.setLayoutParams(new ViewGroup.LayoutParams(
                         ViewGroup.LayoutParams.MATCH_PARENT,
                         ViewGroup.LayoutParams.MATCH_PARENT));
    ll.setOrientation(LinearLayout.VERTICAL);

    final ListView lv = new ListView(this);
    lv.setFastScrollEnabled(true);

    ArrayAdapter<String> add = createDirAdapter(dir,false);
    lv.setAdapter(add);

    Button save = null;
    Button cancel = null;
    try {
      ll.addView(lv);

      if(textView != null) {
        LinearLayout hl = new LinearLayout(AbstractActivity.this);
        hl.setLayoutParams(new ViewGroup.LayoutParams(
                             ViewGroup.LayoutParams.MATCH_PARENT,
                             ViewGroup.LayoutParams.MATCH_PARENT));
        hl.setOrientation(LinearLayout.HORIZONTAL);

        save = new Button(this);
        save.setLayoutParams(new LinearLayout.LayoutParams(
                               ViewGroup.LayoutParams.MATCH_PARENT,
                               ViewGroup.LayoutParams.WRAP_CONTENT,1f));
        save.setGravity(Gravity.CENTER);
        save.setText("Save");

        hl.addView(save);

        cancel = new Button(this);
        cancel.setLayoutParams(new LinearLayout.LayoutParams(
                                 ViewGroup.LayoutParams.MATCH_PARENT,
                                 ViewGroup.LayoutParams.WRAP_CONTENT,1f));
        cancel.setText("Cancel");
        cancel.setTextAppearance(this, 12);

        cancel.setGravity(Gravity.CENTER);
        hl.addView(cancel);

        ll.addView(textView);
        ll.addView(hl);
      }
      final Button dev = new Button(this);
      dev.setLayoutParams(new ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
      dev.setText("browse " + (theApp.isLocalFile() ? "sdcard" : "local"));
      ll.addView(dev);

      builder.setTitle(buildTitle(dir));
      builder.setView(ll);

      final AlertDialog ad = builder.create();

      dev.setOnClickListener(new View.OnClickListener() {
        public void onClick(View vv) {
          theApp.setLocalFile(!theApp.isLocalFile());
          File newfile = theApp.getCurrentDirectory();
          ad.dismiss();
          ll.removeView(textView);
          requestFileSelect(newfile, ffa, textView);
        }
      });

      lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> arg0, View vv,
                                int index, long id) {
          TextView ttv = ((TextView) vv);
          File d = theApp.getCurrentDirectory();
          String ss = ttv.getText().toString();
          File newfile = ss.startsWith("..") ? d.getParentFile() : new File(d, ss);
          Log.d(JConsoleApp.LogTag, "file selected: " + newfile.getPath());

          if (newfile.isDirectory()) {
            theApp.setCurrentDirectory(newfile);
            ArrayAdapter<String> add = createDirAdapter(newfile,false);
            lv.setAdapter(add);
            try {
              ad.setTitle(buildTitle(newfile));
            } catch(IOException e) {
              Log.e(JConsoleApp.LogTag,"error building title",e);
            }
            lv.setAdapter(add);
          } else {
            if(EMPTY.equals(ss)) {
              return;
            }
            ffa.useFile(newfile);
            if(textView == null) ad.dismiss();
          }
        }
      });

      if(save != null) save.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          String name = new StringBuilder(textView.getText()).toString();
          File myfile = new File(theApp.getCurrentDirectory(),name);
          FileEdit fe = getEditor();
          try {
            theApp.saveAs(fe,myfile);
          } catch(IOException e) {
            Toast.makeText(AbstractActivity.this, "there was an error saving " + myfile.getName(),
                           Toast.LENGTH_LONG).show();

            Log.e(JConsoleApp.LogTag,"there was an error saving the file " + myfile.getName(),e);
          }
          ad.dismiss();

        }
      });
      if(cancel != null) cancel.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          ad.cancel();
        }
      });
      ad.show();
    } catch(IOException e) {
      Log.e(JConsoleApp.LogTag,"an error occurred opening file dialog.",e);
    }
  }

  protected  List<String> loadFileList(File mPath,final  String ext,final boolean dirsOnly)
  {
    List<String> result = new ArrayList<String>();
    if (mPath.exists()) {
      FilenameFilter filter = new FilenameFilter() {
        public boolean accept(File dir, String filename) {
          File sel = new File(dir, filename);
          if(dirsOnly) {
            return sel.isDirectory();
          } else {
            return ext == null || filename.endsWith(ext)
                   || sel.isDirectory();
          }
        }
      };
      String[] r = mPath.list(filter);
      for (String ee : r) {
        result.add(ee);
      }
    }
    return result;
  }

  protected ArrayAdapter<String> createDirAdapter(File dir,boolean dirsOnly)
  {
    List<String> files = loadFileList(dir, null,dirsOnly);
    int nfiles = files.size();
    if(!(dir.equals(theApp.getRoot()) || dir.getAbsolutePath().equals("/mnt/sdcard") || dir.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getPath()))) {
      files.add(0, "..");
    }
    String[] fl = filterFilelist(dir, files,nfiles);
    return new ArrayAdapter<String>(this,R.layout.list_item, fl);
  }

  class GetHttpTask extends AsyncTask<String, String, String>
  {

    @Override
    protected String doInBackground(String... url)
    {
      // constants
      int timeoutConnection = 5000;
      try {
        URL url0 = new URL(url[0]);
        HttpURLConnection connection = (HttpURLConnection)url0.openConnection();
        connection.setRequestProperty("User-Agent", "");
        connection.setRequestMethod("GET");
        connection.connect();

        final int statusCode = connection.getResponseCode();

        if(statusCode != 200) {
          Log.d(JConsoleApp.LogTag, "Download Error: " + statusCode + "| for URL: " + url);
          return null;
        }

        String line = "";
        StringBuilder total = new StringBuilder();

        InputStream inputStream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        while ((line = reader.readLine()) != null) {
          total.append(line);
        }

        line = total.toString();
        return line;
      } catch (Exception e) {
        Log.d(JConsoleApp.LogTag, "Download Exception : " + e.toString());
      }
      return null;
    }

    @Override
    protected void onPostExecute(String result)
    {
      if (null==result) return;
      Log.d(JConsoleApp.LogTag, result);
      int p1,p2,p3;
      if (-1!=(p1=result.indexOf("android:versionCode"))) {
        if (-1!=(p2=result.indexOf("\"",p1+19+1))) {
          if (-1!=(p3=result.indexOf("\"",p2+1))) {
            int ver=-1;
            try {
              ver=Integer.valueOf(result.substring(p2+1,p3));
            } catch (NumberFormatException e) {
            }
            Log.d(JConsoleApp.LogTag,"new version: "+ver);
            if (ver>theApp.mVersionCode) {
              AlertDialog.Builder builder = new AlertDialog.Builder(AbstractActivity.this);
              builder.setMessage("There is newer version of this application available.\n\nDo you want to upgrade now?");
              builder.setPositiveButton(android.R.string.yes, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
                  Intent myIntent = new Intent(Intent.ACTION_VIEW,
                                               Uri.parse("http://www.jsoftware.com/download/j" + theApp.jversion + "/install/j" + theApp.jversion + "_android.apk"));
                  startActivity(myIntent);
                }
              });
              builder.setNegativeButton(android.R.string.no, new AlertDialog.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
                }
              });
              builder.show();
            } else
              Toast.makeText(AbstractActivity.this, "Aready up to date", Toast.LENGTH_SHORT).show();
          }
        }
      }
    }
  }

  @Override
  protected void onSaveInstanceState(Bundle savedInstanceState)
  {
    super.onSaveInstanceState(savedInstanceState);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState)
  {
    super.onRestoreInstanceState(savedInstanceState);
  }

}



