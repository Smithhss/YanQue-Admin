-- 考勤管理：挂在「教学管理(teaching)」菜单下
set @teaching_id := (select id from sys_permission where permission_code = 'teaching' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@teaching_id, 'teaching:attendance', '考勤管理', 'MENU', null, 2050, '考勤管理页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @attendance_menu_id := (select id from sys_permission where permission_code = 'teaching:attendance' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@attendance_menu_id, 'api:attendance:roster', '获取课次考勤名单', 'API', '/yq-admin/api/attendance/roster', 2181, '获取某课次考勤名单接口', 'ACTIVE', now(), now()),
    (@attendance_menu_id, 'api:attendance:commit', '提交考勤', 'API', '/yq-admin/api/attendance/commit', 2182, '批量提交/修改考勤接口', 'ACTIVE', now(), now()),
    (@attendance_menu_id, 'api:attendance:page', '分页查询考勤记录', 'API', '/yq-admin/api/attendance', 2183, '分页查询考勤记录接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

-- 课时账户：挂在「学生管理(student)」菜单下
set @student_id := (select id from sys_permission where permission_code = 'student' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@student_id, 'student:coursehour', '课时账户', 'MENU', null, 3020, '学生课时账户页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @coursehour_menu_id := (select id from sys_permission where permission_code = 'student:coursehour' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@coursehour_menu_id, 'api:coursehour:detail', '查询学生课时账户', 'API', '/yq-admin/api/studentCourseHour/{studentId}', 3181, '查询学生课时账户接口', 'ACTIVE', now(), now()),
    (@coursehour_menu_id, 'api:coursehour:adjust', '调整学生课时', 'API', '/yq-admin/api/studentCourseHour/adjust', 3182, '调整学生课时接口', 'ACTIVE', now(), now()),
    (@coursehour_menu_id, 'api:coursehour:page', '分页查询课时账户', 'API', '/yq-admin/api/studentCourseHour', 3183, '分页查询课时账户接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();
