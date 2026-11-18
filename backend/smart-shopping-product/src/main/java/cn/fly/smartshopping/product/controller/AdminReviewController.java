package cn.fly.smartshopping.controller;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.ProductReview;
import cn.fly.smartshopping.mapper.ProductReviewMapper;
import cn.fly.smartshopping.security.RequireAdmin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
@RestController @RequestMapping("/admin/reviews") @RequireAdmin public class AdminReviewController { private final ProductReviewMapper mapper; public AdminReviewController(ProductReviewMapper m){mapper=m;} @GetMapping public Result<List<ProductReview>> list(@RequestParam(required=false) Integer status){LambdaQueryWrapper<ProductReview>w=new LambdaQueryWrapper<>();if(status!=null)w.eq(ProductReview::getAuditStatus,status);w.orderByDesc(ProductReview::getCreateTime);return Result.success(mapper.selectList(w));} @PutMapping("/{id}/audit") public Result<ProductReview> audit(@PathVariable Long id,@RequestParam Integer status,@RequestParam(required=false) String reason){if(status==null||status<1||status>2)throw new IllegalArgumentException("审核状态不合法");ProductReview r=mapper.selectById(id);if(r==null)throw new IllegalArgumentException("评论不存在");r.setAuditStatus(status);r.setAuditReason(reason);r.setAuditTime(LocalDateTime.now());mapper.updateById(r);return Result.success("审核完成",r);} }
