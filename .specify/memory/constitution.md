# Platform Core Constitution

## Core Principles

### I. PRD First

All development must follow the current PRD as the highest product authority. For this repository, `one.md` defines the Phase-1 scope until a later PRD supersedes it. Spec Kit artifacts refine feature-level execution; they do not override PRD boundaries.

### II. Modular Monolith Only

The backend must remain a Modular Monolith. Do not introduce Spring Cloud or split services. New backend modules must follow the parent Maven project and the Controller → Service → Mapper → Entity layering rule when business modules are introduced.

### III. Authentication Boundary

Keycloak owns authentication, login, and user identity. Business modules may map permissions and roles but must not implement independent login logic. Permission-related features must explicitly describe their Keycloak integration boundary in `spec.md` and `plan.md`.

### IV. Database Changes Through Flyway

All schema changes must be represented as Flyway migrations. Direct production-table edits are forbidden. Any feature that adds or changes tables must document the migration, rollback considerations, and affected data model in its feature plan.

### V. Quality Gates Are Mandatory

No code is complete until local checks and GitHub Actions pass. Backend changes must pass Spotless, Checkstyle, and Maven tests. Frontend changes must pass ESLint, Prettier format check, and production build. Frontend UI must follow `docs/code-style.md`, including Ant Design Vue design restrictions.

## Technology and Environment Constraints

- Backend: JDK 21 runtime target for CI, Spring Boot 3.x, MyBatis Plus, PostgreSQL, Redis, Flyway, Keycloak.
- Frontend: Vue 3, TypeScript, Vite, Pinia, Vue Router, Ant Design Vue, Axios.
- Package management: frontend installs use pnpm and `pnpm-lock.yaml` as the primary lock file.
- Execution environment: build, Git, and GitHub CLI commands run in WSL at `/mnt/f/AIworkspance/content`.
- Docker runs in host Docker Desktop and is accessed from WSL through Docker integration.
- CI is the final quality gate for commits pushed to `master`.

## Spec Kit Workflow

All substantial feature work must live under `specs/<number>-<feature>/` and include:

- `spec.md`: user goals, scope, acceptance criteria, constraints.
- `plan.md`: architecture, module impact, data model, API/UI contracts, validation approach.
- `tasks.md`: implementable task list.
- `progress.md`: current status, completed work, verification, next steps, and risks.

Features that must use the full Spec Kit workflow include new modules, database changes, API additions, UI pages, permission points, user management, role management, permission management, audit logging, and downstream business systems. Small maintenance tasks such as typos, CI fixes, lockfile sync, and formatting-only changes may use the existing PRD + CI workflow without a feature spec.

## Progress Hook

Before starting any feature or task, read the feature's `progress.md`. If it does not exist, create an initial progress document before planning or implementation. Each completed task must update `progress.md` with date/time, current task, completed work, verification result, next plan, risks/blockers, and related commit/PR/CI links when available. Feature completion must add a final completion section.

## Governance

This constitution complements but does not replace `one.md`, `AGENTS.md`, `docs/code-style.md`, and `.github/workflows/ci.yml`. Conflicts are resolved in this order:

1. Current PRD (`one.md` until superseded)
2. This constitution
3. `AGENTS.md`
4. `docs/code-style.md`
5. Feature-level Spec Kit artifacts

Amendments must update this file and, when relevant, `AGENTS.md` or `docs/spec-process.md`. Any amendment must pass the same local and CI checks as code changes.

**Version**: 1.0.0 | **Ratified**: 2026-06-08 | **Last Amended**: 2026-06-08
