package org.example.market_backend.Service;

import org.example.market_backend.Entity.CommentReply;
import org.example.market_backend.VO.CommentReplyVO;

public interface CommentReplyService extends BaseService<CommentReply,Integer>{
    void commentOrReply(CommentReplyVO vo);
}
