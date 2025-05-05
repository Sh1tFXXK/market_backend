package org.example.market_backend.Service;

import org.example.market_backend.Entity.CommentReply;
import org.example.market_backend.Entity.Product;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Exception.BusinessException;
import org.example.market_backend.Mapper.CommentReplyMapper;
import org.example.market_backend.Mapper.ProductMapper;
import org.example.market_backend.VO.CommentReplyVO;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CommentReplyServiceImpl implements CommentReplyService{
    private UserService userService;
    private ProductMapper productMapper;
    private CommentReplyMapper commentReplyMapper;
    /**
     * 评论/回复业务
     */
    @Override
    public void commentOrReply(CommentReplyVO vo) {
        //检验token是否为效
        User user = userService.checkToken(vo.getToken());
        //检验商品是否存在
        Product product = productMapper.selectByPrimaryKey(vo.getProductId());
        Assert.notNull(product,"商品信息不存在");
        CommentReply commentReply = new CommentReply();
        //回复
        if(vo.getToUserId() != null || !vo.getToUserId().isEmpty()){
            Assert.notNull(vo.getReplyId(),"回复信息不存在");
            Example example = new Example(CommentReply.class);
            example.createCriteria().andEqualTo("productId",vo.getProductId())
                    .andEqualTo("fromUserId",vo.getToUserId());
            List<CommentReply> list = commentReplyMapper.selectByExample(example);
            Assert.notEmpty(Collections.singleton(list), "该用户没有回复过该商品");
            User toUser = userService.findById(vo.getToUserId());
            commentReply.setToUserAvatar(toUser.getUserAvatar());
            commentReply.setToUserName(toUser.getUserName());
        }else {
            Assert.isTrue(vo.getReplyId() == null,"回复信息错误");
        }
        BeanUtils.copyProperties(vo,commentReply);
        commentReply.setCreateTime(new Date());
        commentReply.setType(vo.getToUserId() == null ? 1 : 2);
        commentReply.setFromUserId(user.getUserId());
        commentReply.setFromUserName(user.getUserName());
        commentReply.setFromUserAvatar(user.getUserAvatar());
        commentReplyMapper.insertSelective(commentReply);
    }


    @Override
    public int save(CommentReply entity) throws BusinessException {
        return 0;
    }

    @Override
    public int update(CommentReply entity) throws BusinessException {
        return 0;
    }

    @Override
    public int delete(Integer id) throws BusinessException {
        return 0;
    }

    @Override
    public CommentReply findById(Integer id) throws BusinessException {
        return null;
    }

    @Override
    public List<CommentReply> findList(Example example) {
        return List.of();
    }


}
