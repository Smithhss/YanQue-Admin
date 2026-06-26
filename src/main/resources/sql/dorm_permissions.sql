-- F014 宿舍管理 RBAC：菜单 / API / 宿管角色
-- PermissionInterceptor 仅按 requestURI 与 api_path 做 AntPath 匹配，不区分 HTTP method。

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'dorm', '宿舍管理', 'MENU', null, 6000, '宿舍管理模块', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @dorm_id := (select id from sys_permission where permission_code = 'dorm' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@dorm_id, 'dorm:building', '宿舍楼栋', 'MENU', null, 6010, '宿舍楼栋管理页面', 'ACTIVE', now(), now()),
    (@dorm_id, 'dorm:room', '宿舍房间', 'MENU', null, 6020, '宿舍房间管理页面', 'ACTIVE', now(), now()),
    (@dorm_id, 'dorm:bed', '宿舍床位', 'MENU', null, 6030, '宿舍床位管理页面', 'ACTIVE', now(), now()),
    (@dorm_id, 'dorm:assignment', '入住管理', 'MENU', null, 6040, '宿舍入住分配页面', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

set @dorm_building_id := (select id from sys_permission where permission_code = 'dorm:building' limit 1);
set @dorm_room_id := (select id from sys_permission where permission_code = 'dorm:room' limit 1);
set @dorm_bed_id := (select id from sys_permission where permission_code = 'dorm:bed' limit 1);
set @dorm_assignment_id := (select id from sys_permission where permission_code = 'dorm:assignment' limit 1);

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (@dorm_building_id, 'api:dorm-building:page', '分页查询宿舍楼栋', 'API', '/yq-admin/api/dorm/building', 6011, '分页查询宿舍楼栋接口', 'ACTIVE', now(), now()),
    (@dorm_building_id, 'api:dorm-building:detail', '查询宿舍楼栋详情', 'API', '/yq-admin/api/dorm/building/{id}', 6012, '根据ID查询宿舍楼栋接口', 'ACTIVE', now(), now()),
    (@dorm_building_id, 'api:dorm-building:create', '新增宿舍楼栋', 'API', '/yq-admin/api/dorm/building', 6013, '新增宿舍楼栋接口', 'ACTIVE', now(), now()),
    (@dorm_building_id, 'api:dorm-building:update', '修改宿舍楼栋', 'API', '/yq-admin/api/dorm/building/{id}', 6014, '修改宿舍楼栋接口', 'ACTIVE', now(), now()),
    (@dorm_building_id, 'api:dorm-building:delete', '删除宿舍楼栋', 'API', '/yq-admin/api/dorm/building/{id}', 6015, '删除宿舍楼栋接口', 'ACTIVE', now(), now()),

    (@dorm_room_id, 'api:dorm-room:page', '分页查询宿舍房间', 'API', '/yq-admin/api/dorm/room', 6021, '分页查询宿舍房间接口', 'ACTIVE', now(), now()),
    (@dorm_room_id, 'api:dorm-room:detail', '查询宿舍房间详情', 'API', '/yq-admin/api/dorm/room/{id}', 6022, '根据ID查询宿舍房间接口', 'ACTIVE', now(), now()),
    (@dorm_room_id, 'api:dorm-room:create', '新增宿舍房间', 'API', '/yq-admin/api/dorm/room', 6023, '新增宿舍房间接口', 'ACTIVE', now(), now()),
    (@dorm_room_id, 'api:dorm-room:update', '修改宿舍房间', 'API', '/yq-admin/api/dorm/room/{id}', 6024, '修改宿舍房间接口', 'ACTIVE', now(), now()),
    (@dorm_room_id, 'api:dorm-room:delete', '删除宿舍房间', 'API', '/yq-admin/api/dorm/room/{id}', 6025, '删除宿舍房间接口', 'ACTIVE', now(), now()),

    (@dorm_bed_id, 'api:dorm-bed:page', '分页查询宿舍床位', 'API', '/yq-admin/api/dorm/bed', 6031, '分页查询宿舍床位接口', 'ACTIVE', now(), now()),
    (@dorm_bed_id, 'api:dorm-bed:detail', '查询宿舍床位详情', 'API', '/yq-admin/api/dorm/bed/{id}', 6032, '根据ID查询宿舍床位接口', 'ACTIVE', now(), now()),
    (@dorm_bed_id, 'api:dorm-bed:create', '新增宿舍床位', 'API', '/yq-admin/api/dorm/bed', 6033, '新增宿舍床位接口', 'ACTIVE', now(), now()),
    (@dorm_bed_id, 'api:dorm-bed:update', '修改宿舍床位', 'API', '/yq-admin/api/dorm/bed/{id}', 6034, '修改宿舍床位接口', 'ACTIVE', now(), now()),
    (@dorm_bed_id, 'api:dorm-bed:delete', '删除宿舍床位', 'API', '/yq-admin/api/dorm/bed/{id}', 6035, '删除宿舍床位接口', 'ACTIVE', now(), now()),

    (@dorm_assignment_id, 'api:dorm-assignment:page', '分页查询入住记录', 'API', '/yq-admin/api/dorm/assignment', 6041, '分页查询入住记录接口', 'ACTIVE', now(), now()),
    (@dorm_assignment_id, 'api:dorm-assignment:assign', '入住分配', 'API', '/yq-admin/api/dorm/assignment/assign', 6042, '入住分配接口', 'ACTIVE', now(), now()),
    (@dorm_assignment_id, 'api:dorm-assignment:checkout', '退宿', 'API', '/yq-admin/api/dorm/assignment/{id}/checkout', 6043, '退宿接口', 'ACTIVE', now(), now()),
    (@dorm_assignment_id, 'api:dorm-assignment:transfer', '调宿', 'API', '/yq-admin/api/dorm/assignment/transfer', 6044, '调宿接口', 'ACTIVE', now(), now())
on duplicate key update
    parent_id = values(parent_id),
    permission_name = values(permission_name),
    permission_type = values(permission_type),
    api_path = values(api_path),
    sort_num = values(sort_num),
    description = values(description),
    status = values(status),
    updated_at = now();

insert into sys_role (role_code, role_name, description, status, created_at, updated_at)
values ('DORM_MANAGER', '宿管员', '负责宿舍楼栋、房间、床位与入住分配管理', 'ACTIVE', now(), now())
on duplicate key update
    role_name = values(role_name),
    description = values(description),
    status = values(status),
    updated_at = now();

set @dorm_manager_role_id := (select id from sys_role where role_code = 'DORM_MANAGER' limit 1);

insert ignore into sys_role_permission (role_id, permission_id, created_at)
select @dorm_manager_role_id, p.id, now()
from sys_permission p
where p.permission_code = 'dorm'
   or p.permission_code like 'dorm:%'
   or p.permission_code like 'api:dorm-%';
