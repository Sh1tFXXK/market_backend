package org.example.market_backend.Mapper;


import org.example.market_backend.Entity.Chat;
import tk.mybatis.mapper.common.Mapper;

/**
* @author 31414
* @description 针对表【chat(聊天主表)】的数据库操作Mapper
* @createDate 2025-05-03 01:22:38
* @Entity Entity.Chat
*/
public interface ChatMapper extends Mapper<Chat> {

    int deleteByPrimaryKey(Long id);

    int insert(Chat record);

    int insertSelective(Chat record);

    Chat selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Chat record);

    int updateByPrimaryKey(Chat record);

}
