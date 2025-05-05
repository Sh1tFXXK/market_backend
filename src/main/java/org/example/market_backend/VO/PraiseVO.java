package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;

@Setter
@Getter
@ToString(callSuper = true)
public class PraiseVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    /**
     * 商品编号
     */
    @NotNull(message = "商品编号不能为空")
    private Integer productId;
    /**
     * 点赞状态 1 点赞  2  取消点赞
     */
    @NotNull(message = "点赞状态不能为空")
    private Integer praiseStatus;
}
