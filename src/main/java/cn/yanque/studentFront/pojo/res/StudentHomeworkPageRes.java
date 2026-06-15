package cn.yanque.studentFront.pojo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 学生作业列表项。
 */
@Data
@Schema(description = "学生作业列表项")
public class StudentHomeworkPageRes {

    private Long id;

    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date homeworkDate;

    private String classContent;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;

    private String contentFileName;

    private String contentObjectKey;

    private Boolean answerVisible;

    private String answerFileName;

    private String answerObjectKey;
}
