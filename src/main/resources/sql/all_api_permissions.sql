-- YanQue Admin permission tree.
-- api_path only stores request URI, for example: /yq-admin/api/sysUser
-- Requires a unique key on sys_permission.permission_code for idempotent upsert.

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'system', '系统管理', 'MENU', null, 10, '系统管理根节点', 'ACTIVE', now(), now()),
    (0, 'teaching', '教学管理', 'MENU', null, 20, '教学管理根节点', 'ACTIVE', now(), now())
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
set @teaching_id := (select id from sys_permission where permission_code = 'teaching' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@system_id, 'system:user', '用户管理', 'MENU', null, 1010, '用户管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:role', '角色管理', 'MENU', null, 1020, '角色管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:permission', '权限管理', 'MENU', null, 1030, '权限管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:config', '参数配置', 'MENU', null, 1040, '系统参数配置页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:campus', '校区管理', 'MENU', null, 2010, '校区管理页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:course', '课程管理', 'MENU', null, 2020, '课程管理页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:class', '班级管理', 'MENU', null, 2030, '班级管理页面', 'ACTIVE', now(), now())
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
set @config_menu_id := (select id from sys_permission where permission_code = 'system:config' limit 1);
set @campus_menu_id := (select id from sys_permission where permission_code = 'teaching:campus' limit 1);
set @course_menu_id := (select id from sys_permission where permission_code = 'teaching:course' limit 1);
set @class_menu_id := (select id from sys_permission where permission_code = 'teaching:class' limit 1);

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
    (@permission_menu_id, 'api:permission:delete', '删除权限', 'API', '/yq-admin/api/sysPermission/{id}', 1135, '删除权限接口', 'ACTIVE', now(), now()),

    (@config_menu_id, 'api:config:page', '分页查询配置', 'API', '/yq-admin/api/sysConfig', 1141, '分页查询系统配置接口', 'ACTIVE', now(), now()),
    (@config_menu_id, 'api:config:detail', '查询配置详情', 'API', '/yq-admin/api/sysConfig/{id}', 1142, '根据ID查询系统配置接口', 'ACTIVE', now(), now()),
    (@config_menu_id, 'api:config:create', '新增配置', 'API', '/yq-admin/api/sysConfig', 1143, '新增系统配置接口', 'ACTIVE', now(), now()),
    (@config_menu_id, 'api:config:update', '修改配置', 'API', '/yq-admin/api/sysConfig/{id}', 1144, '修改系统配置接口', 'ACTIVE', now(), now()),
    (@config_menu_id, 'api:config:delete', '删除配置', 'API', '/yq-admin/api/sysConfig/{id}', 1145, '删除系统配置接口', 'ACTIVE', now(), now()),

    (@campus_menu_id, 'api:campus:page', '分页查询校区', 'API', '/yq-admin/api/campus', 2111, '分页查询校区接口', 'ACTIVE', now(), now()),
    (@campus_menu_id, 'api:campus:detail', '查询校区详情', 'API', '/yq-admin/api/campus/{id}', 2112, '根据ID查询校区接口', 'ACTIVE', now(), now()),
    (@campus_menu_id, 'api:campus:create', '新增校区', 'API', '/yq-admin/api/campus', 2113, '新增校区接口', 'ACTIVE', now(), now()),
    (@campus_menu_id, 'api:campus:update', '修改校区', 'API', '/yq-admin/api/campus/{id}', 2114, '修改校区接口', 'ACTIVE', now(), now()),
    (@campus_menu_id, 'api:campus:delete', '删除校区', 'API', '/yq-admin/api/campus/{id}', 2115, '删除校区接口', 'ACTIVE', now(), now()),

    (@course_menu_id, 'api:course:page', '分页查询课程', 'API', '/yq-admin/api/course', 2121, '分页查询课程接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course:detail', '查询课程详情', 'API', '/yq-admin/api/course/{id}', 2122, '根据ID查询课程接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course:create', '新增课程', 'API', '/yq-admin/api/course', 2123, '新增课程接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course:update', '修改课程', 'API', '/yq-admin/api/course/{id}', 2124, '修改课程接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course:delete', '删除课程', 'API', '/yq-admin/api/course/{id}', 2125, '删除课程接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:page', '查询课程明细列表', 'API', '/yq-admin/api/course/{courseId}/details', 2126, '查询课程明细列表接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:detail', '查询课程明细详情', 'API', '/yq-admin/api/course/details/{id}', 2127, '根据ID查询课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:create', '新增课程明细', 'API', '/yq-admin/api/course/{courseId}/details', 2128, '新增课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:update', '修改课程明细', 'API', '/yq-admin/api/course/details/{id}', 2129, '修改课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:delete', '删除课程明细', 'API', '/yq-admin/api/course/details/{id}', 2130, '删除课程明细接口', 'ACTIVE', now(), now()),

    (@class_menu_id, 'api:class:page', '分页查询班级', 'API', '/yq-admin/api/classes', 2131, '分页查询班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:detail', '查询班级详情', 'API', '/yq-admin/api/classes/{id}', 2132, '根据ID查询班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:create', '新增班级', 'API', '/yq-admin/api/classes', 2133, '新增班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:update', '修改班级', 'API', '/yq-admin/api/classes/{id}', 2134, '修改班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:delete', '删除班级', 'API', '/yq-admin/api/classes/{id}', 2135, '删除班级接口', 'ACTIVE', now(), now())
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
