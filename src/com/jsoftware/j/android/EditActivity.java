package com.jsoftware.j.android;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.jsoftware.j.android.JConsoleApp.ResponseAction;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class EditActivity extends AbstractActivity
{

  JConsoleApp theApp;
  FileEdit editor;
  File file;
  boolean textChanged = false;
  EditorData edat;

  public void setFile(File f)
  {
    this.file = f;
  }

  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.edit);
    editor = (FileEdit) findViewById(R.id.edit);
    editor.setActivity(this);
    theApp = (JConsoleApp) this.getApplication();
    /*

    if(!restore(s)) {
    	try {
    		if(file.exists()) {
    			BufferedReader reader = new BufferedReader(
    				new InputStreamReader(new FileInputStream(file)));
    			StringBuilder sb = new StringBuilder();
    			String line;
    			while((line = reader.readLine())!=null) {
    				sb.append(line).append("\n");
    			}
    			editor.setText(sb.toString());
    			editor.textChanged = false;
    		}
    	} catch(IOException e) {
    		editor.setText("The was an error reading the requested file " + s);
    		Log.e(JConsoleApp.LogTag,"error loading file",e);
    	}
    	capture();
    }
    */

    /*
    if((savedInstanceState== null) || ! savedInstanceState.containsKey("editor")) {
    	try {
    		if(file.exists()) {
    			BufferedReader reader = new BufferedReader(
    				new InputStreamReader(new FileInputStream(file)));
    			StringBuilder sb = new StringBuilder();
    			String line;
    			while((line = reader.readLine())!=null) {
    				sb.append(line).append("\n");
    			}
    			editor.setText(sb.toString());
    			editor.textChanged = false;
    		}
    	} catch(IOException e) {
    		editor.setText("The was an error reading the requested file " + s);
    		Log.e(JConsoleApp.LogTag,"error loading file",e);
    	}
    }

    if(savedInstanceState != null) {
    	editor.setText 	(savedInstanceState.getCharSequence("editor"));
    	int n = savedInstanceState.getInt("cursor");
    	boolean tc = savedInstanceState.getBoolean("textchanged");
    	editor.textChanged = tc;
    	editor.setSelection(n);
    }
    */
  }
  @Override
  public void onPause()
  {
    super.onPause();
    capture();
  }
  @Override
  public void onResume()
  {
    super.onResume();
    String s = getIntent().getData().getPath();
    file = new File(s);
    theApp.addIntent(file.getAbsolutePath(),getIntent());
    editor.setName(file.getName());
    if(!restore(s)) {
      try {
        if(file.exists()) {
          BufferedReader reader = new BufferedReader(
            new InputStreamReader(new FileInputStream(file)));
          StringBuilder sb = new StringBuilder();
          String line;
          while((line = reader.readLine())!=null) {
            sb.append(line).append("\n");
          }
          editor.setText(sb.toString());
          editor.textChanged = false;
        }
      } catch(IOException e) {
        editor.setText("The was an error reading the requested file " + s);
        Log.e(JConsoleApp.LogTag,"error loading file",e);
      }

    }
    setTitle(file.getName());

  }
  public void capture()
  {
    theApp.editorMap.put(file.getAbsolutePath(), new EditorData(file.getName(),
                         file.getAbsolutePath(),
                         editor.getText(),
                         editor.getSelectionStart(),
                         textChanged));
  }

  public boolean restore(String path)
  {
    boolean result = false;

    EditorData edat = theApp.editorMap.get(path);
    if(edat!=null) {
      editor.setText(edat.text);
      editor.setSelection(edat.cursorPosition);
      editor.textChanged = edat.changed;
      setFile(new File(edat.path));
      result = true;
    }
    return result;
  }
  /*
  //	@Override
  	public void runFile() {
  		runFile(file);
  	}
   */
  @Override
  public void onDestroy()
  {
    capture();
    super.onDestroy();
    Log.d("jandroid", "editactivity destroy");
    if(isFinishing()) {
      Log.d("jandroid", "editactivity destroy isFinishing");
      theApp.removeFile(file.getAbsolutePath());
    }
  }


  protected FileEdit getEditor()
  {
    return editor;
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = this.getMenuInflater();
    inflater.inflate(R.menu.edit, menu);
    return true;
  }
  @Override
  public boolean onMenuItemSelected(int featureId, MenuItem item)
  {
    boolean result = true;
    int itemId = item.getItemId();
    switch(itemId) {
    case R.id.close:
      close();
      break;
    case R.id.save:
      save();
      break;
    case R.id.runc:
      runCurrentFile();
      break;
    case R.id.saveas:
      requestFileSaveAs(file);
      break;

    default :
      result = false;
    }
    if(!result) {
      result = super.onMenuItemSelected(featureId, item);
    }

    return result;
  }

  public void save()
  {
    try {

      editor.save(file);
//			setTitle(buildTitle(file));
    } catch (IOException e) {
      Log.e(JConsoleApp.LogTag,"error saving file " + file.getName(),e);
      Toast.makeText(this, "error saving file " + file.getName()
                     + ": " + e.getLocalizedMessage() , Toast.LENGTH_LONG).show();
    }
  }
  public void close()
  {
//		testQuit();
    theApp.removeFile(file.getAbsolutePath());
    this.finish();
  }
  /*
  @Override
  public void onSaveInstanceState(Bundle outState) {
  	int pos = editor.getSelectionStart();
  	outState.putCharSequence("editor",editor.getText());
  	outState.putInt("cursor",pos);
  	outState.putBoolean("textchanged",textChanged);
  //		outState.putParcelable("console", console);
  }
  */

  /*
  public void onSaveInstanceState(Bundle outState) {
  	int pos = editor.getSelectionStart();

  	outState.putInt("cursor",pos);
  	outState.putBoolean("loaded",true);
  }

  public void onTextChanged() {

  }
  */
  protected void runCurrentFile()
  {
    try {
      if(editor.textChanged) {
        theApp.promptSaveWithAction(editor, file, new ResponseAction() {
          public void action(boolean state) {
            if(state) {
              runFile(file);
            }
          }
        });
      } else {
//					File f = editor.getFile();
        runFile(file);
      }
    } catch(IOException e) {
      Log.e(JConsoleApp.LogTag,"error running current file",e);
    }
  }

  @Override
  public void onBackPressed()
  {
    moveTaskToBack(true);
  }

}
