
#ifdef QT54
#endif

import com.jsoftware.jn.wd.wd;
import com.jsoftware.jn.wd.multimedia;
import com.jsoftware.jn.wd.form;
import com.jsoftware.jn.wd.pane;
import com.jsoftware.jn.wd.cmd;

// ---------------------------------------------------------------------
Multimedia::Multimedia(String n, String s, Form f, Pane p) : Child(n,s,f,p)
, mediaPlayer(f)
{
  type="multimedia";
  isVideo=false;
  String qn=n;
  String[] opt=Cmd.qsplit(s);
  if (JConsoleApp.theWd.invalidopt(n,opt,"video")) return;
  childStyle(opt);
#ifdef QT54
  if (opt.contains("video")) {
    isVideo=true;
    QVideoWidget *w=new QVideoWidget;
    mediaPlayer.setVideoOutput(w);
    widget=(View ) w;
  }
#endif
  QObject::connect(&mediaPlayer, SIGNAL( bufferStatusChanged(int)), this, SLOT( bufferStatusChanged(int)));
  QObject::connect(&mediaPlayer, SIGNAL( durationChanged(qint64)), this, SLOT( durationChanged(qint64)));
  QObject::connect(&mediaPlayer, SIGNAL( JConsoleApp.theWd.error(QMediaPlayer::Error)), this, SLOT( mJConsoleApp.theWd.error(QMediaPlayer::Error)));
  QObject::connect(&mediaPlayer, SIGNAL( mediaStatusChanged(QMediaPlayer::MediaStatus)), this, SLOT( mediaStatusChanged(QMediaPlayer::MediaStatus)));
  QObject::connect(&mediaPlayer, SIGNAL( positionChanged(qint64)), this, SLOT( positionChanged(qint64)));
  QObject::connect(&mediaPlayer, SIGNAL( stateChanged(QMediaPlayer::State)), this, SLOT( stateChanged(QMediaPlayer::State)));
  QObject::connect(&mediaPlayer, SIGNAL( volumeChanged(int)), this, SLOT( volumeChanged(int)));
}

// ---------------------------------------------------------------------
void Multimedia::bufferStatusChanged(int percentFilled)
{
  event="bufferstatus";
  sysdata=Util.i2s(percentFilled);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void Multimedia::durationChanged(qint64 duration)
{
  event="duration";
  sysdata=Util.i2s(duration);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
static  char * errortab[]= {
  (char *)"no",
  (char *)"resource",
  (char *)"format",
  (char *)"network",
  (char *)"accessdenied",
  (char *)"servicemissing",
};

// ---------------------------------------------------------------------
void Multimedia::mJConsoleApp.theWd.error(QMediaPlayer::Error error)
{
  event="error";
  sysdata=String(errortab[error]);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
static  char * statustab[]= {
  (char *)"unknownmediastatus",
  (char *)"nomedia",
  (char *)"loadingmedia",
  (char *)"loadedmedia",
  (char *)"stalledmedia",
  (char *)"bufferingmedia",
  (char *)"bufferedmedia",
  (char *)"endofmedia",
  (char *)"invalidmedia",
};

// ---------------------------------------------------------------------
void Multimedia::mediaStatusChanged(QMediaPlayer::MediaStatus status)
{
  event="mediastatus";
  sysdata=String(statustab[status]);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void Multimedia::positionChanged(qint64 position)
{
  event="position";
  sysdata=Util.i2s(position);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
static char * statetab[]= {
  (char *)"stopped",
  (char *)"playing",
  (char *)"paused",
};

// ---------------------------------------------------------------------
void Multimedia::stateChanged(QMediaPlayer::State state)
{
  event="playstate";
  sysdata=String(statetab[state]);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
void Multimedia::volumeChanged(int volume)
{
  event="volume";
  sysdata=Util.i2s(volume);
  pform.signalevent(this);
}

// ---------------------------------------------------------------------
static char * artab[]= {
  (char *)"ignore",
  (char *)"keep",
  (char *)"expand",
};

// ---------------------------------------------------------------------
String Multimedia::get(String p,String v)
{
#ifdef QT54
  QVideoWidget *w=(QVideoWidget*) widget;
#else
  View w=widget;
#endif
  Q_UNUSED(w);
  String r;
  if (p.equals("property")) {
    if (isVideo && w)
      r+=String("aspectratio")+"\012"+ "brightness"+"\012"+ "contrast"+"\012"+ "duration"+"\012"+ "error"+"\012"+ "fullscreen"+"\012"+ "hue"+"\012"+ "mute"+"\012"+ "playstate"+"\012"+ "position"+"\012"+ "saturation"+"\012"+ "seekable"+"\012"+ "status"+"\012"+ "volume"+"\012";
    else
      r+=String("duration")+"\012"+ "error"+"\012"+ "mute"+"\012"+ "playstate"+"\012"+ "position"+"\012"+ "seekable"+"\012"+ "status"+"\012"+ "volume"+"\012";
    r+=super.get(p,v);
  } else if (p.equals("duration"))
    r=d2s(mediaPlayer.duration());
  else if (p.equals("error"))
    r=String(errortab[mediaPlayer.JConsoleApp.theWd.error()]);
  else if (p.equals("mute"))
    r=Util.i2s(mediaPlayer.isMuted());
  else if (p.equals("playstate"))
    r=String(statetab[mediaPlayer.state()]);
  else if (p.equals("position"))
    r=Util.i2s(mediaPlayer.position());
  else if (p.equals("seekable"))
    r=Util.i2s(mediaPlayer.isSeekable());
  else if (p.equals("status"))
    r=String(statustab[mediaPlayer.mediaStatus()]);
  else if (p.equals("volume"))
    r=Util.i2s(mediaPlayer.volume());
  else if (isVideo && w) {
#ifdef QT54
    if (p.equals("aspectratio"))
      r=String(artab[w.aspectRatioMode()]);
    else if (p.equals("brightness"))
      r=Util.i2s(w.brightness());
    else if (p.equals("contrast"))
      r=Util.i2s(w.contrast());
    else if (p.equals("fullscreen"))
      r=Util.i2s(w.isFullScreen());
    else if (p.equals("hue"))
      r=Util.i2s(w.hue());
    else if (p.equals("saturation"))
      r=Util.i2s(w.saturation());
    else
      r=super.get(p,v);
#endif
  } else
    r=super.get(p,v);
  return r;
}

// ---------------------------------------------------------------------
void Multimedia::set(String p,String v)
{
#ifdef QT54
  QVideoWidget *w=(QVideoWidget*) widget;
#else
  View w=widget;
#endif
  if ((p.equals("pause" || p=="play" || p=="stop")) && v.length()) {
    JConsoleApp.theWd.error("extra parameters: " + p + " " + v);
    return;
  }
  if (p.equals("media")) {
    String f=Util.remquotes(v);
    if (f.contains("://"))
      mediaPlayer.setMedia(QUrl(f));
    else
      mediaPlayer.setMedia(QUrl::fromLocalFile(f));
  } else if (p.equals("mute"))
    mediaPlayer.setMuted(Util.remquotes(v)!="0");
  else if (p.equals("pause"))
    mediaPlayer.pause();
  else if (p.equals("play"))
    mediaPlayer.play();
  else if (p.equals("playbackrate"))
    mediaPlayer.setPlaybackRate(Util.c_strtod(v));
  else if (p.equals("position"))
    mediaPlayer.setPosition(Util.c_strtol(v));
  else if (p.equals("stop"))
    mediaPlayer.stop();
  else if (p.equals("volume"))
    mediaPlayer.setVolume(Util.c_strtoi(v));
  else if (isVideo && w) {
#ifdef QT54
    if (p.equals("aspectratio")) {
      if (v.equals("ignore"))
        w.setAspectRatioMode(Qt::IgnoreAspectRatio);
      else if (v.equals("keep"))
        w.setAspectRatioMode(Qt::KeepAspectRatio);
      else if (v.equals("expand"))
        w.setAspectRatioMode(Qt::KeepAspectRatioByExpanding);
      else {
        JConsoleApp.theWd.error("invalid option: " + p + " " + v);
        return;
      }
    } else if (p.equals("brightness"))
      w.setBrightness(Util.c_strtoi(v));
    else if (p.equals("contrast"))
      w.setContrast(Util.c_strtoi(v));
    else if (p.equals("fullscreen"))
      w.setFullScreen(Util.remquotes(v)!="0");
    else if (p.equals("hue"))
      w.setHue(Util.c_strtoi(v));
    else if (p.equals("saturation"))
      w.setSaturation(Util.c_strtoi(v));
    else
      super.set(p,v);
#endif
  } else super.set(p,v);
}

// ---------------------------------------------------------------------
String Multimedia::state()
{
  return "";
}
