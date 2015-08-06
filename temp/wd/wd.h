#ifndef WD_H
#define WD_H

import com.jsoftware.jn.wd.../base/base;

// for wd only:

extern "C" {
  Dllexport int wd(char *s,int slen,char *&r,int &len);
}

void error(String s);
boolean invalidopt(String n,String[] opt,String valid);
boolean invalidoptn(String n,String[] opt,String valid);
String Util.remquotes(String s);
String mb(String c,String p);
String sm(String c);
void wdactivateform();
int translateqkey(int);
void wdsetfocuspolicy(View widget,String p);
void wdsetsizepolicy(View widget,String p);
void wdsetwh(View widget,String p);
int wdstandardicon(String s);

#endif
