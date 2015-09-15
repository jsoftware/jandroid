package com.jsoftware.jn.wd;

import android.util.Log;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.Editable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class FileChooser
{
  private static final String PARENT_DIR = "..";

  private final Activity activity;
  private ListView list;
  private Dialog dialog;
  private File currentPath;

  // filter on file extension
  private String[] extension = null;

  private String type;
  TextView fileView;

  public void setExtension(String s)
  {
    if (s == null || s.isEmpty())
      extension = null;
    else {
      if (s.contains("|"))
        extension = s.toLowerCase().split("\\|");
      else
        extension = new String[] {s};
    }
  }

  // file selection event handling
  public interface FileSelectedListener
  {
    void fileSelected(File file);
  }
  public FileChooser setFileListener(FileSelectedListener fileListener)
  {
    this.fileListener = fileListener;
    return this;
  }
  private FileSelectedListener fileListener;

  public FileChooser(Activity activity, String type, String title, String dir, String ext)
  {
    this.activity = activity;
    this.type = type;

    LinearLayout titleLayout = new LinearLayout(activity);
    titleLayout.setOrientation(LinearLayout.VERTICAL);
    titleLayout.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));

    fileView = new TextView(activity);
    fileView.setGravity(Gravity.CENTER_HORIZONTAL);
//    fileView.setSingleLine();
    fileView.setTextAppearance(activity, android.R.style.TextAppearance_Large);
    fileView.setText(dir);

    LinearLayout dirLayout = new LinearLayout(activity);
    dirLayout.setOrientation(LinearLayout.HORIZONTAL);

    Button useDir = new Button(activity);
    useDir.setText("Choose");
    useDir.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if (fileListener != null) {
          fileListener.fileSelected(currentPath);
          dialog.dismiss();
        }
      }
    });


    Button newDir = new Button(activity);
    newDir.setText("New folder");
    newDir.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        final EditText input = new EditText(FileChooser.this.activity);

        // Show new folder name input dialog
        new AlertDialog.Builder(FileChooser.this.activity).
        setTitle("New folder name").
        setView(input).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int whichButton) {
            Editable newDir = input.getText();
            String newDirName = newDir.toString();
            // Create new directory
            if ( createSubDir(currentPath.getPath() + "/" + newDirName) ) {
              // TODO Navigate into the new directory
              refresh(new File(currentPath.getPath() + "/" + newDirName) );
            } else {
              Toast.makeText(
                FileChooser.this.activity, "Failed to create '" + newDirName +
                "' folder", Toast.LENGTH_SHORT).show();
            }
          }
        }).setNegativeButton(android.R.string.no, null).show();
      }
    });

    if (!type.equals("dir")) {
      newDir.setVisibility(View.GONE);
      useDir.setVisibility(View.GONE);
    }

    dialog = new Dialog(activity);
    dialog.setTitle(title);
    list = new ListView(activity);
    list.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,0,1));
    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int which, long id) {
        String fileChosen = (String) list.getItemAtPosition(which);
        File chosenFile = getChosenFile(fileChosen);
        if (chosenFile.isDirectory()) {
          refresh(chosenFile);
        } else {
          if (fileListener != null) {
            fileListener.fileSelected(chosenFile);
          }
          dialog.dismiss();
        }
      }
    });

    dirLayout.addView(newDir);
    dirLayout.addView(useDir);
    if (!type.equals("dir")) {
      dirLayout.setVisibility(View.GONE);
    }

    titleLayout.addView(fileView);
    titleLayout.addView(dirLayout);
    titleLayout.addView(list);

    dialog.setContentView(titleLayout);
    dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    setExtension(ext);
    refresh((null==dir)?Environment.getExternalStorageDirectory():new File(dir));
  }

  public void showDialog()
  {
    dialog.show();
  }

  private boolean createSubDir(String newDir)
  {
    File newDirFile = new File(newDir);
    if (! newDirFile.exists() ) {
      return newDirFile.mkdir();
    }

    return false;
  }


  /**
   * Sort, filter and display the files for the given path.
   */
  private void refresh(File path)
  {
    this.currentPath = path;
    if (path.exists()) {
      File[] dirs = path.listFiles(new FileFilter() {
        @Override public boolean accept(File file) {
          return (file.isDirectory() && file.canRead());
        }
      });
      File[] files = path.listFiles(new FileFilter() {
        @Override public boolean accept(File file) {
          if (type.equals("dir")) return false;
          if (!file.isDirectory()) {
            if (!file.canRead()) {
              return false;
            } else if (extension == null) {
              return true;
            } else {
              for (String s : extension)
                if (file.getName().toLowerCase().endsWith(s)) return true;
              return false;
            }
          } else {
            return false;
          }
        }
      });

      // convert to an array
      int i = 0;
      String[] fileList;
      if (path.getParentFile() == null) {
        fileList = new String[dirs.length + files.length];
      } else {
        fileList = new String[dirs.length + files.length + 1];
        fileList[i++] = PARENT_DIR;
      }
      Arrays.sort(dirs);
      Arrays.sort(files);
      for (File dir : dirs) {
        fileList[i++] = dir.getName()+"/";
      }
      for (File file : files ) {
        fileList[i++] = file.getName();
      }

      // refresh the user interface
      fileView.setText(currentPath.getPath());
      list.setAdapter(new ArrayAdapter(activity,
      android.R.layout.simple_list_item_1, fileList) {
        @Override public View getView(int pos, View view, ViewGroup parent) {
          view = super.getView(pos, view, parent);
          ((TextView) view).setSingleLine(true);
          return view;
        }
      });
    }
  }


  /**
   * Convert a relative filename into an actual File object.
   */
  private File getChosenFile(String fileChosen)
  {
    if (fileChosen.equals(PARENT_DIR)) {
      return currentPath.getParentFile();
    } else {
      return new File(currentPath, fileChosen);
    }
  }
}

