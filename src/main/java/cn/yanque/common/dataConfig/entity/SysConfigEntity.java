package cn.yanque.common.dataConfig.entity;

import lombok.Data;

/**
 * 系统配置实体,对应 sys_config 表。
 */
@Data
public class SysConfigEntity {

    /** 配置ID */
    private Long id;

    /** 配置key */
    private String k;

    /** 配置值 */
    private String v;
}
