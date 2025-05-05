package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Setter
@Getter
@ToString(callSuper = true)
public class CommentReplyVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    @NotNull(message = "productId不能为空")
    private Integer productId;
    @NotBlank(message = "content不能为空")
    private String content;
    /**
    *回复目标用户 传参代表回复 不传代表评论
     */
    private String toUserId;
    private String replyId;

}
