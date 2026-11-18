package cn.fly.smartshopping.order;

import cn.fly.smartshopping.common.ApiResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "smart-shopping-user")
public interface UserClient {
    @GetMapping("/internal/users/{id}/exists")
    ApiResult<Boolean> exists(@PathVariable("id") Long id);
}
