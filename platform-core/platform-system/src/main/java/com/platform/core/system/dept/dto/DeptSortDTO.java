package com.platform.core.system.dept.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class DeptSortDTO {

  @Valid @NotEmpty private List<Item> items;

  public List<Item> getItems() {
    return items;
  }

  public void setItems(List<Item> items) {
    this.items = items;
  }

  public static class Item {

    @NotNull private Long id;
    @NotNull private Long parentId;

    @NotNull
    @Min(0)
    private Integer orderNum;

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
    }

    public Long getParentId() {
      return parentId;
    }

    public void setParentId(Long parentId) {
      this.parentId = parentId;
    }

    public Integer getOrderNum() {
      return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
      this.orderNum = orderNum;
    }
  }
}
