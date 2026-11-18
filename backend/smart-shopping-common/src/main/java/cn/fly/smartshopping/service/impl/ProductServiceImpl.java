package cn.fly.smartshopping.service.impl;

import cn.fly.smartshopping.entity.Product;
import cn.fly.smartshopping.entity.ProductCategory;
import cn.fly.smartshopping.mapper.ProductCategoryMapper;
import cn.fly.smartshopping.mapper.ProductMapper;
import cn.fly.smartshopping.service.ProductService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductCategoryMapper categoryMapper;
    public ProductServiceImpl(ProductMapper productMapper, ProductCategoryMapper categoryMapper) { this.productMapper = productMapper; this.categoryMapper = categoryMapper; }

    @Override public Product getById(Long id) { return withCategory(productMapper.selectById(id)); }
    @Override public List<Product> getAll() { return productMapper.selectList(null).stream().map(this::withCategory).toList(); }
    @Override public Product create(Product product) { productMapper.insert(product); return product; }
    @Override public Product update(Long id, Product product) { product.setId(id); productMapper.updateById(product); return productMapper.selectById(id); }
    @Override public boolean delete(Long id) { return productMapper.deleteById(id) > 0; }

    @Override
    public List<Product> searchByName(String name) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Product::getName, name);
        return productMapper.selectList(wrapper).stream().map(this::withCategory).toList();
    }

    @Override
    public IPage<Product> getPage(Long current, Long size, Long categoryId) {
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (categoryId != null) wrapper.eq(Product::getCategoryId, categoryId);
        wrapper.orderByDesc(Product::getId);
        IPage<Product> page = productMapper.selectPage(new Page<>(current, size), wrapper);
        page.getRecords().replaceAll(this::withCategory);
        return page;
    }
    private Product withCategory(Product p) { if (p != null && p.getCategoryId() != null) { ProductCategory c = categoryMapper.selectById(p.getCategoryId()); if (c != null) p.setCategoryName(c.getName()); } return p; }
}
