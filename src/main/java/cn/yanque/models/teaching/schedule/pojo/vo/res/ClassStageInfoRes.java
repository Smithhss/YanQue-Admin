package cn.yanque.models.teaching.schedule.pojo.vo.res;

import lombok.Data;

import java.util.Map;

@Data
public class ClassStageInfoRes {

    private String stageName;

    /**
     * 阶段天数
     */
    private Integer stageNumber;

    private Map<Long, String> freeTeacherName;
}


