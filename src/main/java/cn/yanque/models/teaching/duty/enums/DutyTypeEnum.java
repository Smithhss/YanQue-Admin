package cn.yanque.models.teaching.duty.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DutyTypeEnum {

    /** 晚自习班级值班：每个班一个老师。 */
    EVENING_STUDY_CLASS("晚自习值班", "19:00", "21:00", true, false),

    /** 晚自习校区统一值班：每个校区一个老师。 */
    EVENING_STUDY_CAMPUS("晚自习统一值班", "21:00", "22:30", false, true),

    /** 自习日班级值班：每个班一个老师。 */
    SELF_STUDY_CLASS("自习日值班", "09:00", "18:00", true, false);

    private final String desc;

    private final String startTime;

    private final String endTime;

    private final boolean classRequired;

    private final boolean campusRequired;

    public static DutyTypeEnum parse(String value) {
        for (DutyTypeEnum item : values()) {
            if (item.name().equals(value)) {
                return item;
            }
        }
        throw new IllegalArgumentException("值班类型错误：" + value);
    }
}
