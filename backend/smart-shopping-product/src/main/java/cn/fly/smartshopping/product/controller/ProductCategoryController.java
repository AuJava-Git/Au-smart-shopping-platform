package cn.fly.smartshopping.controller;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.ProductCategory;
import cn.fly.smartshopping.security.RequireAdmin;
import cn.fly.smartshopping.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/categories") public class ProductCategoryController { private final ProductCategoryService service; public ProductCategoryController(ProductCategoryService s){service=s;} @GetMapping public Result<List<ProductCategory>> list(){return Result.success(service.list());} @GetMapping("/admin") @RequireAdmin public Result<List<ProductCategory>> adminList(){return Result.success(service.adminList());} @PostMapping @RequireAdmin public Result<ProductCategory> save(@Valid @RequestBody ProductCategory c){return Result.success("分类保存成功",service.save(c));} @PutMapping("/{id}") @RequireAdmin public Result<ProductCategory> update(@PathVariable Long id,@Valid @RequestBody ProductCategory c){c.setId(id);return Result.success("分类修改成功",service.save(c));} @DeleteMapping("/{id}") @RequireAdmin public Result<Boolean> delete(@PathVariable Long id){return Result.success("分类删除成功",service.delete(id));} }
