package com.platform.core.system.post.dto;

import com.platform.core.common.api.PageQuery;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class PostPageQueryDTO extends PageQuery {

  @Size(max = 64)
  private String postCode;

  @Size(max = 128)
  private String postName;

  @Pattern(regexp = "[01]")
  private String status;

  public String getPostCode() {
    return postCode;
  }

  public void setPostCode(String postCode) {
    this.postCode = postCode;
  }

  public String getPostName() {
    return postName;
  }

  public void setPostName(String postName) {
    this.postName = postName;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
