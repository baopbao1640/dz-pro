package com.platform.core.system.user.dto;

import java.util.List;

public class UserAssignPostDTO {

  private List<Long> postIds;

  public List<Long> getPostIds() {
    return postIds;
  }

  public void setPostIds(List<Long> postIds) {
    this.postIds = postIds;
  }
}
