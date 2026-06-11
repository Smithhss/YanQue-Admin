package cn.yanque.common.dataConfig.service;

public class SysConfig {

    public static SystemConfigItem<String> jwtSecret = new SystemConfigItem("jwtSecret", "1234", String.class);
    public static SystemConfigItem<Long> jwtExpireTime = new SystemConfigItem("jwtExpireTime", 1000, Long.class);
}
