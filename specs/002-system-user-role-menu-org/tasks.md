# System User Role Menu Org Tasks

## Phase 2A: Design Freeze

- [x] Create feature directory `specs/002-system-user-role-menu-org/`.
- [x] Create `progress.md` before implementation planning.
- [x] Create `spec.md`.
- [x] Create `plan.md`.
- [x] Create `tasks.md`.
- [x] Define user management page, API, DTO, VO, validation, permission, audit, and exception design.
- [x] Define role management page, API, DTO, VO, data scope, menu tree authorization, half-check, strict mode, and audit design.
- [x] Define menu management page, API, DTO, VO, route metadata, dynamic route, frontend/backend permission association, and audit design.
- [x] Define department management page, API, DTO, VO, ancestors strategy, delete rules, status rules, data permission relation, and audit design.
- [x] Define post management page, API, DTO, VO, validation, binding relation, and audit design.
- [x] Define permission point inventory.
- [x] Define frontend/backend module boundaries.
- [x] Preserve Modular Monolith, Keycloak boundary, Flyway-only schema, MyBatis Plus, PostgreSQL, Ant Design Vue, and no Spring Cloud constraints.

## Verification

- [x] Run `scripts/check-spec-progress.sh`.
- [x] Run `git diff --check -- specs/002-system-user-role-menu-org`.
- [x] Confirm no Java business code was created.
- [x] Confirm no Vue page code was created.
- [x] Confirm no Mapper, Service, Controller, Entity, XML, or OpenAPI implementation files were created.
- [x] Update `progress.md` with verification results.

## Later Implementation Phases

Do not start these tasks until the user confirms the Phase 2A design.

- [ ] Create implementation plan for shared backend security context, permission loading, data scope helper, and audit support.
- [ ] Create implementation plan for menu APIs.
- [ ] Create implementation plan for department APIs.
- [ ] Create implementation plan for post APIs.
- [ ] Create implementation plan for role APIs.
- [ ] Create implementation plan for user APIs.
- [ ] Create implementation plan for frontend pages, one domain at a time.
