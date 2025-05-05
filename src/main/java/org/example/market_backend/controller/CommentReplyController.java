package org.example.market_backend.controller;

import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Entity.CommentReply;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Service.CommentReplyService;
import org.example.market_backend.Service.PraiseService;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.VO.CommentReplyVO;
import org.example.market_backend.VO.PraiseVO;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tk.mybatis.mapper.entity.Example;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/commentReply")
public class CommentReplyController {
    private CommentReplyService commentReplyService;
    private PraiseService praiseService;
    private User user;
    /**
     * 评论/回复接口
     */
    @RequestMapping(value = "commentOrReply", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO commentReply(@Valid @RequestBody CommentReplyVO vo) {
        //检验token是否为空
        String token = checkToken();
        vo.setToken(token);
        //调用service层评论/回复
        commentReplyService.commentOrReply(vo);
        return ResponseBO.success();
    }
    /**
     * 获取评论/回复接口
     */
    @RequestMapping(value = "getCommentReplyList", method = RequestMethod.GET)
    public ResponseBO getCommentReplyList(Integer productId) {
        //检验商品编号
        Assert.notNull(productId,"商品编号不能为空");
        //根据商品编号查询并按时间升序排列
        Example example = new Example(CommentReply.class);
        example.createCriteria().andEqualTo("productId",productId);
        example.orderBy("createTime").asc();
        commentReplyService.findList(example);
        return ResponseBO.success();
    }
    /**
     *点赞/取消点赞
     *
     */
    @RequestMapping(value = "praiseOrUnPraise",  method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseBO praiseOrUnPraise(@Valid @RequestBody PraiseVO vo) {
        //检验token是否为空
        String token = checkToken();
        vo.setToken(token);
        //点赞状态枚举值只能为1为2
        Assert.isTrue(vo.getPraiseStatus()==1||vo.getPraiseStatus()==2,"点赞状态异常");
        //调用service层点赞点赞
        praiseService.praiseOrUnPraise(vo);
        return ResponseBO.success();
    }
    private String checkToken(){
        String token = RedisUtils.get(TokenUtils.getToken(user.getUserId()));
        Assert.notNull(token, "用户未登录");
        return token;
    }
}
