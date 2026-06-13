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

}
