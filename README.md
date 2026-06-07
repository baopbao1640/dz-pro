# Platform Core

企业级平台底座项目。

## 环境要求

- Java 17+
- Maven 3.8+
- Node.js 18+
- PostgreSQL 14+
- Redis 6+
- Keycloak 21+

## 项目结构

```
.
├── docker-compose.yml
├── platform-core/
│   ├── platform-framework/
│   ├── platform-admin/
│   ├── platform-system/
│   └── platform-common/
├── platform-ui/
│   ├── src/
│   │   ├── views/
│   │   ├── layouts/
│   │   ├── router/
│   │   └── store/
│   ├── package.json
│   └── vite.config.ts
```

## 启动方式

### 1）启动基础设施（Docker）

```bash
docker compose up -d
```

服务包含：
- PostgreSQL：`127.0.0.1:5432`，库名 `platform_core`，用户 `platform_admin`
- Redis：`127.0.0.1:6379`
- Keycloak：`127.0.0.1:8080`

默认密码均为 `platform_admin`（通过环境变量 `POSTGRES_PASSWORD` / `KC_DB_PASSWORD` 控制）。

### 2）后端启动

```bash
cd platform-core
mvn clean install -DskipTests
cd platform-admin
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

默认端口：`8081`

### 3）前端启动

```bash
cd platform-ui
pnpm install
pnpm dev
```

默认端口：`5173`

## 验证联调

- 前端页面：`http://localhost:5173`
- 后端健康：`http://localhost:8081/actuator/health`
- OAuth2 登录跳转：`http://localhost:8081/oauth2/authorization/keycloak` 应跳转 Keycloak `platform` realm

## 常用命令

```bash
# 初始化数据库
docker compose exec postgres pg_isready -U platform_admin -d platform_core

# 构建
cd platform-core && mvn clean install -DskipTests

# 测试
cd platform-admin && mvn test
```
