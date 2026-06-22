# 考勤与课时模块 测试指南

> 适用：本地/独立联调库。后端 `local` profile（连本地 MySQL）。
> 管理端 http://localhost:5173 （`admin / admin123`）；学生端 http://localhost:5174。

---

## 〇、本地测试环境（已搭好,长期保留复用）

| 组件 | 地址 | 说明 |
|---|---|---|
| MySQL | `localhost:3306` 库 `yanque` | 账号 `root/root`,37 表 + 测试数据 |
| Redis | `localhost:6379` | 无密码 |
| 后端 profile | `application-local.yaml` | 连本地库,不影响 dev/prod |

**启动**：
```bash
# 后端（YanQue-Admin 目录）—— local profile 必须用 JVM 系统属性指定
#   注意：-Dspring-boot.run.profiles=local 无效（application.yaml 硬编码了 active），必须用 jvmArguments
mvn -s settings.xml spring-boot:run -Dspring-boot.run.jvmArguments="-Dspring.profiles.active=local"
# 前端
cd ../YanQue-Admin-Web  && npm run dev   # http://localhost:5173
cd ../YanQue-Student-Web && npm run dev   # http://localhost:5174
```
登录：管理端 `admin/admin123`；学生端用学生手机号 + `123456`。

---

## 一、准备测试数据（可重复执行，每次重置测试环境）

把下面整段贴进 MySQL 执行（同一连接，依赖 `@变量`）：

```sql
-- ===== 清理上一轮测试数据 =====
delete from class_attendance;
delete from student_course_hour_log;
delete from student_course_hour;
delete from student where student_phone in ('13900000001','13900000002','13900000003','13900000004') or student_no like 'TEST%';
delete from sys_class_schedule where course_content like '【测试】%';
delete from sys_class where class_period like '【测试】%';
delete from sys_course where course_name like '【测试】%';
delete from sys_campus where campus_location like '【测试】%';

-- ===== 造测试数据 =====
insert into sys_campus (campus_location, manager_name, manager_phone, created_at, updated_at)
 values ('【测试】演示校区','张老师','13800000000',now(),now());
set @campus := last_insert_id();

insert into sys_course (course_name, course_days, teaching_mode, material_path, created_at, updated_at)
 values ('【测试】Java+AI',30,'OFFLINE','/test',now(),now());
set @course := last_insert_id();

insert into sys_class (class_period, head_teacher_id, campus_id, course_id, created_at, updated_at)
 values ('【测试】班级A',1,@campus,@course,now(),now());
set @classA := last_insert_id();
insert into sys_class (class_period, head_teacher_id, campus_id, course_id, created_at, updated_at)
 values ('【测试】班级B',1,@campus,@course,now(),now());
set @classB := last_insert_id();

insert into sys_class_schedule (class_id, teacher_id, schedule_date, course_content, class_type, created_at, updated_at) values
 (@classA,1,'2026-06-22','【测试】第1课 上课','CLASS',now(),now()),
 (@classA,1,'2026-06-23','【测试】第2课 上课','CLASS',now(),now()),
 (@classA,1,'2026-06-24','【测试】自习日','SELF_STUDY',now(),now());

insert into student (student_no, student_name, student_phone, password, education, grade_year, school, teaching_mode, class_id, status, created_at, updated_at) values
 ('TEST001','张三','13900000001','123456','本科',2026,'演示大学','OFFLINE',@classA,'ACTIVE',now(),now()),
 ('TEST002','李四','13900000002','123456','本科',2026,'演示大学','OFFLINE',@classA,'ACTIVE',now(),now()),
 ('TEST003','王五','13900000003','123456','本科',2026,'演示大学','OFFLINE',@classA,'ACTIVE',now(),now()),
 ('TEST004','赵六','13900000004','123456','本科',2026,'演示大学','OFFLINE',@classB,'ACTIVE',now(),now());

set @zhangsan := (select id from student where student_no='TEST001');
set @lisi := (select id from student where student_no='TEST002');
insert into student_course_hour (student_id, total_hours, used_hours, remaining_hours, created_at, updated_at) values
 (@zhangsan,10,0,10,now(),now()),
 (@lisi,2,0,2,now(),now());
```

**执行方式**（任选）：
- 命令行：`mysql -h127.0.0.1 -uroot -proot --default-character-set=utf8mb4 yanque < 上述脚本.sql`
- 或 Navicat/DBeaver 连 `localhost:3306/yanque`（root/root）整段运行。

**数据概览**：

| 班级 | 课次 | 学生（课时余额） |
|---|---|---|
| 【测试】班级A | 第1课(06-22,上课)、第2课(06-23,上课)、自习日(06-24,自习) | 张三(10)、李四(2)、王五(无账户=0) |
| 【测试】班级B | 无 | 赵六(无账户) |

---

## 二、管理端功能测试（http://localhost:5173,admin/admin123）

### 课时账户（学生管理 → 课时账户）
| # | 操作 | 预期 |
|---|---|---|
| 1 | 打开页面 | 列表显示张三(剩余10)、李四(剩余2)；王五/赵六无账户不显示 |
| 2 | 输入张三学生ID查询 | 只显示张三 |
| 3 | 张三「调整课时」填 `+5` | 张三累计15、剩余15 |
| 4 | 张三「调整课时」填 `-3`、原因「补扣」 | 张三已用3、剩余12 |

### 考勤（教学管理 → 考勤管理）
| # | 操作 | 预期 |
|---|---|---|
| 5 | 选「班级A」→ 课次下拉 | 只出现两节**上课**(06-22/06-23)，自习日**不出现**（前端按 CLASS 过滤） |
| 6 | 选「第1课」 | 名单出现张三/李四/王五（赵六属班级B,不在内）,状态默认「出勤」 |
| 7 | 张三=出勤、李四=出勤、王五=出勤 → 提交 | 提示已提交3条;**弹出欠课时预警:王五 剩余 -1**（王五原0,扣1） |
| 8 | 看课时账户页 | 张三12→11、李四2→1、王五新增账户 已用1/剩余-1 |
| 9 | 回第1课,把张三改为「请假」→ 提交 | 张三课时**退回**(11→12);无张三预警 |
| 10 | 第1课王五改「旷课」→ 提交 | 旷课仍扣（先退回再扣,余额不变 -1） |
| 11 | 第1课李四改「迟到」→ 提交 | 迟到扣1（退回再扣,余额不变 1） |
| 12 | 考勤记录分页（GET /api/attendance） | 可按班级/学生/日期区间筛选 |

### 扣减规则速查
| 状态 | 扣课时 |
|---|---|
| 出勤 PRESENT / 迟到 LATE / 旷课 ABSENT | 1 |
| 请假 LEAVE | 0 |

---

## 三、学生端功能测试（http://localhost:5174）

用学生手机号 + 密码 `123456` 登录：

| # | 账号 | 操作 | 预期 |
|---|---|---|---|
| 13 | 13900000001(张三) | 首页→学习记录 | 剩余课时卡片显示余额;考勤列表显示第1课「请假」 |
| 14 | 13900000003(王五) | 学习记录 | 剩余课时 **-1（红色）**;考勤显示「旷课」 |

---

## 四、边界/异常测试（需 API,见第五节脚本）

| # | 场景 | 预期 |
|---|---|---|
| 15 | 对自习日课次(06-24)调 commit | 返回业务异常「非上课课次不可考勤」(code 18002) |
| 16 | 对不存在的 scheduleId 调 roster | 「课次不存在」(code 18001) |
| 17 | 同一课次重复提交相同名单 | 幂等：不重复扣课时（改点名先退后扣） |

---

## 五、测试方法

### 方式 1：浏览器 UI（推荐,最直观）
按第二/三节在页面点。

### 方式 2：浏览器 Console API 测试（验证后端/边界）
管理端登录后,F12 Console 粘贴下面 helper（自动按登录态签名）：

```js
window.api = async (method, path, query='', body=null) => {
  const token = localStorage.getItem('yanque_token');
  const secret = localStorage.getItem('yanque_sign_secret');
  const ts = String(Date.now());
  const nonce = [...crypto.getRandomValues(new Uint8Array(16))].map(b=>b.toString(16).padStart(2,'0')).join('');
  const source = `${method}\n/yq-admin${path}\n${query}\n${ts}\n${nonce}`;
  const key = await crypto.subtle.importKey('raw', new TextEncoder().encode(secret), {name:'HMAC',hash:'SHA-256'}, false, ['sign']);
  const sig = await crypto.subtle.sign('HMAC', key, new TextEncoder().encode(source));
  const sign = [...new Uint8Array(sig)].map(b=>b.toString(16).padStart(2,'0')).join('');
  const url = '/yq-admin'+path + (query?('?'+query):'');
  const res = await fetch(url, {method, headers:{Authorization:'Bearer '+token,'Content-Type':'application/json','X-Timestamp':ts,'X-Nonce':nonce,'X-Sign':sign}, body: body?JSON.stringify(body):undefined});
  return await res.json();
};
// 用法示例（先把 scheduleId 换成你库里第1课的 id）：
// await api('GET','/api/attendance/roster','scheduleId=1')
// await api('POST','/api/attendance/commit','',{scheduleId:1,items:[{studentId:1,status:'PRESENT'}]})
// await api('GET','/api/studentCourseHour','')            // 课时账户分页
// await api('GET','/api/studentCourseHour/1')             // 查某学生课时
// await api('POST','/api/studentCourseHour/adjust','',{studentId:1,changeHours:5,reason:'充值'})
```

> 注：`scheduleId`/`studentId` 用测试数据里实际生成的 id（造数据后 `select id,class_period from sys_class;`、`select id,schedule_date,class_type from sys_class_schedule;`、`select id,student_name from student where student_no like 'TEST%';` 查到）。
