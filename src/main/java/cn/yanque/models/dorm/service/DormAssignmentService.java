package cn.yanque.models.dorm.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignReq;
import cn.yanque.models.dorm.pojo.vo.req.DormAssignmentPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormTransferReq;
import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;

/**
 * 宿舍入住业务接口。
 * <p>
 * 约束：性别隔离（学生性别须与楼栋一致）、一人一床（同时仅一条在住）、床位唯一占用。
 */
public interface DormAssignmentService {

    /** 入住分配，返回入住记录ID。 */
    Long assign(DormAssignReq req, Long operatorId);

    /** 退宿，返回入住记录ID。 */
    Long checkout(Long assignmentId);

    /** 调宿（退旧床+入新床），返回新入住记录ID。 */
    Long transfer(DormTransferReq req, Long operatorId);

    /** 分页查询入住记录。 */
    PageResult<DormAssignmentRes> pageAssignment(DormAssignmentPageReq req);
}
