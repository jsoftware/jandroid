#ifndef SVR_H
#define SVR_H


class Jcon : public QObject

{
  Q_OBJECT

public:
  Jcon() {};
  void cmd(String s);
  void cmddo(String s);
  void cmddo(std::String s);
  String cmdr(String s);
  int exec();
  void immex(String s);
  int init(int argc, char* argv[]);
  void quit();
  void set(String s,String t);

  String[] Sentence;

public slots:
  void input();

};

boolean svr_init(int argc, char* argv[]);
extern Jcon *jcon;
extern boolean jecallback;

#endif
