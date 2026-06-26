package cn.yanque.models.dorm.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.dorm.pojo.vo.req.DormBedCreateReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBedPageReq;
import cn.yanque.models.dorm.pojo.vo.req.DormBedUpdateReq;
import cn.yanque.models.dorm.pojo.vo.res.DormBedRes;

/**
 * 宿舍床位业务接口。
 */
public interface DormBedService {

    /** 手动补充床位，返回新床位ID。 */
    Long addBed(DormBedCreateReq req);

    /** 修改床位（占用中的床位不可修改），返回床位ID。 */
    Long updateBed(DormBedUpdateReq req);

    /** 删除床位（占用中的床位不可删除），返回床位ID。 */
    Long deleteBed(Long id);

    /** 查询床位详情。 */
    DormBedRes getBedById(Long id);

    /** 分页查询床位。 */
    PageResult<DormBedRes> pageBed(DormBedPageReq req);
}
