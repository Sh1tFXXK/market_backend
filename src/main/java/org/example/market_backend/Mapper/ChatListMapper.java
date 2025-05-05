package org.example.market_backend.Mapper;


import org.example.market_backend.BO.ChatBO;
import org.example.market_backend.Entity.ChatList;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
* @author 31414
* @description 针对表【chat_list(聊天列表)】的数据库操作Mapper
* @createDate 2025-05-03 00:02:33
* @Entity .ProductType.org.example.market_backend.Entity.ChatList
*/
public interface ChatListMapper extends Mapper<ChatList> {

    int deleteByPrimaryKey(Long id);

    int insert(ChatList record);

    int insertSelective(ChatList record);

    ChatList selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ChatList record);

    int updateByPrimaryKey(ChatList record);

    List<ChatBO> selectChatList(String userId);
}
