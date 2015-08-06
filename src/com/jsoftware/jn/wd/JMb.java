package com.jsoftware.jn.wd;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.jsoftware.j.android.JConsoleApp;
import com.jsoftware.jn.base.Util;
import com.jsoftware.jn.base.Utils;
import com.jsoftware.jn.wd.Cmd;
import com.jsoftware.jn.wd.Form;
import com.jsoftware.jn.wd.Wd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
// import com.jsoftware.jn.base.dialog;
// import com.jsoftware.jn.base.state;

// the syntax for messages is:
//   wd 'mb type buttons title message'
//
// type specifies the icon and default behaviour:
//  about
//  info      (same as about)
//  query     (requires two or three buttons)
//
// if one button, there is no result,
// otherwise the result is the button name (ok, cancel, ...)
//
// At most three buttons: positive, negative, neutral

public class JMb
{

  String type;
  String[] arg;
  Form form;

// ---------------------------------------------------------------------
  public JMb(Form form)
  {
    this.form=form;
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

    if (type.equals("about"))
      return mbmsg();
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
    if (type.equals("info")||type.equals("query"))
      return mbmsg();
    JConsoleApp.theWd.error("invalid mb type: " + type);
    return "";
  }

// ---------------------------------------------------------------------
  String mbmsg()
  {
    String t,m;

    int ptr;


    if (arg.length==1) {
      t="Message Box";
      m=arg[0];
      ptr=1;
    } else if (arg.length==2) {
      t=arg[0];
      m=arg[1];
      ptr=2;
    } else {
      JConsoleApp.theWd.error("Need title and message: "+Util.q2s(Util.sajoinstr(arg," ")));
      return "";
    }

    String button1=getonebutton(ptr);
    String button2=getonebutton(ptr+1);
    String button3=getonebutton(ptr+2);

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
// String mbcolor()
// {
//   QColor c;
//   int r,g,b;
//
//   if (arg.length==3) {
//     r=Util.c_strtoi(Util.q2s(arg.at(0)));
//     g=Util.c_strtoi(Util.q2s(arg.at(1)));
//     b=Util.c_strtoi(Util.q2s(arg.at(2)));
//     c=QColor(r,g,b);
//   } else
//     c=Qt::white;
//   c=QColorDialog::getColor(c);
//   if (!c.isValid()) return "";
//   return Util.s2q(Util.i2s(c.red()) + " " + Util.i2s(c.green()) + " " + Util.i2s(c.blue()));
// }
//
// // ---------------------------------------------------------------------
// String mbdir()
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
// String mbopen()
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
// String mbopen1()
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
// String mbsave()
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
  String fixsep(String s)
  {
    return s.replaceAll("|",";;");
  }

// ---------------------------------------------------------------------
  String getonebutton(int ptr)
  {
    if (ptr>arg.length-1) return "";
    return arg[ptr];
  }

// ---------------------------------------------------------------------
  void messageBox(String title,String text,String pos,String neg,String neu)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(form.activity);
    builder.setTitle(title);
    builder.setMessage(text);

    if (!pos.isEmpty())builder.setPositiveButton(pos, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // User clicked positive
        form.event="dialog";
        form.signalevent(null,0);
      }
    });
    if (!neg.isEmpty()) builder.setNegativeButton(neg, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // User clicked negative
        form.event="dialog";
        form.signalevent(null,1);
      }
    });
    if (!neu.isEmpty()) builder.setNeutralButton(neg, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) {
        // User clicked neutral
        form.event="dialog";
        form.signalevent(null,2);
      }
    });
    builder.show();
  }
}
