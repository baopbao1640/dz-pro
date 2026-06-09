# Code Comment Governance Spec

## 目标

建立长期可维护的中文代码注释规范，解决 AI 生成代码可移交性差、边界不清、风险不显性、Deferred 隐藏在实现细节中的问题。

当前阶段只做规范设计，不直接大规模修改 Java、Vue、SQL、XML 或业务功能代码。

## 适用范围

本规范适用于后续所有新增和修改代码，重点覆盖：

- Java 类注释。
- Java 方法注释。
- framework 边界注释。
- Deferred 注释。
- 风险与限制说明注释。
- MyBatis XML SQL 注释。
- Vue 页面与组合逻辑注释。
- TypeScript API client 注释。
- TODO 替代规范。
- 禁止无意义注释。

## 注释语言规范

注释必须中文主导。

英文仅保留：

- 技术名词，例如 `Keycloak`、`Flyway`、`MyBatis Plus`、`Spring Security`。
- 类名、方法名、字段名、API、annotation、权限码。
- 数据库表名、字段名、配置键。

禁止大段英文注释和 AI 执行说明。

## 注释价值原则

注释必须解释“为什么”，而不是重复“做什么”。

应该写：

- 为什么这样设计。
- 为什么放在当前模块。
- 为什么暂缓。
- 为什么不能跨边界调用。
- 当前实现有什么风险。
- 当前实现依赖什么前置条件。
- 后续扩展点在哪里。

不应该写：

- 设置值。
- 返回结果。
- 调用方法。
- 遍历列表。
- 查询数据库。
- 保存对象。

## Java 类注释规范

Java 类注释用于说明类的职责、边界和设计原因。

必须写类注释的类型：

- framework 公共能力类。
- security、authz、audit、datascope、dynamic-route 相关类。
- 跨模块使用的 DTO、VO、annotation、context、helper。
- 复杂业务 Service。
- 自定义异常、拦截器、过滤器、AOP、配置类。

类注释应包含：

- 当前类负责什么。
- 不负责什么。
- 所属模块边界。
- 关键设计原因。
- 与 Deferred 或技术债相关的说明。

普通 Entity、简单 DTO、简单 VO 可不写类注释，除非字段语义或边界容易误解。

## Java 方法注释规范

方法注释只用于解释非显然行为。

必须写方法注释的情况：

- public API 或跨模块调用入口。
- 事务边界复杂的方法。
- 权限、数据权限、审计、动态路由相关方法。
- 有明显副作用的方法。
- 有 Deferred、风险、兼容或降级行为的方法。
- 方法名无法完整表达业务规则的方法。

禁止为 getter、setter、简单转换、简单 CRUD 包装写复述型注释。

## framework 边界注释规范

framework 模块注释必须优先说明边界。

必须明确：

- 当前能力是框架通用能力还是业务实现。
- 是否允许依赖 `platform-system`。
- 是否允许访问数据库表。
- 是否只是 contract、scaffold 或 fallback。
- 业务模块如何扩展或替换。

特别要求：

- security 注释必须说明认证边界仍由 Keycloak 负责。
- authz 注释必须说明权限来源、强制时机和未覆盖范围。
- audit 注释必须说明异步、非阻塞、脱敏和持久化边界。
- datascope 注释必须说明 SQL 条件注入点、适用范围和风险。
- dynamic-route 注释必须说明菜单表、权限码和前端路由的关系。

## Deferred 注释规范

禁止只写 `TODO`。

暂缓事项必须使用结构化注释：

```java
// Deferred: 当前先保留 no-op fallback，避免 framework 反向依赖 system 日志表。
// 后续处理：在 platform-system 中提供 AuditEventHandler 持久化实现，写入 sys_oper_log。
```

Deferred 注释必须包含：

- 暂缓原因。
- 当前影响。
- 后续处理方向。

如果 Deferred 已进入 `progress.md`，代码注释可以引用对应 feature 和章节，但不能只写链接。

## 风险与限制说明注释规范

风险注释用于提示维护者当前实现的限制，不用于掩盖缺陷。

必须写风险注释的情况：

- 临时放行安全路径。
- 当前未做权限强制。
- 当前只支持基础数据范围。
- 当前存在 fallback、mock、no-op。
- 当前实现依赖外部配置或部署前替换值。
- 当前逻辑有性能、并发、数据一致性风险。

风险注释必须包含：

- 风险是什么。
- 影响范围。
- 后续处理方向。

## MyBatis XML SQL 注释规范

MyBatis XML 注释必须说明 SQL 的业务约束或非显然查询策略。

建议注释：

- 数据权限拼接位置。
- 逻辑外键关系。
- partial unique index 对应的软删除语义。
- 树结构查询、`ancestors` 匹配策略。
- 聚合字段、去重、分页前后语义。

禁止注释：

- 查询用户。
- 查询列表。
- 根据 ID 查询。
- 插入数据。

XML 注释应保持短句，避免大段解释挤压 SQL 可读性。

## Vue 页面与组合逻辑注释规范

Vue 注释只解释页面状态、权限边界和组合逻辑原因。

建议注释：

- 为什么页面需要本地 permission denied 状态。
- 为什么某些 loading、empty、error 状态必须独立维护。
- 为什么选择 Drawer、Modal、Tree、Tabs 等 Ant Design Vue 组件组合。
- 为什么某个 watcher、computed、route guard 存在。
- 当前页面与后端接口的 scaffold 或 Deferred 边界。

禁止注释：

- 点击按钮。
- 打开弹窗。
- 设置 loading。
- 调用接口。

## TypeScript API client 注释规范

API client 注释用于说明契约差异和边界，不用于重复 endpoint。

建议注释：

- 后端字段与前端类型不完全一致时。
- 当前接口是 scaffold，后续会接真实权限或动态路由。
- 响应 envelope 兼容策略。
- 权限码、路由、菜单之间的映射关系。

禁止为每个普通 `get/post/put` 方法写“调用某接口”。

## TODO 替代规范

禁止孤立 `TODO`。

允许使用以下结构化标签：

- `Deferred:` 当前明确暂缓，后续阶段处理。
- `Risk:` 当前存在风险，需要明确影响和处理方向。
- `Debt:` 当前可接受的技术债，需要未来偿还。
- `Extension:` 未来扩展点，不承诺当前实现。
- `Invariant:` 不变量或必须保持的约束。

每条标签注释必须写完整句子。

## 禁止无意义注释规范

以下注释类型禁止出现：

- 复述代码行为。
- 解释语法本身。
- 用中文翻译方法名。
- 为 getter、setter、简单赋值、简单 return 写注释。
- 写“待优化”“后续处理”“TODO”但不说明原因和方向。
- 写 AI 执行痕迹，例如“这里由 Agent 生成”。

## 验收标准

- 已创建 `specs/004-code-comment-governance/` 四件套。
- 规范覆盖用户要求的十类注释治理范围。
- 文档中文主导。
- 未修改 Java、Vue、SQL、XML 业务代码。
- 已列出优先重构模块、不建议立即重构模块和后续实施顺序。
