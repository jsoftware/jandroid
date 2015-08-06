#ifndef FONT_H
#define FONT_H



// ---------------------------------------------------------------------
class Font : public QObject
{
  Q_OBJECT

public:
  Font(String s, float pointsize=-1.0);
  Font(String s,int size10, boolean bold, boolean italic, boolean strikeout, boolean underline, int angle10);
  QFont font;
  int angle;
  boolean error;

private:

};

extern Font *FontExtent;

#endif
