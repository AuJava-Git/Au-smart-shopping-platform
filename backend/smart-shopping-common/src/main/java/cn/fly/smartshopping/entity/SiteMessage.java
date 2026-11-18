package cn.fly.smartshopping.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
@TableName("site_message")
public class SiteMessage {
 @TableId(type=IdType.AUTO) private Long id; private Long userId; private String title; private String content; private String messageType; private Boolean isRead; private LocalDateTime createTime;
 public Long getId(){return id;} public void setId(Long v){id=v;} public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;} public String getTitle(){return title;} public void setTitle(String v){title=v;} public String getContent(){return content;} public void setContent(String v){content=v;} public String getMessageType(){return messageType;} public void setMessageType(String v){messageType=v;} public Boolean getIsRead(){return isRead;} public void setIsRead(Boolean v){isRead=v;} public LocalDateTime getCreateTime(){return createTime;} public void setCreateTime(LocalDateTime v){createTime=v;}
}
