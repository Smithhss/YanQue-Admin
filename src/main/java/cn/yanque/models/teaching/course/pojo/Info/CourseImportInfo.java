package cn.yanque.models.teaching.course.pojo.Info;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class CourseImportInfo {

    /** 第一列:阶段 */
    @ExcelProperty(index = 0)
    private String stageName;

    /** 第二列:第几天 */
    @ExcelProperty(index = 1)
    private Integer dayNumber;

    /** 第三列:上课内容 */
    @ExcelProperty(index = 2)
    private String classContent;
}
