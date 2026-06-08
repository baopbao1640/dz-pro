# System Management Database Design Plan

## 1. Scope

This plan covers database design and the first Flyway SQL implementation phase for the system management domain. It does not include Java, Vue, Mapper, Service, or Controller code.

In scope:

- Confirm table responsibilities and relationships.
- Confirm primary key, logical foreign key, audit field, status flag, and soft-delete strategy.
- Confirm Keycloak authentication boundary.
- Confirm RBAC data model.
- Confirm log table performance requirements.
- Confirm Quartz as the scheduler technology.
- Confirm initial seed data requirements.
- Prepare implementation tasks for later SQL migration work.

Out of scope:

- Java code.
- Vue code.
- Java Entity, Mapper, Service, Controller, and Vue implementation.
- Spring Cloud.
- ORM auto DDL.
- Local login implementation.

## 2. Architecture Decision

The system management domain will be modeled as platform-owned relational tables inside the current modular monolith.

Authentication remains outside the platform database and is owned by Keycloak. The local `sys_user` table stores business user mapping and platform metadata only.

Primary keys are standardized across the system management domain:

- Type: `bigint`
- Generation: application side
- MyBatis Plus strategy: `ASSIGN_ID`
- Algorithm style: Snowflake-style ID

UUID primary keys, auto-increment primary keys, and mixed primary key strategies are forbidden.

The platform adopts a logical foreign key plus application-layer validation strategy. PostgreSQL physical foreign keys are not used in the current phase. Query performance is protected through explicit indexes.

Reasons:

- Lower migration complexity.
- Fewer restrictions during batch imports.
- Lower module coupling.
- Better operational flexibility.

Audit fields use local platform user identity. `create_by` and `update_by` store `sys_user.id` as `varchar(64)`. They must not store Keycloak tokens, Keycloak subjects, email addresses, or display names.

RBAC is represented by local system tables:

- Users: `sys_user`
- Roles: `sys_role`
- Permission resources: `sys_menu`
- User-role assignments: `sys_user_role`
- Role-menu assignments: `sys_role_menu`
- Data permission scope: `sys_role.data_scope` and `sys_role_dept`

Schema creation and later schema changes must be implemented through Flyway migrations. The application must not auto-create or auto-update tables.

## 3. Files

Created in this feature:

- `specs/001-system-database-design/spec.md`: system management database design specification.
- `specs/001-system-database-design/plan.md`: planning document for database design and later migration implementation.
- `specs/001-system-database-design/tasks.md`: task checklist for confirmation and future execution.
- `specs/001-system-database-design/progress.md`: progress record for this feature.
- `platform-core/platform-admin/src/main/resources/db/migration/V2__create_system_management_tables.sql`: first system management Flyway migration.

No Java, Vue, Mapper, Service, Controller, Maven, or npm files are created in this step.

## 4. Table Groups

### 4.1 Organization and User Mapping

- `sys_user`
- `sys_dept`
- `sys_post`
- `sys_user_post`

Design intent:

- Keep Keycloak identity separate from platform business metadata.
- Support department tree and user post assignment.
- Support disabled and soft-deleted users without controlling Keycloak authentication state directly.
- Ensure `sys_user` never stores password, session, refresh token, or MFA information.

### 4.2 RBAC and Data Permission

- `sys_role`
- `sys_menu`
- `sys_user_role`
- `sys_role_menu`
- `sys_role_dept`

Design intent:

- Use role-based access control.
- Use `sys_menu` as the resource tree for directories, menus, and buttons.
- Use `data_scope` and `sys_role_dept` for row-level or department-level data permissions.

### 4.3 Dictionary, Config, and Notice

- `sys_dict_type`
- `sys_dict_data`
- `sys_config`
- `sys_notice`

Design intent:

- Keep dictionary and configuration data auditable and soft deletable.
- Keep notice publishing state explicit.

### 4.4 Logs and Jobs

- `sys_oper_log`
- `sys_login_log`
- `sys_job`
- `sys_job_log`

Design intent:

- Logs are append-oriented and should not block main flows.
- Log tables use reduced event fields instead of full unified audit fields.
- Job configuration is separated from job execution logs.
- Quartz is the confirmed scheduler technology for later implementation.
- Later implementation must use asynchronous or fault-tolerant log writing.

## 5. Flyway Migration Plan for Later Implementation

The current Flyway migration directory is `platform-core/platform-admin/src/main/resources/db/migration`.

The first system management migration file is `V2__create_system_management_tables.sql`.

Recommended migration split:

1. Create system management base tables.
2. Create indexes and unique constraints.
3. Create partial unique indexes for active records on soft-deleted business tables.
4. Insert required initial seed data.
5. Add optional comments for tables and columns, if the team wants database-level documentation.

Required implementation rules:

- Use versioned Flyway migrations.
- Do not enable Hibernate or MyBatis automatic DDL.
- Do not create tables from application startup logic.
- Keep SQL PostgreSQL-compatible unless the project database target changes.
- Do not use PostgreSQL physical foreign keys in the current phase.
- Use logical relationships plus application-layer validation.
- Use PostgreSQL partial unique indexes with `WHERE del_flag = '0'` for unique constraints on active soft-deleted records.
- Document rollback handling before executing against shared environments.

Initial seed data required in the first Flyway implementation phase:

- Super administrator role.
- `system` management directory.
- User management menu.
- Role management menu.
- Menu management menu.
- Department management menu.
- Post management menu.
- Default administrator user mapping.
- Basic dictionary types.

Seed data restrictions:

- Do not create a default password.
- Do not create local authentication logic.
- The default administrator user mapping uses temporary `keycloak_user_id = 'admin-keycloak-sub'`.
- Seed IDs use fixed `bigint` values starting from `1000000000000000001`.
- Basic dictionary types are `sys_normal_disable`, `sys_yes_no`, `sys_user_sex`, `sys_notice_type`, and `sys_job_status`.

## 6. Log Performance Plan

Log tables must not affect main business performance.

Later implementation should follow these rules:

- Operation log writes should be asynchronous or queued.
- Login log writes should not block token callback or user entry flow.
- Job log writes should not block scheduler state transitions.
- Logging failure should be observable but must not fail the main business action.
- Sensitive fields must be masked before persistence.
- `sys_oper_log`, `sys_login_log`, and `sys_job_log` do not include `update_by`, `update_time`, `remark`, or `del_flag`.
- Current phase does not use PostgreSQL partitioning.
- Default retention is 180 days.
- The design reserves future support for monthly archive, partitioning, and cold-data cleanup through event time indexes and retention planning.

## 7. Verification Plan

For this implementation step:

- Run `scripts/check-spec-progress.sh`.
- Run `git diff --check -- specs/001-system-database-design`.
- Confirm no Java or Vue business files were changed.
- Run backend migration tests or application startup against a clean local database.
- Run `cd platform-core && mvn spotless:check checkstyle:check && mvn test`.
- Confirm generated tables, indexes, unique constraints, and seed data.
- Confirm no auto DDL is enabled.

## 8. Confirmed SQL Implementation Inputs

The following decisions are confirmed and used by `V2__create_system_management_tables.sql`:

- Primary key strategy: `bigint`, application-side MyBatis Plus `ASSIGN_ID`, Snowflake-style ID.
- Foreign key strategy: logical foreign keys plus application-layer validation; no PostgreSQL physical foreign keys.
- Audit identity format: `create_by` and `update_by` store local `sys_user.id` as `varchar(64)`.
- Initial seed data: include the required administrator role, system menus, default administrator user mapping, and basic dictionary types.
- Default administrator Keycloak mapping: `admin-keycloak-sub`.
- Seed ID strategy: fixed `bigint` IDs starting from `1000000000000000001`.
- Basic dictionary types: `sys_normal_disable`, `sys_yes_no`, `sys_user_sex`, `sys_notice_type`, and `sys_job_status`.
- Log storage strategy: no PostgreSQL partitioning in the current phase; reserve monthly archive, partitioning, and cold-data cleanup.
- Log retention strategy: 180 days by default.
- Scheduler engine: Quartz.

The following details remain for later production hardening:

- Replace temporary `admin-keycloak-sub` with the real Keycloak subject before production use.
- Decide when to add log archive, partitioning, and cold-data cleanup jobs.

## 9. Completion Criteria for This Step

- `spec.md` exists and covers all requested tables.
- `plan.md` exists and documents migration and performance strategy.
- `tasks.md` exists and keeps implementation work deferred.
- `progress.md` exists and records this documentation step.
- `V2__create_system_management_tables.sql` exists in the current Flyway migration directory.
- The repo contains no new Java or Vue business code for this feature.
- The implementation is limited to Phase 4 Flyway SQL and Spec progress updates.
