package cn.yanque.models.teaching.course.mapper;

import cn.yanque.models.teaching.course.pojo.bo.QueryCourseHomeworkTemplateBo;
import cn.yanque.models.teaching.course.pojo.entity.CourseHomeworkTemplateEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 课程作业标准数据访问接口。
 */
public interface CourseHomeworkTemplateMapper {

    void insert(CourseHomeworkTemplateEntity template);

    int updateById(CourseHomeworkTemplateEntity template);

    int deleteById(@Param("id") Long id);

    int deleteByCourseId(@Param("courseId") Long courseId);

    CourseHomeworkTemplateEntity selectById(@Param("id") Long id);

    List<CourseHomeworkTemplateEntity> selectList(QueryCourseHomeworkTemplateBo bo);

    CourseHomeworkTemplateEntity selectDuplicate(QueryCourseHomeworkTemplateBo bo);
}
