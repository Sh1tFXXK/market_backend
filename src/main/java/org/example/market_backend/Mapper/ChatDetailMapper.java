package org.example.market_backend.Mapper;


import org.example.market_backend.Entity.ChatDetail;
import tk.mybatis.mapper.common.Mapper;

/**
* @author 31414
* @description 针对表【chat_detail(聊天详情表)】的数据库操作Mapper
* @createDate 2025-05-03 00:18:48
* @Entity .ChatDetail
*/
public interface ChatDetailMapper extends Mapper<ChatDetail> {

    int deleteByPrimaryKey(Long id);

    int insert(ChatDetail record);

    int insertSelective(ChatDetail record);

    ChatDetail selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatDetail record);

    int updateByPrimaryKey(ChatDetail record);

}
