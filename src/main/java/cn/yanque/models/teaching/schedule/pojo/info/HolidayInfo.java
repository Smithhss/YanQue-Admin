package cn.yanque.models.teaching.schedule.pojo.info;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HolidayInfo {

    private Boolean holiday;

    private String name;
}
