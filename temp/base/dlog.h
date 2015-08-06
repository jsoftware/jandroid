#ifndef DLOG_H
#define DLOG_H


class Slog : public QDialog
{
  Q_OBJECT

public:
  Slog();

public slots:
  void reject();
  void itemActivated();

private:
  void init();
  void savepos();
  void keyReleaseEvent(QKeyEvent *e);
};

void dlog_add(String);
void dlog_init();
String dlog_scroll(int);
void dlog_write();

#endif
