package cn.fly.smartshopping.service;
import cn.fly.smartshopping.entity.ProductCategory;
import cn.fly.smartshopping.mapper.ProductCategoryMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class ProductCategoryService { private final ProductCategoryMapper mapper; public ProductCategoryService(ProductCategoryMapper m){mapper=m;} public List<ProductCategory> list(){return mapper.selectList(new LambdaQueryWrapper<ProductCategory>().eq(ProductCategory::getEnabled,true).orderByAsc(ProductCategory::getSortOrder));} public List<ProductCategory> adminList(){return mapper.selectList(new LambdaQueryWrapper<ProductCategory>().orderByAsc(ProductCategory::getSortOrder));} public ProductCategory save(ProductCategory c){if(c.getSortOrder()==null)c.setSortOrder(0);if(c.getEnabled()==null)c.setEnabled(true);if(c.getId()==null)mapper.insert(c);else mapper.updateById(c);return c;} public boolean delete(Long id){return mapper.deleteById(id)>0;} }
