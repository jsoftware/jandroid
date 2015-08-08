
import com.jsortware.jn.base.base;
import com.jsortware.jn.base.term;
import com.jsortware.jn.base.note;


public class Util
{

  static int NoEvents=0;

// ---------------------------------------------------------------------
  public static void about(String t,String s)
  {
    QMessageBox::about(getmbparent(), t, s);
  }

// ---------------------------------------------------------------------
// converts J 16-26 box chars to utf8
  public static String boxj2utf8(String s)
  {
    QByteArray b(s.Util.c_str());
    b.replace('\20',"\342\224\214");
    b.replace('\21',"\342\224\254");
    b.replace('\22',"\342\224\220");
    b.replace('\23',"\342\224\234");
    b.replace('\24',"\342\224\274");
    b.replace('\25',"\342\224\244");
    b.replace('\26',"\342\224\224");
    b.replace('\27',"\342\224\264");
    b.replace('\30',"\342\224\230");
    b.replace('\31',"\342\224\202");
    b.replace('\32',"\342\224\200");
    return String::fromUtf8(b);
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
    return new String(c.toByteArray(Charset.forName("UTF-8")))
  }

// ---------------------------------------------------------------------
  public static byte[] s2ba(String s)
  {
    return s.getByte(Charset.forName("UTF-8"));
  }

// ---------------------------------------------------------------------
  public static boolean cderase(String name)
  {
    boolean r;
    QDir dir(name);
    if (!dir.exists(name)) return true;
    Q_FOREACH(QFileInfo info,dir.entryInfoList(
                QDir::NoDotAndDotDot | QDir::System | QDir::Hidden
                | QDir::AllDirs | QDir::Files, QDir::DirsFirst)) {
      if (info.isDir())
        r = cderase(info.absoluteFilePath());
      else
        r = QFile::remove(info.absoluteFilePath());
      if (!r) return r;
    }
    return dir.rmdir(name);
  }

// ---------------------------------------------------------------------
  public static int cfappend (QFile *file, String s)
  {
    if (!file.open(QIODevice::Append | QIODevice::Text))
      return 0;
    QTextStream out(file);
    out << s;
    file.close();
    return s.length();
  }

// ---------------------------------------------------------------------
  public static int cfappend (QFile *file, QByteArray b)
  {
    if (!file.open(QIODevice::Append))
      return 0;
    int r=file.write(b);
    file.close();
    return r;
  }

// ---------------------------------------------------------------------
  public static String cfcase(String s)
  {
    return s;
  }

// ---------------------------------------------------------------------
  public static boolean cfcopy(String from, String to)
  {
    QDir d;
    if (!d.mkpath(cfpath(to))) return false;
    QFile f(from);
    QFile t(to);
    t.remove();
    return f.copy(to);
  }

// ---------------------------------------------------------------------
  public static boolean cfcreate(String s)
  {
    QFile f(s);
    String p;
    cfwrite(&f,p);
    return f.exists();
  }

// ---------------------------------------------------------------------
  public static boolean cftouch(String s)
  {
    QFile f(s);
    f.open(QIODevice::Append);
    return f.exists();
  }

// ---------------------------------------------------------------------
  public static boolean cfdelete(String s)
  {
    QFile f(s);
    return f.remove();
  }

// ---------------------------------------------------------------------
  public static boolean cfexist(String s)
  {
    QFile f(s);
    return f.exists();
  }

// ---------------------------------------------------------------------
  public static String cfext(String s)
  {
    int n=s.lastIndexOf('.');
    if (n<0) return "";
    return s.mid(n);
  }

// ---------------------------------------------------------------------
// file list
  public static String[] cflist(String path,String filters)
  {
    QDir d(path);
    String[] f=getfilters(filters);
    return d.entryList(f,QDir::Files|QDir::Readable,QDir::Name);
  }

// ---------------------------------------------------------------------
// prepend path
  public static String[] cflistfull(String b,String filters)
  {
    String[] r=cflist(b,filters);
    String t=b+"/";
    for(int i=0; i<r.length(); i++)
      r.replace(i,t+r[i]);
    return r;
  }

// ---------------------------------------------------------------------
// list text files
// is utf8 and size < 1e6
  public static String[] cflisttext(String path)
  {
    QByteArray b;
    String[] p=cflistfull(path,"");
    QFile f;
    String[] r;
    foreach (String s,p) {
      f.setFileName(s);
      if (f.length() < 1e6 && f.open(QIODevice::ReadOnly)) {
        if(isutf8(f.readAll()))
          r.append(s);
        f.close();
      }
    }
    return r;
  }

// ---------------------------------------------------------------------
// get path from filename
// same as parent for a directory
  public static String cfpath(String s)
  {
    int n = s.lastIndexOf('/');
    if (n < 1) return "";
    return s.left(n);
  }

// ---------------------------------------------------------------------
  public static String cfread(QFile *file)
  {
    if (!file.open(QIODevice::ReadOnly|QIODevice::Text))
      return "";
    QTextStream in(file);
    in.setCodec("UTF-8");
    String r = in.readAll();
    file.close();
    return r;
  }

// ---------------------------------------------------------------------
  public static String cfread(String s)
  {
    QFile f(s);
    return cfread(&f);
  }

// ---------------------------------------------------------------------
  public static QByteArray cfreadbin(String s)
  {
    QByteArray r;
    QFile f(s);
    if (f.open(QIODevice::ReadOnly)) {
      r = f.readAll();
      f.close();
    }
    return r;
  }

// ---------------------------------------------------------------------
  public static String[] cfreads(QFile *file)
  {
    int i;
    String[] r;
    String s;
    s=cfread(file);
    if (s.isEmpty()) return r;
    s = s.remove('\r').replace('\t',' ');
    for (i=s.length(); i>0; i--)
      if (s[i-1] != '\n') break;
    return s.left(i).split('\n');
  }

// ---------------------------------------------------------------------
  public static String[] cfreads(String s)
  {
    QFile f(s);
    return cfreads(&f);
  }

// ---------------------------------------------------------------------
  public static String[] cfreadx(String s)
  {
    String[] r=cfreads(s);
    String t;
    r.removeDuplicates();
    for(int i=r.length()-1; i>=0; i--) {
      t=r[i];
      if (t.isEmpty() || t[0]=='#' || t.mid(0,3).equals("NB."))
        r.removeAt(i);
    }
    return r;
  }

// ---------------------------------------------------------------------
// remove directory (may be non-empty)
// when used should first check OK to remove directory
  public static boolean cfrmdir(const String & d)
  {
    boolean r = true;
    QDir dir(d);

    if (dir.exists(d)) {
      Q_FOREACH(QFileInfo info, dir.entryInfoList(QDir::NoDotAndDotDot | QDir::System | QDir::Hidden  | QDir::AllDirs | QDir::Files, QDir::DirsFirst)) {
        if (info.isDir())
          r = cfrmdir(info.absoluteFilePath());
        else
          r = QFile::remove(info.absoluteFilePath());
        if (!r)
          return r;
      }
      r = dir.rmdir(d);
    }
    return r;
  }

// ---------------------------------------------------------------------
// get short name from filename
  public static String cfsname(String s)
  {
    int n = s.lastIndexOf('/');
    if (n < 1) return s;
    return s.mid(n+1);
  }

// ---------------------------------------------------------------------
  public static QDateTime cftime(String s)
  {
    QFileInfo f(s);
    return f.lastModified();
  }

// ---------------------------------------------------------------------
  public static int cfwrite(QFile *file, String t)
  {
    if (!file.open(QIODevice::WriteOnly|QIODevice::Text))
      return 0;
    QTextStream out(file);
    out.setCodec("UTF-8");
    out << t;
    file.close();
    return t.length();
  }

// ---------------------------------------------------------------------
  public static int cfwrite(String s, String t)
  {
    QFile f(s);
    return cfwrite(&f,t);
  }

// ---------------------------------------------------------------------
  public static int cfwrite(String s, QByteArray b)
  {
    QFile f(s);
    if (!f.open(QIODevice::WriteOnly))
      return 0;
    int r=f.write(b);
    f.close();
    return r;
  }

// ---------------------------------------------------------------------
  public static boolean createdir(QDir d)
  {
    if (d.exists()) return true;
    return  d.mkpath(".");
  }

// ---------------------------------------------------------------------
  public static String detab(String s)
  {
    return s.replace('\t',' ');
  }

// ---------------------------------------------------------------------
  public static String dlb(String s)
  {
    for (int n=0; n<s.length(); n++) {
      if (!s[n].isSpace()) {
        return s.mid(n);
      }
    }
    return "";
  }

// ---------------------------------------------------------------------
  public static String dtb(String s)
  {
    for (int n = s.length()-1; n>=0; n--) {
      if (!s[n].isSpace()) {
        return s.left(n + 1);
      }
    }
    return "";
  }

// ---------------------------------------------------------------------
// delete a trailing LF
  public static String dtLF(String s)
  {
    if (s.endsWith('\n')) s.chop(1);
    return s;
  }

// ---------------------------------------------------------------------
  public static String[] getfilters(String s)
  {
    int i;
    String p;
    QRegExp sep("(\\s|,)");
    String[] f=s.split(sep);     // SkipEmptyParts
    for(i=0; i<f.length(); i++) {
      p=f[i];
      if(!p.contains("*"))
        f.replace(i,"*."+p);
    }
    return f;
  }

// ---------------------------------------------------------------------
// integer to String
  public static String Util.i2s(int i)
  {
    Stringstream s;
    s << i;
    String s1=s.str();
    if (s1[0]=='-') s1[0]='_';
    return s1;
  }

// ---------------------------------------------------------------------
// double to String
  public static String d2s(double d)
  {
    Stringstream s;
    s.precision(16);
    s << d;
    String s1=s.str();
    if (s1[0]=='-') s1[0]='_';
    return s1;
  }

// ---------------------------------------------------------------------
  public static boolean ifshift()
  {
    return QApplication::keyboardModifiers().testFlag(Qt::ShiftModifier);
  }

// ---------------------------------------------------------------------
  public static void info(String t,String s)
  {
    QMessageBox::about(getmbparent(), t, s);
  }

// ---------------------------------------------------------------------
  public static int initialblanks(String t)
  {
    int r=0;
    for (; r<t.length(); r++)
      if (t[r] != ' ') break;
    return r;
  }

// ---------------------------------------------------------------------
  public static String intlist2qs(List<int> p)
  {
    String s("");
    int n=p.length();
    for(int i=0; i<n; i++) {
      if (i>0) s.append(" ");
      s.append(String::number(p[i]));
    }
    return s;
  }

// ---------------------------------------------------------------------
// is non-empty and all digit
  public static boolean isint(const String s)
  {
    int n=(int)s.length();
    if (n==0) return false;
    for(int i=0; i<n; i++)
      if(!isdigit(s[i])) return false;
    return true;
  }

// ---------------------------------------------------------------------
  public static boolean isroot(String s)
  {
    return s.length()>0 && s[0] == '/';
  }

// ---------------------------------------------------------------------
  public static boolean isutf8(QByteArray b)
  {
    return b==String::fromUtf8(b).toUtf8();
  }

// ---------------------------------------------------------------------
// match smaller vs larger
  public static boolean matchhead(String s, String t)
  {
    if (s.length() > t.length()) return false;
    return s == t.left(s.length());
  }

// ---------------------------------------------------------------------
// match foldername vs path
  public static boolean matchfolder(String s, String t)
  {
    if (s.length()>t.length()) return false;
    if (s.length()==t.length()) return s==t;
    return (s+"/")==t.left(s.length() + 1);
  }

// ---------------------------------------------------------------------
//void f(String s)
//{
//cout << s << " " << matchparens('j',s) << endl;
//}
//f ("[{(OK)}]");
//f ("[(missing opening brace)}]");
//f ("[{(missing trailing bracket)}");
//f ("[{(order mixed up)]}");
  /*
   *  matchparens
   *  modes: jkqrs
   *   0  matches
   *   1  too few closing - more to come
   *   2  mismatched parens
   *   3  mismatched quotes
  */

// !!! need remove quoted and comments first...
  public static int matchparens(QChar mode, String p)
  {
    Q_UNUSED(mode);
    String s=p;
    char c;
    int n, len=(int)s.length();
    String t="";
    for(int i=0; i<len; i++) {
      if(s[i] == '(' || s[i] == '[' || s[i] == '{')
        t.append(s[i]);
      else if(s[i] == ')' || s[i] == ']' || s[i] == '}') {
        n=(int)t.length()-1;
        if(n<0)
          return 2;
        else {
          c=t[n];
          t.resize(n);
          if (!((s[i] == ')' && c == '(') || (s[i] == '}' && c == '{')
                || (s[i] == ']' && c == '[')))
            return 1;
        }
      }
    }
    return (t.length()==0) ? 0 : 2;
  }

// ---------------------------------------------------------------------
  public static int modpy(int p, int y)
  {
    return (p+y)%p;
  }

// ---------------------------------------------------------------------
  public static void noevents(int n)
  {
    NoEvents=qMax(0,NoEvents + ((n==0) ? -1 : 1));
  }

// ---------------------------------------------------------------------
  public static void notyet(String s)
  {
    info("Not Yet",s);
  }

// ---------------------------------------------------------------------
  public static String p2q(List<int> n)
  {
    String s("");
    s.append(String("%1").arg(n[0]));
    for(int i=1; i<4; i++)
      s.append(" " + String("%1").arg(n[i]));
    return s;
  }

// ---------------------------------------------------------------------
// pointer to String
  public static String p2s(const void *p)
  {
    Stringstream s;
    s << (SI) p;
    String s1=s.str();
    if (s1[0]=='-') s1[0]='_';
    return s1;
  }

// ---------------------------------------------------------------------
// 0=cancel, 1=no, 2=yes
  public static int queryCNY(String t,String s)
  {
    int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::Cancel|QMessageBox::No|QMessageBox::Yes,QMessageBox::Yes);

    return (r==QMessageBox::Cancel) ? 0 : ((r==QMessageBox::No) ? 1 : 2);
  }

// ---------------------------------------------------------------------
  public static boolean queryNY(String t,String s)
  {
    int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::No|QMessageBox::Yes,QMessageBox::Yes);
    return r==QMessageBox::Yes;
  }

// ---------------------------------------------------------------------
  public static boolean queryOK(String t,String s)
  {
    int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::Cancel|QMessageBox::Ok,QMessageBox::Ok);
    return r==QMessageBox::Ok;
  }

// ---------------------------------------------------------------------
  public static boolean queryRETRY(String t,String s)
  {
    int r=QMessageBox::question(getmbparent(),t,s,QMessageBox::Cancel|QMessageBox::Retry,QMessageBox::Retry);
    return r==QMessageBox::Retry;
  }

// ---------------------------------------------------------------------
  public static List<int> qs2intlist(String c)
  {
    String[] s=c.split(' ');     // SkipEmptyParts
    return qsl2intlist(s);
  }

// ---------------------------------------------------------------------
  public static List<int> qsl2intlist(String[] s)
  {
    List<int> r;
    for (int i=0; i<s.length(); i++)
      r.append(s[i].toInt());
    return r;
  }

// ---------------------------------------------------------------------
// if s has only characters in t
  public static boolean qshasonly(const String s, const String t)
  {
    for (int i=0; i<s.length(); i++)
      if (!t.contains(s[i])) return false;
    return true;
  }

// ---------------------------------------------------------------------
  public static QVector<int> qs2intvector(String c)
  {
    String[] s=c.split(' ');     // SkipEmptyParts
    QVector<int> r(s.length());
    for (int i=0; i<s.length(); i++)
      r[i]=s[i].toInt();
    return r;
  }

// ---------------------------------------------------------------------
  public static String qstaketo(String s,String c)
  {
    int n=s.indexOf(c);
    if (n<0) return s;
    return s.left(n);
  }

// ---------------------------------------------------------------------
  public static String[] qsldtbeach(String[] s)
  {
    String[] r;
    for(int i=0; i<s.length(); i++)
      r.append(dtb(s[i]));
    return r;
  }

// ---------------------------------------------------------------------
  public static String[] qsldropeach(int n,String[] s)
  {
    String[] r;
    for(int i=0; i<s.length(); i++)
      r.append(s[i].mid(n));
    return r;
  }

// ---------------------------------------------------------------------
  public static String[] qslexists(String[] s)
  {
    String[] r;
    foreach (String f,s)
      if (cfexist(f))
        r.append(f);
    return r;
  }

// ---------------------------------------------------------------------
  public static String[] qslfcase(String[] s)
  {
    String[] r;
    for(int i=0; i<s.length(); i++)
      r.append(cfcase(s[i]));
    return r;
  }

// ---------------------------------------------------------------------
  public static String[] qslprependeach(String p,String[] s)
  {
    String[] r;
    for(int i=0; i<s.length(); i++)
      r.append(p+s[i]);
    return r;
  }

// ---------------------------------------------------------------------
  public static String[] qslreverse(String[] s)
  {
    int i,n=s.length();
    for(i=0; i<n/2; i++) s.swap(i,n-1-i);
    return s;
  }

// ---------------------------------------------------------------------
  public static String[] qsltrim(String[] p)
  {
    while (p.length()) {
      if (p.first().trimmed().length()) break;
      p.removeFirst();
    }
    while (p.length()) {
      if (p.last().trimmed().length()) break;
      p.removeLast();
    }
    return p;
  }

// ---------------------------------------------------------------------
  public static String[] qsltrimeach(String[] s)
  {
    String[] r;
    for(int i=0; i<s.length(); i++)
      r.append(s[i].simplified());
    return r;
  }

// ---------------------------------------------------------------------
// dyadic -. for String[]
  public static String[] qsless(String[] a,String[] w)
  {
    foreach(String s, w)
      if (a.contains(s)) a.removeAll(s);
    return a;
  }

// ---------------------------------------------------------------------
// return true if all items are numbers
  public static boolean qsnumeric(String[] a)
  {
    foreach(String s,a)
      if (s.length() && s[0]!='_' && s[0]!='-' && s[0]!='0' && s[0]!='1' && s[0]!='2' && s[0]!='3' && s[0]!='4' && s[0]!='5' && s[0]!='6' && s[0]!='7' && s[0]!='8' && s[0]!='9') return false;
    return true;
  }

// ---------------------------------------------------------------------
  public static String strless(String a,String w)
  {
    String r="";
    for (size_t i=0; i<a.length(); i++) {
      if (String::npos == w.find_first_of(a[i])) r+=a[i];
    }
    return r;
  }

// ---------------------------------------------------------------------
  public static List<int> q2p(String s)
  {
    String[] t = s.split(" ");     // SkipEmptyParts
    List<int> r;
    for (int i=0; i<4; i++)
      r.append(t[i].toInt());
    return r;
  }

// ---------------------------------------------------------------------
  public static String remsep(String s)
  {
    if (s.endsWith("/"))
      s=s.left(s.length()-1);
    return s;
  }

// ---------------------------------------------------------------------
  public static String remtilde(String s)
  {
    if (s.startsWith("~"))
      s=s.mid(1);
    return s;
  }

// ---------------------------------------------------------------------
// pair Strings with zero delimeter
  public static byte[] spair(String s,String t)
  {
    ArrayList<byte> r=new ArrayList<byte>();
    r.add(s.getByte(Charset.forName("UTF-8")));
    r.add('\0');
    r.add(s.getByte(Charset.forName("UTF-8")));
    r.add('\0');
    return r.toArray(new byte[r.length()]);
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
  public static String toqlist(String[] s)
  {
    int n=s.length();
    if (n == 0) return "";
    String r = "(\"" + s[0] + "\"";
    for (int i=1; i<n; i++)
      r.append(";\"" + s[i] + "\"");
    r.append(")");
    return r;
  }

// ---------------------------------------------------------------------
// trim trailing whitespace (for TrimTrailingWS)
// trims WS on each line, and trim extra trailing LFs
  public static String trimtws(String s)
  {
    String[] r=s.split('\n');
    for (int i=0; i<r.length(); i++)
      r[i]=dtb(r[i]);
    while ((r.length()>1) && (r[r.length()-1].isEmpty()) && (r[r.length()-2].isEmpty()))
      r.removeLast();
    return r.join("\n");
  }

// ---------------------------------------------------------------------
  public static int Util.c_strtoi(String s)
  {
    if (!s.length()) return 0;
    String p=s;
    if (p[0]=='_') p[0]='-';
    return (int)strtol(p.Util.c_str(),NULL,0);
  }

// ---------------------------------------------------------------------
  public static SI Util.c_strtol(String s)
  {
    if (!s.length()) return 0;
    String p=s;
    if (p[0]=='_') p[0]='-';
    return strtol(p.Util.c_str(),NULL,0);
  }

// ---------------------------------------------------------------------
  public static double Util.c_strtod(String s)
  {
    if (!s.length()) return 0;
    String p=s;
    if (p[0]=='_') p[0]='-';
    return strtod(p.Util.c_str(),NULL);
  }

}
