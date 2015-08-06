/* common state */


import com.jsortware.jn.base.base;
import com.jsortware.jn.base.state;
import com.jsortware.jn.base.menu;
import com.jsortware.jn.base.note;
import com.jsortware.jn.base.term;

List<int> DefPos;

// ---------------------------------------------------------------------
void Config::dirmatch_init()
{
  if (DMTypes.size()) return;

  int i,n;
  String t,v,w;
  String[] s;
  DMType="All";
  DMTypeIndex=3;
  DMTypes=DefTypes;
  s=cfreadx(ConfigPath.filePath("dirmatch.cfg"));

  while (s.size()) {
    v=s.at(0);
    v.remove(' ');
    s.removeFirst();
    if (v.isEmpty())
      continue;

    if (matchhead("DMType=",v)) {
      DMType=v.mid(7).trimmed();
      continue;
    }

    if (matchhead("DMTypes=",v)) {
      DMTypes.clear();
      while(s.size()) {
        if(s.at(0).at(0)!=' ') break;
        DMTypes.append(s.at(0).trimmed());
        s.removeFirst();
      }
      continue;
    }

    if (matchhead("DMFavorites=",v)) {
      DMFavorites.clear();
      while(s.size()) {
        if(s.at(0).at(0)!=' ') break;
        DMFavorites.append(s.at(0).trimmed());
        s.removeFirst();
      }
      DMFavorites.removeDuplicates();
      DMFavorites.removeAll("");
      n=DMFavorites.size();
      if (n > 2 * n / 2)
        DMFavorites.removeLast();
    }
  }

  for(i=0; i<DMTypes.size(); i++) {
    v=DMTypes.at(i);
    n=v.indexOf(' ');
    s.clear();
    t.clear();
    if(n>0) {
      s=v.mid(n).remove(' ').split(',');     // SkipEmptyParts
      s=qslprependeach("*.",s);
      t=s.join(",");
      v=v.mid(0,n);
    }
    DMTypex.append(t);
    if (v==DMType) DMTypeIndex=i;
    if(t.size())
      v+=" (" + t + ")";
    DMTypes.replace(i,v);
  }

}

// ---------------------------------------------------------------------
int Config::filepos_get(String f)
{
  return FilePos.value(f,0);
}

// ---------------------------------------------------------------------
void Config::filepos_set(String f, int p)
{
  FilePos.insert(f,p);
}

// ---------------------------------------------------------------------
void Config::launch_init()
{
  int i;
  String s;
  String[] t;
  t=cfreadx(ConfigPath.filePath("launch.cfg"));
  foreach(String s,t) {
    i=s.indexOf(";");
    if (i<0) {
      LaunchPadKeys.append("=");
      LaunchPadValues.append("");
    } else {
      LaunchPadKeys.append(s.left(i).trimmed().replace("_","&"));
      LaunchPadValues.append(s.mid(i+1).trimmed());
    }
  }
}

// ---------------------------------------------------------------------
void Config::togglelinenos()
{
  LineNos=!LineNos;
  term.refresh();
  if (note) {
    note.setlinenos(LineNos);
    if(note2)
      note2.setlinenos(LineNos);
  }
}

// ---------------------------------------------------------------------
void Config::togglelinewrap()
{
  LineWrap=!LineWrap;
  note.setlinewrap(LineWrap);
  if(note2)
    note2.setlinewrap(LineWrap);
}

// ---------------------------------------------------------------------
void Config::userkeys_init()
{
  String[] err,t;
  String[] v=getuserkeys();
  t=cfreadx(ConfigPath.filePath("userkeys.cfg"));
  foreach (String f,t)
    UserKeys.append(userkeys_split(f));
  foreach (String[] f,UserKeys) {
    if (!v.contains(f.first())) {
      err+="User key not available: "+f.first();
      UserKeys.removeOne(f);
    }
  }
  if (err.size()>0)
    info("User Keys",err.join("\n"));
  term.menuBar.createuserkeyMenu();
}

// ---------------------------------------------------------------------
String[] Config::userkeys_split(String s)
{
  QChar c;
  for (int i=0; i<s.length(); i++) {
    c=s.at(i);
    if (!(c.isLetter() || c.isDigit() || c=='+')) break;
  }
  return s.split(c);
}

// ---------------------------------------------------------------------
void Config::winpos_init()
{
  DefPos << 100 << 100 << 300 << 300;

  WinPos["Ctag"]=DefPos;
  WinPos["Dirm"]=DefPos;
  WinPos["Dlog"]=DefPos;
  WinPos["Fif"]=DefPos;
  WinPos["Fiw"]=DefPos;
  WinPos["Picm"]=DefPos;
  WinPos["View"]=DefPos;

  QSettings s(ConfigPath.filePath("winpos.dat"),QSettings::IniFormat);
  String[] keys = s.allKeys();
  String[] d;
  List<int> v;
  foreach (const String &k, keys) {
    d = s.value(k,"").toString().split(' ');
    if (d.size()==4) {
      v.clear();
      for(int i=0; i<4; i++)
        v.append(d.at(i).toInt());
      WinPos[k]=v;
    }
  }
}

// ---------------------------------------------------------------------
List<int> Config::winpos_read(String id)
{
  return (WinPos.contains(id)) ? WinPos.value(id) : DefPos;
}

// ---------------------------------------------------------------------
void Config::winpos_save(QWidget *w,String id)
{
  QSettings s(ConfigPath.filePath("winpos.dat"),QSettings::IniFormat);
  winpos_save1(winpos_get(w),id);
}

// ---------------------------------------------------------------------
void Config::winpos_save1(List<int>d,String id)
{
  QSettings s(ConfigPath.filePath("winpos.dat"),QSettings::IniFormat);
  WinPos[id]=d;
  String r;
  r.append(
    String::number(d[0]) + " " +
    String::number(d[1]) + " " +
    String::number(d[2]) + " " +
    String::number(d[3]));
  s.setValue(id, r);
}
