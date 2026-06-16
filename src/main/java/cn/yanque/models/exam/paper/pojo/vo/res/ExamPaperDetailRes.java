package cn.yanque.models.exam.paper.pojo.vo.res;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 试卷详情响应。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ExamPaperDetailRes extends ExamPaperPageRes {

    private List<ExamPaperQuestionRes> questions;
}
