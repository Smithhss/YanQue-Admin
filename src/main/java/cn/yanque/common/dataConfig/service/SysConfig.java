package cn.yanque.common.dataConfig.service;

public class SysConfig {

    public static SystemConfigItem<String> jwtSecret = new SystemConfigItem<>("jwtSecret", "1234", String.class);
    public static SystemConfigItem<Long> jwtExpireTime = new SystemConfigItem<>("jwtExpireTime", 1000L, Long.class);
    public static SystemConfigItem<String> teachingScheduleRule = new SystemConfigItem<>(
            "teaching.schedule.rule",
            "{\"classDays\":[1,2,3,5,6],\"selfStudyDays\":[4],\"restDays\":[7],\"holidayRest\":true}",
            String.class);


    public static SystemConfigItem<String> createOrderGoodsName = new SystemConfigItem<>("createOrderGoodsName", "缴费下单", String.class);
    public static SystemConfigItem<Integer> createOrderExpireTime = new SystemConfigItem<>("createOrderExpireTime", 5, Integer.class);

    public static SystemConfigItem<Long> tosUploadExpireSeconds = new SystemConfigItem<>("tos.upload.expire.seconds", 600L, Long.class);

    public static SystemConfigItem<Long> tosPreviewExpireSeconds = new SystemConfigItem<>("tos.preview.expire.seconds", 300L, Long.class);

    public static SystemConfigItem<String> studentTagOptions = new SystemConfigItem<>(
            "student.tag.options",
            "普通学员,摆烂学员,失联学员,已就业学员,高潜力学员,持续关注学员",
            String.class);

}
