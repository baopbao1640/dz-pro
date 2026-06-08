# Spec Kit 开发流程

## 适用范围

以下工作必须走 Spec Kit：

- 新增后端模块、前端页面、API、数据库表、权限点。
- 用户管理、角色管理、权限管理、审计日志。
- 在平台底座上扩展新的业务系统或业务数据库内容。

以下维护任务可以不建 feature spec：

- typo、文档小修、格式化、CI 小修、依赖锁文件同步。
- 不改变接口、数据库、页面、权限、业务行为的健康检查。

## 功能目录

每个功能使用独立目录：

```text
specs/<number>-<feature>/
├── spec.md
├── plan.md
├── tasks.md
└── progress.md
```

`progress.md` 是必需文件。没有进度文档时，不允许进入实现。

## 标准流程

1. 创建或更新 `spec.md`，明确用户目标、范围、验收标准。
2. 澄清需求，补齐业务边界、数据边界、权限边界。
3. 创建或更新 `plan.md`，明确模块、接口、数据迁移、前端页面和测试策略。
4. 创建或更新 `tasks.md`，拆分可执行任务。
5. 创建或读取 `progress.md`，确认当前状态、风险和下一步。
6. 实现任务。
7. 每完成一个任务，更新 `progress.md`。
8. 完成功能后，写入 completion section，并记录验证和后续风险。

## 进度文档要求

每条进度记录必须包含：

- 日期时间
- 当前任务
- 已完成内容
- 验证结果
- 下一步计划
- 当前风险或阻塞
- 相关 commit、PR、CI 链接，若已有

推荐初始模板：

```markdown
# Progress

## Current Status

- Status: Not started
- Last updated: YYYY-MM-DD HH:mm
- Current task: Initialize feature progress

## Completed

- Created feature progress document.

## Verification

- Not run yet.

## Next Plan

- Read `spec.md`, confirm scope, then create or update `plan.md`.

## Risks / Blockers

- None identified.

## Activity Log

### YYYY-MM-DD HH:mm - Initialize progress

- Current task: Initialize feature progress
- Completed: Created `progress.md`
- Verification: Not run
- Next plan: Continue with feature planning
- Risks/blockers: None
- Links: N/A
```

## 检查命令

```bash
scripts/check-spec-progress.sh
```

该脚本检查所有 `specs/*/` 功能目录是否同时包含 `spec.md`、`plan.md`、`tasks.md`、`progress.md`。
