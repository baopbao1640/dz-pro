package com.platform.core.system.menu.vo;

import java.util.ArrayList;
import java.util.List;

/** Dynamic route contract returned from enabled directory and menu nodes. */
public class RouteVO {

  private String name;
  private String path;
  private String component;
  private String redirect;
  private RouteMetaVO meta;
  private List<RouteVO> children = new ArrayList<>();

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  public String getRedirect() {
    return redirect;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
  }

  public RouteMetaVO getMeta() {
    return meta;
  }

  public void setMeta(RouteMetaVO meta) {
    this.meta = meta;
  }

  public List<RouteVO> getChildren() {
    return children;
  }

  public void setChildren(List<RouteVO> children) {
    this.children = children;
  }

  /** Route metadata matching frontend dynamic route needs. */
  public static class RouteMetaVO {

    private String title;
    private String icon;
    private Boolean hidden;
    private Boolean keepAlive;

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public String getIcon() {
      return icon;
    }

    public void setIcon(String icon) {
      this.icon = icon;
    }

    public Boolean getHidden() {
      return hidden;
    }

    public void setHidden(Boolean hidden) {
      this.hidden = hidden;
    }

    public Boolean getKeepAlive() {
      return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
      this.keepAlive = keepAlive;
    }
  }
}
