package cn.yanque.models.teaching.homework.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 作业发布预填响应。
 */
@Data
@Schema(description = "作业发布预填响应")
public class HomeworkPrepareRes {

    private Long classId;

    private String classPeriod;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date homeworkDate;

    private String classContent;

    private String defaultTitle;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date deadline;
}
