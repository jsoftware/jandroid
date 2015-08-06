#ifndef MULTIMEDIA_H
#define MULTIMEDIA_H

import com.jsoftware.jn.wd.child;

class Form;
class Pane;

// ---------------------------------------------------------------------
class Multimedia : public Child
{
  Q_OBJECT

public:
  Multimedia(String n, String s, Form f, Pane p);
  String get(String p,String v);
  void set(String p,String v);
  String state();
  boolean isVideo;

private slots:
  void bufferStatusChanged(int percentFilled);
  void durationChanged(qint64 duration);
  void merror(QMediaPlayer::Error error);
  void mediaStatusChanged(QMediaPlayer::MediaStatus status);
  void positionChanged(qint64 position);
  void stateChanged(QMediaPlayer::State state);
  void volumeChanged(int volume);

private:
  QMediaPlayer mediaPlayer;

};

#endif
