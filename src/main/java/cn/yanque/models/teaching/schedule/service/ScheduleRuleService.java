package cn.yanque.models.teaching.schedule.service;

import cn.yanque.common.dataConfig.service.SysConfig;
import cn.yanque.common.dataConfig.service.SysConfigService;
import cn.yanque.common.exception.BusinessException;
import cn.yanque.models.teaching.schedule.pojo.config.ScheduleRuleConfig;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ScheduleRuleService {

    @Autowired
    private SysConfigService sysConfigService;

    public ScheduleRuleConfig getScheduleRule() {
        try {
            String ruleJson = sysConfigService.get(SysConfig.teachingScheduleRule);
            ScheduleRuleConfig config = JSON.parseObject(ruleJson, ScheduleRuleConfig.class);
            if (config == null) {
                throw BusinessException.DateError.newInstance("课表规则配置不能为空");
            }
            config.validate();
            return config;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw BusinessException.DateError.newInstance("课表规则配置格式错误");
        }
    }




}
