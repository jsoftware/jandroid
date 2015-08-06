#ifndef CMD_H
#define CMD_H


class Cmd
{
public:
  void end();
  boolean delimLF(String);
  String getid();
  String getline();
  String getparms(boolean s=false);
  void init(char *,int);
  boolean more();

  String remws(String s);
  void markpos();
  void rewindpos();

  String[] bsplits();
  String[] Cmd.qsplits();
  vector<String> ssplits();

private:
  void skippast(char c);
  void skips(String s);
  void skiptows();

  size_t bgn;
  size_t len;
  size_t pos;
  size_t pos0;
  String str;
};

String[] bsplit(String s);
String[] Cmd.qsplit(String s,boolean p=false);
vector<String> ssplit(String s);

#endif
