# PR 与提交检查清单

本清单是强制工程规范，不是参考文档。所有提交和 PR 前必须完成检查。

## 006 注释治理状态

`006-code-comment-standardization` 已进入长期增量治理模式。

后续不再做“大爆炸式全项目补注释”，而是在 Feature 开发过程中持续治理新增和修改代码。PR Review 必须确认本次变更范围内的注释符合 `governance/comments/code-comment-policy.md`，并确认未引入低价值注释噪音。

## 范围检查

- 是否以 PRD 和当前 Feature Spec 为最高依据。
- 是否只修改当前任务要求的模块。
- 是否避免无关重构、无关格式化和无关依赖变更。
- 是否已经读取当前 Feature 的 `progress.md`。
- 是否按阶段更新 `progress.md`。
- 是否已经读取 governance，并在开发后执行 Governance Review。

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
- 核心 Controller、Service、Provider、Handler、Aspect、Security、Audit、DataScope、Permission、Mapper 接口具备中文类头 Javadoc。
- public controller/service/provider/authz/audit/data-scope/security handler 方法具备中文 Javadoc，且说明目的、规则、边界、权限或异常影响。
- framework 关键类已使用 Boundary / Deferred / Risk 说明职责边界、暂缓事项和风险。
- 不存在孤立 `TODO` 和复述代码的低价值中文注释。
- 授权、认证、审计、数据权限不能只有前端控制。
- 异常响应、分页、参数校验和事务边界已验证。
- 已执行后端格式、Checkstyle 和测试，或说明本轮不适用原因。

## 前端检查

- 使用 Ant Design Vue 成熟组件和后台管理实践。
- 页面包含 loading、empty、error、disabled 和权限不足状态。
- 权限按钮、动态路由和接口 401/403 处理一致。
- 修改 router、permissions、store、API client 或复杂页面权限状态时，必须同步检查有效注释。
- 未引入无关视觉主题、装饰性页面或重复组件。
- 已执行前端格式、lint 和构建，或说明本轮不适用原因。

## 文档检查

- `spec.md`、`plan.md`、`tasks.md`、`progress.md` 状态一致。
- `progress.md` 使用统一中文结构。
- 已实现、scaffold、intentionally deferred、技术债和未来扩展点区分清楚。
- 验证结果写入 `progress.md`，但不粘贴大段命令输出。
- 006 相关 Deferred 不得被后续 Feature 默认为必须一次性全量补齐；应按实际变更范围增量治理。

## Governance Review 结论

提交前必须给出结论：

- 本次修改范围。
- 已执行验证。
- `scripts/check-comments.sh` 执行结果。
- 未执行验证及原因。
- Deferred、风险和技术债。
- 注释治理是否符合长期增量治理原则。
- 明确是否允许提交。
