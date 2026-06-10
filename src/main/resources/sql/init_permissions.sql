-- YanQue Admin permission seed data.
-- Requires a unique key on sys_permission.permission_code for idempotent upsert.

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'system', '系统管理', 'MENU', null, 10, '系统管理根菜单', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    ((select id from (select id from sys_permission where permission_code = 'system') p), 'system:user', '用户管理', 'MENU', null, 1010, '用户管理页面', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system') p), 'system:role', '角色管理', 'MENU', null, 1020, '角色管理页面', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system') p), 'system:permission', '权限管理', 'MENU', null, 1030, '权限管理页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'system:user:create', '新增用户', 'BUTTON', null, 1011, '用户新增按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'system:user:update', '修改用户', 'BUTTON', null, 1012, '用户修改按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'system:user:delete', '删除用户', 'BUTTON', null, 1013, '用户删除按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'system:user:assign-role', '分配角色', 'BUTTON', null, 1014, '用户分配角色按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'system:role:create', '新增角色', 'BUTTON', null, 1021, '角色新增按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'system:role:update', '修改角色', 'BUTTON', null, 1022, '角色修改按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'system:role:delete', '删除角色', 'BUTTON', null, 1023, '角色删除按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'system:role:assign-permission', '分配权限', 'BUTTON', null, 1024, '角色分配权限按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'system:permission:create', '新增权限', 'BUTTON', null, 1031, '权限新增按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'system:permission:update', '修改权限', 'BUTTON', null, 1032, '权限修改按钮', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'system:permission:delete', '删除权限', 'BUTTON', null, 1033, '权限删除按钮', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'api:user:page', '分页查询用户', 'API', 'GET /yq-admin/api/sysUser', 1111, '分页查询用户接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'api:user:detail', '查询用户详情', 'API', 'GET /yq-admin/api/sysUser/{id}', 1112, '根据ID查询用户接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'api:user:create', '新增用户接口', 'API', 'POST /yq-admin/api/sysUser', 1113, '新增用户接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'api:user:update', '修改用户接口', 'API', 'PUT /yq-admin/api/sysUser/{id}', 1114, '修改用户接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'api:user:delete', '删除用户接口', 'API', 'DELETE /yq-admin/api/sysUser/{id}', 1115, '删除用户接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:user') p), 'api:user:assign-role', '用户分配角色接口', 'API', 'PUT /yq-admin/api/sysUser/{id}/roles', 1116, '用户分配角色接口', 'ACTIVE', now(), now()),

    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'api:role:page', '分页查询角色', 'API', 'GET /yq-admin/api/sysRole', 1121, '分页查询角色接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'api:role:detail', '查询角色详情', 'API', 'GET /yq-admin/api/sysRole/{id}', 1122, '根据ID查询角色接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'api:role:create', '新增角色接口', 'API', 'POST /yq-admin/api/sysRole', 1123, '新增角色接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'api:role:update', '修改角色接口', 'API', 'PUT /yq-admin/api/sysRole/{id}', 1124, '修改角色接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'api:role:delete', '删除角色接口', 'API', 'DELETE /yq-admin/api/sysRole/{id}', 1125, '删除角色接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:role') p), 'api:role:assign-permission', '角色分配权限接口', 'API', 'PUT /yq-admin/api/sysRole/{id}/permissions', 1126, '角色分配权限接口', 'ACTIVE', now(), now()),

    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'api:permission:page', '分页查询权限', 'API', 'GET /yq-admin/api/sysPermission', 1131, '分页查询权限接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'api:permission:detail', '查询权限详情', 'API', 'GET /yq-admin/api/sysPermission/{id}', 1132, '根据ID查询权限接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'api:permission:create', '新增权限接口', 'API', 'POST /yq-admin/api/sysPermission', 1133, '新增权限接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'api:permission:update', '修改权限接口', 'API', 'PUT /yq-admin/api/sysPermission/{id}', 1134, '修改权限接口', 'ACTIVE', now(), now()),
    ((select id from (select id from sys_permission where permission_code = 'system:permission') p), 'api:permission:delete', '删除权限接口', 'API', 'DELETE /yq-admin/api/sysPermission/{id}', 1135, '删除权限接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();
