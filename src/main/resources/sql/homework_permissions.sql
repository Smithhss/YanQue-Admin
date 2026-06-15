set @teaching_id := (select id from sys_permission where permission_code = 'teaching' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@teaching_id, 'teaching:homework', '作业管理', 'MENU', null, 2040, '作业管理页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @homework_menu_id := (select id from sys_permission where permission_code = 'teaching:homework' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@homework_menu_id, 'api:homework:page', '分页查询作业', 'API', '/yq-admin/api/homeworks', 2171, '分页查询作业接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:create', '新增作业', 'API', '/yq-admin/api/homeworks', 2172, '新增作业接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:prepare', '获取作业发布预填信息', 'API', '/yq-admin/api/homeworks/prepare', 2173, '获取作业发布预填信息接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:publish-answer', '发布作业答案', 'API', '/yq-admin/api/homeworks/{id}/answer', 2177, '发布作业答案接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:submissions', '查询作业提交记录', 'API', '/yq-admin/api/homeworks/{id}/submissions', 2178, '查询作业提交记录接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:grade-submission', '批改作业提交', 'API', '/yq-admin/api/homeworks/submissions/{submissionId}/grade', 2179, '批改作业提交接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();
