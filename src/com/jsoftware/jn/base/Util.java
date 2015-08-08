package com.jsoftware.jn.base;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Context;
import android.util.Log;
import com.jsoftware.j.android.JConsoleApp;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Character;
import java.lang.Math;
import java.lang.System;
import java.nio.charset.Charset;
import java.util.ArrayList;


public class Util
{

  public static int NoEvents=0;
  public static boolean ShowIde=true;

// ---------------------------------------------------------------------
  public static int find_first_of(String s, String t, int start)
  {
    int pos=s.length();
    int len=t.length();
    int j;
    for (int i=0; i<len; i++) {
      j=s.indexOf(t.charAt(i),start);
      if (-1!=j && j<pos) pos=j;
    }
    return (pos!=s.length())?pos:-1;
  }

// ---------------------------------------------------------------------
  public static boolean sacontains(String[] t, String s)
  {
    for (String a : t) {
      if (s.equals(a)) return true;
      if (s.isEmpty() && a.isEmpty()) return true;
    }
    return false;
  }

// ---------------------------------------------------------------------
  public static void qDebug(String s)
  {
    Log.d(JConsoleApp.LogTag,s);
  }

// ---------------------------------------------------------------------
  public static void about(Context ctx, String t,String s)
  {
    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    builder.setTitle(t);
    builder.setMessage(s);
    builder.setPositiveButton("OK", null);
    AlertDialog dialog = builder.show();
  }

// ---------------------------------------------------------------------
// converts J 16-26 box chars to utf8
  public static String boxj2utf8(String s)
  {
    s=s.replace('\20',(char)9484 );
    s=s.replace('\21',(char)9516 );
    s=s.replace('\22',(char)9488 );
    s=s.replace('\23',(char)9500 );
    s=s.replace('\24',(char)9532 );
    s=s.replace('\25',(char)9508 );
    s=s.replace('\26',(char)9492 );
    s=s.replace('\27',(char)9524 );
    s=s.replace('\30',(char)9496 );
    s=s.replace('\31',(char)9474 );
    s=s.replace('\32',(char)9472 );
    return s;
  }

// ---------------------------------------------------------------------
  public static String c2q(byte[] c)
  {
    return ba2s(c);
  }

// ---------------------------------------------------------------------
  public static String c2s(byte[] c)
  {
    return ba2s(c);
  }

// ---------------------------------------------------------------------
  public static String ba2s(byte[] c)
  {
    return new String(c,Charset.forName("UTF-8"));
  }

// ---------------------------------------------------------------------
  public static byte[] s2ba(String s)
  {
    return s.getBytes(Charset.forName("UTF-8"));
  }

// ---------------------------------------------------------------------
  public static boolean cderase(String name)
  {
    File dir=new File(name);
    if (!dir.exists()) return true;
    if (dir.isDirectory()) {
      for (String s : dir.list()) {
        boolean success = cderase(dir.getName()+"/"+s);
        if (!success) return false;
      }
    }
    // The directory is now empty so delete it
    return dir.delete();
  }

// ---------------------------------------------------------------------
  public static int cfappend (File file, String s)
  {
    try {
      FileWriter f = new FileWriter(file,true);
      f.write(s);
      f.flush();
      f.close();
      return s.length();
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
      return 0;
    }
  }

// ---------------------------------------------------------------------
  public static int cfappend (File file, byte[] b)
  {
    try {
      FileOutputStream f = new FileOutputStream(file,true);
      f.write(b);
      f.flush();
      f.close();
      return b.length;
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
      return 0;
    }
  }

// ---------------------------------------------------------------------
  public static String cfcase(String s)
  {
    return s;
  }

// // ---------------------------------------------------------------------
// public static boolean cfcopy(String from, String to)
// {
//   File d=new File(cfpath(to));
//   if (!d.mkdirs()) return false;
//   File f=new File(from);
//   File t=new File(to);
//   t.delete();
//   return f.copy(to);
// }
//
// ---------------------------------------------------------------------
// public static boolean cfcreate(String s)
// {
//   File f=new File(s);
//   String p="";
//   cfwrite(f,p);
//   return f.exists();
// }
//
// // ---------------------------------------------------------------------
// public static boolean cftouch(String s)
// {
//   File f=new File(s);
//   f.open();
//   return f.exists();
// }
//
// ---------------------------------------------------------------------
  public static boolean cfdelete(String s)
  {
    File f=new File(s);
    return f.delete();
  }

// ---------------------------------------------------------------------
  public static boolean cfexist(String s)
  {
    File f=new File(s);
    return f.exists();
  }

// ---------------------------------------------------------------------
  public static String cfext(String s)
  {
    int n=s.lastIndexOf('.');
    if (n<0) return "";
    return s.substring(n);
  }

// // ---------------------------------------------------------------------
// // file list
// public static String[] cflist(String path,String filters)
// {
//   File d(path);
//   String[] f=getfilters(filters);
//   return d.entryList(f,File::Files|File::Readable,File::Name);
// }
//
// // ---------------------------------------------------------------------
// // prepend path
// public static String[] cflistfull(String b,String filters)
// {
//   String[] r=cflist(b,filters);
//   String t=b+"/";
//   for(int i=0; i<r.size(); i++)
//     r.replace(i,t+r.at(i));
//   return r;
// }
//
// // ---------------------------------------------------------------------
// // list text files
// // is utf8 and size < 1e6
// public static String[] cflisttext(String path)
// {
//   byte[] b;
//   String[] p=cflistfull(path,"");
//   File f;
//   String[] r;
//   for (String s,p) {
//     f.setFileName(s);
//     if (f.size() < 1e6 && f.open(QIODevice::ReadOnly)) {
//       if(isutf8(f.readAll()))
//         r.append(s);
//       f.close();
//     }
//   }
//   return r;
// }
//
// ---------------------------------------------------------------------
// get path from filename
// same as parent for a directory
  public static String cfpath(String s)
  {
    int n = s.lastIndexOf('/');
    if (n < 1) return "";
    return s.substring(0,n-1);
  }

// // ---------------------------------------------------------------------
// public static String cfread(File *file)
// {
//   if (!file.open(QIODevice::ReadOnly|QIODevice::Text))
//     return s2q("");
//   QTextStream in(file);
//   in.setCodec("UTF-8");
//   String r = in.readAll();
//   file.close();
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String cfread(String s)
// {
//   File f=new File(s);
//   return cfread(&f);
// }
//
// // ---------------------------------------------------------------------
// public static byte[] cfreadbin(String s)
// {
//   byte[] r;
//   File f=new File(s);
//   if (f.open(QIODevice::ReadOnly)) {
//     r = f.readAll();
//     f.close();
//   }
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String[] cfreads(File *file)
// {
//   int i;
//   String[] r;
//   String s;
//   s=cfread(file);
//   if (s.isEmpty()) return r;
//   s = s.remove('\r').replace('\t',' ');
//   for (i=s.size(); i>0; i--)
//     if (s.at(i-1) != '\n') break;
//   return s.left(i).split('\n');
// }
//
// // ---------------------------------------------------------------------
// public static String[] cfreads(String s)
// {
//   File f=new File(s);
//   return cfreads(&f);
// }
//
// // ---------------------------------------------------------------------
// public static String[] cfreadx(String s)
// {
//   String[] r=cfreads(s);
//   String t;
//   r.removeDuplicates();
//   for(int i=r.size()-1; i>=0; i--) {
//     t=r.at(i);
//     if (t.isEmpty() || t.at(0)=='#' || t.mid(0,3).equals("NB."))
//       r.removeAt(i);
//   }
//   return r;
// }
//
// // ---------------------------------------------------------------------
// // remove directory (may be non-empty)
// // when used should first check OK to remove directory
// public static boolean cfrmdir(String & d)
// {
//   boolean r = true;
//   File dir(d);
//
//   if (dir.exists(d)) {
//     Q_FOREACH(FileInfo info, dir.entryInfoList(File::NoDotAndDotDot | File::System | File::Hidden  | File::AllDirs | File::Files, File::DirsFirst)) {
//       if (info.isDir())
//         r = cfrmdir(info.absoluteFilePath());
//       else
//         r = File::remove(info.absoluteFilePath());
//       if (!r)
//         return r;
//     }
//     r = dir.rmdir(d);
//   }
//   return r;
// }
//
// // ---------------------------------------------------------------------
// // get short name from filename
// public static String cfsname(String s)
// {
//   int n = s.lastIndexOf('/');
//   if (n < 1) return s;
//   return s.mid(n+1);
// }
//
// ---------------------------------------------------------------------
  public static long cftime(String s)
  {
    File f=new File(s);
    return f.lastModified();
  }

// // ---------------------------------------------------------------------
// public static int cfwrite(File file, String t)
// {
//   if (!file.open())
//     return 0;
//   QTextStream out(file);
//   out.setCodec("UTF-8");
//   out << t;
//   file.close();
//   return t.size();
// }
//
// // ---------------------------------------------------------------------
// public static int cfwrite(String s, String t)
// {
//   File f=new File(s);
//   return cfwrite(f,t);
// }
//
// // ---------------------------------------------------------------------
// public static int cfwrite(String s, byte[] b)
// {
//   File f=new File(s);
//   if (!f.open())
//     return 0;
//   int r=f.write(b);
//   f.close();
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static boolean createdir(File d)
// {
//   if (d.exists()) return true;
//   return  d.mkdirs(".");
// }
//
// // ---------------------------------------------------------------------
// public static String detab(String s)
// {
//   return s.replace('\t',' ');
// }
//
// // ---------------------------------------------------------------------
// public static String dlb(String s)
// {
//   for (int n=0; n<s.size(); n++) {
//     if (!s.at(n).isSpace()) {
//       return s.mid(n);
//     }
//   }
//   return "";
// }
//
// // ---------------------------------------------------------------------
// public static String dtb(String s)
// {
//   for (int n = s.size()-1; n>=0; n--) {
//     if (!s.at(n).isSpace()) {
//       return s.left(n + 1);
//     }
//   }
//   return "";
// }
//
// // ---------------------------------------------------------------------
// // delete a trailing LF
// public static String dtLF(String s)
// {
//   if (s.endsWith('\n')) s.chop(1);
//   return s;
// }
//
// // ---------------------------------------------------------------------
// public static String[] getfilters(String s)
// {
//   int i;
//   String p;
//   QRegExp sep("(\\s|,)");
//   String[] f=s.split(sep);     // SkipEmptyParts
//   for(i=0; i<f.size(); i++) {
//     p=f.at(i);
//     if(!p.contains("*"))
//       f.replace(i,"*."+p);
//   }
//   return f;
// }
//
// ---------------------------------------------------------------------
// integer to String
  public static String i2s(int i)
  {
    String s=Integer.toString(i);
    return s.replace('-','_');
  }

// ---------------------------------------------------------------------
// double to String
  public static String d2s(double d)
  {
    String s=Double.toString(d);
    return s.replace('-','_');
  }

// // ---------------------------------------------------------------------
// public static boolean ifshift()
// {
//   return QApplication::keyboardModifiers().testFlag(Qt::ShiftModifier);
// }
//
// // ---------------------------------------------------------------------
// public static void info(String t,String s)
// {
//   QMessageBox::about(getmbparent(), t, s);
// }
//
// // ---------------------------------------------------------------------
// public static int initialblanks(String t)
// {
//   int r=0;
//   for (; r<t.length(); r++)
//     if (t[r] != ' ') break;
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String intlist2qs(List<int> p)
// {
//   String s("");
//   int n=p.size();
//   for(int i=0; i<n; i++) {
//     if (i>0) s.append(" ");
//     s.append(String::number(p[i]));
//   }
//   return s;
// }
//
// // ---------------------------------------------------------------------
// // is non-empty and all digit
  public static boolean isint(String s)
  {
    int n=(int)s.length();
    if (n==0) return false;
    for(int i=0; i<n; i++)
      if(!Character.isDigit(s.charAt(i))) return false;
    return true;
  }
//
// ---------------------------------------------------------------------
  public static boolean isroot(String s)
  {
    return s.length()>0 && s.charAt(0) == '/';
  }
//
// // ---------------------------------------------------------------------
// public static boolean isutf8(byte[] b)
// {
//   return b.equals(String::fromUtf8(b).toUtf8());
// }
//
// // ---------------------------------------------------------------------
// // match smaller vs larger
// public static boolean matchhead(String s, String t)
// {
//   if (s.size() > t.size()) return false;
//   return s.equals(t.left(s.size()));
// }
//
// // ---------------------------------------------------------------------
// // match foldername vs path
// public static boolean matchfolder(String s, String t)
// {
//   if (s.size()>t.size()) return false;
//   if (s.size()==t.size()) return s==t;
//   return (s+"/")==t.left(s.size() + 1);
// }
//
// // ---------------------------------------------------------------------
// //void f(String s)
// //{
// //cout << s << " " << matchparens('j',s) << endl;
// //}
// //f ("[{(OK)}]");
// //f ("[(missing opening brace)}]");
// //f ("[{(missing trailing bracket)}");
// //f ("[{(order mixed up)]}");
// /*
//  *  matchparens
//  *  modes: jkqrs
//  *   0  matches
//  *   1  too few closing - more to come
//  *   2  mismatched parens
//  *   3  mismatched quotes
// */
//
// // !!! need remove quoted and comments first...
// public static int matchparens(QChar mode, String p)
// {
//   String s=q2s(p);
//   char c;
//   int n, len=(int)s.size();
//   String t="";
//   for(int i=0; i<len; i++) {
//     if(s[i] == '(' || s[i] == '[' || s[i] == '{')
//       t.append(s[i]);
//     else if(s[i] == ')' || s[i] == ']' || s[i] == '}') {
//       n=(int)t.size()-1;
//       if(n<0)
//         return 2;
//       else {
//         c=t.at(n);
//         t.resize(n);
//         if (!((s[i] == ')' && c == '(') || (s[i] == '}' && c == '{')
//               || (s[i] == ']' && c == '[')))
//           return 1;
//       }
//     }
//   }
//   return (t.size()==0) ? 0 : 2;
// }
//
// // ---------------------------------------------------------------------
// public static int modpy(int p, int y)
// {
//   return (p+y)%p;
// }
//
// // ---------------------------------------------------------------------
  public static void noevents(int n)
  {
    NoEvents=Math.max(0,NoEvents + ((n==0) ? -1 : 1));
  }

// // ---------------------------------------------------------------------
// public static void notyet(String s)
// {
//   info("Not Yet",s);
// }
//
// ---------------------------------------------------------------------
  public static String p2q(int[] n)
  {
    StringBuilder s=new StringBuilder();
    s.append(new String(Integer.toString(n[0])));
    for(int i=1; i<4; i++)
      s.append(" " + new String(Integer.toString(n[i])));
    return s.toString();
  }

// // ---------------------------------------------------------------------
// // 0=cancel, 1=no, 2=yes
// public static int queryCNY(String t,String s)
// {
//   int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::Cancel|QMessageBox::No|QMessageBox::Yes,QMessageBox::Yes);
//
//   return (r==QMessageBox::Cancel) ? 0 : ((r==QMessageBox::No) ? 1 : 2);
// }
//
// // ---------------------------------------------------------------------
// public static boolean queryNY(String t,String s)
// {
//   int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::No|QMessageBox::Yes,QMessageBox::Yes);
//   return r==QMessageBox::Yes;
// }
//
// // ---------------------------------------------------------------------
// public static boolean queryOK(String t,String s)
// {
//   int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::Cancel|QMessageBox::Ok,QMessageBox::Ok);
//   return r==QMessageBox::Ok;
// }
//
// // ---------------------------------------------------------------------
// public static boolean queryRETRY(String t,String s)
// {
//   int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::Cancel|QMessageBox::Retry,QMessageBox::Retry);
//   return r==QMessageBox::Retry;
// }
//
// // ---------------------------------------------------------------------
// public static List<int> qs2intlist(String c)
// {
//   String[] s=c.split(' ');     // SkipEmptyParts
//   return qsl2intlist(s);
// }
//
// // ---------------------------------------------------------------------
// public static List<int> qsl2intlist(String[] s)
// {
//   List<int> r;
//   for (int i=0; i<s.size(); i++)
//     r.append(s.at(i).toInt());
//   return r;
// }
//
// ---------------------------------------------------------------------
// if s has only characters in t
  public static boolean qshasonly(String s, String t)
  {
    for (int i=0; i<s.length(); i++)
      if (-1!=t.indexOf(s.charAt(i))) return false;
    return true;
  }
//
// // ---------------------------------------------------------------------
// public static QVector<int> qs2intvector(String c)
// {
//   String[] s=c.split(' ');     // SkipEmptyParts
//   QVector<int> r(s.size());
//   for (int i=0; i<s.size(); i++)
//     r[i]=s.at(i).toInt();
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String qstaketo(String s,String c)
// {
//   int n=s.indexOf(c);
//   if (n<0) return s;
//   return s.left(n);
// }
//
// // ---------------------------------------------------------------------
// public static String[] qsldtbeach(String[] s)
// {
//   String[] r;
//   for(int i=0; i<s.size(); i++)
//     r.append(dtb(s.at(i)));
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qsldropeach(int n,String[] s)
// {
//   String[] r;
//   for(int i=0; i<s.size(); i++)
//     r.append(s.at(i).mid(n));
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qslexists(String[] s)
// {
//   String[] r;
//   for (String f,s)
//     if (cfexist(f))
//       r.append(f);
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qslfcase(String[] s)
// {
//   String[] r;
//   for(int i=0; i<s.size(); i++)
//     r.append(cfcase(s.at(i)));
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qslprependeach(String p,String[] s)
// {
//   String[] r;
//   for(int i=0; i<s.size(); i++)
//     r.append(p+s.at(i));
//   return r;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qslreverse(String[] s)
// {
//   int i,n=s.size();
//   for(i=0; i<n/2; i++) s.swap(i,n-1-i);
//   return s;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qsltrim(String[] p)
// {
//   while (p.size()) {
//     if (p.first().trimmed().size()) break;
//     p.removeFirst();
//   }
//   while (p.size()) {
//     if (p.last().trimmed().size()) break;
//     p.removeLast();
//   }
//   return p;
// }
//
// // ---------------------------------------------------------------------
// public static String[] qsltrimeach(String[] s)
// {
//   String[] r;
//   for(int i=0; i<s.size(); i++)
//     r.append(s.at(i).simplified());
//   return r;
// }
//
// ---------------------------------------------------------------------
// dyadic -. for String[]
  public static String[] qsless(String[] a,String[] w)
  {
    ArrayList<String> a1=new ArrayList<String>();
    for(String s : a) {
      if (!sacontains(w,s)) a1.add(s);
    }
    return a1.toArray(new String[a1.size()]);
  }

// ---------------------------------------------------------------------
  public static String sajoinstr(String[] a,String b)
  {
    StringBuilder r=new StringBuilder();
    for (String s : a) {
      r.append(b);
      r.append(s);
    }
    String t=r.toString();
    if (t.length()>b.length())
      return t.substring(b.length());
    else
      return "";
  }

// ---------------------------------------------------------------------
// dyadic -. for String[]
  public static String[] sajoin(String[] a,String[] b)
  {
    int aLen = a.length;
    int bLen = b.length;
    String[] c= new String[aLen+bLen];
    System.arraycopy(a, 0, c, 0, aLen);
    System.arraycopy(b, 0, c, aLen, bLen);
    return c;
  }

// ---------------------------------------------------------------------
// return true if all items are numbers
  public static boolean qsnumeric(String[] a)
  {
    for(String s : a)
      if (!isNumber(s)) return false;
    return true;
  }

// ---------------------------------------------------------------------
  public static String strless(String a,String w)
  {
    StringBuilder r=new StringBuilder();
    for (int i=0; i<a.length(); i++) {
      if (-1 == w.indexOf(a.charAt(i))) r.append(a.charAt(i));
    }
    return r.toString();
  }

// // ---------------------------------------------------------------------
// public static List<int> q2p(String s)
// {
//   String[] t = s.split(" ");     // SkipEmptyParts
//   List<int> r;
//   for (int i=0; i<4; i++)
//     r.append(t.at(i).toInt());
//   return r;
// }
//
// ---------------------------------------------------------------------
  public static String q2s(String s)
  {
    return s;
  }

// // ---------------------------------------------------------------------
// public static String remsep(String s)
// {
//   if (s.endsWith("/"))
//     s=s.left(s.size()-1);
//   return s;
// }
//
// // ---------------------------------------------------------------------
// public static String remtilde(String s)
// {
//   if (s.startsWith("~"))
//     s=s.mid(1);
//   return s;
// }
//
// ---------------------------------------------------------------------
  public static String s2q(String s)
  {
    return s;
  }

// ---------------------------------------------------------------------
// pair Strings with zero delimeter
  public static byte[] spair(String s,String t)
  {
    ByteArrayOutputStream r=new ByteArrayOutputStream();
    try {
      r.write(s.getBytes(Charset.forName("UTF-8")));
      r.write('\0');
      r.write(t.getBytes(Charset.forName("UTF-8")));
      r.write('\0');
    } catch (IOException exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    } catch (Exception exc) {
      Log.d(JConsoleApp.LogTag,Log.getStackTraceString(exc));
    }
    return r.toByteArray();
  }

// ---------------------------------------------------------------------
  public static String termLF(String s)
  {
    if (s.isEmpty()||s.endsWith("\n")) return s;
    return s + "\n";
  }

// ---------------------------------------------------------------------
  public static String termsep(String s)
  {
    if (s.isEmpty()||s.endsWith("/")) return s;
    return s + "/";
  }

// ---------------------------------------------------------------------
  public static String toList(String[] s)
  {
    int n=s.length;
    if (n == 0) return "";
    StringBuilder r=new StringBuilder();
    r.append("(\"" + s[0] + "\"");
    for (int i=1; i<n; i++)
      r.append(";\"" + s[i] + "\"");
    r.append(")");
    return r.toString();
  }

// // ---------------------------------------------------------------------
// // trim trailing whitespace (for TrimTrailingWS)
// // trims WS on each line, and trim extra trailing LFs
// public static String trimtws(String s)
// {
//   String[] r=s.split('\n');
//   for (int i=0; i<r.size(); i++)
//     r[i]=dtb(r.at(i));
//   while ((r.size()>1) && (r.at(r.size()-1).isEmpty()) && (r.at(r.size()-2).isEmpty()))
//     r.removeLast();
//   return r.join("\n");
// }
//
// ---------------------------------------------------------------------
  public static int c_strtoi(String s)
  {
    if (!isInteger(s)) return 0;
    return Integer.valueOf(s.replace('_','-'));
  }

// ---------------------------------------------------------------------
  public static long c_strtol(String s)
  {
    if (!isInteger(s)) return 0;
    return Long.valueOf(s.replace('_','-')).longValue();
  }

// ---------------------------------------------------------------------
  public static double c_strtod(String s)
  {
    if (!isNumber(s)) return 0;
    return Double.parseDouble(s.replace('_','-'));
  }

// ---------------------------------------------------------------------
  public static String remquotes(String s)
  {
    int len=(int)s.length();
    if (len<2) return s;
    if ((s.charAt(0)=='"' && s.charAt(len-1)=='"')||(s.charAt(0)=='\177' && s.charAt(len-1)=='\177'))
      s=s.substring(1,1+len-2);
    return s;
  }

// ---------------------------------------------------------------------
  public static boolean isInteger(String s)
  {
    if (0==s.length()) return false;
    if (s.charAt(0)=='-' || s.charAt(0)=='_') s=s.substring(1);
    if (0==s.length()) return false;
    return android.text.TextUtils.isDigitsOnly(s);
  }

// ---------------------------------------------------------------------
  public static boolean isNumber(String s)
  {
    if (0==s.length()) return false;
    if (s.charAt(0)=='-' || s.charAt(0)=='_') s=s.substring(1);
    if (0==s.length()) return false;
    int counter = 0;
    for( int i=0; i<s.length(); i++ ) {
      if( s.charAt(i) == '.' ) {
        counter++;
      }
    }
    if (counter>1) return false;
    return android.text.TextUtils.isDigitsOnly(s.replace('.','0'));
  }

// ---------------------------------------------------------------------
  public static String int2String ( int[] buf , int pos, int len)
  {
    byte[] b=new byte[len];
    for (int i=0; i<len; i++) b[i]=(byte)buf[pos+i];
    return (new String(b, Charset.forName("UTF-8"))).trim();
  }

// ---------------------------------------------------------------------
  @SuppressWarnings( "deprecation" )
  public static void clipcopy(Context ctx, String text)
  {
    int sdk = android.os.Build.VERSION.SDK_INT;
    if(sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
      android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
      clipboard.setText(text);
    } else {
      android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
      android.content.ClipData clip = android.content.ClipData.newPlainText("j text",text);
      clipboard.setPrimaryClip(clip);
    }
  }

// ---------------------------------------------------------------------
  @SuppressWarnings( "deprecation" )
  public static String clipread(Context ctx)
  {
    String res="";
    int sdk = android.os.Build.VERSION.SDK_INT;
    if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
      android.text.ClipboardManager clipboard = (android.text.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
      if (clipboard.getText() != null) {
        res=clipboard.getText().toString();
      }
    } else {
      android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
      android.content.ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
      if (item.getText() != null) {
        res=item.getText().toString();
      }
    }
    return res;
  }

}
