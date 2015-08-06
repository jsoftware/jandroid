
import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.cmd;
import com.jsoftware.jn.base.wssvr;
import com.jsoftware.jn.base.wscln;


static WsSvr *wssvr;
static WsCln *wscln;

String ws(String p);
static String wsconnect();
static String wsclose();
static String wslisten();
static String wsquery();
#ifdef QT53
static String wsstate();
#endif
static String wssend(int binary);

static vector<String> arg;
static String argjoin;

// ---------------------------------------------------------------------
String ws(String p)
{
  argjoin=p;
  arg=ssplit(p);
  if (arg.size()<1) {
    JConsoleApp.theWd.error("missing ws cmd");
    return "";
  }

  String type=arg.front();
  arg.erase(arg.begin());
  if (type.equals("listen"))
    return wslisten();
  if (type.equals("connect"))
    return wsconnect();
  if (type.equals("send"))
    return wssend(0);
  if (type.equals("sendb"))
    return wssend(1);
  if (type.equals("close"))
    return wsclose();
  if (type.equals("query"))
    return wsquery();
#ifdef QT53
  if (type.equals("state"))
    return wsstate();
#endif
  JConsoleApp.theWd.error("invalid ws cmd: " + type);
  return "";
}

// ---------------------------------------------------------------------
String wsconnect()
{
  String q;

  if (arg.size()==1) {
    q=arg.at(0);
  } else {
    JConsoleApp.theWd.error("Need url: "+argjoin);
    return "";
  }
  if (!((q.substring(0,5).equals("ws://")||(q.substring(0,6)=="wss://")))) {
    JConsoleApp.theWd.error("bad url: "+argjoin);
    return "";
  }
  if (!wscln)
    wscln = new WsCln();
  return p2s(wscln.openurl(Util.s2q(q)));
}

// ---------------------------------------------------------------------
String wsclose()
{
  if (arg.size()==0) {
    return "";
  }
  I socket=Util.c_strtol(arg.at(0));
  if ((wssvr) && wssvr.hasSocket((void *)socket)) {
    wssvr.disconnect((void *)socket);
  } else if ((wscln) && wscln.hasSocket((void *)socket)) {
    wscln.disconnect((void *)socket);
  }
  return "";
}

// ---------------------------------------------------------------------
String wslisten()
{
  int port=0,protocol=0;

  if (arg.size()==1) {
    port=Util.c_strtoi(arg.at(0));
  } else if (arg.size()==2) {
    port=Util.c_strtoi(arg.at(0));
    protocol=Util.c_strtoi(arg.at(1));
  } else {
    JConsoleApp.theWd.error("Need port [protocol]: "+argjoin);
    return "";
  }
  if (wssvr) {
    delete wssvr;
    wssvr = 0;
  }
  if (port) {
    wssvr = new WsSvr(port,1+protocol);
    if (!(wssvr.errString.empty())) {
      JConsoleApp.theWd.error("ws listen failed: " + wssvr.errString);
      delete wssvr;
      wssvr = 0;
    }
  }
  return "";
}

// ---------------------------------------------------------------------
String wsquery()
{
  int type=0;
  String r="";
  if (arg.size()!=0)
    type=Util.c_strtoi(arg.at(0));
  if (0==type) {
    if (wssvr)
      r=wssvr.querySocket();
  } else {
    if (wscln)
      r=wscln.querySocket();
  }
  return r;
}

#ifdef QT53
// ---------------------------------------------------------------------
String wsstate()
{
  I socket=0;
  String r="";
  if (arg.size()==0) {
    JConsoleApp.theWd.error("Need socket: "+argjoin);
    return "";
  }
  socket=Util.c_strtol(arg.at(0));
  if ((wssvr) && wssvr.hasSocket((void *)socket)) {
    r=wssvr.state((void *)socket);
  } else if ((wscln) && wscln.hasSocket((void *)socket)) {
    r=wscln.state((void *)socket);
  }
  return r;
}
#endif

// ---------------------------------------------------------------------
String wssend(int binary)
{
  I socket=0;
  String r, data="";

  if (arg.size()==1) {
    socket=Util.c_strtol(arg.at(0));
  } else if (arg.size()==2) {
    socket=Util.c_strtol(arg.at(0));
    data=arg.at(1);
  } else {
    JConsoleApp.theWd.error("Need socket [data]: "+argjoin);
    return "";
  }
  if ((wssvr) && (0==socket))
    r=  p2s((void *)wssvr.write((void *)0, data.Util.c_str(), data.size(), binary));
  else if ((wscln) && (1==socket))
    r=  p2s((void *)wscln.write((void *)0, data.Util.c_str(), data.size(), binary));
  else if ((wssvr) && wssvr.hasSocket((void *)socket))
    r=  p2s((void *)wssvr.write((void *)socket, data.Util.c_str(), data.size(), binary));
  else if ((wscln) && wscln.hasSocket((void *)socket))
    r=  p2s((void *)wscln.write((void *)socket, data.Util.c_str(), data.size(), binary));
  else {
    JConsoleApp.theWd.error("Need active websocket connection: "+argjoin);
    return "";
  }
  return r;
}

