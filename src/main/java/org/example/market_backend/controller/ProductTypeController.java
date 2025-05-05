package org.example.market_backend.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.market_backend.Entity.ProductType;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Utils.RedisUtils;
import org.example.market_backend.Utils.TokenUtils;
import org.example.market_backend.BO.ResponseBO;
import org.example.market_backend.Service.ProductService;
import org.example.market_backend.Service.ProductTypeService;
import org.example.market_backend.VO.PublishProductVO;
import org.example.market_backend.VO.UpdateProductVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import org.example.market_backend.VO.ProductSearchVO;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.example.market_backend.controller.ProductController.successPageInfo;

@RestController
@RequestMapping("/productType")
public class ProductTypeController {
    @Autowired
    private ProductTypeService productTypeService;
    private User user;
    private ProductService productService;
    /**
     * 获取所有商品分类列表
     * @return
     */
    @RequestMapping(value = "getProductTypeList",method = RequestMethod.GET)
    public ResponseBO getProductTypeList(){
        Example example = new Example(ProductType.class);
        Example.Criteria criteria = example.createCriteria();
        //查询有效数据
        criteria.andEqualTo("status",1);
        productTypeService.findList(example);
        return ResponseBO.success();
    }

    /**
     * 获取商品列表，不需要登录
     * @param vo
     * 商品名称，商品类别动态查询
     * @return
     */
    @RequestMapping(value = "getProductList",method = RequestMethod.POST)
    public ResponseBO getProductList(@RequestBody ProductSearchVO vo){
        //使用pagehelper工具直接开发分页查询
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        //调用service层查询并返回给客户端
        return successPageInfo(productService.getProductList(vo));
    }

    /**
     * 发布商品
     * @param productImgs
     * @param vo
     * @return
     */
    @RequestMapping(value = "publishProduct",method = RequestMethod.POST)
    public ResponseBO publishProduct(@RequestParam("productImgs") MultipartFile[] productImgs, @Valid PublishProductVO vo){
        //检验token是否为空
        String token=checkToken();
        Assert.notNull(productImgs,"商品图片不能为空");
        vo.setToken(token);
        //调用service层发布商品
        productService.publishProduct(productImgs,vo);
        return ResponseBO.success();
    }
    /**
     * 修改商品信息
     * @param productImgs
     * @param vo
     * @return
     */
    @RequestMapping(value = "updateProduct",method = RequestMethod.POST)
    public ResponseBO updateProduct(@RequestParam("productImgs") MultipartFile[] productImgs, @Valid UpdateProductVO vo){
        //检验token是否为空
        String token=checkToken();
        vo.setToken(token);
        //调用service层修改商品信息
        productService.updateProduct(productImgs,vo);
        return ResponseBO.success();
    }

    private String checkToken(){
        String token = RedisUtils.get(TokenUtils.getToken(user.getUserId()));
        Assert.notNull(token, "用户未登录");
        return token;
    }

    /**
     * 获取商品检测
     * @param productId
     * @return
     */
    @RequestMapping(value = "getProductDetail",method = RequestMethod.GET)
    public ResponseBO getProductDetail(Integer productId){
        Assert.notNull(productId,"商品ID不能为空");
        productService.getProductDetail(productId);
        return ResponseBO.success();
    }

}
