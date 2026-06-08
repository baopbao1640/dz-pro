package com.platform.core.framework.security.route;

/** 路由元信息只保留前端渲染所需字段。风险：如果后续加入更多 UI 状态，必须先确认这些状态 是否属于菜单表持久化字段，避免前端临时状态污染后端菜单模型。 */
public record RouteMeta(String title, String icon, boolean hidden, boolean keepAlive) {}
