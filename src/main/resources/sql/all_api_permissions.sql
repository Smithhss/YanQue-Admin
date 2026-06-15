-- YanQue Admin permission tree.
-- api_path only stores request URI, for example: /yq-admin/api/sysUser
-- Requires a unique key on sys_permission.permission_code for idempotent upsert.

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'system', '系统管理', 'MENU', null, 10, '系统管理根节点', 'ACTIVE', now(), now()),
    (0, 'teaching', '教学管理', 'MENU', null, 20, '教学管理根节点', 'ACTIVE', now(), now()),
    (0, 'student', '学生管理', 'MENU', null, 30, '学生管理根节点', 'ACTIVE', now(), now()),
    (0, 'order', '订单管理', 'MENU', null, 40, '订单管理根节点', 'ACTIVE', now(), now())
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
set @student_id := (select id from sys_permission where permission_code = 'student' limit 1);
set @order_id := (select id from sys_permission where permission_code = 'order' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@system_id, 'system:user', '用户管理', 'MENU', null, 1010, '用户管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:role', '角色管理', 'MENU', null, 1020, '角色管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:permission', '权限管理', 'MENU', null, 1030, '权限管理页面', 'ACTIVE', now(), now()),
    (@system_id, 'system:config', '参数配置', 'MENU', null, 1040, '系统参数配置页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:campus', '校区管理', 'MENU', null, 2010, '校区管理页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:course', '课程管理', 'MENU', null, 2020, '课程管理页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:class', '班级管理', 'MENU', null, 2030, '班级管理页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:duty', '值班管理', 'MENU', null, 2050, '值班管理页面', 'ACTIVE', now(), now()),
    (@student_id, 'student:list', '学生列表', 'MENU', null, 3010, '学生列表页面', 'ACTIVE', now(), now()),
    (@order_id, 'order:product', '产品管理', 'MENU', null, 3010, '订单产品管理页面', 'ACTIVE', now(), now()),
    (@order_id, 'order:prepay', '预支付订单管理', 'MENU', null, 3020, '预支付订单管理页面', 'ACTIVE', now(), now()),
    (@order_id, 'order:payment', '订单管理', 'MENU', null, 3030, '支付订单管理页面', 'ACTIVE', now(), now())
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
set @student_menu_id := (select id from sys_permission where permission_code = 'student:list' limit 1);
set @duty_menu_id := (select id from sys_permission where permission_code = 'teaching:duty' limit 1);
set @product_menu_id := (select id from sys_permission where permission_code = 'order:product' limit 1);
set @prepay_order_menu_id := (select id from sys_permission where permission_code = 'order:prepay' limit 1);
set @payment_order_menu_id := (select id from sys_permission where permission_code = 'order:payment' limit 1);

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
    (@course_menu_id, 'api:course-detail:import', '导入课程明细', 'API', '/yq-admin/api/course/{courseId}/details/import', 2131, '导入课程明细接口', 'ACTIVE', now(), now()),

    (@class_menu_id, 'api:class:page', '分页查询班级', 'API', '/yq-admin/api/classes', 2141, '分页查询班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:detail', '查询班级详情', 'API', '/yq-admin/api/classes/{id}', 2142, '根据ID查询班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:create', '新增班级', 'API', '/yq-admin/api/classes', 2143, '新增班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:update', '修改班级', 'API', '/yq-admin/api/classes/{id}', 2144, '修改班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class:delete', '删除班级', 'API', '/yq-admin/api/classes/{id}', 2145, '删除班级接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:generate', '生成班级课表', 'API', '/yq-admin/api/classes/schedules/generate', 2146, '生成班级课表接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:list', '查询班级课表', 'API', '/yq-admin/api/classes/schedules/{classId}', 2147, '查询班级课表接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:stage-info', '查询班级阶段信息', 'API', '/yq-admin/api/classes/schedules/{classId}/classStageInfo', 2148, '查询班级阶段信息接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:assign-teacher', '分配课表老师', 'API', '/yq-admin/api/classes/schedules/{classId}/teachers', 2149, '按阶段分配课表老师接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:date-detail', '查询当天课程详情', 'API', '/yq-admin/api/classes/schedules/{classId}/date-detail', 2150, '查询班级当天课程详情接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:add-course', '新增临时课程', 'API', '/yq-admin/api/classes/schedules/{classId}/addClassSchule', 2151, '新增临时课程接口', 'ACTIVE', now(), now()),
    (@class_menu_id, 'api:class-schedule:teacher-detail', '查询老师课表', 'API', '/yq-admin/api/classes/schedules/teacher-detail', 2152, '查询老师上课详情接口', 'ACTIVE', now(), now()),

    (@student_menu_id, 'api:student:page', '分页查询学生', 'API', '/yq-admin/api/students', 2156, '分页查询学生接口', 'ACTIVE', now(), now()),

    (@duty_menu_id, 'api:class-duty:page', '分页查询值班', 'API', '/yq-admin/api/classDuties', 2161, '分页查询值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:detail', '查询值班详情', 'API', '/yq-admin/api/classDuties/{id}', 2162, '查询值班详情接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:create', '新增值班', 'API', '/yq-admin/api/classDuties', 2163, '新增值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:update', '修改值班', 'API', '/yq-admin/api/classDuties/{id}', 2164, '修改值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:delete', '删除值班', 'API', '/yq-admin/api/classDuties/{id}', 2165, '删除值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:date', '按日期查询值班', 'API', '/yq-admin/api/classDuties/date', 2166, '按日期查询值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:date-save', '按日期保存值班', 'API', '/yq-admin/api/classDuties/date', 2167, '按日期保存值班接口', 'ACTIVE', now(), now()),

    (@product_menu_id, 'api:product:page', '分页查询产品', 'API', '/yq-admin/api/products', 3111, '分页查询产品接口', 'ACTIVE', now(), now()),
    (@product_menu_id, 'api:product:detail', '查询产品详情', 'API', '/yq-admin/api/products/{id}', 3112, '根据ID查询产品接口', 'ACTIVE', now(), now()),
    (@product_menu_id, 'api:product:create', '新增产品', 'API', '/yq-admin/api/products', 3113, '新增产品接口', 'ACTIVE', now(), now()),
    (@product_menu_id, 'api:product:update', '修改产品', 'API', '/yq-admin/api/products/{id}', 3114, '修改产品接口', 'ACTIVE', now(), now()),
    (@product_menu_id, 'api:product:delete', '删除产品', 'API', '/yq-admin/api/products/{id}', 3115, '删除产品接口', 'ACTIVE', now(), now()),

    (@prepay_order_menu_id, 'api:prepay-order:page', '分页查询预支付订单', 'API', '/yq-admin/api/prepayOrders', 3121, '分页查询预支付订单接口', 'ACTIVE', now(), now()),
    (@prepay_order_menu_id, 'api:prepay-order:detail', '查询预支付订单详情', 'API', '/yq-admin/api/prepayOrders/{id}', 3122, '根据ID查询预支付订单接口', 'ACTIVE', now(), now()),
    (@prepay_order_menu_id, 'api:prepay-order:create', '新增预支付订单', 'API', '/yq-admin/api/prepayOrders', 3123, '新增预支付订单接口', 'ACTIVE', now(), now()),
    (@prepay_order_menu_id, 'api:prepay-order:update', '修改预支付订单', 'API', '/yq-admin/api/prepayOrders/{id}', 3124, '修改预支付订单接口', 'ACTIVE', now(), now()),
    (@prepay_order_menu_id, 'api:prepay-order:delete', '删除预支付订单', 'API', '/yq-admin/api/prepayOrders/{id}', 3125, '删除预支付订单接口', 'ACTIVE', now(), now()),

    (@payment_order_menu_id, 'api:order:page', '分页查询支付订单', 'API', '/yq-admin/api/orders', 3131, '分页查询支付订单接口', 'ACTIVE', now(), now())
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
