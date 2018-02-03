package com.jsoftware.j.android;


public class SliderMenuItem
{

  public enum SliderMenuStatus {
    ERROR_IN_REFRESH, DEFAULT, REFRESHING
  }

  public interface MenuItemClickHandler
  {
    public void handleMenuClick();
  }

  MenuItemClickHandler clickHandler;

  private String title;
  private int iconImageId, id;
  private SliderMenuStatus status = SliderMenuStatus.DEFAULT;


  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public int getIconImageId()
  {
    return iconImageId;
  }

  public void setIconImageId(int iconImageId)
  {
    this.iconImageId = iconImageId;
  }

  public SliderMenuStatus getStatus()
  {
    return status;
  }

  public void setStatus(SliderMenuStatus status)
  {
    this.status = status;
  }


  public void setClickHandler(MenuItemClickHandler clickHandler)
  {
    this.clickHandler = clickHandler;
  }

  public MenuItemClickHandler getClickHandler()
  {
    return clickHandler;
  }

}
