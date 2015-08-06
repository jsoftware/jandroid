package com.jsoftware.j.android;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<String>
{

  File parent;
  public FileComparator(File parent)
  {
    this.parent = parent;
  }

  public int compare(String as, String bs)
  {
    File a = new File(parent,as);
    File b = new File(parent,bs);

    boolean ad = a.isDirectory();
    boolean bd = b.isDirectory();

    if(ad == bd) {
      return a.getName().compareTo(b.getName());
    } else {
      return ad ? -1 : 1;
    }
  }

}
