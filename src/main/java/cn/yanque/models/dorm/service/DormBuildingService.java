package cn.yanque.models.dorm.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBuildingUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormBuildingRes;

/**
 * 宿舍楼栋业务接口。
 */
public interface DormBuildingService {

    /** 新增楼栋，返回新楼栋ID。 */
    Long addBuilding(DormBuildingCreateReq req);

    /** 修改楼栋，返回楼栋ID。 */
    Long updateBuilding(DormBuildingUpdateReq req);

    /** 删除楼栋（楼栋下存在房间时不允许删除），返回楼栋ID。 */
    Long deleteBuilding(Long id);

    /** 查询楼栋详情。 */
    DormBuildingRes getBuildingById(Long id);

    /** 分页查询楼栋。 */
    PageResult<DormBuildingRes> pageBuilding(DormBuildingPageReq req);
}
