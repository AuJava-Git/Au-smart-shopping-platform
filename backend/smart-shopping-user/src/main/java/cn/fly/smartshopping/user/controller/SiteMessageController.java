package cn.fly.smartshopping.controller;
import cn.fly.smartshopping.DTO.LoginResponse;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.SiteMessage;
import cn.fly.smartshopping.service.SiteMessageService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/messages") public class SiteMessageController {
 private final SiteMessageService service; public SiteMessageController(SiteMessageService s){service=s;}
 @GetMapping public Result<List<SiteMessage>> list(HttpSession s){return Result.success(service.list(uid(s)));}
 @GetMapping("/unread-count") public Result<Long> unread(HttpSession s){return Result.success(service.unread(uid(s)));}
 @PutMapping("/{id}/read") public Result<Void> read(@PathVariable Long id,HttpSession s){service.read(uid(s),id);return Result.success("已读",null);}
 @PostMapping("/feedback") public Result<SiteMessage> feedback(@RequestBody Feedback f,HttpSession s){return Result.success("留言已提交",service.send(uid(s),f.title,f.content,"CUSTOMER_SERVICE"));}
 private Long uid(HttpSession s){LoginResponse u=(LoginResponse)s.getAttribute("loginUser");if(u==null)throw new IllegalArgumentException("请先登录");return u.getId();}
 public static class Feedback {@NotBlank @Size(max=100) public String title; @NotBlank @Size(max=1000) public String content;}
}
