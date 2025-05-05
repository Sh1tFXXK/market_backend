package org.example.market_backend.Service;

import org.example.market_backend.BO.ProductBO;
import org.example.market_backend.BO.ProductDetailBO;
import org.example.market_backend.Entity.Product;
import org.example.market_backend.VO.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

public interface ProductService extends BaseService<Product,Integer>{

    List<ProductBO> getProductList(ProductSearchVO vo);

    void updateProduct(MultipartFile[] productImgs, @Valid UpdateProductVO vo);


    void publishProduct(MultipartFile[] files, @Valid PublishProductVO vo);

    ProductDetailBO getProductDetail(Integer productId);

    List getBannerList();

    List<?> getMyProductList(@Valid MyProductSearchVO vo);

    void deleteProduct(DelProductVO vo);

    Map<String, Object> getProductNum(String token);
}
