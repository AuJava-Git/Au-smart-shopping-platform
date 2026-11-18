package cn.fly.smartshopping.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
@TableName("point_account")
public class PointAccount { @TableId private Long userId; private Integer balance; private Integer totalEarned; private LocalDateTime updateTime; public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;} public Integer getBalance(){return balance;} public void setBalance(Integer v){balance=v;} public Integer getTotalEarned(){return totalEarned;} public void setTotalEarned(Integer v){totalEarned=v;} public LocalDateTime getUpdateTime(){return updateTime;} public void setUpdateTime(LocalDateTime v){updateTime=v;} }
