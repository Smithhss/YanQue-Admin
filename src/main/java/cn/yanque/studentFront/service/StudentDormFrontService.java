package cn.yanque.studentFront.service;

import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;

/**
 * 学生端"我的宿舍"接口。
 */
public interface StudentDormFrontService {

    /** 查询当前登录学生的在住宿舍信息，未分配宿舍时返回 null。 */
    DormAssignmentRes myDorm();
}
