package org.example.market_backend.Mapper;


import org.example.market_backend.Entity.CommentReply;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
* @author 31414
* @description 针对表【comment_reply(评论回复表)】的数据库操作Mapper
* @createDate 2025-05-01 23:45:12
* @Entity .ProductType.org.example.market_backend.Entity.CommentReply
*/
public interface CommentReplyMapper extends Mapper<CommentReply> {

    int deleteByPrimaryKey(Long id);

    int insert(CommentReply record);

    int insertSelective(CommentReply record);

    CommentReply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CommentReply record);

    int updateByPrimaryKey(CommentReply record);

}
