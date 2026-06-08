# System User Role Menu Org Spec

## 1. Background

The system management database design and Flyway Phase 4 migration are complete. This Phase 2A feature freezes the implementation design for user, role, menu, department, and post management before any Java or Vue code is written.

This feature defines PRD scope, API contracts, DTO/VO structures, permission points, page structures, validation rules, audit points, data permission behavior, and frontend/backend boundaries.

## 2. Goals

- Freeze the implementation design for system user, role, menu, department, and post management.
- Keep authentication delegated to Keycloak.
- Use the existing system management tables created by Flyway.
- Define API, DTO, VO, permission, audit, and page contracts before implementation.
- Keep later implementation incremental and reviewable.

## 3. Non-Goals

This phase must not create:

- Java business code
- Vue page code
- Mapper
- Service
- Controller
- Entity
- XML
- OpenAPI implementation
- Multi-agent parallel CRUD generation
- Large-scale automatic CRUD generation

## 4. Technical Constraints

- Architecture: Modular Monolith
- Authentication: Keycloak owns login, password, sessions, tokens, MFA, and identity lifecycle
- Local user table: `sys_user` stores business user mapping only
- Database: PostgreSQL
- Schema management: Flyway only
- Backend persistence style: MyBatis Plus
- Frontend stack: Vue 3, TypeScript, Ant Design Vue
- Forbidden: Spring Cloud
- Frontend UI must use Ant Design Vue components for tables, forms, drawers, modals, tree controls, pop confirms, messages, and status tags

## 5. Domain Scope

This feature covers:

- User management
- Role management
- Menu management
- Department management
- Post management

Related tables:

- `sys_user`
- `sys_dept`
- `sys_post`
- `sys_menu`
- `sys_role`
- `sys_user_role`
- `sys_user_post`
- `sys_role_menu`
- `sys_role_dept`

## 6. Common API Rules

API base path:

- `/api/system/users`
- `/api/system/roles`
- `/api/system/menus`
- `/api/system/depts`
- `/api/system/posts`

Common response envelope:

| Field | Type | Description |
| --- | --- | --- |
| `code` | `string` | Business result code. |
| `message` | `string` | Human-readable message. |
| `data` | `object` | Response payload. |
| `traceId` | `string` | Request trace ID for audit and troubleshooting. |

Common page response:

| Field | Type | Description |
| --- | --- | --- |
| `records` | `array` | Current page records. |
| `total` | `number` | Total matched records. |
| `pageNum` | `number` | Current page number. |
| `pageSize` | `number` | Current page size. |

Common request rules:

- Mutating APIs must be audited.
- Delete APIs are soft-delete or relationship removal only.
- Status changes must be explicit endpoints, not hidden inside generic update semantics.
- APIs must enforce permission points and data permission scope.
- APIs must not expose Keycloak tokens, refresh tokens, sessions, or MFA data.

## 7. User Management

### 7.1 Functional Scope

- User pagination
- User query
- Create user mapping
- Edit user profile and organization data
- Enable or disable user
- Assign roles
- Assign posts
- Assign department
- User detail
- User data permission visibility

### 7.2 Page Structure

Recommended Ant Design Vue layout:

- Left area: department tree filter
- Right area: search form, toolbar, user table
- Drawer: create/edit user form
- Modal or drawer: assign roles
- Modal or drawer: assign posts
- Detail drawer: user profile, roles, posts, department, audit summary

Ant Design Vue components:

- `a-tree` for department filter
- `a-form` for search and edit
- `a-table` for users
- `a-drawer` for create/edit/detail
- `a-modal` for role and post assignment
- `a-popconfirm` for status changes
- `a-tag` for status, user type, and data scope display

### 7.3 Search Conditions

| Field | Component | Description |
| --- | --- | --- |
| `deptId` | `a-tree` | Department filter, includes children when backend data permission allows. |
| `userName` | `a-input` | Fuzzy query by username. |
| `nickName` | `a-input` | Fuzzy query by display name. |
| `phoneNumber` | `a-input` | Fuzzy query by phone. |
| `status` | `a-select` | `0` normal, `1` disabled. |
| `createdTimeRange` | `a-range-picker` | Filter by create time. |

### 7.4 Table Fields

| Field | Description |
| --- | --- |
| `id` | User ID. |
| `userName` | Platform username. |
| `nickName` | Display name. |
| `deptName` | Department name. |
| `postNames` | Bound posts. |
| `roleNames` | Bound roles. |
| `phoneNumber` | Phone number. |
| `email` | Email. |
| `status` | Enabled or disabled. |
| `lastSyncTime` | Last identity sync time. |
| `createTime` | Create time. |
| `actions` | Detail, edit, status change, assign roles, assign posts. |

### 7.5 Drawer/Form Structure

Create/edit user drawer sections:

- Basic profile
- Organization assignment
- Contact information
- Role and post summary
- Status and remark

Fields:

| Field | Required | Rule |
| --- | --- | --- |
| `keycloakUserId` | Yes on create | Must map to existing Keycloak subject. |
| `userName` | Yes | 2-64 chars; unique among active users. |
| `nickName` | Yes | 2-64 chars. |
| `deptId` | No | Must reference enabled department visible to current operator. |
| `postIds` | No | Each post must be enabled. |
| `roleIds` | No | Each role must be enabled and assignable by current operator. |
| `email` | No | Valid email format. |
| `phoneNumber` | No | Valid phone format according to project rule. |
| `sex` | No | `0`, `1`, `2`. |
| `status` | Yes | `0` or `1`. |
| `remark` | No | Max 500 chars. |

### 7.6 APIs

| Method | Path | Permission | Description |
| --- | --- | --- | --- |
| `GET` | `/api/system/users` | `system:user:list` | User page query. |
| `GET` | `/api/system/users/{id}` | `system:user:query` | User detail. |
| `POST` | `/api/system/users` | `system:user:add` | Create local user mapping. |
| `PUT` | `/api/system/users/{id}` | `system:user:edit` | Edit local user profile. |
| `PATCH` | `/api/system/users/{id}/status` | `system:user:status` | Enable or disable user. |
| `PUT` | `/api/system/users/{id}/roles` | `system:user:assign-role` | Replace user role assignments. |
| `PUT` | `/api/system/users/{id}/posts` | `system:user:assign-post` | Replace user post assignments. |
| `GET` | `/api/system/users/{id}/permissions` | `system:user:query` | Effective roles, menus, buttons, and data scope summary. |

### 7.7 DTO Design

`UserPageQueryDTO`:

| Field | Type | Description |
| --- | --- | --- |
| `pageNum` | `number` | Page number. |
| `pageSize` | `number` | Page size. |
| `deptId` | `string` | Department ID as string bigint. |
| `includeChildren` | `boolean` | Whether department filter includes child departments. |
| `userName` | `string` | Username keyword. |
| `nickName` | `string` | Display name keyword. |
| `phoneNumber` | `string` | Phone keyword. |
| `status` | `string` | `0` or `1`. |
| `createdStartTime` | `string` | ISO datetime. |
| `createdEndTime` | `string` | ISO datetime. |

`UserSaveDTO`:

| Field | Type | Description |
| --- | --- | --- |
| `keycloakUserId` | `string` | Keycloak subject. |
| `userName` | `string` | Platform username. |
| `nickName` | `string` | Display name. |
| `deptId` | `string` | Department ID. |
| `postIds` | `string[]` | Post IDs. |
| `roleIds` | `string[]` | Role IDs. |
| `email` | `string` | Email. |
| `phoneNumber` | `string` | Phone. |
| `sex` | `string` | `0`, `1`, `2`. |
| `status` | `string` | `0`, `1`. |
| `remark` | `string` | Remark. |

`UserStatusDTO`:

| Field | Type | Description |
| --- | --- | --- |
| `status` | `string` | `0` normal, `1` disabled. |
| `reason` | `string` | Optional reason for audit. |

`UserRoleAssignDTO`:

| Field | Type | Description |
| --- | --- | --- |
| `roleIds` | `string[]` | Full replacement role ID list. |

`UserPostAssignDTO`:

| Field | Type | Description |
| --- | --- | --- |
| `postIds` | `string[]` | Full replacement post ID list. |

### 7.8 VO Design

`UserListVO`:

- `id`
- `keycloakUserId`
- `userName`
- `nickName`
- `deptId`
- `deptName`
- `postNames`
- `roleNames`
- `email`
- `phoneNumber`
- `sex`
- `status`
- `lastSyncTime`
- `createTime`

`UserDetailVO`:

- Basic `UserListVO` fields
- `roleIds`
- `roles`
- `postIds`
- `posts`
- `dataScopeSummary`
- `permissionCodes`
- `menuIds`

### 7.9 Status Flow

- Create: enabled by default unless request sets disabled.
- Enable: disabled user becomes available in platform authorization.
- Disable: user cannot access platform business permissions after token/session refresh or permission reload.
- Disable does not disable the Keycloak account.
- Keycloak account lifecycle must be handled outside this feature.

### 7.10 Audit Points

- Create user mapping
- Edit user profile
- Enable user
- Disable user
- Assign roles
- Assign posts
- Change department
- View detail may be logged only when compliance requires it

### 7.11 Exceptions

- Keycloak subject already mapped.
- Username conflicts with active user.
- Department disabled or not visible.
- Role disabled or not assignable.
- Post disabled.
- Current operator lacks data scope.
- Attempt to disable the only active super administrator should be blocked.

## 8. Role Management

### 8.1 Functional Scope

- Role pagination
- Create role
- Edit role
- Assign menu permissions
- Configure data permission scope
- Enable or disable role
- View associated users

### 8.2 Page Structure

- Search form and role table.
- Drawer for create/edit role.
- Drawer or modal for menu tree assignment.
- Drawer or modal for data permission department tree.
- Detail drawer with assigned users, menus, and data scope.

### 8.3 Table Fields

| Field | Description |
| --- | --- |
| `id` | Role ID. |
| `roleName` | Role name. |
| `roleKey` | Role key. |
| `roleSort` | Sort order. |
| `dataScope` | Data permission scope. |
| `status` | Enabled or disabled. |
| `createTime` | Create time. |
| `actions` | Detail, edit, menu permission, data permission, status change, users. |

### 8.4 Search Conditions

| Field | Component | Description |
| --- | --- | --- |
| `roleName` | `a-input` | Fuzzy query by role name. |
| `roleKey` | `a-input` | Fuzzy query by role key. |
| `status` | `a-select` | `0` normal, `1` disabled. |
| `createdTimeRange` | `a-range-picker` | Filter by create time. |

### 8.5 APIs

| Method | Path | Permission | Description |
| --- | --- | --- | --- |
| `GET` | `/api/system/roles` | `system:role:list` | Role page query. |
| `GET` | `/api/system/roles/{id}` | `system:role:query` | Role detail. |
| `POST` | `/api/system/roles` | `system:role:add` | Create role. |
| `PUT` | `/api/system/roles/{id}` | `system:role:edit` | Edit role. |
| `PATCH` | `/api/system/roles/{id}/status` | `system:role:status` | Enable or disable role. |
| `PUT` | `/api/system/roles/{id}/menus` | `system:role:assign-menu` | Replace role menu permissions. |
| `PUT` | `/api/system/roles/{id}/data-scope` | `system:role:data-scope` | Update role data scope and department IDs. |
| `GET` | `/api/system/roles/{id}/users` | `system:role:query` | Users assigned to role. |

### 8.6 DTO Design

`RolePageQueryDTO`:

- `pageNum`
- `pageSize`
- `roleName`
- `roleKey`
- `status`
- `createdStartTime`
- `createdEndTime`

`RoleSaveDTO`:

- `roleName`
- `roleKey`
- `roleSort`
- `dataScope`
- `menuCheckStrictly`
- `deptCheckStrictly`
- `status`
- `remark`

`RoleMenuAssignDTO`:

- `menuIds`: selected menu and button IDs
- `halfCheckedMenuIds`: half-selected parent IDs for UI restore
- `menuCheckStrictly`: `Y` or `N`

`RoleDataScopeDTO`:

- `dataScope`: `1`, `2`, `3`, `4`, `5`
- `deptIds`: required only when `dataScope = 2`
- `deptCheckStrictly`: `Y` or `N`

### 8.7 VO Design

`RoleListVO`:

- `id`
- `roleName`
- `roleKey`
- `roleSort`
- `dataScope`
- `dataScopeLabel`
- `status`
- `createTime`

`RoleDetailVO`:

- Basic role fields
- `menuIds`
- `halfCheckedMenuIds`
- `deptIds`
- `assignedUserCount`

### 8.8 Data Scope Behavior

| Value | Name | Behavior |
| --- | --- | --- |
| `1` | All data | Access all department data. |
| `2` | Custom departments | Access departments in `sys_role_dept`. |
| `3` | Current department | Access operator's own department. |
| `4` | Current department and children | Access operator's department subtree. |
| `5` | Current user only | Access records owned by current user. |

When a user has multiple roles, effective data scope should use the broadest allowed scope. If any enabled role has `1`, the result is all data. Otherwise combine custom and department-derived scopes according to implementation rules.

### 8.9 Menu Tree Authorization Logic

- `sys_menu` is the source tree.
- Directory and menu nodes can be selected.
- Button nodes represent function permissions.
- `menuCheckStrictly = Y`: parent and child selection are independent.
- `menuCheckStrictly = N`: parent and child selection cascade.
- Half-selected parent IDs should be returned for UI reconstruction but only fully selected effective menu IDs are persisted in `sys_role_menu`.

### 8.10 Data Permission and Department Tree

- `dataScope = 2` requires department tree selection.
- `deptCheckStrictly = Y`: parent and child department selection are independent.
- `deptCheckStrictly = N`: parent and child department selection cascade.
- Department IDs are persisted in `sys_role_dept`.
- Disabled or deleted departments cannot be newly selected.

### 8.11 Audit Points

- Create role
- Edit role
- Enable or disable role
- Assign menu permissions
- Update data permission scope
- Associate or remove users through user assignment flow

### 8.12 Validation Rules

- `roleName` required, 2-128 chars, unique enough for operator clarity.
- `roleKey` required, 2-128 chars, lowercase letters, numbers, colon, underscore, or hyphen; unique among active roles.
- `roleSort` required, non-negative integer.
- `dataScope` required and must be one of `1`, `2`, `3`, `4`, `5`.
- `deptIds` required when `dataScope = 2`.
- `menuCheckStrictly` required and must be `Y` or `N`.
- `deptCheckStrictly` required and must be `Y` or `N`.
- Built-in super administrator role cannot be disabled or narrowed by non-super operators.

### 8.13 Exceptions

- Role key conflicts with active role.
- Role does not exist or has been deleted.
- Current operator lacks permission to modify the role.
- Current operator attempts to grant menus they do not own.
- `dataScope = 2` but no department IDs are supplied.
- Selected department is disabled, deleted, or outside current operator data scope.
- Attempt to disable or weaken the only effective super administrator role.

## 9. Menu Management

### 9.1 Functional Scope

- Directory management
- Menu management
- Button permission management
- Frontend route metadata
- Icon configuration
- Cache configuration
- Visibility configuration
- External link configuration
- Dynamic route and permission association design

### 9.2 Page Structure

- Tree table showing menu hierarchy.
- Drawer for create/edit menu node.
- Node type segmented control: directory, menu, button.
- Conditional form fields based on `menuType`.

### 9.3 Fields

| Field | Applies To | Description |
| --- | --- | --- |
| `menuName` | All | Display name. |
| `parentId` | All | Parent node. |
| `orderNum` | All | Sort order. |
| `menuType` | All | `M`, `C`, `F`. |
| `path` | Directory/Menu | Frontend route path or external URL. |
| `component` | Menu | Frontend component path. |
| `routeName` | Directory/Menu | Frontend route name. |
| `permissionCode` | Menu/Button | Permission code, required for button. |
| `icon` | Directory/Menu | Icon key. |
| `visible` | Directory/Menu | `Y` visible, `N` hidden. |
| `isCache` | Menu | `Y` keep alive, `N` no cache. |
| `isFrame` | Directory/Menu | `Y` external link, `N` internal route. |
| `status` | All | `0` normal, `1` disabled. |

Database field mapping:

| API Field | Database Column |
| --- | --- |
| `menuType` | `menu_type` |
| `permissionCode` | `permission_code` |
| `routeName` | `route_name` |
| `isCache` | `is_cache` |
| `isFrame` | `is_frame` |

### 9.4 Search Conditions

| Field | Component | Description |
| --- | --- | --- |
| `menuName` | `a-input` | Fuzzy query by menu name. |
| `menuType` | `a-select` | `M` directory, `C` menu, `F` button. |
| `permissionCode` | `a-input` | Fuzzy query by permission code. |
| `status` | `a-select` | `0` normal, `1` disabled. |

### 9.5 Table Fields

| Field | Description |
| --- | --- |
| `menuName` | Directory, menu, or button name. |
| `menuType` | Directory, menu, or button type. |
| `permissionCode` | Backend permission code. |
| `path` | Route path or external URL. |
| `component` | Frontend component path. |
| `routeName` | Frontend route name. |
| `visible` | Navigation visibility. |
| `isCache` | Route cache flag. |
| `isFrame` | External link flag. |
| `status` | Enabled or disabled. |
| `orderNum` | Sort order. |
| `actions` | Detail, edit, status change, delete. |

### 9.6 APIs

| Method | Path | Permission | Description |
| --- | --- | --- | --- |
| `GET` | `/api/system/menus/tree` | `system:menu:list` | Menu tree query. |
| `GET` | `/api/system/menus/{id}` | `system:menu:query` | Menu detail. |
| `POST` | `/api/system/menus` | `system:menu:add` | Create menu node. |
| `PUT` | `/api/system/menus/{id}` | `system:menu:edit` | Edit menu node. |
| `PATCH` | `/api/system/menus/{id}/status` | `system:menu:status` | Enable or disable menu node. |
| `DELETE` | `/api/system/menus/{id}` | `system:menu:delete` | Soft delete menu node. |
| `GET` | `/api/system/menus/routes` | authenticated | Current user's dynamic routes. |
| `GET` | `/api/system/menus/permissions` | authenticated | Current user's permission codes. |

### 9.7 DTO Design

`MenuSaveDTO`:

- `menuName`
- `parentId`
- `orderNum`
- `path`
- `component`
- `queryParam`
- `routeName`
- `isFrame`
- `isCache`
- `menuType`
- `visible`
- `status`
- `permissionCode`
- `icon`
- `remark`

`MenuStatusDTO`:

- `status`
- `reason`

### 9.8 VO Design

`MenuTreeVO`:

- `id`
- `parentId`
- `menuName`
- `menuType`
- `path`
- `component`
- `routeName`
- `permissionCode`
- `icon`
- `visible`
- `isCache`
- `isFrame`
- `status`
- `children`

`RouteVO`:

- `name`
- `path`
- `component`
- `redirect`
- `meta.title`
- `meta.icon`
- `meta.hidden`
- `meta.keepAlive`
- `children`

### 9.9 Dynamic Route Mechanism

- Backend returns current user's enabled directory and menu nodes.
- Button nodes are not converted into routes.
- Frontend maps `component` to local route component registry.
- Disabled and hidden menus are excluded from visible navigation; hidden routes may still be returned if needed for detail pages.
- External links use `isFrame = Y` and must validate `path` as URL.

### 9.10 Permission Association Mechanism

- Backend computes permission codes from enabled roles and enabled menu/button nodes.
- Frontend uses permission codes for button-level visibility.
- Backend remains the final permission enforcement authority.
- Button permissions require unique `permissionCode`.

### 9.11 Validation Rules

- Directory: `menuName`, `parentId`, `orderNum`, `path`, `menuType` required.
- Menu: `menuName`, `parentId`, `orderNum`, `path`, `component`, `routeName`, `menuType` required.
- Button: `menuName`, `parentId`, `orderNum`, `permissionCode`, `menuType` required.
- Node cannot be its own parent.
- Delete is blocked when enabled children exist.

### 9.12 Audit Points

- Create menu node
- Edit menu node
- Enable or disable menu node
- Delete menu node
- Changes to `permissionCode`

### 9.13 Exceptions

- Permission code conflicts with active menu or button.
- Parent menu does not exist, is disabled, or is deleted.
- Directory, menu, or button field combination does not match `menuType`.
- External link has invalid URL when `isFrame = Y`.
- Component path is missing for menu node.
- Attempt to delete a node with active children.
- Attempt to disable a parent while active children remain enabled.

## 10. Department Management

### 10.1 Functional Scope

- Department tree
- Create/edit department
- Drag sorting
- Department leader
- Status control
- Data permission association

### 10.2 Page Structure

- Tree table for departments.
- Drawer for create/edit department.
- Drag sort interaction with explicit save.
- Leader selector uses existing enabled users visible by data permission.

### 10.3 Search Conditions

| Field | Component | Description |
| --- | --- | --- |
| `deptName` | `a-input` | Fuzzy query by department name. |
| `status` | `a-select` | `0` normal, `1` disabled. |

### 10.4 Table Fields

| Field | Description |
| --- | --- |
| `deptName` | Department name. |
| `orderNum` | Sort order. |
| `leaderName` | Department leader. |
| `phone` | Department phone. |
| `email` | Department email. |
| `status` | Enabled or disabled. |
| `actions` | Detail, edit, status change, drag sort, delete. |

### 10.5 APIs

| Method | Path | Permission | Description |
| --- | --- | --- | --- |
| `GET` | `/api/system/depts/tree` | `system:dept:list` | Department tree query. |
| `GET` | `/api/system/depts/{id}` | `system:dept:query` | Department detail. |
| `POST` | `/api/system/depts` | `system:dept:add` | Create department. |
| `PUT` | `/api/system/depts/{id}` | `system:dept:edit` | Edit department. |
| `PATCH` | `/api/system/depts/{id}/status` | `system:dept:status` | Enable or disable department. |
| `PUT` | `/api/system/depts/sort` | `system:dept:sort` | Save drag sort result. |
| `DELETE` | `/api/system/depts/{id}` | `system:dept:delete` | Soft delete department. |

### 10.6 DTO Design

`DeptSaveDTO`:

- `parentId`
- `deptName`
- `orderNum`
- `leaderUserId`
- `phone`
- `email`
- `status`
- `remark`

`DeptSortDTO`:

- `items`: array of `{ id, parentId, orderNum }`

### 10.7 VO Design

`DeptTreeVO`:

- `id`
- `parentId`
- `ancestors`
- `deptName`
- `orderNum`
- `leaderUserId`
- `leaderName`
- `phone`
- `email`
- `status`
- `children`

### 10.8 Ancestors Maintenance Strategy

- `ancestors` stores comma-separated ancestor IDs.
- Creating a department derives `ancestors` from parent.
- Moving a department recalculates its own `ancestors` and all descendants.
- Moving under self or descendant is forbidden.
- Sort-only changes do not change `ancestors`.

### 10.9 Delete and Status Rules

- Delete is blocked when active child departments exist.
- Delete is blocked when active users belong to the department.
- Disable is blocked when enabled child departments exist, unless implementation explicitly cascades after confirmation.
- Disabled departments cannot be selected for new users, role data scope, or child departments.

### 10.10 Data Permission Association

- Department tree is used by role data scope.
- Current operator can only manage departments within effective data scope.
- `dataScope = 2` stores selected department IDs in `sys_role_dept`.

### 10.11 Validation Rules

- `deptName` required, 2-128 chars.
- `parentId` required and must reference an active department or root `0`.
- `orderNum` required, non-negative integer.
- `leaderUserId`, if provided, must reference an enabled user visible to current operator.
- `phone`, if provided, must match project phone format.
- `email`, if provided, must match email format.
- Department cannot be moved under itself or its descendants.

### 10.12 Audit Points

- Create department
- Edit department
- Move department
- Sort department
- Enable or disable department
- Delete department
- Change leader

### 10.13 Exceptions

- Parent department does not exist, is disabled, or is deleted.
- Department name conflicts under the same parent when active.
- Current operator lacks data scope for the target department.
- Delete blocked by active child departments.
- Delete blocked by active users assigned to the department.
- Disable blocked by enabled child departments unless a later confirmed cascade flow exists.
- Move blocked because target parent is self or descendant.

## 11. Post Management

### 11.1 Functional Scope

- Post pagination
- Create/edit post
- Post code
- Sort order
- Status control
- User binding relationship

### 11.2 Page Structure

- Search form and post table.
- Drawer for create/edit post.
- Detail drawer with bound user count and sample users.

### 11.3 Search Conditions

| Field | Component | Description |
| --- | --- | --- |
| `postCode` | `a-input` | Fuzzy query by post code. |
| `postName` | `a-input` | Fuzzy query by post name. |
| `status` | `a-select` | `0` normal, `1` disabled. |

### 11.4 Table Fields

| Field | Description |
| --- | --- |
| `postCode` | Post code. |
| `postName` | Post name. |
| `postSort` | Sort order. |
| `status` | Enabled or disabled. |
| `boundUserCount` | Number of active users bound to post. |
| `createTime` | Create time. |
| `actions` | Detail, edit, status change, delete, bound users. |

### 11.5 APIs

| Method | Path | Permission | Description |
| --- | --- | --- | --- |
| `GET` | `/api/system/posts` | `system:post:list` | Post page query. |
| `GET` | `/api/system/posts/{id}` | `system:post:query` | Post detail. |
| `POST` | `/api/system/posts` | `system:post:add` | Create post. |
| `PUT` | `/api/system/posts/{id}` | `system:post:edit` | Edit post. |
| `PATCH` | `/api/system/posts/{id}/status` | `system:post:status` | Enable or disable post. |
| `DELETE` | `/api/system/posts/{id}` | `system:post:delete` | Soft delete post. |
| `GET` | `/api/system/posts/{id}/users` | `system:post:query` | Users bound to post. |

### 11.6 DTO Design

`PostPageQueryDTO`:

- `pageNum`
- `pageSize`
- `postCode`
- `postName`
- `status`

`PostSaveDTO`:

- `postCode`
- `postName`
- `postSort`
- `status`
- `remark`

### 11.7 VO Design

`PostListVO`:

- `id`
- `postCode`
- `postName`
- `postSort`
- `status`
- `boundUserCount`
- `createTime`

### 11.8 Validation Rules

- `postCode` required, 2-64 chars, unique among active posts.
- `postName` required, 2-128 chars.
- `postSort` required, non-negative integer.
- Disable is blocked or warned when active users are bound.
- Delete is blocked when active users are bound.

### 11.9 Audit Points

- Create post
- Edit post
- Enable or disable post
- Delete post
- User-post binding changes are audited from user management

### 11.10 Exceptions

- Post code conflicts with active post.
- Post does not exist or has been deleted.
- Current operator lacks permission to manage posts.
- Disable blocked or requires confirmation because active users are bound.
- Delete blocked because active users are bound.

## 12. Permission Point Inventory

User:

- `system:user:list`
- `system:user:query`
- `system:user:add`
- `system:user:edit`
- `system:user:status`
- `system:user:assign-role`
- `system:user:assign-post`

Role:

- `system:role:list`
- `system:role:query`
- `system:role:add`
- `system:role:edit`
- `system:role:status`
- `system:role:assign-menu`
- `system:role:data-scope`

Menu:

- `system:menu:list`
- `system:menu:query`
- `system:menu:add`
- `system:menu:edit`
- `system:menu:status`
- `system:menu:delete`

Department:

- `system:dept:list`
- `system:dept:query`
- `system:dept:add`
- `system:dept:edit`
- `system:dept:status`
- `system:dept:sort`
- `system:dept:delete`

Post:

- `system:post:list`
- `system:post:query`
- `system:post:add`
- `system:post:edit`
- `system:post:status`
- `system:post:delete`

## 13. Data Permission Design

Data permission applies to:

- User list and detail
- Department tree and department mutation
- Role data-scope department selection
- Post bound-user query

Data permission does not replace function permission. A user must pass both function permission and data permission checks.

Default rules:

- Super administrator role can access all system management data.
- Non-super roles are restricted by effective `dataScope`.
- Department filters must be intersected with current operator's allowed department IDs.
- Mutations must verify target record is within operator's data scope.

## 14. Audit Log Design

Audit log table:

- `sys_oper_log`

Audit content should include:

- Module title
- Business type
- Request method
- Operator local user ID
- Operator name
- Department ID
- Request URL
- Sanitized request parameters
- Sanitized result summary
- Status
- Error message
- Cost time
- Operation time

Sensitive values must be masked. Keycloak tokens, refresh tokens, sessions, password fields, and MFA data must never be logged.

## 15. Frontend and Backend Module Boundaries

Backend:

- `platform-system` should own system domain business contracts in later implementation.
- `platform-admin` should expose admin application endpoints in later implementation.
- `platform-framework` should provide shared security, authentication context, data permission, and audit support where appropriate.
- `platform-common` should hold shared response, constants, and common validation patterns where appropriate.

Frontend:

- System pages should live under a system management module in `platform-ui`.
- API clients should be separated by domain: user, role, menu, department, post.
- UI must use Ant Design Vue components.
- Frontend permission checks control visibility only; backend enforces authorization.

## 16. Frontend State Requirements

Every page must support:

- Loading state
- Empty state
- Error state
- Disabled submit state
- Permission-denied state
- Tree loading state
- Assignment save loading state

## 17. Acceptance Criteria

- `spec.md`, `plan.md`, `tasks.md`, and `progress.md` exist.
- User, role, menu, department, and post management are covered.
- API designs are defined without implementation.
- DTO and VO designs are defined without Java classes.
- Permission points are listed.
- Page structures and table/search/form designs are defined.
- Data permission behavior is defined.
- Audit points are defined.
- Keycloak boundary is preserved.
- Flyway-only schema boundary is preserved.
- No Java, Vue, Mapper, Service, Controller, Entity, XML, or OpenAPI implementation files are created.
