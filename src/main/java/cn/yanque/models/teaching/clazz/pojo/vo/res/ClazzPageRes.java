package cn.yanque.models.teaching.clazz.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
@Schema(description = "班级分页响应")
public class ClazzPageRes {

    @Schema(description = "班级ID")
    private Long id;

    @Schema(description = "班级期数")
    private String classPeriod;

    @Schema(description = "班主任ID")
    private Long headTeacherId;

    @Schema(description = "班主任名称")
    private String headTeacherName;

    @Schema(description = "校区ID")
    private Long campusId;

    @Schema(description = "校区名称")
    private String campusName;

    @Schema(description = "课程ID")
    private Long courseId;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "创建时间")
    private Date createdAt;
}
