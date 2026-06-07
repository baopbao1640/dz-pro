# 项目目录树（三层）
.
├── AGENTS.md
├── README.md
├── one.md
├── docker-compose.yml
├── platform-core/
│   ├── platform-admin/
│   │   └── src/
│   ├── platform-common/
│   ├── platform-framework/
│   │   └── src/
│   ├── platform-system/
│   └── pom.xml
├── platform-ui/
│   ├── src/
│   │   ├── layouts/
│   │   ├── locales/
│   │   ├── permissions/
│   │   ├── router/
│   │   ├── store/
│   │   ├── utils/
│   │   ├── views/
│   │   ├── App.vue
│   │   ├── main.ts
│   │   └── ...
│   ├── index.html
│   ├── vite.config.ts
│   ├── package.json
│   ├── package-lock.json
│   └── tsconfig.json
└── tmp-mp/
    └── pom.xml

# 说明
# 后端模块（platform-admin 等）实际 target/ 在构建后生成，仅在 Maven 构建目录生成时存在；
# 前端依赖目录 node_modules/ 为运行时生成目录，不纳入结构统计。