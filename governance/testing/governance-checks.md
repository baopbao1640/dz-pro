# Governance 验证规范

本规范是强制工程规范，不是参考文档。所有开发结束后必须执行与修改范围匹配的验证。

## 通用检查

每次提交前至少执行：

```bash
git status --short
git diff --check
scripts/check-spec-progress.sh
```

同时必须检查 staged 文件，确认没有构建产物、依赖目录、临时上下文和无关文件。

## 后端检查

修改 Java、Mapper XML、POM、配置、安全、权限、审计或数据库相关内容时，必须执行：

```bash
cd platform-core
mvn spotless:check checkstyle:check test
```

涉及打包、启动、运行时配置或跨模块依赖时，必须追加：

```bash
cd platform-core
mvn package -DskipTests
```

## 前端检查

修改 Vue、TypeScript、路由、权限、状态管理、样式或前端依赖时，必须执行：

```bash
cd platform-ui
pnpm format
pnpm lint
pnpm format:check
pnpm build
```

## 文档与治理检查

修改 `specs/`、`docs/` 或 `governance/` 时，必须执行：

```bash
git diff --check
scripts/check-spec-progress.sh
```

如果只修改 `governance/` 且未修改业务代码，可不执行 Java/Vue 构建，但必须在结论中明确“不适用”。

## 验证结果记录

验证结果必须写入对应 `progress.md` 或本轮交付说明，包含：

- 执行命令。
- 是否通过。
- 失败项和处理结果。
- 未执行项及原因。
- 后续风险和技术债。

不得提交未验证代码；不得用历史验证结果替代本轮必要验证。
