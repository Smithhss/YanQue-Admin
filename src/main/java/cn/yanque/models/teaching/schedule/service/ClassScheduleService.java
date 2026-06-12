package cn.yanque.models.teaching.schedule.service;

import cn.yanque.models.teaching.schedule.pojo.vo.req.AddClassSchuleReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleGenerateReq;
import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleTeacherAssignReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleGenerateRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleItemRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleDateDetailRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleTeacherAssignRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassStageInfoRes;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Date;

/**
 * 班级课表业务接口。
 */
public interface ClassScheduleService {

    /**
     * 根据班级课程和课表规则生成课表。
     *
     * @param req 生成课表请求
     * @return 生成结果
     */
    ClassScheduleGenerateRes generateSchedule(ClassScheduleGenerateReq req);

    /**
     * 查询班级课表。
     *
     * @param classId 班级ID
     * @return 班级课表列表
     */
    List<ClassScheduleItemRes> listSchedule(Long classId);

    /**
     * 查询班级阶段信息
     */
    List<ClassStageInfoRes> classStageInfo(Long classId);

    /**
     * 查询班级某一天的课程详情。
     *
     * @param classId      班级ID
     * @param scheduleDate 上课日期
     * @return 当天课程详情
     */
    ClassScheduleDateDetailRes getDateDetail(Long classId, Date scheduleDate);

    /**
     * 按课程阶段给课表分配老师。
     *
     * @param classId 班级ID
     * @param req     阶段老师分配请求
     * @return 更新结果
     */
    ClassScheduleTeacherAssignRes assignTeachers(Long classId, ClassScheduleTeacherAssignReq req);

    /**
     * 添加课程
     */
    void addClassSchule(Long classId, AddClassSchuleReq req);
}
