package cn.yanque.models.teaching.course.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CourseEntity {

    private Long id;

    private String courseName;

    private Integer courseDays;

    private String materialPath;

    private Date createdAt;

    private Date updatedAt;
}
