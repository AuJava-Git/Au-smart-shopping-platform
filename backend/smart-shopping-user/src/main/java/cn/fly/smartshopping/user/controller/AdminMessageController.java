package cn.fly.smartshopping.controller;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.SiteMessage;
import cn.fly.smartshopping.mapper.SiteMessageMapper;
import cn.fly.smartshopping.security.RequireAdmin;
import cn.fly.smartshopping.service.SiteMessageService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/admin/messages") @RequireAdmin public class AdminMessageController {
 private final SiteMessageMapper mapper; private final SiteMessageService service; public AdminMessageController(SiteMessageMapper m,SiteMessageService s){mapper=m;service=s;}
 @GetMapping public Result<List<SiteMessage>> list(){return Result.success(mapper.selectList(new LambdaQueryWrapper<SiteMessage>().orderByDesc(SiteMessage::getCreateTime)));}
 @PostMapping public Result<SiteMessage> send(@Valid @RequestBody SendMessage req){return Result.success("消息发送成功",service.send(req.userId,req.title,req.content,"SYSTEM"));}
 public static class SendMessage { @NotNull public Long userId; @NotBlank @Size(max=100) public String title; @NotBlank @Size(max=1000) public String content; }
}
