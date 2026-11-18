package cn.fly.smartshopping.controller;

import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.Product;
import cn.fly.smartshopping.security.RequireAdmin;
import cn.fly.smartshopping.service.ProductService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) { this.productService = productService; }

    @GetMapping("/{id}") public Result<Product> getById(@PathVariable Long id) { return Result.success(productService.getById(id)); }
    @GetMapping public Result<List<Product>> getAll() { return Result.success(productService.getAll()); }

    @PostMapping @RequireAdmin
    public Result<Product> create(@Valid @RequestBody Product product) { return Result.success("商品新增成功", productService.create(product)); }

    @PutMapping("/{id}") @RequireAdmin
    public Result<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) { return Result.success("商品修改成功", productService.update(id, product)); }

    @DeleteMapping("/{id}") @RequireAdmin
    public Result<Boolean> delete(@PathVariable Long id) { return Result.success(productService.delete(id)); }

    @GetMapping("/search") public Result<List<Product>> searchByName(@RequestParam String name) { return Result.success(productService.searchByName(name)); }

    @GetMapping("/page")
    public Result<IPage<Product>> getPage(@RequestParam(defaultValue = "1") Long page,
                                          @RequestParam(defaultValue = "8") Long size,
                                          @RequestParam(required = false) Long categoryId) {
        return Result.success(productService.getPage(page, size, categoryId));
    }
}
