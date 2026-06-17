package cn.yanque.models.student.mapper;

import cn.yanque.models.student.followup.pojo.bo.QueryStudentFollowupRecordBo;
import cn.yanque.models.student.followup.pojo.bo.StudentFollowupRecordStatsBo;
import cn.yanque.models.student.followup.pojo.entity.StudentFollowupRecordEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface StudentFollowupRecordMapper {

    int insertIgnore(StudentFollowupRecordEntity entity);

    int updateComplete(@Param("id") Long id,
                       @Param("followupUserId") Long followupUserId,
                       @Param("followupTime") Date followupTime,
                       @Param("followupContent") String followupContent,
                       @Param("followupVideoObjectKey") String followupVideoObjectKey,
                       @Param("followupVideoFileName") String followupVideoFileName,
                       @Param("remark") String remark,
                       @Param("updatedAt") Date updatedAt);

    int updateCancel(@Param("id") Long id,
                     @Param("remark") String remark,
                     @Param("updatedAt") Date updatedAt);

    StudentFollowupRecordEntity selectById(@Param("id") Long id);

    List<StudentFollowupRecordEntity> selectLatestByStudentIds(@Param("studentIds") List<Long> studentIds);

    List<StudentFollowupRecordEntity> selectPage(QueryStudentFollowupRecordBo query);

    StudentFollowupRecordStatsBo selectStats(@Param("todayStart") Date todayStart,
                                             @Param("tomorrowStart") Date tomorrowStart);
}
