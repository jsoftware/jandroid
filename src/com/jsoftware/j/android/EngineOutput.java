package com.jsoftware.j.android;

/*
public EditorData getWindow(String name) {
	return windows.get(name);
}
*/
class EngineOutput
{
  public int type;
  public String content;

  public EngineOutput(int type, String content)
  {
    this.type = type;
    this.content = content;
  }
}