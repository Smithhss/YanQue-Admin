package cn.yanque.models.ai.chat.mapper;

import cn.yanque.models.ai.chat.pojo.entity.AiChatSessionEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI问答会话数据访问接口。
 */
public interface AiChatSessionMapper {

    void insert(AiChatSessionEntity session);

    AiChatSessionEntity selectByIdAndStudentId(@Param("id") Long id, @Param("studentId") Long studentId);

    List<AiChatSessionEntity> selectByStudentId(@Param("studentId") Long studentId);

    int updateTitleIfBlank(@Param("id") Long id, @Param("studentId") Long studentId, @Param("title") String title);

    int touch(@Param("id") Long id, @Param("studentId") Long studentId);

    int softDelete(@Param("id") Long id, @Param("studentId") Long studentId);
}
