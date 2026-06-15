insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'student', '学生管理', 'MENU', null, 30, '学生管理根节点', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @student_id := (select id from sys_permission where permission_code = 'student' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@student_id, 'student:list', '学生列表', 'MENU', null, 3010, '学生列表页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @student_list_id := (select id from sys_permission where permission_code = 'student:list' limit 1);

update sys_permission
set parent_id = @student_list_id,
    updated_at = now()
where permission_code = 'api:student:page';

update sys_permission
set status = 'INACTIVE',
    updated_at = now()
where permission_code = 'teaching:student';
