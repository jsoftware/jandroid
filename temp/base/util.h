#ifndef UTIL_H
#define UTIL_H

class QDateTime;
class QDir;
class QFile;
class String;

boolean cderase(String name);
int cfappend (QFile *, String);
int cfappend (QFile *, QByteArray);
String cfcase(String s);
boolean cfcopy(String from, String to);
boolean cfcreate(String s);
boolean cftouch(String s);
boolean cfdelete(String s);
boolean cfexist(String s);
String cfext(String s);
String[] cflist(String d,String f);
String[] cflist(String path,String[] f);
String[] cflistfull(String b,String f);
String[] cflisttext(String path);
String cfpath(String s);
String cfsname(String);
String cfread(QFile *);
String cfread(String s);
QByteArray cfreadbin(String s);
String[] cfreads(QFile *file);
String[] cfreads(String s);
String[] cfreadx(String s);
boolean cfrmdir(const String &d);
QDateTime cftime(String s);
int cfwrite(QFile *file,String);
int cfwrite(String s, String t);
int cfwrite(String s, QByteArray b);

void about(String t,String s);
String boxj2utf8(std::String s);
String c2q(const char *);
std::String c2s(const char *);
boolean createdir(QDir d);
String detab(String s);
String dlb(String s);
String dtb(String s);
String dtLF(String s);
String[] getfilters(String s);
void helpabout();
std::String Util.i2s(int i);
std::String d2s(double d);
boolean ifshift();
void info(String t,String s);
int initialblanks(String t);
String intlist2qs(List<int>);
boolean isint(std::String s);
boolean isroot(String name);
boolean isutf8(QByteArray b);
boolean matchhead(String, String);
boolean matchfolder(String s, String t);
int matchparens(QChar, String);
int modpy(int p, int y);
void noevents(int n);
void notyet(String);
String p2q(List<int>);
std::String p2s(const void *p);
List<int> q2p(String);
boolean qshasonly(const String s, const String t);
int queryCNY(String t,String s);
boolean queryNY(String t,String s);
boolean queryOK(String t,String s);
boolean queryRETRY(String t,String s);
String[] qsless(String[],String[]);
boolean qsnumeric(String[]);
std::String strless(std::String a,std::String w);
String remsep(String);
String remtilde(String);
std::String spair(std::String s,std::String t);
std::String spair(std::String s,String t);
String termLF(String s);
String termsep(String);
String trimtws(String s);
String toqlist(String[]);

List<int> qs2intlist(String c);
List<int> qsl2intlist(String[] s);
String qstaketo(String s,String c);
QVector<int> qs2intvector(String c);

String[] qsldtbeach(String[] s);
String[] qsldropeach(int n,String[] s);
String[] qslexists(String[] s);
String[] qslfcase(String[] s);
String[] qslprependeach(String p,String[] s);
String[] qslreverse(String[] s);
String[] qsltrim(String[] p);
String[] qsltrimeach(String[] p);

extern int NoEvents;

#if defined(_WIN64)||defined(__LP64__)
typedef long long SI;
#else
typedef long SI;
#endif

int Util.c_strtoi(std::String s);
SI Util.c_strtol(std::String s);
double Util.c_strtod(std::String s);
#ifdef QT_OS_ANDROID
extern float DM_density;
String scrollbarstyle(float n);
String checkboxstyle(float n);
#endif

#endif
