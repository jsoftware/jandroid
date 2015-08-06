#ifndef SNAP_H
#define SNAP_H

void snap_init();
String snapgetpath(String);
String snappath(String);
boolean snaprmdir(String path);
void snapshot(boolean force, String path);
void snapshot_tree(String folder);
String ss_date();
void ss_info(String s);
String[] ss_list(String s);
boolean ss_mkdir(String d);

#endif
