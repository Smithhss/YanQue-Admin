-- YanQue Admin permission tree.
-- api_path only stores request URI, for example: /yq-admin/api/sysUser
-- Requires a unique key on sys_permission.permission_code for idempotent upsert.

insert into sys_permission
    (parent_id, permission_code, permission_name, permission_type, api_path, sort_num, description, status, created_at, updated_at)
values
    (0, 'system', '系统管理', 'MENU', null, 10, '系统管理根节点', 'ACTIVE', now(), now()),
    (0, 'teaching', '教学管理', 'MENU', null, 20, '教学管理根节点', 'ACTIVE', now(), now()),
    (0, 'student', '学生管理', 'MENU', null, 30, '学生管理根节点', 'ACTIVE', now(), now()),
    (0, 'order', '订单管理', 'MENU', null, 40, '订单管理根节点', 'ACTIVE', now(), now()),
    (0, 'exam', '考试管理', 'MENU', null, 50, '考试管理根节点', 'ACTIVE', now(), now())
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
set @exam_id := (select id from sys_permission where permission_code = 'exam' limit 1);

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
    (@teaching_id, 'teaching:homework', '作业管理', 'MENU', null, 2040, '作业管理页面', 'ACTIVE', now(), now()),
    (@teaching_id, 'teaching:duty', '值班管理', 'MENU', null, 2050, '值班管理页面', 'ACTIVE', now(), now()),
    (@student_id, 'student:list', '学生列表', 'MENU', null, 3010, '学生列表页面', 'ACTIVE', now(), now()),
    (@student_id, 'student:followup-tag', '回访标签管理', 'MENU', null, 3020, '学生回访标签管理页面', 'ACTIVE', now(), now()),
    (@student_id, 'student:followup-record', '回访管理', 'MENU', null, 3030, '学生回访管理页面', 'ACTIVE', now(), now()),
    (@student_id, 'student:sop', 'SOP管理', 'MENU', null, 3040, '学生入学SOP管理页面', 'ACTIVE', now(), now()),
    (@student_id, 'student:learning-plan', '线上学习计划', 'MENU', null, 3050, '线上学员学习计划页面', 'ACTIVE', now(), now()),
    (@order_id, 'order:product', '产品管理', 'MENU', null, 3010, '订单产品管理页面', 'ACTIVE', now(), now()),
    (@order_id, 'order:prepay', '预支付订单管理', 'MENU', null, 3020, '预支付订单管理页面', 'ACTIVE', now(), now()),
    (@order_id, 'order:payment', '订单管理', 'MENU', null, 3030, '支付订单管理页面', 'ACTIVE', now(), now()),
    (@exam_id, 'exam:question', '题库管理', 'MENU', null, 5010, '题库管理页面', 'ACTIVE', now(), now()),
    (@exam_id, 'exam:paper', '试卷管理', 'MENU', null, 5020, '试卷管理页面', 'ACTIVE', now(), now()),
    (@exam_id, 'exam:list', '考试列表', 'MENU', null, 5030, '考试列表页面', 'ACTIVE', now(), now())
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
set @homework_menu_id := (select id from sys_permission where permission_code = 'teaching:homework' limit 1);
set @student_menu_id := (select id from sys_permission where permission_code = 'student:list' limit 1);
set @student_followup_tag_menu_id := (select id from sys_permission where permission_code = 'student:followup-tag' limit 1);
set @student_followup_record_menu_id := (select id from sys_permission where permission_code = 'student:followup-record' limit 1);
set @student_sop_menu_id := (select id from sys_permission where permission_code = 'student:sop' limit 1);
set @student_learning_plan_menu_id := (select id from sys_permission where permission_code = 'student:learning-plan' limit 1);
set @duty_menu_id := (select id from sys_permission where permission_code = 'teaching:duty' limit 1);
set @product_menu_id := (select id from sys_permission where permission_code = 'order:product' limit 1);
set @prepay_order_menu_id := (select id from sys_permission where permission_code = 'order:prepay' limit 1);
set @payment_order_menu_id := (select id from sys_permission where permission_code = 'order:payment' limit 1);
set @exam_question_menu_id := (select id from sys_permission where permission_code = 'exam:question' limit 1);
set @exam_paper_menu_id := (select id from sys_permission where permission_code = 'exam:paper' limit 1);
set @exam_list_menu_id := (select id from sys_permission where permission_code = 'exam:list' limit 1);

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
    (@system_id, 'api:upload:presign-upload', '获取上传预签名', 'API', '/yq-admin/api/upload/presign-upload', 1151, '获取通用上传预签名接口', 'ACTIVE', now(), now()),
    (@system_id, 'api:upload:presign-download', '获取下载预签名', 'API', '/yq-admin/api/upload/presign-download', 1152, '获取通用下载预签名接口', 'ACTIVE', now(), now()),

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
    (@course_menu_id, 'api:course-detail:detail', '查询课程明细详情', 'API', '/yq-admin/api/course-details/{id}', 2127, '根据ID查询课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:create', '新增课程明细', 'API', '/yq-admin/api/course/{courseId}/details', 2128, '新增课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:update', '修改课程明细', 'API', '/yq-admin/api/course-details/{id}', 2129, '修改课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:delete', '删除课程明细', 'API', '/yq-admin/api/course-details/{id}', 2130, '删除课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-detail:import', '导入课程明细', 'API', '/yq-admin/api/course/{courseId}/details/import', 2131, '导入课程明细接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-homework-template:list', '查询课程作业标准列表', 'API', '/yq-admin/api/course/{courseId}/homeworkTemplates', 2132, '查询课程作业标准列表接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-homework-template:detail', '查询课程作业标准详情', 'API', '/yq-admin/api/course/homeworkTemplates/{id}', 2133, '查询课程作业标准详情接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-homework-template:create', '新增课程作业标准', 'API', '/yq-admin/api/course/{courseId}/homeworkTemplates', 2134, '新增课程作业标准接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-homework-template:update', '修改课程作业标准', 'API', '/yq-admin/api/course/homeworkTemplates/{id}', 2135, '修改课程作业标准接口', 'ACTIVE', now(), now()),
    (@course_menu_id, 'api:course-homework-template:delete', '删除课程作业标准', 'API', '/yq-admin/api/course/homeworkTemplates/{id}', 2136, '删除课程作业标准接口', 'ACTIVE', now(), now()),

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

    (@homework_menu_id, 'api:homework:page', '分页查询作业', 'API', '/yq-admin/api/homeworks', 2171, '分页查询作业接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:create', '新增作业', 'API', '/yq-admin/api/homeworks', 2172, '新增作业接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:prepare', '获取作业发布预填信息', 'API', '/yq-admin/api/homeworks/prepare', 2173, '获取作业发布预填信息接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:publish-answer', '发布作业答案', 'API', '/yq-admin/api/homeworks/{id}/answer', 2177, '发布作业答案接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:submissions', '查询作业提交记录', 'API', '/yq-admin/api/homeworks/{id}/submissions', 2178, '查询作业提交记录接口', 'ACTIVE', now(), now()),
    (@homework_menu_id, 'api:homework:grade-submission', '批改作业提交', 'API', '/yq-admin/api/homeworks/submissions/{submissionId}/grade', 2179, '批改作业提交接口', 'ACTIVE', now(), now()),

    (@student_menu_id, 'api:student:page', '分页查询学生', 'API', '/yq-admin/api/students', 2156, '分页查询学生接口', 'ACTIVE', now(), now()),
    (@student_menu_id, 'api:student:assign-class', '学生分配班级', 'API', '/yq-admin/api/students/{id}/class', 2157, '给线下学生分配班级接口', 'ACTIVE', now(), now()),
    (@student_menu_id, 'api:student:tag-options', '查询学生标签选项', 'API', '/yq-admin/api/students/tag-options', 2158, '查询学生标签选项接口', 'ACTIVE', now(), now()),
    (@student_menu_id, 'api:student:update-tag', '修改学生标签', 'API', '/yq-admin/api/students/{id}/tag', 2159, '修改学生标签接口', 'ACTIVE', now(), now()),
    (@student_menu_id, 'api:student:assign-sop', '分配学生入学SOP', 'API', '/yq-admin/api/students/{id}/sop', 2160, '给线上学生分配入学SOP接口', 'ACTIVE', now(), now()),
    (@student_followup_tag_menu_id, 'api:student-followup-tag:page', '分页查询回访标签配置', 'API', '/yq-admin/api/studentFollowupTags', 2161, '分页查询学生回访标签配置接口', 'ACTIVE', now(), now()),
    (@student_followup_tag_menu_id, 'api:student-followup-tag:detail', '查询回访标签配置详情', 'API', '/yq-admin/api/studentFollowupTags/{id}', 2162, '查询学生回访标签配置详情接口', 'ACTIVE', now(), now()),
    (@student_followup_tag_menu_id, 'api:student-followup-tag:create', '新增回访标签配置', 'API', '/yq-admin/api/studentFollowupTags', 2163, '新增学生回访标签配置接口', 'ACTIVE', now(), now()),
    (@student_followup_tag_menu_id, 'api:student-followup-tag:update', '修改回访标签配置', 'API', '/yq-admin/api/studentFollowupTags/{id}', 2164, '修改学生回访标签配置接口', 'ACTIVE', now(), now()),
    (@student_followup_tag_menu_id, 'api:student-followup-tag:delete', '删除回访标签配置', 'API', '/yq-admin/api/studentFollowupTags/{id}', 2165, '删除学生回访标签配置接口', 'ACTIVE', now(), now()),
    (@student_followup_record_menu_id, 'api:student-followup-record:page', '分页查询回访记录', 'API', '/yq-admin/api/studentFollowupRecords', 2166, '分页查询学生回访记录接口', 'ACTIVE', now(), now()),
    (@student_followup_record_menu_id, 'api:student-followup-record:stats', '查询回访统计', 'API', '/yq-admin/api/studentFollowupRecords/by-stats', 2166, '查询学生回访统计接口', 'ACTIVE', now(), now()),
    (@student_followup_record_menu_id, 'api:student-followup-record:detail', '查询回访记录详情', 'API', '/yq-admin/api/studentFollowupRecords/{id}', 2167, '查询学生回访记录详情接口', 'ACTIVE', now(), now()),
    (@student_followup_record_menu_id, 'api:student-followup-record:generate', '手动生成回访记录', 'API', '/yq-admin/api/studentFollowupRecords/generate', 2168, '手动生成学生回访记录接口', 'ACTIVE', now(), now()),
    (@student_followup_record_menu_id, 'api:student-followup-record:complete', '完成回访记录', 'API', '/yq-admin/api/studentFollowupRecords/{id}/complete', 2169, '完成学生回访记录接口', 'ACTIVE', now(), now()),
    (@student_followup_record_menu_id, 'api:student-followup-record:cancel', '取消回访记录', 'API', '/yq-admin/api/studentFollowupRecords/{id}/cancel', 2170, '取消学生回访记录接口', 'ACTIVE', now(), now()),
    (@student_sop_menu_id, 'api:student-sop:page', '分页查询学生入学SOP', 'API', '/yq-admin/api/studentSops', 2171, '分页查询学生入学SOP接口', 'ACTIVE', now(), now()),
    (@student_sop_menu_id, 'api:student-sop:complete', '完成学生入学SOP', 'API', '/yq-admin/api/studentSops/{id}/complete', 2172, '完成学生入学SOP接口', 'ACTIVE', now(), now()),
    (@student_learning_plan_menu_id, 'api:student-learning-plan:create', '创建线上学习计划', 'API', '/yq-admin/api/studentLearningPlans', 2173, '创建线上学习计划接口', 'ACTIVE', now(), now()),
    (@student_learning_plan_menu_id, 'api:student-learning-plan:page', '分页查询线上学习计划', 'API', '/yq-admin/api/studentLearningPlans', 2174, '分页查询线上学习计划接口', 'ACTIVE', now(), now()),
    (@student_learning_plan_menu_id, 'api:student-learning-plan:detail', '查询线上学习计划详情', 'API', '/yq-admin/api/studentLearningPlans/{id}', 2175, '查询线上学习计划详情接口', 'ACTIVE', now(), now()),
    (@student_learning_plan_menu_id, 'api:student-learning-plan:calendar', '查询线上学习日历', 'API', '/yq-admin/api/studentLearningPlans/{id}/calendar', 2176, '查询线上学习日历接口', 'ACTIVE', now(), now()),

    (@duty_menu_id, 'api:class-duty:page', '分页查询值班', 'API', '/yq-admin/api/classDuties', 2161, '分页查询值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:detail', '查询值班详情', 'API', '/yq-admin/api/classDuties/{id}', 2162, '查询值班详情接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:create', '新增值班', 'API', '/yq-admin/api/classDuties', 2163, '新增值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:update', '修改值班', 'API', '/yq-admin/api/classDuties/{id}', 2164, '修改值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:delete', '删除值班', 'API', '/yq-admin/api/classDuties/{id}', 2165, '删除值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:date', '按日期查询值班', 'API', '/yq-admin/api/classDuties/by-date', 2166, '按日期查询值班接口', 'ACTIVE', now(), now()),
    (@duty_menu_id, 'api:class-duty:date-save', '按日期保存值班', 'API', '/yq-admin/api/classDuties/by-date', 2167, '按日期保存值班接口', 'ACTIVE', now(), now()),

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

    (@payment_order_menu_id, 'api:order:page', '分页查询支付订单', 'API', '/yq-admin/api/orders', 3131, '分页查询支付订单接口', 'ACTIVE', now(), now()),
    (@payment_order_menu_id, 'api:refund-order:page', '分页查询退款订单', 'API', '/yq-admin/api/refundOrders', 3132, '分页查询退款订单接口', 'ACTIVE', now(), now()),
    (@payment_order_menu_id, 'api:refund-order:create', '创建退款订单号', 'API', '/yq-admin/api/refundOrders/create', 3133, '创建退款订单号接口', 'ACTIVE', now(), now()),
    (@payment_order_menu_id, 'api:refund-order:apply', '申请退款', 'API', '/yq-admin/api/refundOrders/{refundOrderNo}/apply', 3134, '申请退款接口', 'ACTIVE', now(), now()),

    (@exam_question_menu_id, 'api:exam-question:page', '分页查询题库', 'API', '/yq-admin/api/examQuestions', 5111, '分页查询题库接口', 'ACTIVE', now(), now()),
    (@exam_question_menu_id, 'api:exam-question:detail', '查询题目详情', 'API', '/yq-admin/api/examQuestions/{id}', 5112, '根据ID查询题目接口', 'ACTIVE', now(), now()),
    (@exam_question_menu_id, 'api:exam-question:create', '新增题目', 'API', '/yq-admin/api/examQuestions', 5113, '新增题目接口', 'ACTIVE', now(), now()),
    (@exam_question_menu_id, 'api:exam-question:update', '修改题目', 'API', '/yq-admin/api/examQuestions/{id}', 5114, '修改题目接口', 'ACTIVE', now(), now()),
    (@exam_question_menu_id, 'api:exam-question:delete', '删除题目', 'API', '/yq-admin/api/examQuestions/{id}', 5115, '删除题目接口', 'ACTIVE', now(), now()),
    (@exam_question_menu_id, 'api:exam-question:status', '修改题目状态', 'API', '/yq-admin/api/examQuestions/{id}/status', 5116, '修改题目状态接口', 'ACTIVE', now(), now()),

    (@exam_paper_menu_id, 'api:exam-paper:page', '分页查询试卷', 'API', '/yq-admin/api/examPapers', 5211, '分页查询试卷接口', 'ACTIVE', now(), now()),
    (@exam_paper_menu_id, 'api:exam-paper:detail', '查询试卷详情', 'API', '/yq-admin/api/examPapers/{id}', 5212, '根据ID查询试卷接口', 'ACTIVE', now(), now()),
    (@exam_paper_menu_id, 'api:exam-paper:save', '保存试卷', 'API', '/yq-admin/api/examPapers', 5213, '保存试卷和题目接口', 'ACTIVE', now(), now()),
    (@exam_paper_menu_id, 'api:exam-paper:delete', '删除试卷', 'API', '/yq-admin/api/examPapers/{id}', 5214, '删除试卷接口', 'ACTIVE', now(), now()),

    (@exam_list_menu_id, 'api:exam:page', '分页查询考试', 'API', '/yq-admin/api/exams', 5311, '分页查询考试接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:detail', '查询考试详情', 'API', '/yq-admin/api/exams/{id}', 5312, '根据ID查询考试接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:create', '新增考试', 'API', '/yq-admin/api/exams', 5313, '新增考试接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:update', '修改考试', 'API', '/yq-admin/api/exams/{id}', 5314, '修改考试接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:delete', '删除考试', 'API', '/yq-admin/api/exams/{id}', 5315, '删除考试接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:answer-visible', '公布考试结果', 'API', '/yq-admin/api/exams/{id}/answer-visible', 5316, '公布或取消公布考试结果接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:submission-page', '分页查询考试提交记录', 'API', '/yq-admin/api/exams/{id}/submissions', 5317, '分页查询考试提交记录接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:submission-detail', '查询考试答卷详情', 'API', '/yq-admin/api/exam-submissions/{recordId}', 5318, '查询考试答卷详情接口', 'ACTIVE', now(), now()),
    (@exam_list_menu_id, 'api:exam:submission-grade', '批改考试答卷', 'API', '/yq-admin/api/exam-submissions/{recordId}/grade', 5319, '批改考试答卷接口', 'ACTIVE', now(), now())
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
