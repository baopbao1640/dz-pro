# 当前阶段

006-code-comment-standardization 实施阶段已完成本轮注释标准化、脚本门禁和验证，当前停止在提交前审阅状态。

# 当前目标

基于已提交的 `governance/comments/code-comment-policy.md`，并参考阿里巴巴 Java 开发手册中注释应准确反映设计思想、业务逻辑和代码意图的原则，对平台底座进行可移交、可维护、可强制检查的注释标准化。

# 当前完成情况

- 已读取 `governance/` 强制工程规范入口。
- 已读取 `specs/000-documentation-governance/`。
- 已读取 `specs/004-code-comment-governance/`。
- 已读取 `specs/005-security-authz-hardening/`。
- 已创建 006 Spec 四件套。
- 已新增 `scripts/check-comments.sh`，可检查核心 Java 类头 Javadoc、public 入口方法 Javadoc、framework Boundary / Deferred / Risk、孤立 `TODO` 和明显低价值中文注释。
- 已更新 `governance/testing/governance-checks.md`，把 `scripts/check-comments.sh` 纳入每次开发后的强制检查。
- 已更新 `governance/review/pr-checklist.md`，补充注释治理 Review 项。
- 已完成 006A `platform-framework` 中 config、security、audit、web 重点类注释标准化。
- 已完成 006B `platform-system` 中 auth、audit、user、role、menu、dept、post 核心 Controller、Service、ServiceImpl、Mapper 类头注释标准化。
- 已完成 006C 前端 router、permissions、store、auth API client 和 system 页面结构注释标准化。
- 已确认未修改 SQL、API 路径、权限逻辑、返回结构或业务流程。

# 当前架构决策

- 本阶段是注释标准化实施，不继续扩展抽象规范。原因是 `004` 已经定义注释治理原则，006 必须把规则落到代码和脚本门禁。
- 采用“有价值注释强制化”，不机械覆盖所有代码。原因是复述型注释会降低可维护性，并违背治理入口要求。
- 检查脚本先采用启发式规则。原因是当前目标是建立本地可执行门禁，不引入 Checkstyle/ESLint 插件扩展或复杂 AST 解析。
- 分批覆盖 framework、system、frontend。原因是跨全项目一次性扫改会放大 review 噪音和冲突风险。
- 前端注释只补充权限、路由、API client 和后台页面结构边界。原因是 Vue 页面大量交互细节如果机械补注释会变成低价值噪音。

# 已实现能力

- 006 Feature 目录和四件套。
- 注释治理检查脚本。
- governance 强制检查入口更新。
- framework 关键安全、授权、审计、数据权限、动态路由类的 Boundary / Deferred / Risk 注释。
- system 核心管理域 Controller、Service、ServiceImpl、Mapper 类头注释。
- auth/audit/provider/permission/data-scope/dynamic-route 关键 public 方法注释。
- 前端动态路由、权限判断、用户 store、auth API client 和系统管理页面结构注释。

# Deferred（暂缓事项）

- 暂不接入 Maven、Checkstyle、ESLint 或 CI，只通过独立 shell 脚本作为本地门禁。
- 暂不要求 DTO、VO、Entity、简单 getter/setter 补注释。
- 暂不修改 SQL、API、权限逻辑、返回结构或业务流程。
- 暂不处理业务风险本身，只通过注释记录 Boundary / Deferred / Risk。
- 暂不全量审计所有普通 CRUD public 方法的逐方法 Javadoc；本轮优先覆盖 auth、audit、provider、permission、data-scope、dynamic-route 和核心类头，避免产生复述型注释。

# 风险与技术债

- 风险：启发式脚本可能存在漏报或少量误报。当前接受原因是本阶段先建立可执行门禁，后续可逐步增强解析精度。
- 风险：注释标准化如果混入格式化或逻辑变更，会影响 review。处理方向是本阶段严格限制 diff，只允许注释、脚本和文档变更。
- 技术债：DTO、VO、Entity、普通 CRUD 方法和 MyBatis XML 非显然 SQL 注释尚未全量治理。处理方向是后续按 006 后续批次继续推进，仍坚持有价值注释而不是机械覆盖。
- 风险：`pnpm build` 会生成 `platform-ui/dist/`，Maven 会更新 `platform-core/**/target/`。这些都是构建产物，必须保持不提交。

# 下一阶段计划

- 人工审阅本轮 diff，确认只有注释、脚本和文档治理变更。
- 提交前只 stage 本轮允许文件，排除 `target/`、`dist/`、`node_modules/`、`docs/prd-generation-context.md`。
- 后续如继续注释治理，优先处理 MyBatis XML 非显然 SQL 注释和普通 CRUD public 方法中确有边界/风险的方法。

# 验证结果

- `bash scripts/check-comments.sh`：通过。
- `cd platform-core && mvn spotless:check checkstyle:check test`：通过。PowerShell 无 `mvn`，实际使用 WSL 路径执行：`bash -lc "cd /mnt/f/AIworkspance/content/platform-core && mvn spotless:check checkstyle:check test"`。
- `cd platform-ui && pnpm lint`：通过。实际使用 WSL 路径执行。
- `cd platform-ui && pnpm build`：通过，仍存在 Vite 大 chunk warning；这是既有前端构建风险，不属于本阶段业务逻辑变更。
- `scripts/check-spec-progress.sh`：通过。实际使用 WSL 路径执行。
- `git diff --check`：通过，仅输出 LF/CRLF 工作区提示，无 whitespace error。
- 已运行静态搜索确认目标范围内无孤立 `TODO`、明显复述型中文注释，以及 Javadoc 放在常见注解之后的格式风险。

# Agent 协作备注

本阶段由单 Agent 串行处理注释标准化，避免多个 Agent 同时修改同一批 Java/Vue 文件造成注释冲突。后续如果多 Agent 继续推进，必须按 framework、system 子域、frontend 子域拆分，禁止全项目扫改。
