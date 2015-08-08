package com.jsoftware.jn.wd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.Toast;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// the syntax for messages is:
//   wd 'mb type title message buttons '
//
// type specifies the icon and default behaviour:
//  about
//  info      (same as about)
//  query     (requires two or three buttons)
//
// if zero or button, there is no result,
// otherwise the result be signal an dialog event, either
//  form_dialog_positive
//  form_dialog_negative
//  form_dialog_neutral
//
// At most three buttons: positive, negative, neutral
//
// if type is toast, format is
// mb toast message [0|1]
//   duration 1 (or elided) is short

class JMb
{

  String type;
  String[] arg;
  Form form;
  Activity activity;
  String childid;

// ---------------------------------------------------------------------
  JMb(Form form)
  {
    if (null!=form) {
      this.form=form;
      activity=form.activity;
    }
  }

// ---------------------------------------------------------------------
// c is type, p is parameter, possibly preceded by *
  String mb(String c,String p)
  {
    type=c;
    if (type.isEmpty()) {
      JConsoleApp.theWd.error("missing mb type");
      return "";
    }

    arg=Cmd.qsplit(p,true);
    if (type.equals("info")||type.equals("about"))
      return mbmsg();
    if (type.equals("query"))
      return mbmsg();
    if (type.equals("toast"))
      return mbtoast();
//   if (type.equals("color"))
//     return mbcolor();
//   if (type.equals("dir"))
//     return mbdir();
//   if (type.equals("open"))
//     return mbopen();
//   if (type.equals("open1"))
//     return mbopen1();
//   if (type.equals("save"))
//     return mbsave();
    JConsoleApp.theWd.error("invalid mb type: " + type);
    return "";
  }

// ---------------------------------------------------------------------
  private String mbtoast()
  {
    String m;
    int duration=Toast.LENGTH_SHORT;

    if (arg.length==1) {
      m=arg[0];
    } else if (arg.length>1) {
      m=arg[0];
      duration=(arg[1].equals("0"))?Toast.LENGTH_LONG:Toast.LENGTH_SHORT;
    } else {
      JConsoleApp.theWd.error("Need message: "+Util.sajoinstr(arg," "));
      return "";
    }
    if (null!=form)
      Toast.makeText(activity, m, duration).show();
    else
      Toast.makeText(JConsoleApp.theApp.getApplicationContext(), m, duration).show();

    return "";
  }


// ---------------------------------------------------------------------
  private String mbmsg()
  {
    String t="",m="";
    int ptr=0,offset=0;
    childid="";
    String button1="";
    String button2="";
    String button3="";

    if (0==arg.length) {
      JConsoleApp.theWd.error("Need title and message: "+Util.sajoinstr(arg," "));
      return "";
    }
    if (type.equals("query") && 1==arg.length) {
      JConsoleApp.theWd.error("Need callback, title and message: "+Util.sajoinstr(arg," "));
      return "";
    }
    if (type.equals("query")) {
      childid=arg[ptr++];
    }

    if (arg.length>ptr+1) {
      t=arg[ptr++];
      m=arg[ptr++];
    } else if (arg.length>ptr)
      m=arg[ptr++];
    if (arg.length>ptr)
      button1=arg[ptr++];
    if (arg.length>ptr)
      button2=arg[ptr++];
    if (arg.length>ptr)
      button3=arg[ptr++];

    if (type.equals("query")) {
      if (button1.isEmpty()) {
        button1="Ok";
        button2="Cancel";
      } else if (button2.isEmpty())
        button2="Cancel";
    }
    if (type.equals("query"))
      messageBox(t,m,button1,button2,button3);
    else if (type.equals("info")||type.equals("about"))
      messageBox(t,m,"","","");
    return "";
  }

// // ---------------------------------------------------------------------
// private String mbcolor()
// {
//   QColor c;
//   int r,g,b;
//
//   if (arg.length==3) {
//     r=Util.c_strtoi(arg[0]);
//     g=Util.c_strtoi(arg[1]);
//     b=Util.c_strtoi(arg[2]);
//     c=QColor(r,g,b);
//   } else
//     c=Qt::white;
//   c=QColorDialog::getColor(c);
//   if (!c.isValid()) return "";
//   return Util.i2s(c.red() + " " + Util.i2s(c.green()) + " " + Util.i2s(c.blue()));
// }
//
// // ---------------------------------------------------------------------
// private String mbdir()
// {
//   String title,dir,fl;
//   if (arg.length!=2) {
//     JConsoleApp.theWd.error("dir needs title and directory");
//     return "";
//   }
//   title=arg.at(0);
//   dir=arg.at(1);
//   fl=QFileDialog::getExistingDirectory(
//        title,dir);
//   return fl;
// }
//
// // ---------------------------------------------------------------------
// private String mbopen()
// {
//   String title,dir,filter;
//   String[] fl;
//   if (arg.length<2) {
//     JConsoleApp.theWd.error("open needs title, directory, [filters]");
//     return "";
//   }
//   title=arg.at(0);
//   dir=arg.at(1);
//   if (arg.length==3)
//     filter=fixsep(arg.at(2));
//   fl=QFileDialog::getOpenFileNames(
//        title,dir,filter);
//   if (fl.isEmpty())
//     return "";
//   else return fl.join("\012") + "\012";
// }
//
// // ---------------------------------------------------------------------
// private String mbopen1()
// {
//   String title,dir,filter,fl;
//   if (arg.legnth<2) {
//     JConsoleApp.theWd.error("open1 needs title, directory, [filters]");
//     return "";
//   }
//   title=arg.at(0);
//   dir=arg.at(1);
//   if (arg.length==3)
//     filter=fixsep(arg.at(2));
//   fl=QFileDialog::getOpenFileName(
//        title,dir,filter);
//   return fl;
// }
//
// // ---------------------------------------------------------------------
// private String mbsave()
// {
//   String title,dir,filter,fl;
//   if (arg.length<2) {
//     JConsoleApp.theWd.error("save needs title, directory, [filters]");
//     return "";
//   }
//   title=arg.at(0);
//   dir=arg.at(1);
//   if (arg.length==3)
//     filter=fixsep(arg.at(2));
//   fl=QFileDialog::getSaveFileName(
//        title,dir,filter);
//   return fl;
// }
//
// ---------------------------------------------------------------------
  private String fixsep(String s)
  {
    return s.replaceAll("|",";;");
  }

// ---------------------------------------------------------------------
  private void messageBox(String title,String text,String pos,String neg,String neu)
  {
    if (null==form) {
      Toast.makeText(JConsoleApp.theApp.getApplicationContext(), text, Toast.LENGTH_SHORT).show();
      return;
    }
    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
    builder.setTitle(title);
    builder.setMessage(text);

    if (!pos.isEmpty())builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
        // User clicked positive
        form.dialogchild=JMb.this.childid;
        form.event="dialogpositive";
        form.signalevent(null,null);
      }
    });
    if (!neg.isEmpty()) builder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
        // User clicked negative
        form.dialogchild=JMb.this.childid;
        form.event="dialognegative";
        form.signalevent(null,null);
      }
    });
    if (!neu.isEmpty()) builder.setNeutralButton(neu, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        dialog.dismiss();
        // User clicked neutral
        form.dialogchild=JMb.this.childid;
        form.event="dialogneutral";
        form.signalevent(null,null);
      }
    });
    builder.show();
  }
}
