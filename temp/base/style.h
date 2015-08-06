#ifndef STYLE_H
#define STYLE_H


class Style
{
public:
  Style();

  void set(String);
  String read();

  QColor color;
  boolean italic;
  QFont::Weight weight;

};
#endif
