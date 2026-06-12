package cn.yanque.models.teaching.schedule.pojo.config;

import cn.yanque.common.exception.BusinessException;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 班级课表生成规则，对应 sys_config 中的 teaching.schedule.rule。
 */
@Data
public class ScheduleRuleConfig {

    /** 上课日，1-7 表示周一到周日 */
    private List<Integer> classDays;

    /** 自习日，1-7 表示周一到周日 */
    private List<Integer> selfStudyDays;

    /** 固定休息日，1-7 表示周一到周日 */
    private List<Integer> restDays;

    /** 法定节假日是否休息 */
    private Boolean holidayRest;

    public void validate() {
        if (classDays == null || classDays.isEmpty()) {
            throw BusinessException.DateError.newInstance("课表规则至少需要配置一个上课日");
        }
        validateDays(classDays, "上课日");
        validateDays(selfStudyDays, "自习日");
        validateDays(restDays, "休息日");

        Set<Integer> all = new HashSet<>();
        addWithoutConflict(all, classDays, "上课日");
        addWithoutConflict(all, selfStudyDays, "自习日");
        addWithoutConflict(all, restDays, "休息日");

        if (holidayRest == null) {
            throw BusinessException.DateError.newInstance("课表规则 holidayRest 不能为空");
        }
    }

    private void validateDays(List<Integer> days, String name) {
        if (days == null) {
            return;
        }
        for (Integer day : days) {
            if (day == null || day < 1 || day > 7) {
                throw BusinessException.DateError.newInstance(name + "只能配置 1-7");
            }
        }
    }

    private void addWithoutConflict(Set<Integer> all, List<Integer> days, String name) {
        if (days == null) {
            return;
        }
        for (Integer day : days) {
            if (!all.add(day)) {
                throw BusinessException.DateError.newInstance("课表规则存在重复星期：" + name + day);
            }
        }
    }


}
