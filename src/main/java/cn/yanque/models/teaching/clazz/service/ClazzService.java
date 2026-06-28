package cn.yanque.models.teaching.clazz.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzCreateReq;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzPageReq;
import cn.yanque.models.teaching.clazz.pojo.vo.req.ClazzUpdateReq;
import cn.yanque.models.teaching.clazz.pojo.vo.res.*;

/**
 * 班级管理业务接口。
 * <p>
 * 班级保存班级期数,班主任ID,校区ID,课程ID,列表展示时由服务层补充对应名称。
 */
public interface ClazzService {

    /**
     * 新增班级。
     *
     * @param req 班级新增请求
     * @return 新增后的班级ID
     */
    ClazzCreateRes addClazz(ClazzCreateReq req);

    /**
     * 修改班级。
     *
     * @param req 班级修改请求,ID由Controller从路径参数写入
     * @return 被修改的班级ID
     */
    ClazzUpdateRes updateClazz(ClazzUpdateReq req);

    /**
     * 删除班级。
     *
     * @param id 班级ID
     * @return 被删除的班级ID
     */
    ClazzDeleteRes deleteClazz(Long id);

    /**
     * 根据ID查询班级详情。
     *
     * @param id 班级ID
     * @return 班级详情,包含班主任,校区,课程名称
     */
    ClazzDetailRes getClazzById(Long id);

    /**
     * 分页查询班级列表。
     *
     * @param req 分页和筛选条件
     * @return 班级分页结果,包含班主任,校区,课程名称
     */
    PageResult<ClazzPageRes> pageClazz(ClazzPageReq req);
}
