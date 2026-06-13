package cn.yanque.models.teaching.duty.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyCreateReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyDateSaveReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyPageReq;
import cn.yanque.models.teaching.duty.pojo.vo.req.ClassDutyUpdateReq;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyCreateRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDateRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDateSaveRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDeleteRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyDetailRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyPageRes;
import cn.yanque.models.teaching.duty.pojo.vo.res.ClassDutyUpdateRes;

import java.util.Date;

/**
 * 值班管理业务接口。
 */
public interface ClassDutyService {

    /**
     * 新增值班。
     *
     * @param req 新增请求
     * @return 新增后的值班ID
     */
    ClassDutyCreateRes addDuty(ClassDutyCreateReq req);

    /**
     * 修改值班。
     *
     * @param req 修改请求
     * @return 被修改的值班ID
     */
    ClassDutyUpdateRes updateDuty(ClassDutyUpdateReq req);

    /**
     * 删除值班。
     *
     * @param id 值班ID
     * @return 被删除的值班ID
     */
    ClassDutyDeleteRes deleteDuty(Long id);

    /**
     * 查询值班详情。
     *
     * @param id 值班ID
     * @return 值班详情
     */
    ClassDutyDetailRes getDutyById(Long id);

    /**
     * 分页查询值班。
     *
     * @param req 查询条件
     * @return 分页结果
     */
    PageResult<ClassDutyPageRes> pageDuty(ClassDutyPageReq req);

    /**
     * 按日期查询值班排班页面数据。
     *
     * @param dutyDate 值班日期
     * @return 当天班级值班和校区统一值班数据
     */
    ClassDutyDateRes getDateDuty(Date dutyDate);

    /**
     * 按日期覆盖保存值班。
     *
     * @param req 保存请求
     * @return 保存数量
     */
    ClassDutyDateSaveRes saveDateDuty(ClassDutyDateSaveReq req);
}
