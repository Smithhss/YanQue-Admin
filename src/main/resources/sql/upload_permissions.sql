set @system_id := (select id from sys_permission where permission_code = 'system' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@system_id, 'api:upload:presign-upload', '获取上传预签名', 'API', '/yq-admin/api/upload/presign-upload', 1151, '获取通用上传预签名接口', 'ACTIVE', now(), now()),
    (@system_id, 'api:upload:presign-download', '获取下载预签名', 'API', '/yq-admin/api/upload/presign-download', 1152, '获取通用下载预签名接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();
