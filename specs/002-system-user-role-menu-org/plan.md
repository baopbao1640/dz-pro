# System User Role Menu Org Implementation-Free Plan

> Phase 2A freezes design before implementation. Do not use subagents or generate CRUD code in this phase.

**Goal:** Define implementable contracts for user, role, menu, department, and post management without writing business code.

**Architecture:** The feature remains inside the Modular Monolith. Keycloak owns authentication, while local system tables own authorization, organization, role, menu, and post metadata. Backend and frontend implementation must be split into later small tasks after this design is confirmed.

**Tech Stack:** Spring Boot, MyBatis Plus, PostgreSQL, Flyway, Keycloak, Vue 3, TypeScript, Ant Design Vue.

---

## 1. Scope

Allowed in this phase:

- PRD / Spec
- API design
- DTO design
- VO design
- Permission point design
- Page structure design
- Table field design
- Search condition design
- Form validation design
- Status flow design
- Audit log design
- Data permission design
- Frontend/backend module boundary design

Forbidden in this phase:

- Java business code
- Vue page code
- Mapper
- Service
- Controller
- Entity
- XML
- OpenAPI implementation
- Multi-agent parallel code generation
- Large-scale CRUD automatic generation

## 2. Files

Created in this phase:

- `specs/002-system-user-role-menu-org/spec.md`
- `specs/002-system-user-role-menu-org/plan.md`
- `specs/002-system-user-role-menu-org/tasks.md`
- `specs/002-system-user-role-menu-org/progress.md`

No source code files are created.

## 3. Existing Dependencies

This feature depends on:

- `specs/001-system-database-design/`
- `platform-core/platform-admin/src/main/resources/db/migration/V2__create_system_management_tables.sql`

Relevant tables:

- `sys_user`
- `sys_dept`
- `sys_post`
- `sys_menu`
- `sys_role`
- `sys_user_role`
- `sys_user_post`
- `sys_role_menu`
- `sys_role_dept`
- `sys_oper_log`

## 4. Backend Boundary Design

Later implementation should keep responsibilities split:

- Controller layer: HTTP request handling, permission annotations, request validation entry.
- Service layer: business rules, data permission, assignment replacement, audit event creation.
- Mapper layer: MyBatis Plus persistence and query composition.
- DTO: incoming request contracts.
- VO: outgoing response contracts.
- Entity: table mapping only.

This phase defines these contracts but does not create classes.

## 5. Frontend Boundary Design

Later frontend implementation should split by domain:

- User management page and API client.
- Role management page and API client.
- Menu management page and API client.
- Department management page and API client.
- Post management page and API client.

Ant Design Vue must be used for:

- Tables
- Forms
- Drawers
- Modals
- Trees
- Tree tables
- Pop confirms
- Messages
- Status tags

## 6. Implementation Sequencing After Confirmation

Recommended later sequence:

1. Implement shared backend context required by these pages: current user, permission code loading, and data scope helper.
2. Implement menu and permission APIs first because user and role pages depend on them.
3. Implement department tree APIs before user management.
4. Implement post APIs.
5. Implement role APIs and role-menu/data-scope assignment.
6. Implement user APIs and user-role/user-post assignment.
7. Implement frontend pages incrementally, one domain at a time.

Do not implement all CRUD at once.

## 7. Verification Plan for This Phase

Run:

```bash
scripts/check-spec-progress.sh
git diff --check -- specs/002-system-user-role-menu-org
```

Manual checks:

- Confirm the feature directory contains only Markdown design files.
- Confirm no Java, Vue, XML, Mapper, Service, Controller, Entity, or OpenAPI files were created.
- Confirm the design preserves Keycloak, Flyway, Modular Monolith, PostgreSQL, MyBatis Plus, and Ant Design Vue constraints.

## 8. Later Implementation Test Strategy

Backend implementation should include:

- Permission enforcement tests.
- Data scope behavior tests.
- Role-menu tree assignment tests, including half-check reconstruction.
- Department ancestors update tests.
- Status transition tests.
- Audit log behavior tests.

Frontend implementation should include:

- Page smoke tests.
- Form validation tests where local test tooling supports it.
- Permission visibility tests.
- Tree selection behavior tests.

## 9. Open Decisions Before Implementation

- Whether user creation should require an existing Keycloak subject or support a provisioning request to Keycloak in a separate feature.
- Exact button-level permission seed list beyond menu-level permissions.
- Whether department drag sorting should support cross-parent movement in the first implementation.
- Whether disabling a department with enabled children should be blocked or require explicit cascade confirmation.
- Whether disabled posts should remain visible in historical user detail.
