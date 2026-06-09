# PR 与提交检查清单

本清单是强制工程规范，不是参考文档。所有提交和 PR 前必须完成检查。

## 范围检查

- 是否以 PRD 和当前 Feature Spec 为最高依据。
- 是否只修改当前任务要求的模块。
- 是否避免无关重构、无关格式化和无关依赖变更。
- 是否已经读取当前 Feature 的 `progress.md`。
- 是否按阶段更新 `progress.md`。

## 禁止提交内容

以下内容不得进入提交：

- `docs/prd-generation-context.md`
- `platform-core/**/target/`
- `platform-ui/dist/`
- `node_modules/`
- `.class`
- `maven-status`
- 临时日志、运行时缓存、IDE 私有文件和无关测试输出。

## 后端检查

- 父工程统一管理依赖版本，子模块不擅自固定冲突版本。
- 新增 Controller、Service、Mapper、权限和审计逻辑具备必要注释。
- 授权、认证、审计、数据权限不能只有前端控制。
- 异常响应、分页、参数校验和事务边界已验证。
- 已执行后端格式、Checkstyle 和测试，或说明本轮不适用原因。

## 前端检查

- 使用 Ant Design Vue 成熟组件和后台管理实践。
- 页面包含 loading、empty、error、disabled 和权限不足状态。
- 权限按钮、动态路由和接口 401/403 处理一致。
- 未引入无关视觉主题、装饰性页面或重复组件。
- 已执行前端格式、lint 和构建，或说明本轮不适用原因。

## 文档检查

- `spec.md`、`plan.md`、`tasks.md`、`progress.md` 状态一致。
- `progress.md` 使用统一中文结构。
- 已实现、scaffold、intentionally deferred、技术债和未来扩展点区分清楚。
- 验证结果写入 `progress.md`，但不粘贴大段命令输出。

## Governance Review 结论

提交前必须给出结论：

- 本次修改范围。
- 已执行验证。
- 未执行验证及原因。
- Deferred、风险和技术债。
- 明确是否允许提交。
