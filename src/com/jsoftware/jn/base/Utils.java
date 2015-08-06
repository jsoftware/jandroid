package com.jsoftware.jn.base;

import java.lang.Character;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.lang.System;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;

import android.util.Log;

import com.jsoftware.j.android.JConsoleApp;

import com.jsoftware.jn.base.Util;
// import com.jsoftware.jn.base.Dialog;
// import com.jsoftware.jn.base.Note;
// import com.jsoftware.jn.base.Jsvr;
// import com.jsoftware.jn.base.Proj;
// import com.jsoftware.jn.base.Recent;
// import com.jsoftware.jn.base.State;
// import com.jsoftware.jn.base.Tedit;
// import com.jsoftware.jn.base.Term;

public class Utils
{

  public static boolean ShowIde=false;
// public static List<int> Modifiers = new List<int> { Key_Alt , Key_AltGr , Key_Control , Key_Meta , Key_Shift } ;
  public static int[] Modifiers = new int[0];

// // ---------------------------------------------------------------------
// // convert name to full path name
// public static String cpath(String s)
// {
//   int t;
//   String f,p;
//
//   if ((s.length() == 0) || isroot(s))
//     return cfcase(s);
//   t=(int) (s.at(0)=='~');
//   int n = s.indexOf('/');
//   if (n < 0) {
//     f=s.substring(t);
//     p="";
//   } else {
//     f=s.substring(t,t+n-t);
//     p=s.substring(n);
//   }
//
//   if (f.length() == 0) f = "home";
//   boolean par = f.at(0) == '.';
//   if (par) f.remove(0,1);
//
//   n = config.AllFolderKeys.indexOf(f);
//   if (n<0) return cfcase(s);
//   f = config.AllFolderValues.at(n);
//
//   if (par) f = cfpath(f);
//   return cfcase(f + p);
// }
//
// // ---------------------------------------------------------------------
// public static String defext(String s)
// {
//   if (s.isEmpty() || -1!=s.indexOf('.')) return s;
//   return s + config.DefExt;
// }
//
// // ---------------------------------------------------------------------
// public static int fkeynum(int key,boolean c,boolean s)
// {
//   return key + (c*100) + (s*100000);
// }
//
// // ---------------------------------------------------------------------
// // b is base directory
// public static String[] folder_tree(String b,String filters,boolean subdir)
// {
//   if (!subdir)
//     return cflistfull(b,filters);
//   return folder_tree1(b,"",getfilters(filters));
// }
//
// // ---------------------------------------------------------------------
// // b is base directory, s is current subdirectory
// public static String[] folder_tree1(String b,String s,String[] f)
// {
//   String n;
//   String t=b + "/" + s;
//
//   QDir d(t);
//   d.setNameFilters(f);
//   String[] r=d.entryList(QDir::Files|QDir::Readable);
//   for(int i=0; i<r.size(); i++)
//     r.replace(i,t+r.at(i));
//
//   QDirIterator p(t,QDir::Dirs|QDir::NoDotAndDotDot);
//   while (p.hasNext()) {
//     p.next();
//     if (!config.DirTreeX.contains(p.fileName()))
//       r=r+folder_tree1(b,s+p.fileName()+"/",f);
//   }
//
//   return r;
// }
//
// ---------------------------------------------------------------------
// public static void fontdiff(int n)
// {
//   config.Font.setPointSize(n+config.Font.pointSize());
//   fontset(config.Font);
// }
//
// ---------------------------------------------------------------------
  public static void fontset(Typeface font)
  {
//  config.Font=font;
//   tedit.setFont(font);
//   if (note) {
//     note.setfont(font);
//     if (note2)
//       note2.setfont(font);
//   }
//   tedit.ifResized=false;
  }
//
// // ---------------------------------------------------------------------
// public static void fontsetsize(int n)
// {
//   config.Font.setPointSize(n);
//   fontset(config.Font);
// }
//
// // ---------------------------------------------------------------------
// public static String fontspec(Typeface font)
// {
//   StringBuilder r=new StringBuilder();
//   r.append("\"" + font.family() + "\" " + String.toString(font.pointSizeF()));
//   if (font.isBold()) r.append(" bold");
//   if (font.isItalic()) r.append(" italic");
//   return r.toString();
// }
//
// // ---------------------------------------------------------------------
// // if editor is active, return the note edit control,
// // otherwise return the term edit control
// public static Bedit * getactiveedit()
// {
//   if (note && ActiveWindows.indexOf(note)<ActiveWindows.indexOf(term))
//     return (Bedit *)note.editPage();
//   return tedit;
// }
//
// // ---------------------------------------------------------------------
// // get command String in form: mode)text
// public static String getcmd(String mode,String t)
// {
//   String v=q2s(t.trimmed());
//   const char *c=v.c_str();
//   int i=0,p=0,s=(int)v.size();
//   for (; i<s; i++) {
//     if (c[i]==')') p=i;
//     if (! (isalnum(c[i]) || c[i]==')' || c[i]=='.')) break;
//   }
//   if (p==0) return mode + ")" + t;
//   size_t b = v.find_last_of(')',p-1);
//   if (b==String::npos) return t;
//   v.erase(0,b+1);
//   return s2q(v);
// }
//
// // ---------------------------------------------------------------------
// public static int gethash(String s, byte[] t, int wid, String[] msg)
// {
//   String hashbuf;
//   int rc=0;
//   QCryptographicHash::Algorithm a;
//   String m=c2s(s);
//   if (m.equals("md4"))
//     a=QCryptographicHash::Md4;
//   else if (m.equals("md5"))
//     a=QCryptographicHash::Md5;
//   else if (m.equals("sha1"))
//     a=QCryptographicHash::Sha1;
//   else if (m.equals("sha224"))
//     a=QCryptographicHash::Sha224;
//   else if (m.equals("sha256"))
//     a=QCryptographicHash::Sha256;
//   else if (m.equals("sha384"))
//     a=QCryptographicHash::Sha384;
//   else if (m.equals("sha512"))
//     a=QCryptographicHash::Sha512;
//   else if (m.equals("sha3_224"))
//     a=QCryptographicHash::Sha3_224;
//   else if (m.equals("sha3_256"))
//     a=QCryptographicHash::Sha3_256;
//   else if (m.equals("sha_384"))
//     a=QCryptographicHash::Sha3_384;
//   else if (m.equals("sha3_512"))
//     a=QCryptographicHash::Sha3_512;
//   else {
//     rc=1;
//     hashbuf="Hash type unknown: " + m;
//   }
//   if (rc==0)
//     hashbuf=q2s(QCryptographicHash::hash(byte[](t,wid),a).toHex());
//   msg=(char *)hashbuf.c_str();
//   len=(int)hashbuf.size();
//   return rc;
// }
//
// // --------------------------------------// ---------------------------------------------------------------------
// // get parent for message box
// public static View getmbparent()
// {
//   View w;
//   w=QApplication::focusWidget();
//   if (!w)
//     w=QApplication::activeWindow();
//   if (!w)
//     w=getactivewindow();
//   return w;
// }
//
// // ---------------------------------------------------------------------
// public static String getsha1(String s)
// {
//   return QCryptographicHash::hash(s.toUtf8(),QCryptographicHash::Sha1).toHex();
// }

// // ---------------------------------------------------------------------
// public static String getversion()
// {
//   String r;
//   r=APP_VERSION;
//   r=r+"s";
//   r=r+"/"+qVersion();
//   return r;
// }

// ---------------------------------------------------------------------
// return if git available
  public static boolean gitavailable()
  {
    return false;
  }

// // ---------------------------------------------------------------------
// // git gui
// public static void gitgui(String path)
// {
//   if (config.ifGit) {
//     QProcess p;
//     p.startDetached("git",String[]() << "gui",path);
//   }
// }
//
// // ---------------------------------------------------------------------
// // return status or empty if not git
// public static String gitstatus(String path)
// {
//   if (config.ifGit)
//     return shell("git status",path).at(0);
//   return "";
// }
//
// // ---------------------------------------------------------------------
// public static String[] globalassigns(String s,String ext)
// {
//   String[] p,r;
//   String t;
//   QRegExp rx;
//   int c,i,pos = 0;
//
//   t=rxassign(ext,true);
//   if (t.isEmpty()) return r;
//   rx.setPattern("(([a-z]|[A-Z])\\w*)"+t);
//
//   while ((pos = rx.indexIn(s, pos)) != -1) {
//     p << rx.cap(1);
//     pos += rx.matchedLength();
//   }
//
//   qSort(p);
//   i=0;
//   while (i<p.size()) {
//     t=p.at(i);
//     c=i++;
//     while (i<p.size()&&t==p.at(i)) i++;
//     if (1<i-c)
//       t=t + " (" + String::number(i-c) + ")";
//     r.append(t);
//   }
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static void helpabout()
// {
//   String[] s=state_about();
//   about(s.at(0),s.at(1));
// }
//
// // ---------------------------------------------------------------------
// public static boolean ismodifier(int key)
// {
//   return Modifiers.contains(key);
// }

// ---------------------------------------------------------------------
  public static void logcat(byte[] s)
  {
// for debug android standalone app
    Log.d(JConsoleApp.LogTag,new String(s,Charset.forName("UTF-8")));
  }

// ---------------------------------------------------------------------
// public static void newfile(View w)
// {
//   String s = dialogsaveas(w,"New File", getfilepath());
//   if (s.isEmpty()) return;
//   if (!s.contains('.'))
//     s+=config.DefExt;
//   cfcreate(s);
//   note.fileopen(s);
// }
//
// // ---------------------------------------------------------------------
// public static String newtempscript()
// {
//   int i,m,len,t;
//   String f;
//   String[] s=cflist(config.TempPath.absolutePath(),"*" + config.DefExt);
//
//   List<int> n;
//   len = config.DefExt.size();
//   foreach (String p, s) {
//     p.chop(len);
//     m=p.toInt();
//     if (m)
//       n.append(m);
//   }
//   t=1;
//   if (n.size()) {
//     qSort(n);
//     for (i=1; i<n.size()+2; i++)
//       if (!n.contains(i)) {
//         t=i;
//         break;
//       }
//   }
//   return config.TempPath.filePath(String::number(t) + config.DefExt);
// }
//
// // ---------------------------------------------------------------------
// public static void openfile(View w,String type)
// {
//   String f = dialogfileopen(w,type);
//   if (f.isEmpty()) return;
//   openfile1(f);
// }
//
// // ---------------------------------------------------------------------
// public static void openfile1(String f)
// {
//   term.vieweditor();
//   note.fileopen(f);
//   recent.filesadd(f);
// }
//
// // ---------------------------------------------------------------------
// public static void openj(const char *s)
// {
//   if (!term) return;
//   if (!ShowIde) return;
//   String f(s);
//   f=f.trimmed();
//   if (f.isEmpty()) return;
//   if(!cfexist(f)) {
//     info("Open","Not found: "+f);
//     return;
//   }
//   openfile1(f);
// }
//
// // ---------------------------------------------------------------------
// public static void projectclose()
// {
//   project.close();
//   term.projectenable();
//   if (note) {
//     note.Id="";
//     note.setindex(note.editIndex());
//     note.projectenable();
//   }
// }
//
// // ---------------------------------------------------------------------
// public static void projectenable()
// {
//   term.projectenable();
//   if (note) {
//     note.projectenable();
//   }
// }
//
// // ---------------------------------------------------------------------
// // folder tree from folder name
// public static String[] project_tree(String s)
// {
//   String[] r;
//   r=project_tree1(cpath(s),"");
//   r.sort();
//   return r;
// }
//
// // ---------------------------------------------------------------------
// // b is base directory, s is current subdirectory
// public static String[] project_tree1(String b,String s)
// {
//   String n,p;
//   String t=b + "/" + s;
//   QDirIterator d(t,QDir::Dirs|QDir::NoDotAndDotDot);
//   String[] r;
//   while (d.hasNext()) {
//     d.next();
//     n=d.fileName();
//     if (FileInfo(t + n + "/" + n + config.ProjExt).exists())
//       r.append(s + n);
//     r = r + project_tree1(b,s + n + "/");
//   }
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static void projectterminal()
// {
//   if (config.Terminal.isEmpty()) {
//     info("Terminal","The Terminal command should be defined in qtide.cfg.");
//     return;
//   }
//   String d;
//   if (project.Id.isEmpty()) {
//     if (note.editIndex()<0)
//       d=config.TempPath.absolutePath();
//     else
//       d=cfpath(note.editFile());
//   } else
//     d=project.Path;
//   QProcess p;
//   String[] a;
//   p.startDetached(config.Terminal,a,d);
// }
//
// // ---------------------------------------------------------------------
// public static String rxassign(String ext, boolean ifglobal)
// {
//   String r;
//   if (ext.equals(".ijs")||ext.equals(".ijt"))
//     r=ifglobal ? "\\s*=:" : "\\s*=[.:]" ;
//   else if (ext.equals(".k")||ext.equals(".q"))
//     r="\\s*:";
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static void setwh(View w, String s)
// {
//   List<int>p=config.winpos_read(s);
//   w.resize(qMax(p[2],300),qMax(p[3],300));
// }
//
// // ---------------------------------------------------------------------
// public static void setxywh(View w, String s)
// {
//   List<int>p=config.winpos_read(s);
//   w.move(p[0],p[1]);
//   w.resize(qMax(p[2],300),qMax(p[3],300));
// }
//
// // ---------------------------------------------------------------------
// // return standard output, standard error
// public static String[] shell(String cmd, String dir)
// {
//   String[] r;
//   QProcess p;
//   if (!dir.isEmpty())
//     p.setWorkingDirectory(dir);
//   p.start(cmd);
//   try {
//     if (!p.waitForStarted())
//       return r;
//   } catch (...) {
//     return r;
//   }
//   if (!p.waitForFinished())
//     return r;
//   r.append((String)p.readAllStandardOutput());
//   r.append((String)p.readAllStandardError());
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static void setnote(Note *n)
// {
//   if (note!=n) {
//     note2=note;
//     if (note2)
//       note2.settitle2(true);
//     note=n;
//     note.settitle2(false);
//     note.setid();
//   }
// }

// ---------------------------------------------------------------------
  public static void showide(boolean b)
  {
//  if (!term) return;
//   if (note2)
//     note2.setVisible(b);
//   if (note)
//     note.setVisible(b);
//   term.setVisible(b);
    ShowIde=b;
  }

// // ---------------------------------------------------------------------
// // convert file name to folder name
// // searches for the best fit
// // if none then check for parent folders
// public static String tofoldername(String f)
// {
//   int i;
//   String g,p,s,t;
//   if (f.isEmpty()) return f;
//
//   for (i=0; i<config.AllFolderValues.size(); i++) {
//     t=config.AllFolderValues.at(i);
//     if (matchfolder(t,f) && t.size() > s.size())
//       s=t;
//     else if (matchfolder(cfpath(t),f) && t.size() > p.size())
//       p=t;
//   }
//
//   if (s.size()) {
//     i=config.AllFolderValues.indexOf(s);
//     return '~' + config.AllFolderKeys.at(i) + f.mid(s.size());
//   }
//
//   if (p.size()) {
//     i=config.AllFolderValues.indexOf(p);
//     return "~." + config.AllFolderKeys.at(i) + f.mid(cfpath(p).size());
//   }
//
//   return f;
// }
//
// // ---------------------------------------------------------------------
// // shortest name relative to current project if any
// public static String toprojectname(String f)
// {
//   String s=cpath(f);
//
//   if (project.Id.size() && matchfolder(project.Path,s))
//     s=cfsname(s);
//   else {
//     s=tofoldername(s);
//     if (s.at(0).equals("~"))
//       s=s.mid(1);
//   }
//   return s;
// }
//
// // ---------------------------------------------------------------------
// public static void userkey(int mode, String s)
// {
//   Bedit *w=getactiveedit();
//
//   if (mode==0 || mode==1) {
//     writewinstate(w);
//     var_runs(s,mode==1);
//     return;
//   }
//
//   if (!w) return;
//
//   if (mode==2) {
//     if (w==tedit) s=tedit.getprompt()+s;
//     w.appendPlainText(s);
//     return;
//   }
//
//   if (mode==4)
//     w.moveCursor(QTextCursor::EndOfBlock, QTextCursor::MoveAnchor);
//   w.textCursor().insertText(s);
// }
//
// // ---------------------------------------------------------------------
// // get window position
// // subject to minimum/maximum size and fit on screen
// public static List<int> winpos_get(View s)
// {
//   List<int> d;
//   QPoint p=s.pos();
//   QSize z=s.size();
//   int x=p.rx();
//   int y=p.ry();
//   int w=z.width();
//   int h=z.height();
//
//   w=qMax(100,qMin(w,config.ScreenWidth));
//   h=qMax(50,qMin(h,config.ScreenHeight));
//   x=qMax(0,qMin(x,config.ScreenWidth-w));
//   y=qMax(0,qMin(y,config.ScreenHeight-h));
//
//   d << x << y << w << h;
//   return d;
// }
//
// // ---------------------------------------------------------------------
// public static void winpos_set(View w,List<int>p)
// {
//   if (p[0] >= 0)
//     w.move(p[0],p[1]);
//   w.resize(p[2],p[3]);
// }
//
// // ---------------------------------------------------------------------
// public static void writewinstate(Bedit *w)
// {
//   if (w==0) {
//     sets("WinText_jqtide_","");
//     var_cmd("WinSelect_jqtide_=: $0");
//     return;
//   }
//   QTextCursor c=w.textCursor();
//   int b=c.selectionStart();
//   int e=c.selectionEnd();
//   String t=w.toPlainText();
//   String s=String::number(b)+" "+String::number(e);
//   sets("WinText_jqtide_",q2s(t));
//   sets("inputx_jrx_",q2s(s));
//   var_cmd("WinSelect_jqtide_=: 0 \". inputx_jrx_");
// }
//
// // ---------------------------------------------------------------------
// public static void xdiff(String s,String t)
// {
//   if (config.XDiff.size()==0) {
//     info ("External Diff","First define XDiff in the config");
//     return;
//   }
//   String[] a;
//   a << s << t;
//   QProcess p;
//   p.startDetached(config.XDiff,a);
// }

}
