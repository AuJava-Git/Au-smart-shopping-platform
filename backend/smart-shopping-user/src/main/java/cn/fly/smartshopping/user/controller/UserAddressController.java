package cn.fly.smartshopping.controller;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.entity.UserAddress;
import cn.fly.smartshopping.service.UserAddressService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/addresses") public class UserAddressController {
 private final UserAddressService service; public UserAddressController(UserAddressService s){service=s;}
 @GetMapping public Result<List<UserAddress>> list(HttpSession s){return Result.success(service.list(uid(s)));}
 @PostMapping public Result<UserAddress> create(@Valid @RequestBody UserAddress a,HttpSession s){return Result.success("地址新增成功",service.save(uid(s),a));}
 @PutMapping("/{id}") public Result<UserAddress> update(@PathVariable Long id,@Valid @RequestBody UserAddress a,HttpSession s){return Result.success("地址修改成功",service.update(uid(s),id,a));}
 @PutMapping("/{id}/default") public Result<UserAddress> setDefault(@PathVariable Long id,HttpSession s){return Result.success("默认地址设置成功",service.setDefault(uid(s),id));}
 @DeleteMapping("/{id}") public Result<Boolean> delete(@PathVariable Long id,HttpSession s){return Result.success("地址删除成功",service.delete(uid(s),id));}
 private Long uid(HttpSession s){LoginResponse u=(LoginResponse)s.getAttribute("loginUser");if(u==null)throw new IllegalArgumentException("请先登录");return u.getId();}
}
