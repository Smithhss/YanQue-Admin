package cn.yanque.studentFront.pojo.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 学生基础信息。
 */
@Data
@Schema(description = "学生基础信息")
public class StudentInfoRes {

    /** 学生ID */
    @Schema(description = "学生ID")
    private Long id;

    /** 学生姓名 */
    @Schema(description = "学生姓名")
    private String name;

    /** 手机号 */
    @Schema(description = "手机号")
    private String phone;
}
