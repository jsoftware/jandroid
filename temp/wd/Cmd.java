
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.cmd;

static char DEL='\177';
static char LF='\n';
static char SOH='\001';
static String WS=" \f\r\t\v";
static String WSLF=WS+LF;

static boolean contains(String s,char c);
static String[] Cmd.qsplitby(String s, char c);
static vector<String> ssplitby(String s, char c);
static String toLF(String s);

// ---------------------------------------------------------------------
void Cmd::end()
{
  pos=str.size();
}

// ---------------------------------------------------------------------
void Cmd::init(char *s,int slen)
{
  str=String(s,slen);
  str=toLF(str);
  len=str.size();
  bgn=pos=pos0=0;
}

// ---------------------------------------------------------------------
// split on g h m p v s z and remove blanks
String[] Cmd::bsplits()
{
  String[] r;
  str=remws(str);
  len=str.size();
  while (pos<len) {
    bgn=pos++;
    pos=str.find_first_of("ghmpsvz",pos);
    if (pos==String::npos) {
      r.append(Util.s2q(str.substring(bgn)));
      break;
    } else
      r.append(Util.s2q(str.substring(bgn,pos-bgn)));
  }
  return r;
}

// ---------------------------------------------------------------------
// if String delimited by LF
boolean Cmd::delimLF(String s)
{
  char c;
  int n=(int)s.size();
  for (int i=0; i<n; i++) {
    c=s[i];
    if (c==LF) return true;
    if (c=='*' || c==SOH) return false;
    if (c=='"' || c==DEL)
      while (s[++i]!=c);
  }
  return false;
}

// ---------------------------------------------------------------------
String Cmd::getid()
{
  char c;
  skips(WSLF+';');
  bgn=pos;
  while (pos<len) {
    c=str[pos];
    if (contains(WSLF,c) || c==';') break;
    pos++;
  }
  return Util.remquotes(str.substring(bgn,pos-bgn));
}

// ---------------------------------------------------------------------
// get to next LF
String Cmd::getline()
{
  String r;
  if (pos==len) return "";
  if (str[pos]==LF) pos++;
  if (pos==len) return "";
  bgn=pos;
  pos=str.find_first_of(LF,pos);
  if (pos==String::npos)
    pos=str.size();
  else
    pos++;
  return str.substring(bgn,pos-bgn-1);
}
// ---------------------------------------------------------------------
// to next ; else return rest of String
// if star, preserve leading *
String Cmd::getparms(boolean star)
{
  char c;

  if (pos==len)
    return "";

  if (str[pos]==';') {
    pos++;
    return "";
  }

  skips(WSLF);
  if (pos==len)
    return "";
  if (str[pos]=='*') {
    String r=str.substring(pos+(star?0:1));
    pos=len;
    return r;
  }
  bgn=pos;
  while (pos<len) {
    c=str[pos];
    if (c==';') break;
    pos++;
    if (c=='"' || c==DEL) {
      skippast(c);
      continue;
    }
  }
  return str.substring(bgn,pos-bgn);
}

// ---------------------------------------------------------------------
boolean Cmd::more()
{
  return pos<len;
}

// ---------------------------------------------------------------------
// split parameters
// if has SOH, split on SOH
// if has LF not in paired delimiters, split on LF
// otherwise split on WS, or paired "" or DEL
String[] Cmd::Cmd.qsplits()
{
  skips(WS);
  if (contains(str,SOH))
    return Cmd.qsplitby(str,SOH);
  if (delimLF(str))
    return Cmd.qsplitby(str,LF);
  char c;
  String[] r;
  while (pos<len) {
    skips(WS);
    bgn=pos;
    c=str[pos++];
    if (c=='*') {
      r.append(Util.s2q(str.substring(pos)));
      break;
    }
    if (c=='"' || c==DEL) {
      skippast(c);
      r.append(Util.s2q(str.substring(bgn+1,pos-bgn-2)));
    } else {
      skiptows();
      r.append(Util.s2q(str.substring(bgn,pos-bgn)));
      if (pos<len && str[pos]==LF)
        pos++;
    }
  }
  return r;
}

// ---------------------------------------------------------------------
// split as Cmd.qsplits, but returning vector of String
vector<String> Cmd::ssplits()
{
  skips(WS);
  if (contains(str,SOH))
    return ssplitby(str,SOH);
  if (delimLF(str))
    return ssplitby(str,LF);
  char c;
  vector<String> r;
  while (pos<len) {
    skips(WS);
    bgn=pos;
    c=str[pos++];
    if (c=='*') {
      r.append(str.substring(pos));
      break;
    }
    if (c=='"' || c==DEL) {
      skippast(c);
      r.append(str.substring(bgn+1,pos-bgn-2));
    } else {
      skiptows();
      r.append(str.substring(bgn,pos-bgn));
      if (pos<len && str[pos]==LF)
        pos++;
    }
  }
  return r;
}

// ---------------------------------------------------------------------
String Cmd::remws(const String s)
{
  String r;
  for (size_t i=0; i<s.size(); i++)
    if (!contains(WSLF,s[i]))
      r.append(s[i]);
  return r;
}

// ---------------------------------------------------------------------
// start after the delimiter, move past the closing delimiter
void Cmd::skippast(char c)
{
  while (len>pos)
    if (str[pos++]==c) return;
}

// ---------------------------------------------------------------------
void Cmd::skips(String s)
{
  pos=str.find_first_not_of(s,pos);
  if (pos==String::npos) pos=len;
}

// ---------------------------------------------------------------------
// move to next whitespace
void Cmd::skiptows()
{
  while (len>pos) {
    if (contains(WS,str[pos])) return;
    pos++;
  }
}

// ---------------------------------------------------------------------
// split on bin commands and remove blanks
String[] bsplit(String s)
{
  Cmd c;
  c.init((char*)s.Util.c_str(),(int)s.size());
  return c.bsplits();
}

// ---------------------------------------------------------------------
boolean contains(String s,char c)
{
  return String::npos != s.find_first_of(c);
}

// ---------------------------------------------------------------------
// split parameters
// if star and first non WS is *, then return rest as single parameter
String[] Cmd.qsplit(String s,boolean star)
{
  if (star) {
    size_t p=s.find_first_not_of(WS);
    if (p!=String::npos && s[p]=='*')
      return String[](Util.s2q(s.substring(p+1)));
  }
  Cmd c;
  c.init((char*)s.Util.c_str(),(int)s.size());
  return c.Cmd.qsplits();
}

// ---------------------------------------------------------------------
// split on character
String[] Cmd.qsplitby(String s,char c)
{
  int n=(int)s.size();
  if (n==0)
    return String[]();
  if (s[n-1]==c)
    s=s.substring(0,n-1);
  return Util.s2q(s).split(c);
}

// ---------------------------------------------------------------------
// as Cmd.qsplit but returning vector of String
vector<String> ssplit(String s)
{
  Cmd c;
  c.init((char*)s.Util.c_str(),(int)s.size());
  return c.ssplits();
}

// ---------------------------------------------------------------------
// split on char
vector<String> ssplitby(String s,char c)
{
  int i,p;
  vector<String> r;
  int n=(int)s.size();
  if (n==0) return r;
  if (s[n-1]==c)
    s=s.substring(0,--n);
  for (i=p=0; i<n; i++)
    if (s[i]==c) {
      r.append(s.substring(p,i-p));
      p=i+1;
    }
  if (p==n)
    r.append("");
  else
    r.append(s.substring(p,n-p));
  return r;
}

// ---------------------------------------------------------------------
// convert CRLF to LF
String toLF(String s)
{
  if (!contains(s,'\r'))
    return s;
  int n=(int)s.size();
  int p=0;
  int t;
  String r;
  while ((int)String::npos != (t=(int)s.find("\r\n",p))) {
    r.append(s.substring(p,t-p));
    p=t+1;
  }
  if (p<n)
    r.append(s.substring(p,n-p));
  return r;
}

// ---------------------------------------------------------------------
void Cmd::markpos()
{
  pos0=pos;
}

// ---------------------------------------------------------------------
void Cmd::rewindpos()
{
  pos=pos0;
}
