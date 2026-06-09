# 当前阶段

系统管理域数据库设计与 Flyway Phase 4 已确认完成。

# 当前目标

为系统管理基础能力建立稳定的数据模型底座，覆盖用户、部门、岗位、菜单、角色、字典、配置、通知、操作日志、登录日志、定时任务和任务日志。

# 当前完成情况

- 已完成 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。
- 已确认 17 张系统管理表的第一版设计。
- 已创建 `platform-core/platform-admin/src/main/resources/db/migration/V2__create_system_management_tables.sql`。
- 已通过 Flyway 管理建表、索引和种子数据。
- 已插入第一阶段种子数据，固定 ID 从 `1000000000000000001` 开始。
- 已写入默认管理员业务映射，`keycloak_user_id = admin-keycloak-sub`。
- 已写入基础字典类型：`sys_normal_disable`、`sys_yes_no`、`sys_user_sex`、`sys_notice_type`、`sys_job_status`。
- 已写入默认日志保留配置：`sys.log.retention.days = 180`。

# 当前架构决策

- 主键统一采用 `bigint`，由应用侧通过 MyBatis Plus `ASSIGN_ID` 生成。原因是避免 UUID、自增主键和混合主键策略导致的长期维护复杂度。
- 平台采用逻辑外键 + 应用层校验策略，不使用 PostgreSQL 物理 foreign key。原因是降低迁移复杂度、批量导入限制和模块耦合。
- `create_by`、`update_by` 统一存储本地 `sys_user.id`，类型为 `varchar(64)`。原因是审计主体应绑定平台业务用户，而不是 Keycloak token、sub 或 email。
- `sys_user` 只保存业务用户映射，不保存密码、session、refresh token 或 MFA 信息。原因是认证边界由 Keycloak 负责。
- 带软删除的唯一业务字段使用 PostgreSQL partial unique index，并统一带 `WHERE del_flag = '0'`。原因是避免软删除数据阻塞后续同名业务记录创建。
- `sys_oper_log`、`sys_login_log`、`sys_job_log` 作为 append-only 日志表，不使用完整通用审计字段。原因是日志写入不应承担主业务表的更新语义。
- 定时任务引擎确认使用 Quartz。原因是当前阶段是 Modular Monolith，不引入 Spring Cloud 或分布式调度。

# 已实现能力

- Flyway-only schema 管理。
- 17 张系统管理表。
- 普通索引与 partial unique index。
- RBAC 关系表：`sys_user_role`、`sys_role_menu`、`sys_role_dept`。
- 菜单支持目录、菜单、按钮。
- 角色支持数据权限范围。
- 字典、配置、通知、日志、任务表的数据底座。
- 第一阶段种子数据。

# Deferred（暂缓事项）

- 暂不实现 Java Entity、Mapper、Service、Controller。
- 暂不实现 Vue 页面。
- 暂不实现本地登录、密码、session、refresh token、MFA。
- 暂不使用 PostgreSQL partition。
- 暂不实现日志月度归档、冷数据清理和分区迁移。
- 暂不实现 Quartz Job 业务执行逻辑。
- 暂不拆分微服务，不引入 Spring Cloud。

# 风险与技术债

- 风险：`admin-keycloak-sub` 是临时占位符。影响范围是默认管理员登录和业务用户映射。处理方向是部署前替换为真实 Keycloak subject。
- 风险：逻辑外键依赖应用层校验。影响范围是数据一致性。处理方向是在 Service 层、批量导入流程和管理工具中补齐引用校验。
- 风险：日志表当前只预留 180 天保留策略。影响范围是长期运行后的存储成本。处理方向是后续实现归档、分区和冷数据清理。
- 技术债：当前数据库设计没有独立 ADR 文件。当前接受原因是 Spec 阶段先在 `progress.md` 记录关键决策。后续如决策变多，应迁移到 ADR。

# 下一阶段计划

- 在后续系统管理实现阶段基于 V2 表结构开发用户、角色、菜单、部门、岗位等能力。
- 在实现阶段继续遵守 Keycloak 认证边界、Flyway-only schema、逻辑外键和 `ASSIGN_ID` 主键策略。
- 在日志功能阶段补充异步写入、归档策略、冷数据清理和必要的查询索引。

# 验证结果

- `scripts/check-spec-progress.sh` 已通过。
- `git diff --check` 已通过。
- 干净 PostgreSQL 环境中 V1 + V2 Flyway migration 已通过。
- Spring Boot / Flyway 校验已通过，数据库达到 version `v2`。
- 数据库检查确认：17 张系统表、9 个 partial unique index、5 个基础字典类型、默认管理员映射和日志保留配置存在。
- `cd platform-core && mvn spotless:check checkstyle:check && mvn test` 已通过。

# Agent 协作备注

本阶段不需要多 Agent 并行。后续开发 Agent 必须先读取本文件，明确数据库设计约束，不得绕过 Flyway 自动建表，不得修改认证边界。
