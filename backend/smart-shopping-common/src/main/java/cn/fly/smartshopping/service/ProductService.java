package cn.fly.smartshopping.service;

import cn.fly.smartshopping.entity.Product;
import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.List;

public interface ProductService {
    Product getById(Long id);
    List<Product> getAll();
    Product create(Product product);
    Product update(Long id, Product product);
    boolean delete(Long id);
    List<Product> searchByName(String name);
    IPage<Product> getPage(Long current, Long size, Long categoryId);
}
