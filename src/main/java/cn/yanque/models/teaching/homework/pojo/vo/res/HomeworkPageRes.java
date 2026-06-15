package cn.yanque.models.teaching.homework.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 作业分页项。
 */
@Data
@Schema(description = "作业分页项")
public class HomeworkPageRes {

    private Long id;

    private String title;

    private String contentObjectKey;

    private String contentFileName;

    private String answerObjectKey;

    private String answerFileName;

    private Boolean answerStudentVisible;

    private Long classId;

    private String classPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date homeworkDate;

    private String classContent;

    private Date startTime;

    private Date deadline;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
