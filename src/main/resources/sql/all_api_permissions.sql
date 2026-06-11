-- YanQue Admin permission tree.
-- api_path only stores request URI, for example: /yq-admin/api/sysUser
-- Requires a unique key on sys_permission.permission_code for idempotent upsert.

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'system', '系统管理', 'MENU', null, 10, '系统管理根节点', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @system_id := (select id from sys_permission where permission_code = 'system' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@system_id, 'system:user', '用户管理', 'MENU', null, 1010, '用户管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:role', '角色管理', 'MENU', null, 1020, '角色管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:permission', '权限管理', 'MENU', null, 1030, '权限管理页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @user_menu_id := (select id from sys_permission where permission_code = 'system:user' limit 1);
set @role_menu_id := (select id from sys_permission where permission_code = 'system:role' limit 1);
set @permission_menu_id := (select id from sys_permission where permission_code = 'system:permission' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@user_menu_id, 'api:user:page', '分页查询用户', 'API', '/yq-admin/api/sysUser', 1111, '分页查询用户接口', 'ACTIVE', now(), now()),
    (@user_menu_id, 'api:user:detail', '查询用户详情', 'API', '/yq-admin/api/sysUser/{id}', 1112, '根据ID查询用户接口', 'ACTIVE', now(), now()),
    (@user_menu_id, 'api:user:create', '新增用户', 'API', '/yq-admin/api/sysUser', 1113, '新增用户接口', 'ACTIVE', now(), now()),
    (@user_menu_id, 'api:user:update', '修改用户', 'API', '/yq-admin/api/sysUser/{id}', 1114, '修改用户接口', 'ACTIVE', now(), now()),
    (@user_menu_id, 'api:user:delete', '删除用户', 'API', '/yq-admin/api/sysUser/{id}', 1115, '删除用户接口', 'ACTIVE', now(), now()),
    (@user_menu_id, 'api:user:assign-role', '用户分配角色', 'API', '/yq-admin/api/sysUser/{id}/roles', 1116, '用户分配角色接口', 'ACTIVE', now(), now()),

    (@role_menu_id, 'api:role:page', '分页查询角色', 'API', '/yq-admin/api/sysRole', 1121, '分页查询角色接口', 'ACTIVE', now(), now()),
    (@role_menu_id, 'api:role:detail', '查询角色详情', 'API', '/yq-admin/api/sysRole/{id}', 1122, '根据ID查询角色接口', 'ACTIVE', now(), now()),
    (@role_menu_id, 'api:role:create', '新增角色', 'API', '/yq-admin/api/sysRole', 1123, '新增角色接口', 'ACTIVE', now(), now()),
    (@role_menu_id, 'api:role:update', '修改角色', 'API', '/yq-admin/api/sysRole/{id}', 1124, '修改角色接口', 'ACTIVE', now(), now()),
    (@role_menu_id, 'api:role:delete', '删除角色', 'API', '/yq-admin/api/sysRole/{id}', 1125, '删除角色接口', 'ACTIVE', now(), now()),
    (@role_menu_id, 'api:role:assign-permission', '角色分配权限', 'API', '/yq-admin/api/sysRole/{id}/permissions', 1126, '角色分配权限接口', 'ACTIVE', now(), now()),

    (@permission_menu_id, 'api:permission:page', '分页查询权限', 'API', '/yq-admin/api/sysPermission', 1131, '分页查询权限接口', 'ACTIVE', now(), now()),
    (@permission_menu_id, 'api:permission:detail', '查询权限详情', 'API', '/yq-admin/api/sysPermission/{id}', 1132, '根据ID查询权限接口', 'ACTIVE', now(), now()),
    (@permission_menu_id, 'api:permission:create', '新增权限', 'API', '/yq-admin/api/sysPermission', 1133, '新增权限接口', 'ACTIVE', now(), now()),
    (@permission_menu_id, 'api:permission:update', '修改权限', 'API', '/yq-admin/api/sysPermission/{id}', 1134, '修改权限接口', 'ACTIVE', now(), now()),
    (@permission_menu_id, 'api:permission:delete', '删除权限', 'API', '/yq-admin/api/sysPermission/{id}', 1135, '删除权限接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

-- Login is excluded from JwtAuthInterceptor and PermissionInterceptor in WebMvcConfig,
-- so it is normally not inserted as an API permission.
