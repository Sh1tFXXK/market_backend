package org.example.market_backend.controller;

import com.github.pagehelper.PageInfo;
import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Service.ProductService;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.VO.MyProductSearchVO;
import org.example.market_backend.VO.DelProductVO;

import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/product")

public class ProductController {
    private ProductService productService;
    private User user;
    /**
     * 获取首轮轮播商品列表
     */
    @RequestMapping(value = "getBannerList",method = RequestMethod.GET)
    public ResponseBO getBannerList(){
        productService.getBannerList();
        return ResponseBO.success();
    }
    /**
     * 获取商品列表
     * @param vo
     * @return
     */
    @RequestMapping(value = "getProductList",method = RequestMethod.POST)
    public ResponseBO getProductList(@Valid  @RequestBody MyProductSearchVO vo){
        Assert.notNull(vo,"参数不能为空");
        String token = checkToken();
        Assert.isTrue(1==vo.getType() || 2==vo.getType() || 3==vo.getType(),"type参数错误");
        vo.setToken(token);
        successPageInfo(productService.getMyProductList(vo));
        return ResponseBO.success();
    }
    public static ResponseBO successPageInfo(List<?> list){
        Map<String,Object> map = new HashMap<>(16);
        map.put("list",list);
        map.put("total",Math.toIntExact(new PageInfo<>(list).getTotal()));
        return  ResponseBO.success(map);
    }
    /**
     * 删除商品
     */
    @RequestMapping(value = "deleteProduct",method = RequestMethod.POST)
    public ResponseBO deleteProduct( @RequestBody DelProductVO vo){
        String token = checkToken();
        Assert.isNull(vo.getProductId(),"商品ID不能为空");
        Assert.isNull(vo.getType(),"type不能为空");
        //校验类型是否有效
        Assert.isTrue(1==vo.getType() || 2==vo.getType() || 3==vo.getType(),"type参数错误");
        vo.setToken(token);
        productService.deleteProduct(vo);
        return ResponseBO.success();
    }
    /**
     * 查询各类商品数量
     */
    @RequestMapping(value = "getProductNum",method = RequestMethod.GET)
    public ResponseBO getProductNum(){
        String  token = checkToken();
        return ResponseBO.success(productService.getProductNum(token));
    }
    private String checkToken(){
        String token = RedisUtils.get(TokenUtils.getToken(user.getUserId()));
        Assert.notNull(token, "用户未登录");
        return token;
    }

}
