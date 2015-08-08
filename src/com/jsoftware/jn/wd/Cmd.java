package com.jsoftware.jn.wd;

import android.util.Log;
import com.jsoftware.jn.base.Util;
import java.util.ArrayList;
import java.util.List;

public class Cmd
{
  int bgn;
  int len;
  int pos;
  int pos0;
  String str;

  final static char DEL='\177';
  final static char LF='\n';
  final static char SOH='\001';
  final static String WS=" \f\r\t";
  final static String WSLF=WS+LF;

  static int find_first_not_of(String str, String searchChars, int pos)
  {
    if (-1==pos) return -1;
    if (isEmpty(str) || isEmpty(searchChars)) {
      return -1;
    }
    for (int i = pos; i < str.length(); i++) {
      if (searchChars.indexOf(str.charAt(i)) == -1) {
        return i;
      }
    }
    return -1;
  }

  static boolean isEmpty(String array)
  {
    if (array == null || array.isEmpty()) {
      return true;
    }
    return false;
  }

// ---------------------------------------------------------------------
  public static String remquotes(String s)
  {
    int len=s.length();
    if (len<2) return s;
    if ((s.charAt(0)=='"' && s.charAt(len-1)=='"')||(s.charAt(0)=='\177' && s.charAt(len-1)=='\177'))
      s=s.substring(1,1+len-2);
    return s;
  }

// ---------------------------------------------------------------------
  public void end()
  {
    pos=str.length();
  }

// ---------------------------------------------------------------------
  public void init(String s)
  {
    str=s;
    str=toLF(str);
    len=str.length();
    bgn=pos=pos0=0;
  }

// ---------------------------------------------------------------------
// split on g h l m p u v s z and remove blanks
  public String[] bsplits()
  {
    List<String> r=new ArrayList<String>();
    str=remws(str);
    len=str.length();
    while (pos<len) {
      bgn=pos++;
      pos=Util.find_first_of(str,Wd.bintype,pos);
      if (pos==-1) {
        r.add(str.substring(bgn));
        break;
      } else
        r.add(str.substring(bgn,bgn+pos-bgn));
    }
    return r.toArray(new String[r.size()]);
  }

// ---------------------------------------------------------------------
// if String delimited by LF
  public boolean delimLF(String s)
  {
    char c;
    int n=s.length();
    for (int i=0; i<n; i++) {
      c=s.charAt(i);
      if (c==LF) return true;
      if (c=='*' || c==SOH) return false;
      if (c=='"' || c==DEL)
        while (++i<s.length() && s.charAt(i)!=c);
    }
    return false;
  }

// ---------------------------------------------------------------------
  public String getid()
  {
    char c;
    skips(WSLF+';');
    bgn=pos;
    while (pos<len) {
      c=str.charAt(pos);
      if (contains(WSLF,c) || c==';') break;
      pos++;
    }
    return remquotes(str.substring(bgn,bgn+pos-bgn));
  }

// ---------------------------------------------------------------------
// get to next LF
  public String getline()
  {
    String r;
    if (pos==len) return "";
    if (str.charAt(pos)==LF) pos++;
    if (pos==len) return "";
    bgn=pos;
    pos=str.indexOf(LF,pos);
    if (pos==-1)
      pos=str.length();
    else
      pos++;
    return str.substring(bgn,bgn+pos-bgn-1);
  }
  public String getparms()
  {
    return getparms(false);
  }
// ---------------------------------------------------------------------
// to next ; else return rest of String
// if star, preserve leading *
  public String getparms(boolean star)
  {
    char c;

    if (pos==len)
      return "";

    if (str.charAt(pos)==';') {
      pos++;
      return "";
    }

    skips(WSLF);
    if (pos==len)
      return "";
    if (str.charAt(pos)=='*') {
      String r=str.substring(pos+(star?0:1));
      pos=len;
      return r;
    }
    bgn=pos;
    while (pos<len) {
      c=str.charAt(pos);
      if (c==';') break;
      pos++;
      if (c=='"' || c==DEL) {
        skippast(c);
        continue;
      }
    }
    return str.substring(bgn,bgn+pos-bgn);
  }

// ---------------------------------------------------------------------
  public boolean more()
  {
    return pos<len;
  }

// ---------------------------------------------------------------------
// split parameters
// if has SOH, split on SOH
// if has LF not in paired delimiters, split on LF
// otherwise split on WS, or paired "" or DEL
  public String[] qsplits()
  {
    skips(WS);
    if (contains(str,SOH))
      return qsplitby(str,SOH);
    if (delimLF(str))
      return qsplitby(str,LF);
    char c;
    List<String> r=new ArrayList<String>();
    while (pos<len) {
      skips(WS);
      bgn=pos;
      c=str.charAt(pos++);
      if (c=='*') {
        r.add(str.substring(pos));
        break;
      }
      if (c=='"' || c==DEL) {
        skippast(c);
        r.add(str.substring(bgn+1,bgn+1+pos-bgn-2));
      } else {
        skiptows();
        r.add(str.substring(bgn,bgn+pos-bgn));
        if (pos<len && str.charAt(pos)==LF)
          pos++;
      }
    }
    return Util.qsless(r.toArray(new String[r.size()]),new String[] {""});
  }

// ---------------------------------------------------------------------
// split as qsplits, but returning vector of String
  public String[] ssplits()
  {
    skips(WS);
    if (contains(str,SOH))
      return ssplitby(str,SOH);
    if (delimLF(str))
      return ssplitby(str,LF);
    char c;
    List<String> r=new ArrayList<String>();
    while (pos<len) {
      skips(WS);
      bgn=pos;
      c=str.charAt(pos++);
      if (c=='*') {
        r.add(str.substring(pos));
        break;
      }
      if (c=='"' || c==DEL) {
        skippast(c);
        r.add(str.substring(bgn+1,bgn+1+pos-bgn-2));
      } else {
        skiptows();
        r.add(str.substring(bgn,bgn+pos-bgn));
        if (pos<len && str.charAt(pos)==LF)
          pos++;
      }
    }
    return r.toArray(new String[r.size()]);
  }

// ---------------------------------------------------------------------
  public String remws(String s)
  {
    StringBuilder r = new StringBuilder();
    for (int i=0; i<s.length(); i++)
      if (!contains(WSLF,s.charAt(i)))
        r.append(s.charAt(i));
    return r.toString();
  }

// ---------------------------------------------------------------------
// start after the delimiter, move past the closing delimiter
  void skippast(char c)
  {
    while (len>pos)
      if (str.charAt(pos++)==c) return;
  }

// ---------------------------------------------------------------------
  void skips(String s)
  {
    pos=find_first_not_of(str,s,pos);
    if (pos==-1) pos=len;
  }

// ---------------------------------------------------------------------
// move to next whitespace
  void skiptows()
  {
    while (len>pos) {
      if (contains(WS,str.charAt(pos))) return;
      pos++;
    }
  }

// ---------------------------------------------------------------------
// split on bin commands and remove blanks
  public static String[] bsplit(String s)
  {
    Cmd c=new Cmd();
    c.init(s);
    return c.bsplits();
  }

// ---------------------------------------------------------------------
  static boolean contains(String s,char c)
  {
    return -1 != s.indexOf(c);
  }

  public static String[] qsplit(String s)
  {
    return qsplit(s,false);
  }
// ---------------------------------------------------------------------
// split parameters
// if star and first non WS is *, then return rest as single parameter
  public static String[] qsplit(String s,boolean star)
  {
    if (star) {
      int p=find_first_not_of(s,WS,0);
      if (p!=-1 && s.charAt(p)=='*')
        return new String[] {(s.substring(p+1))};
    }
    Cmd c=new Cmd();
    c.init(s);
    return c.qsplits();
  }

// ---------------------------------------------------------------------
// split on character
  static String[] qsplitby(String s,char c)
  {
    int n=s.length();
    if (n==0)
      return new String[] {};
    if (s.charAt(n-1)==c)
      s=s.substring(0,(n-1)-1);
    return s.split(String.valueOf(c));
  }

// ---------------------------------------------------------------------
// as qsplit but returning vector of String
  public static String[] ssplit(String s)
  {
    Cmd c=new Cmd();
    c.init(s);
    return c.ssplits();
  }

// ---------------------------------------------------------------------
// split on char
  static String[] ssplitby(String s,char c)
  {
    int i,p;
    List<String> r=new ArrayList<String>();
    int n=s.length();
    if (n==0) r.toArray(new String[r.size()]);
    if (s.charAt(n-1)==c)
      s=s.substring(0,(--n)-1);
    for (i=p=0; i<n; i++)
      if (s.charAt(i)==c) {
        r.add(s.substring(p,p+i-p));
        p=i+1;
      }
    if (p==n)
      r.add("");
    else
      r.add(s.substring(p,p+n-p));
    return r.toArray(new String[r.size()]);
  }

// ---------------------------------------------------------------------
// convert CRLF to LF
  static String toLF(String s)
  {
    if (!contains(s,'\r'))
      return s;
    int n=s.length();
    int p=0;
    int t;
    StringBuilder r = new StringBuilder();
    while (-1 != (t=s.indexOf("\r\n",p))) {
      r.append(s.substring(p,p+t-p));
      p=t+1;
    }
    if (p<n)
      r.append(s.substring(p,p+n-p));
    return r.toString();
  }

// ---------------------------------------------------------------------
  public void markpos()
  {
    pos0=pos;
  }

// ---------------------------------------------------------------------
  public void rewindpos()
  {
    pos=pos0;
  }

}
