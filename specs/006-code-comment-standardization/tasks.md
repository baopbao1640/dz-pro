# Code Comment Standardization Tasks

## Phase 1：Spec 与治理入口

- [x] 创建 `specs/006-code-comment-standardization/spec.md`。
- [x] 创建 `specs/006-code-comment-standardization/plan.md`。
- [x] 创建 `specs/006-code-comment-standardization/tasks.md`。
- [x] 创建 `specs/006-code-comment-standardization/progress.md`。
- [x] 更新 `governance/testing/governance-checks.md`，加入 `scripts/check-comments.sh`。
- [x] 更新 `governance/review/pr-checklist.md`，加入注释治理检查项。

## Phase 2：检查脚本

- [x] 新增 `scripts/check-comments.sh`。
- [x] 检查 Java 核心类缺少类头 Javadoc。
- [x] 检查 public Controller/Service/Provider/AuthZ/Audit/DataScope 方法缺少 Javadoc。
- [x] 检查 framework 关键类缺少 Boundary / Deferred / Risk。
- [x] 检查孤立 TODO。
- [x] 检查明显低价值中文注释。
- [x] 确认不误伤 DTO/VO 简单字段类和 getter/setter。

## Phase 3：006A platform-framework

- [x] 标准化 `SecurityConfig` 注释。
- [x] 标准化 `GlobalExceptionHandler` 注释。
- [x] 标准化 security web handler 注释。
- [x] 标准化 `RequiresPermissionAspect` 注释。
- [x] 标准化 `CurrentUser` 和 `CurrentUserProvider` 注释。
- [x] 标准化 audit 相关类注释。
- [x] 标准化 data scope 相关类注释。
- [x] 标准化 dynamic route 相关类注释。

## Phase 4：006B platform-system

- [x] 标准化 `SystemCurrentUserProvider` 注释。
- [x] 标准化 `SystemPermissionService` 注释。
- [x] 标准化 `SystemDynamicRouteService` 注释。
- [x] 标准化 `SystemDataScopeConditionProvider` 注释。
- [x] 标准化 `SystemAuditEventHandler` 注释。
- [x] 标准化 User/Role/Menu/Dept/Post Controller 注释。
- [x] 标准化 User/Role/Menu/Dept/Post ServiceImpl 注释。
- [x] 标准化 Mapper 接口注释。

## Phase 5：006C 前端

- [x] 标准化 `platform-ui/src/router/` 注释。
- [x] 标准化 `platform-ui/src/permissions/` 注释。
- [x] 标准化 `platform-ui/src/store/` 注释。
- [x] 标准化 `platform-ui/src/api/system/` 注释。
- [x] 标准化 `platform-ui/src/views/system/` 重点页面结构注释。

## Phase 6：验证与收尾

- [x] 运行 `bash scripts/check-comments.sh`。
- [x] 运行 `cd platform-core && mvn spotless:check checkstyle:check test`。
- [x] 运行 `cd platform-ui && pnpm lint`。
- [x] 运行 `cd platform-ui && pnpm build`。
- [x] 运行 `scripts/check-spec-progress.sh`。
- [x] 运行 `git diff --check`。
- [x] 更新 `progress.md` 记录覆盖范围、未覆盖范围、验证结果和建议提交信息。

## 验证

- [x] 确认没有修改业务逻辑、SQL、API 路径、权限逻辑或返回结构。
- [x] 确认没有提交或 staging `target/`、`dist/`、`node_modules/`。
- [x] 确认不执行 git commit。
