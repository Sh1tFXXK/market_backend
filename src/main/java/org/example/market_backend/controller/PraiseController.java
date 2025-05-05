package org.example.market_backend.controller;

import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Service.PraiseService;
import org.example.market_backend.VO.PraiseVO;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/praise")
public class PraiseController {
    private PraiseService praiseService;
    /**
     * 获取点赞列表
     * @param vo
     * @return
     */
    @RequestMapping(value = "getPraiseList", method = RequestMethod.GET)
    public ResponseBO getPraiseList(PraiseVO vo) {
        //与点赞接口共用于入参实体，
        Assert.notNull(vo,  "入参不能为空");
        Assert.notNull(vo.getProductId(),  "商品编号不能为空");
        String token = vo.getToken();
        //调用点赞服务
        praiseService.getPraiseList(vo);
        return ResponseBO.success();
    }

}
