package cn.yanque.common.dataConfig.service;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SystemConfigItem<T> {

    private String key;

    private T defaultValue;

    private Class<T> clazz;
}
