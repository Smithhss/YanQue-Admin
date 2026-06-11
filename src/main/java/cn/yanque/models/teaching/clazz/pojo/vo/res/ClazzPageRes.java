package cn.yanque.models.teaching.clazz.pojo.vo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 班级分页列表响应对象。
 */
@Data
@Schema(description = "班级分页响应")
public class ClazzPageRes {

    /** 班级ID */
    @Schema(description = "班级ID")
    private Long id;

    /** 班级期数 */
    @Schema(description = "班级期数")
    private String classPeriod;

    /** 班主任用户ID */
    @Schema(description = "班主任ID")
    private Long headTeacherId;

    /** 班主任名称，由后端根据用户ID回填 */
    @Schema(description = "班主任名称")
    private String headTeacherName;

    /** 校区ID */
    @Schema(description = "校区ID")
    private Long campusId;

    /** 校区名称，由后端根据校区ID回填 */
    @Schema(description = "校区名称")
    private String campusName;

    /** 课程ID */
    @Schema(description = "课程ID")
    private Long courseId;

    /** 课程名称，由后端根据课程ID回填 */
    @Schema(description = "课程名称")
    private String courseName;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createdAt;
}
