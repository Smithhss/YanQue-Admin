package cn.yanque.models.teaching.course.service;

import cn.yanque.common.api.PageResult;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailCreateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseDetailUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CoursePageReq;
import cn.yanque.models.teaching.course.pojo.vo.req.CourseUpdateReq;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailCreateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailItemRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailUpdateRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDeleteRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseDetailRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CoursePageRes;
import cn.yanque.models.teaching.course.pojo.vo.res.CourseUpdateRes;

import java.util.List;

/**
 * 课程管理业务接口。
 * <p>
 * 课程是主数据，课程详情是课程下面的明细数据，用来维护阶段、第几天和上课内容。
 */
public interface CourseService {

    /**
     * 新增课程。
     *
     * @param req 课程新增请求
     * @return 新增后的课程ID
     */
    CourseCreateRes addCourse(CourseCreateReq req);

    /**
     * 修改课程基础信息。
     *
     * @param req 课程修改请求，ID由Controller从路径参数写入
     * @return 被修改的课程ID
     */
    CourseUpdateRes updateCourse(CourseUpdateReq req);

    /**
     * 删除课程。
     * <p>
     * 删除课程时会同步删除该课程下的课程详情。
     *
     * @param id 课程ID
     * @return 被删除的课程ID
     */
    CourseDeleteRes deleteCourse(Long id);

    /**
     * 根据ID查询课程详情。
     *
     * @param id 课程ID
     * @return 课程详情
     */
    CourseDetailRes getCourseById(Long id);

    /**
     * 分页查询课程列表。
     *
     * @param req 分页和搜索条件
     * @return 课程分页结果
     */
    PageResult<CoursePageRes> pageCourse(CoursePageReq req);

    /**
     * 给指定课程新增一条课程详情。
     *
     * @param courseId 课程ID
     * @param req      课程详情新增请求
     * @return 新增后的课程详情ID
     */
    CourseDetailCreateRes addCourseDetail(Long courseId, CourseDetailCreateReq req);

    /**
     * 修改课程详情。
     *
     * @param req 课程详情修改请求，ID由Controller从路径参数写入
     * @return 被修改的课程详情ID
     */
    CourseDetailUpdateRes updateCourseDetail(CourseDetailUpdateReq req);

    /**
     * 删除课程详情。
     *
     * @param id 课程详情ID
     * @return 被删除的课程详情ID
     */
    CourseDetailDeleteRes deleteCourseDetail(Long id);

    /**
     * 根据ID查询单条课程详情。
     *
     * @param id 课程详情ID
     * @return 课程详情
     */
    CourseDetailItemRes getCourseDetailById(Long id);

    /**
     * 查询指定课程下的全部课程详情。
     *
     * @param courseId 课程ID
     * @return 课程详情列表
     */
    List<CourseDetailItemRes> listCourseDetails(Long courseId);
}
