# 前后端编程规范

## 基本原则

- PRD 是最高依据，禁止脱离 PRD 任意扩展功能。
- 修改必须保持最小有效范围，不借规范化做无关重构。
- 新增和修改代码必须通过本地检查与 GitHub Actions。
- 后端依赖和插件版本统一由 `platform-core` 父工程管理。
- 前端包管理以 `pnpm-lock.yaml` 为准；`package-lock.json` 暂时保留，不作为安装依据。

## Java 规范

- 类名使用 `PascalCase`。
- 方法、变量、字段使用 `camelCase`。
- 常量使用 `UPPER_SNAKE_CASE`。
- 包名全小写，不使用下划线。
- 禁止星号 import，删除未使用 import。
- Controller 公共接口、Service 接口、公共工具方法、复杂逻辑必须写有效注释。
- 注释应说明对外契约、设计原因、边界条件或非显然规则；禁止写复述代码的低价值注释。

## Vue 和 TypeScript 规范

- 组件文件使用 `PascalCase.vue`。
- 变量和函数使用 `camelCase`。
- 类型、interface、组件名使用 `PascalCase`。
- 常量使用 `UPPER_SNAKE_CASE`。
- store 文件可以保持小写模块名，例如 `user.ts`。
- 禁止未使用变量；确实需要保留的参数使用 `_` 前缀。
- 禁止 `v-html`，除非后续有明确安全处理方案并单独评审。
- 导出函数和公共工具应具备清晰命名；复杂逻辑需要说明原因。

## Ant Design Vue 设计限制

- 页面 UI 优先使用 Ant Design Vue 成熟组件，不重复手写已有组件。
- 表单使用 `Form`、`Input`、`Select`、`DatePicker`、`Switch` 等组件族。
- 数据展示使用 `Table`、`List`、`Descriptions`、`Statistic` 等组件族。
- 反馈使用 `Message`、`Modal`、`Notification`、`Popconfirm`。
- 布局使用 `Layout`、`Row`、`Col`、`Space`、`Flex`。
- 图标统一使用 `@ant-design/icons-vue`。
- 颜色、间距、圆角、阴影优先使用 Ant Design token，不随意写大面积自定义视觉风格。
- `Card` 只用于信息分组或重复项，避免页面层层嵌套卡片。
- 新增页面必须考虑 loading、disabled、empty、error 基础状态。

## 本地检查命令

后端：

```bash
cd platform-core
mvn spotless:apply
mvn spotless:check checkstyle:check
mvn test
```

前端：

```bash
cd platform-ui
pnpm install
pnpm format
pnpm lint
pnpm format:check
pnpm build
```
