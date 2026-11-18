package cn.fly.smartshopping.controller;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.*;
import cn.fly.smartshopping.service.PointService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/points") public class PointController {
 private final PointService service; public PointController(PointService s){service=s;}
 @GetMapping("/account") public Result<PointAccount> account(HttpSession s){return Result.success(service.account(uid(s)));}
 @GetMapping("/records") public Result<List<PointRecord>> records(HttpSession s){return Result.success(service.records(uid(s)));}
 private Long uid(HttpSession s){LoginResponse u=(LoginResponse)s.getAttribute("loginUser");if(u==null)throw new IllegalArgumentException("请先登录");return u.getId();}
}
