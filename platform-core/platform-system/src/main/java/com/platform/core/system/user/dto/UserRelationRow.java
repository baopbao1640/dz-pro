package com.platform.core.system.user.dto;

public class UserRelationRow {

  private Long id;
  private Long relationId;

  public UserRelationRow(Long id, Long relationId) {
    this.id = id;
    this.relationId = relationId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getRelationId() {
    return relationId;
  }

  public void setRelationId(Long relationId) {
    this.relationId = relationId;
  }
}
