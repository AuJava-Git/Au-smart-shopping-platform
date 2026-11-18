package cn.fly.smartshopping.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
@TableName("point_record")
public class PointRecord { @TableId(type=IdType.AUTO) private Long id; private Long userId; private Integer changeAmount; private String reason; private Long orderId; private LocalDateTime createTime; public Long getId(){return id;} public void setId(Long v){id=v;} public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;} public Integer getChangeAmount(){return changeAmount;} public void setChangeAmount(Integer v){changeAmount=v;} public String getReason(){return reason;} public void setReason(String v){reason=v;} public Long getOrderId(){return orderId;} public void setOrderId(Long v){orderId=v;} public LocalDateTime getCreateTime(){return createTime;} public void setCreateTime(LocalDateTime v){createTime=v;} }
