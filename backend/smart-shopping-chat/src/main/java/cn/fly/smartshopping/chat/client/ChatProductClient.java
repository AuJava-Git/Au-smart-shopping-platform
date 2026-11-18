package cn.fly.smartshopping.chat.client;

import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 聊天服务调用商品服务的客户端。
 * name 必须与商品服务的 spring.application.name 完全一致。
 */
@FeignClient(name = "smart-shopping-product")
public interface ChatProductClient {

    /**
     * 对应商品服务 ProductController.searchByName。
     * Feign 会通过 Nacos 找到 smart-shopping-product 实例。
     */
    @GetMapping("/products/search")
    Result<List<Product>> searchByName(@RequestParam("name") String name);
}
