package cn.yanque.models.teaching.campus.pojo.entity;

import lombok.Data;

import java.util.Date;

@Data
public class CampusEntity {

    private Long id;

    private String campusLocation;

    private String managerName;

    private String managerPhone;

    private Date createdAt;

    private Date updatedAt;
}
