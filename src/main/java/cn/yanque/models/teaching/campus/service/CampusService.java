package cn.yanque.models.teaching.campus.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusCreateReq;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusPageReq;
import cn.yanque.models.teaching.campus.pojo.vo.req.CampusUpdateReq;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusCreateRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusDeleteRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusDetailRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusPageRes;
import cn.yanque.models.teaching.campus.pojo.vo.res.CampusUpdateRes;

/**
 * 校区管理业务接口。
 * <p>
 * 负责校区地点、负责人、负责人电话的增删改查。
 */
public interface CampusService {

    /**
     * 新增校区。
     *
     * @param req 校区新增请求
     * @return 新增后的校区ID
     */
    CampusCreateRes addCampus(CampusCreateReq req);

    /**
     * 修改校区。
     *
     * @param req 校区修改请求，ID由Controller从路径参数写入
     * @return 被修改的校区ID
     */
    CampusUpdateRes updateCampus(CampusUpdateReq req);

    /**
     * 删除校区。
     *
     * @param id 校区ID
     * @return 被删除的校区ID
     */
    CampusDeleteRes deleteCampus(Long id);

    /**
     * 根据ID查询校区详情。
     *
     * @param id 校区ID
     * @return 校区详情
     */
    CampusDetailRes getCampusById(Long id);

    /**
     * 分页查询校区列表。
     *
     * @param req 分页和搜索条件
     * @return 校区分页结果
     */
    PageResult<CampusPageRes> pageCampus(CampusPageReq req);
}
