package cn.yanque.models.teaching.schedule.service;

import cn.yanque.models.teaching.schedule.pojo.vo.req.ClassScheduleGenerateReq;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleGenerateRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassScheduleItemRes;
import cn.yanque.models.teaching.schedule.pojo.vo.res.ClassStageInfoRes;

import java.util.List;

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
}
