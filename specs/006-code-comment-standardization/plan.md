# Code Comment Standardization Plan

## 当前策略

采用“先门禁、后分批覆盖”的策略实施注释标准化：

1. 先创建 006 Spec 四件套，明确本阶段只做注释标准化实施。
2. 新增 `scripts/check-comments.sh`，把可强制检查的规则落地为启发式门禁。
3. 更新 governance 文档，把注释检查加入每次开发后的强制检查。
4. 按 006A、006B、006C 分批补充有价值注释，不做全项目扫改。
5. 更新 `progress.md`，记录已覆盖范围、Deferred、风险和验证结果。

## 实施边界

允许：

- 修改 Java 注释。
- 修改 Vue / TypeScript 注释。
- 新增 `scripts/check-comments.sh`。
- 更新 `governance/testing/governance-checks.md`。
- 更新 `governance/review/pr-checklist.md`。
- 创建和更新 `specs/006-code-comment-standardization/`。

禁止：

- 修改 Java 控制流、条件、返回值、方法签名、依赖关系。
- 修改 Vue / TypeScript 运行逻辑、路由逻辑、API 路径或状态结构。
- 修改 SQL、Mapper XML 查询语义或 Flyway。
- 修改权限逻辑、认证逻辑、返回结构。
- 自动 git commit。

## 006A：platform-framework

目标文件：

- `platform-core/platform-framework/src/main/java/com/platform/core/framework/config/SecurityConfig.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/web/GlobalExceptionHandler.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/web/*.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/authz/*.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/security/context/*.java`
- `platform-core/platform-framework/src/main/java/com/platform/core/framework/audit/*.java`
- framework 下 data scope、dynamic route 相关类。

注释重点：

- Keycloak 认证边界。
- permission code enforce 边界。
- data scope 当前阶段能力和未全局注入风险。
- audit 异步、脱敏、失败不阻塞边界。
- 统一异常响应边界。

## 006B：platform-system

目标文件：

- `platform-core/platform-system/src/main/java/com/platform/core/system/auth/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/audit/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/user/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/role/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/menu/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/dept/**`
- `platform-core/platform-system/src/main/java/com/platform/core/system/post/**`

注释重点：

- `SystemCurrentUserProvider` 的 Keycloak `sub` 到本地用户映射边界。
- `SystemPermissionService` 的 permission code 来源和超级管理员规则。
- `SystemDynamicRouteService` 的菜单树和前端路由契约。
- `SystemDataScopeConditionProvider` 的数据权限条件边界。
- `SystemAuditEventHandler` 的审计落库、脱敏和失败处理。
- Controller、ServiceImpl、Mapper 接口的职责边界。

## 006C：前端

目标文件：

- `platform-ui/src/router/`
- `platform-ui/src/permissions/`
- `platform-ui/src/store/`
- `platform-ui/src/api/system/`
- `platform-ui/src/views/system/`

注释重点：

- 动态路由注入边界。
- 按钮权限判断只作为体验控制，不能替代后端授权。
- API client 与后端 envelope、401、403 的边界。
- Ant Design Vue 后台页面结构和状态边界。

## 检查脚本策略

`scripts/check-comments.sh` 使用启发式检查，不追求完美语义解析：

- 用文件名、包路径和类名识别核心 Java 类。
- 用近邻 Javadoc 检查类头和 public 方法注释。
- 跳过 DTO、VO、Entity、简单 record、getter、setter。
- 重点强制 framework 关键类包含 Boundary、Deferred、Risk。
- 用正则识别孤立 TODO 和明显复述型中文注释。

## 验证计划

执行：

```bash
bash scripts/check-comments.sh

cd platform-core
mvn spotless:check checkstyle:check test

cd ../platform-ui
pnpm lint
pnpm build

cd ..
scripts/check-spec-progress.sh
git diff --check
```

## 不做事项

- 不补全所有 DTO、VO、Entity 注释。
- 不把注释检查接入 Maven、Checkstyle 或 ESLint。
- 不修复 006 发现的业务逻辑风险。
- 不提交本阶段变更。
