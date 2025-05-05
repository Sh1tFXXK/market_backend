package org.example.market_backend.Mapper;

import org.apache.ibatis.annotations.Param;
import org.example.market_backend.BO.ProductBO;
import org.example.market_backend.BO.ProductDetailBO;
import org.example.market_backend.BO.ProductNumBO;
import org.example.market_backend.Entity.Order;
import org.example.market_backend.Entity.Product;
import org.example.market_backend.VO.ProductSearchVO;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public interface ProductMapper extends Mapper<Product>{
    List<ProductBO> selectProductList(ProductSearchVO vo);

    int insertSelective(Product product);

    ProductDetailBO selectProductDetail(Integer productId);

    List<ProductBO> getProductList(ProductSearchVO vo);

    Product findById(String userId);

    List<ProductBO> selectBannerList();

    List<ProductBO> selectMyProductList(@Param("type") Integer type,@Param("userId") String userId);

    void updateByPrimaryKeySelective(Order order);

    ProductNumBO selectProductNum(@Param("userId") String userId);

    ProductBO selectProductInfoAndTradeStatus(Integer productId);
}
