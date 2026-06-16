package cn.yanque.models.student.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 学生实体。
 */
@Data
public class StudentEntity {

    /** 主键ID */
    private Long id;

    /** 学员编号 */
    private String studentNo;

    /** 学生姓名 */
    private String studentName;

    /** 手机号 */
    private String studentPhone;

    /** 登录密码 */
    private String password;

    /** 学历 */
    private String education;

    /** 届数 */
    private Integer gradeYear;

    /** 学校 */
    private String school;

    /** 专业 */
    private String major;

    /** 上课方式：ONLINE线上，OFFLINE线下 */
    private String teachingMode;

    /** 班级ID，线下班必填 */
    private Long classId;

    /** 学生标签 */
    private String studentTag;

    /** 状态 */
    private String status;

    /** 创建时间 */
    private Date createdAt;

    /** 更新时间 */
    private Date updatedAt;
}
