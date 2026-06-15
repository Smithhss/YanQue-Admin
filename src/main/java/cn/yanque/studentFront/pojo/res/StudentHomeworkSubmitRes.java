package cn.yanque.studentFront.pojo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 学生提交作业响应。
 */
@Data
@Schema(description = "学生提交作业响应")
public class StudentHomeworkSubmitRes {

    private Long id;

    private Long homeworkId;

    private String contentFileName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date submitTime;

    private Boolean lateSubmitted;
}
