package org.example.market_backend.Service;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.github.pagehelper.PageHelper;
import lombok.Setter;
import org.example.market_backend.BO.ProductDetailBO;
import org.example.market_backend.BO.ProductNumBO;
import org.example.market_backend.Entity.Order;
import org.example.market_backend.Entity.Product;
import com.alibaba.fastjson2.util.DateUtils;
import com.aliyun.oss.OSS;
import org.example.market_backend.BO.ProductBO;
import org.example.market_backend.Entity.User;
import org.example.market_backend.Exception.BusinessException;
import org.example.market_backend.Mapper.ProductMapper;
import org.example.market_backend.Utils.OssUtil;
import org.example.market_backend.VO.*;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import jakarta.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProductServiceImpl implements ProductService{
    @Setter
    private UserService userService;
    private ProductMapper productMapper;
    /**
     * 获取商品列表
     * @param vo
     * @return
     */
    @Override
    public List<ProductBO> getProductList(ProductSearchVO vo){
        return productMapper.getProductList(vo);
    }

    /**
     * 修改商品
     *
     */
    @Override
    public void updateProduct(MultipartFile[] files, UpdateProductVO vo) {
        //检验token是否有效
        User user=userService.checkToken(vo.getToken());
        //查询商品是否存在
        Product product=productMapper.findById(vo.getUserId());
        Assert.notNull(product,"商品信息不存在");
        //确认商品发布人为当前登陆人
        Assert.isTrue(product.getPublishUserId().equals(user.getUserId()),"只有商品发布人可以修改");
        String productImgs = null;
        StringBuilder stringBuilder =new StringBuilder();
        //修改商品信息
        if(files!=null && files.length>0){
            //OSS存储
            OSS ossClient = OssUtil.getOssClient();
            String bucketName = OssUtil.getBucketName();
            String diskName= "images/product/" + DateUtils.toString(new Date());
            try{
                stringBuilder = OssUtil.batchUploadInputStreamObject2Oss(ossClient, files,bucketName,diskName);
            }catch (Exception e){
                Assert.isTrue(true, "上传失败");
            }
            Assert.notNull(stringBuilder,  "文件上传失败");
            if(vo.getOldImgs()!=null && vo.getOldImgs().length>0){
                for(String oldImg:vo.getOldImgs()){
                    stringBuilder.append(oldImg+",");
                }
            }
            if(stringBuilder.length()>1){
                productImgs = stringBuilder.substring(0, stringBuilder.length()-1);
            }
        }
        //修改用户信息
        Product update =  new Product();
        Date date =new Date();
        BeanUtils.copyProperties(vo, update);
        update.setProductImgs(productImgs);
        update.setId(vo.getProductId());
        update.setUpdateTime(date);
        productMapper.updateByPrimaryKeySelective(update);
    }



    @Override
    public void publishProduct(MultipartFile[] files, @Valid PublishProductVO vo) {
        User user=userService.checkToken(vo.getToken());
        //OSS存储
        OSS ossClient = OssUtil.getOssClient();
        String bucketName = OssUtil.getBucketName();
        String diskName= "images/product/" + DateUtils.toString(new Date());
        StringBuilder stringBuilder = null;
        try{
            stringBuilder = OssUtil.batchUploadInputStreamObject2Oss(ossClient, files,bucketName,diskName);
        }catch (Exception e){
            Assert.isTrue(true, "上传失败");
        }
        Assert.notNull(stringBuilder,  "上传失败");
        String  productImgs = stringBuilder.substring(0, stringBuilder.length()-1);
        Product product=new Product();
        Date date = new Date();
        BeanUtils.copyProperties(vo, product);
        product.setProductImgs(productImgs);
        product.setCreateTime(date);
        product.setUpdateTime(date);
        product.setPublishUserId(user.getUserId());
        productMapper.insertSelective(product);
    }

    @Override
    public ProductDetailBO getProductDetail(Integer productId) {
        return productMapper.selectProductDetail(productId);
    }

    @Override
    public List<ProductBO> getBannerList(){
        //自定义查询
        List<ProductBO> list = productMapper.selectBannerList();
        //通过filter将商品图片取第一个返回
        list.stream().filter(o->{
            String productImgs = o.getProductImgs();
            String [] imgs = productImgs.split(",");
            o.setProductImgs(imgs[0]);
            return true;
        }).collect(Collectors.toList());
        return list;
    }
    @Override
    public List<ProductBO> getMyProductList( MyProductSearchVO vo) {
        User user=userService.checkToken(vo.getToken());
        PageHelper.startPage(vo.getPageNum(),vo.getPageSize());
        return productMapper.selectMyProductList(vo.getType(),user.getUserId());
    }

    @Override
    public void deleteProduct(DelProductVO vo) {
        User user=userService.checkToken(vo.getToken());
        ProductBO product = productMapper.selectProductInfoAndTradeStatus(vo.getProductId());
        Assert.notNull(product,"商品信息不存在");
        if(vo.getType()==1){
            Assert.isTrue(product.getPublishUserId()==user.getUserId(),"只有商品发布人可以删除");
            Assert.isTrue(product.getTradeStatus()==2,"只有交易完成才能删除");
            productMapper.deleteByPrimaryKey(vo.getProductId());
        }else {
            Assert.isTrue(product.getTradeStatus()==3,"商品状态异常");
            Assert.notNull(product.getOrderId(),"订单信息异常");
            Order order= new Order();
            if(2==vo.getType()){
                order.setSellingStatus(2);
            }else {
                order.setBuyingStatus(2);
            }
            order.setUpdateTime(new Date());
            order.setOrderId(product.getOrderId());
            productMapper.updateByPrimaryKeySelective(order);
        }
    }

    @Override
    public Map<String, Object> getProductNum(String token) {
        User user=userService.checkToken(token);
        ProductNumBO bo  =productMapper.selectProductNum(user.getUserId());
        //我发布的
        Map<String,Object> publish = new HashMap<>(2);
        //我购买的
        Map<String,Object> purchase = new HashMap<>(2);
        //我卖出的
        Map<String,Object> sale = new HashMap<>(2);
        //我发布的商品数量与金额
        publish.put("num",bo.getPublishNum());
        publish.put("money",bo.getPublishAmount());
        //我购买的商品数量与金额
        purchase.put("num",bo.getPurchaseNum());
        purchase.put("money",bo.getPurchaseAmount());
        //我卖出的商品数量与金额
        sale.put("num",bo.getSaleNum());
        sale.put("money",bo.getSaleAmount());
        Map<String,Object> map = new HashMap<>(16);
        map.put("publish",publish);
        map.put("purchase",purchase);
        map.put("sale",sale);
        return map;
    }

    @Override
    public int save(Product entity) throws BusinessException {
        return productMapper.insertSelective(entity);
    }

    @Override
    public int update(Product entity) throws BusinessException {
        return 0;
    }

    @Override
    public int delete(Integer id) throws BusinessException {
        return 0;
    }

    @Override
    public Product findById(Integer id) throws BusinessException {
        return null;
    }

    @Override
    public List<Product> findList(Example example) {
        return productMapper.selectByExample(example);
    }
}