package cn.yanque.models.teaching.clazz.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class ClazzEntity {

    private Long id;

    private String classPeriod;

    private Long headTeacherId;

    private Long campusId;

    private Long courseId;

    private Date createdAt;

    private Date updatedAt;
}
