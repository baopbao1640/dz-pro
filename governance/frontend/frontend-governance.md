# 前端治理规范

本规范是强制工程规范，不是参考文档。平台前端采用后台管理系统实践，优先参考 Ant Design Vue 和 Ant Design Pro 的成熟模式。

## 基本原则

- 前端开发必须服务 PRD 和当前 Feature，不新增无关页面、动效或视觉主题。
- 后台管理界面优先追求清晰、稳定、可扫描和可维护。
- 优先使用 Ant Design Vue 成熟组件，不重复手写已有组件。
- 布局、表格、表单、弹窗、反馈、权限状态必须遵守一致模式。
- 新增页面必须覆盖 loading、empty、error、disabled 和权限不足状态。

## 组件使用

- 表单使用 `Form`、`Input`、`Select`、`DatePicker`、`Switch`、`Radio`、`Checkbox`。
- 数据展示使用 `Table`、`Descriptions`、`List`、`Statistic`。
- 操作反馈使用 `Message`、`Modal`、`Notification`、`Popconfirm`。
- 布局使用 `Layout`、`Row`、`Col`、`Space`、`Flex`。
- 图标统一使用 `@ant-design/icons-vue`。
- 权限按钮必须通过统一权限判断能力控制显示或禁用。

## 后台管理页面规则

- 列表页必须明确搜索区、操作区、表格区和分页区。
- 表单页必须明确必填、校验、提交中、提交失败和返回路径。
- 危险操作必须有二次确认。
- 权限不足不能只隐藏关键入口，必要时必须提供明确的禁用或 403 状态。
- 页面不允许大面积自定义视觉风格覆盖 Ant Design 体系。
- `Card` 只用于必要的信息分组或重复项，禁止层层嵌套卡片。

## 路由与权限

- 动态路由必须来自后端授权结果或明确的 scaffold 契约。
- 前端权限判断只能作为用户体验控制，不能替代后端授权。
- 新增权限点必须同步 PRD、Spec、后端 enforce、前端路由或按钮控制。
- 权限不足、接口 401、接口 403 必须有统一处理路径。

## 验证要求

每次前端修改后必须按任务范围执行：

- `pnpm format`
- `pnpm lint`
- `pnpm format:check`
- `pnpm build`

若本轮未修改前端代码，可在 Governance Review 中明确“不适用”及原因。
