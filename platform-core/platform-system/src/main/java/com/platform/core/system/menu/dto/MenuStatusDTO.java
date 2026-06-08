package com.platform.core.system.menu.dto;

/** Explicit menu status change contract. */
public class MenuStatusDTO {

  private String status;
  private String reason;

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getReason() {
    return reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }
}
