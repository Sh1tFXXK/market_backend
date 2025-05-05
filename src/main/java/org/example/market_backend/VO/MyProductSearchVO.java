package org.example.market_backend.VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import jakarta.validation.constraints.NotNull;


@Setter
@Getter
@ToString(callSuper = true)
public class MyProductSearchVO extends BaseVO{
    private static final long serialVersionUID = 1L;
    @NotNull(message = "type不能为空")
    private  Integer type;
}
