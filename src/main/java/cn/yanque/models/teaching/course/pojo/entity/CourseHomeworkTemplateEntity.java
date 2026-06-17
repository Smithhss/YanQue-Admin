package cn.yanque.models.teaching.course.pojo.entity;

import lombok.Data;

import java.util.Date;

/**
 * 课程作业标准/训练集。
 * <p>
 * 线上课程按阶段配置，线下课程按第几天配置，后续发放作业时可直接引用这里的标准文档。
 */
@Data
public class CourseHomeworkTemplateEntity {

    private Long id;

    private Long courseId;

    /**
     * 冗余课程上课方式，便于列表展示和后续作业发放时快速判断维度。
     */
    private String teachingMode;

    private String stageName;

    private Integer dayNumber;

    /**
     * 标准文档对象Key，文件本体通过预签名上传到对象存储。
     */
    private String contentObjectKey;

    private String contentFileName;

    private String status;

    private String remark;

    private Date createdAt;

    private Date updatedAt;
}
