package cn.yanque.models.dorm.mapper;

import cn.yanque.models.dorm.pojo.entity.DormAssignmentEntity;
import cn.yanque.models.dorm.pojo.vo.res.DormAssignmentRes;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 宿舍入住记录数据访问接口。
 */
public interface DormAssignmentMapper {

    void insert(DormAssignmentEntity assignment);

    DormAssignmentEntity selectById(@Param("id") Long id);

    /** 查询某学生当前在住的入住记录(一人一床约束校验),无则返回null。 */
    DormAssignmentEntity selectLivingByStudentId(@Param("studentId") Long studentId);

    /** 查询某学生当前在住的入住记录(联表带出楼栋/房间/床位名称,供学生端"我的宿舍"展示),无则返回null。 */
    DormAssignmentRes selectLivingResByStudentId(@Param("studentId") Long studentId);

    /** 退宿:将在住记录置为已退宿并写退宿日期,返回影响行数。 */
    int checkout(@Param("id") Long id, @Param("checkOutDate") Date checkOutDate);

    /** 分页查询入住记录(联表带出学生姓名与楼栋/房间/床位信息)。 */
    List<DormAssignmentRes> selectPage(@Param("buildingId") Long buildingId,
                                       @Param("status") String status,
                                       @Param("keyword") String keyword);
}
