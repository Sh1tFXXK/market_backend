package org.example.market_backend.Mapper;


import org.example.market_backend.Entity.Praise;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
* @author 31414
* @description 针对表【comment_reply(评论回复表)】的数据库操作Mapper
* @createDate 2025-05-01 21:39:13
* @Entity .ProductType.org.example.market_backend.Entity.org.example.market_backend.Entity.CommentReply
*/
public interface PraiseMapper extends Mapper<Praise> {

    int deleteByPrimaryKey(Long id);

    int insert(org.example.market_backend.Entity.Praise record);

    int insertSelective(org.example.market_backend.Entity.Praise record);

    org.example.market_backend.Entity.Praise selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(org.example.market_backend.Entity.Praise record);

    int updateByPrimaryKey(org.example.market_backend.Entity.Praise record);

    List<Praise> selectByExample(Example example);
}
