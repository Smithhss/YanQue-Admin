package cn.yanque.models.ai.chat.mapper;

import cn.yanque.models.ai.chat.pojo.entity.AiChatMessageEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AI问答消息数据访问接口。
 */
public interface AiChatMessageMapper {

    void insert(AiChatMessageEntity message);

    List<AiChatMessageEntity> selectBySessionId(@Param("sessionId") Long sessionId);

    List<AiChatMessageEntity> selectLatestBySessionId(@Param("sessionId") Long sessionId, @Param("limit") Integer limit);
}
