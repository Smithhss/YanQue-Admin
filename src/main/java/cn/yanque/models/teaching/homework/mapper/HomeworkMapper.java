package cn.yanque.models.teaching.homework.mapper;

import cn.yanque.models.teaching.homework.pojo.bo.QueryHomeworkBo;
import cn.yanque.models.teaching.homework.pojo.entity.HomeworkEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * 作业数据访问接口。
 */
public interface HomeworkMapper {

    void insert(HomeworkEntity homework);

    HomeworkEntity selectById(@Param("id") Long id);

    List<HomeworkEntity> selectPage(QueryHomeworkBo query);

    HomeworkEntity selectByClassIdAndHomeworkDate(@Param("classId") Long classId, @Param("homeworkDate") Date homeworkDate);

    int updateAnswer(HomeworkEntity homework);
}
