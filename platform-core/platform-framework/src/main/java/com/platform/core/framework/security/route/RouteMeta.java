package com.platform.core.framework.security.route;

/**
 * 路由元信息，承载前端菜单渲染和缓存控制所需的最小字段。
 *
 * <p>职责：表达标题、图标、隐藏状态和 keep-alive 标记。
 *
 * <p>边界：不承载按钮权限、接口权限或运行时页面状态。
 *
 * <p>当前阶段能力：适配 Ant Design Vue 后台菜单和 Vue Router meta。
 */
/*
 * Boundary:
 * 只保留可持久化的菜单元数据，前端临时状态不得写入该契约。
 */
/*
 * Deferred:
 * 面包屑、外链、固定页签等能力暂未纳入，后续如扩展必须同步菜单表设计。
 */
/*
 * Risk:
 * 图标字段如果直接来自数据库，需要前端做图标白名单或降级处理。
 */
public record RouteMeta(String title, String icon, boolean hidden, boolean keepAlive) {}
