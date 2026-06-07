# Platform-Core V1 Phase-1 工程骨架建设PRD

## 项目目标

建设一个长期可复用的企业级平台底座（Platform-Core）。

未来所有项目（数字治理、智慧园区、智慧校园、企业运营平台等）均基于此底座开发。

当前阶段仅搭建工程骨架。

禁止开发任何具体业务功能。

---

# 技术栈

## 后端

* JDK 21
* Spring Boot 3.x
* Spring Security
* Spring OAuth2 Client
* MyBatis Plus
* PostgreSQL 16
* Redis 7

## 统一认证

* Keycloak

说明：

认证、登录、用户身份管理由 Keycloak 负责。

业务系统不自行实现登录逻辑。

---

## 前端

* Vue 3
* TypeScript
* Vite
* Pinia
* Vue Router
* Ant Design Vue
* Axios

---

## 部署

* Docker
* Docker Compose

---

# 架构要求

采用 Modular Monolith（模块化单体架构）。

禁止微服务拆分。

禁止引入 Spring Cloud。

---

# 项目目录结构

后端：

platform-core

├── platform-common

公共工具

├── platform-framework

框架配置

├── platform-system

系统基础模块

├── platform-admin

启动模块

└── docs

项目文档

前端：

platform-ui

├── src/api

├── src/components

├── src/layouts

├── src/router

├── src/store

├── src/views

├── src/utils

├── src/locales

└── src/permissions

---

# Phase-1 实施范围

仅完成以下内容：

## 1. 后端工程初始化

完成：

* Maven 多模块工程
* Spring Boot 启动工程
* 配置中心
* 环境配置

要求：

项目能够正常启动。

---

## 2. PostgreSQL集成

完成：

* 数据源配置
* MyBatis Plus配置
* Flyway数据库版本管理

要求：

项目启动后能够连接数据库。

---

## 3. Redis集成

完成：

* Redis配置
* Redis连接测试

要求：

项目启动正常。

---

## 4. Keycloak集成

完成：

* OIDC登录配置
* Spring Security配置
* Token解析

要求：

能够识别登录用户身份。

当前阶段不开发用户管理页面。

---

## 5. 前端工程初始化

完成：

* Vue3项目初始化
* Ant Design Vue集成
* Pinia集成
* Router集成
* Axios封装

要求：

项目能够正常运行。

---

## 6. 基础布局

完成：

* 登录页
* 主布局
* 顶部导航
* 左侧菜单
* 工作区页面

注意：

全部使用静态假数据。

不要开发业务页面。

---

## 7. Docker环境

完成：

docker-compose.yml

包含：

* PostgreSQL
* Redis
* Keycloak

要求：

一键启动开发环境。

---

## 8. 项目文档

生成：

README.md

内容包含：

* 环境要求
* 启动方式
* Docker启动方式
* 项目结构说明

---

## 9. AI协作规范

创建：

AGENTS.md

内容包括：

### 开发原则

新增模块必须遵循：

Controller

↓

Service

↓

Mapper

↓

Entity

---

### 数据库原则

禁止直接修改生产表。

必须通过 Flyway 管理。

---

### 权限原则

认证由 Keycloak 管理。

业务模块只负责权限映射。

---

### 文档原则

新增模块必须同步更新 docs。

---

# 当前阶段禁止开发

禁止开发：

* 用户管理
* 组织管理
* 角色管理
* 权限管理
* 菜单管理
* 文件中心
* 消息中心
* AI中心
* 工作流
* IoT模块
* 任何业务模块

---

# 完成标准

完成后输出：

1. 项目目录树
2. Docker Compose配置
3. 后端启动截图说明
4. 前端启动截图说明
5. Keycloak集成说明
6. README
7. AGENTS.md

完成后停止。

等待下一阶段任务。
