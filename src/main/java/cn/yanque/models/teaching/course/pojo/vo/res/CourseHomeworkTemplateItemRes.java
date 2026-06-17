package cn.yanque.models.teaching.course.pojo.vo.res;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class CourseHomeworkTemplateItemRes {

    private Long id;

    private Long courseId;

    private String teachingMode;

    private String stageName;

    private Integer dayNumber;

    private String contentObjectKey;

    private String contentFileName;

    private String status;

    private String remark;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private Date updatedAt;
}
