# System Management Database Design Tasks

## Phase 1: Documentation Spec

- [x] Create feature directory `specs/001-system-database-design/`.
- [x] Create `spec.md`.
- [x] Document Keycloak authentication boundary.
- [x] Document Flyway-only schema management rule.
- [x] Document unified audit fields.
- [x] Document MyBatis Plus `ASSIGN_ID` bigint primary key strategy.
- [x] Document logical foreign key plus application-layer validation strategy.
- [x] Document RBAC relationships.
- [x] Document `sys_menu` directory, menu, and button support.
- [x] Document role data permission scope.
- [x] Document PostgreSQL partial unique indexes for active soft-deleted records.
- [x] Document reduced event fields for append-only log tables.
- [x] Document future monthly archive, partitioning, and cold-data cleanup capability.
- [x] Document Quartz as the scheduler technology.
- [x] Document first-phase seed data requirements.
- [x] Document log table performance requirements.
- [x] Create `plan.md`.
- [x] Create `tasks.md`.
- [x] Create `progress.md`.

## Phase 2: Confirmed Design Decisions

- [x] Confirm primary key strategy: `bigint`, application-side MyBatis Plus `ASSIGN_ID`, Snowflake-style ID.
- [x] Confirm foreign key strategy: logical foreign keys plus application-layer validation; no PostgreSQL physical foreign keys.
- [x] Confirm audit identity format: `create_by` and `update_by` store `sys_user.id` as `varchar(64)`.
- [x] Confirm `sys_user` stores only business user mapping and no password, session, refresh token, or MFA information.
- [x] Confirm initial seed data categories.
- [x] Confirm current log partition strategy: no PostgreSQL partitioning now, reserve future archive, partitioning, and cold-data cleanup.
- [x] Confirm scheduler engine: Quartz.

## Phase 3: Confirmed SQL Inputs

- [x] Confirm default administrator mapping uses temporary `keycloak_user_id = 'admin-keycloak-sub'`.
- [x] Confirm seed IDs use fixed `bigint` values starting from `1000000000000000001`.
- [x] Confirm basic dictionary types: `sys_normal_disable`, `sys_yes_no`, `sys_user_sex`, `sys_notice_type`, `sys_job_status`.
- [x] Confirm default log retention period: 180 days.

## Phase 4: Flyway Implementation

Scope restriction: implement Flyway SQL only. Do not create Java, Vue, Mapper, Service, or Controller files.

- [x] Locate the current Flyway migration directory in `platform-core`.
- [x] Create the first system management Flyway migration SQL.
- [x] Define all 17 tables.
- [x] Define indexes and PostgreSQL partial unique indexes.
- [x] Add confirmed seed data.
- [x] Run migration verification against a clean local database.
- [x] Run `cd platform-core && mvn spotless:check checkstyle:check && mvn test`.
- [x] Update `progress.md` with implementation results.

## Phase 5: Later Backend and Frontend Features

Do not start this phase as part of database design.

- [ ] Create separate feature specs for user management.
- [ ] Create separate feature specs for role management.
- [ ] Create separate feature specs for permission/menu management.
- [ ] Create separate feature specs for department and post management.
- [ ] Create separate feature specs for dictionary, config, notice, logs, and jobs.
