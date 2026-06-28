package cn.yanque.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;

    public static final BusinessException UserExist = new BusinessException(10001, "用户已存在");
    public static final BusinessException UserNotExist = new BusinessException(10002, "用户不存在");
    public static final BusinessException PermissionExist = new BusinessException(11001, "权限已存在");
    public static final BusinessException PermissionNotExist = new BusinessException(11002, "权限不存在");
    public static final BusinessException ParamsError = new BusinessException(11005, "参数异常");
    public static final BusinessException PasswordError = new BusinessException(11003, "密码错误");
    public static final BusinessException DateExist = new BusinessException(11006, "数据已存在");
    public static final BusinessException DateError = new BusinessException(11004, "数据错误");
    public static final BusinessException RoleExist = new BusinessException(12001, "角色已存在");
    public static final BusinessException RoleNotExist = new BusinessException(12002, "角色不存在");
    public static final BusinessException ConfigExist = new BusinessException(13001, "配置已存在");
    public static final BusinessException ConfigNotExist = new BusinessException(13002, "配置不存在");
    public static final BusinessException CampusNotExist = new BusinessException(14001, "校区不存在");
    public static final BusinessException CourseNotExist = new BusinessException(15001, "课程不存在");
    public static final BusinessException CourseDetailNotExist = new BusinessException(15002, "课程详情不存在");
    public static final BusinessException ClazzNotExist = new BusinessException(16001, "班级不存在");
    public static final BusinessException ProductNotExist = new BusinessException(17001, "产品不存在");
    public static final BusinessException PrepayOrderNotExist = new BusinessException(17002, "预支付订单不存在");
    public static final BusinessException RemoteError = new BusinessException(17003, "远程调用异常");
    public static final BusinessException ScheduleNotExist = new BusinessException(18001, "课次不存在");
    public static final BusinessException AttendanceNotClassType = new BusinessException(18002, "非上课课次不可考勤");

    // F014 宿舍管理
    public static final BusinessException DormBuildingNotExist = new BusinessException(19001, "宿舍楼栋不存在");
    public static final BusinessException DormBuildingHasRoom = new BusinessException(19002, "楼栋下存在房间,不可删除");
    public static final BusinessException DormRoomNotExist = new BusinessException(19003, "宿舍房间不存在");
    public static final BusinessException DormRoomHasBed = new BusinessException(19004, "房间下存在床位,不可删除");
    public static final BusinessException DormBedNotExist = new BusinessException(19005, "床位不存在");
    public static final BusinessException DormBedOccupied = new BusinessException(19006, "床位已被占用,请先退宿");
    public static final BusinessException DormBedNotFree = new BusinessException(19007, "床位非空闲,不可分配");
    public static final BusinessException DormStudentNotExist = new BusinessException(19008, "学生不存在");
    public static final BusinessException DormStudentGenderUnknown = new BusinessException(19009, "学生性别未知,请先完善资料");
    public static final BusinessException DormGenderMismatch = new BusinessException(19010, "学生性别与楼栋性别不一致");
    public static final BusinessException DormStudentAlreadyAssigned = new BusinessException(19011, "该学生已在住,不可重复分配");
    public static final BusinessException DormAssignmentNotExist = new BusinessException(19012, "入住记录不存在");
    public static final BusinessException DormAssignmentNotLiving = new BusinessException(19013, "入住记录非在住状态");
    public static final BusinessException DormStudentNotLiving = new BusinessException(19014, "该学生当前未入住,无法调宿");
    public static final BusinessException DormStudentNotOffline = new BusinessException(19015, "仅线下学生可分配宿舍");
    public static final BusinessException InsufficientCourseHours = new BusinessException(20001, "剩余课时不足");



    public BusinessException(String message) {
        super(message);
        this.code = 400;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }


    public BusinessException newInstance(String message) {
        return new BusinessException(this.getCode(), message);
    }
}
