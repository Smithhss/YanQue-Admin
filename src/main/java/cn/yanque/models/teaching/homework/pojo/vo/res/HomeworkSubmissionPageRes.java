package cn.yanque.models.teaching.homework.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 后台作业提交分页项。
 */
@Data
@Schema(description = "后台作业提交分页项")
public class HomeworkSubmissionPageRes {

    private Long id;

    private Long homeworkId;

    private Long studentId;

    private String studentName;

    private String studentPhone;

    private String contentObjectKey;

    private String contentFileName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    private Boolean lateSubmitted;

    private String teacherRemark;

    private Integer score;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updatedAt;
}
