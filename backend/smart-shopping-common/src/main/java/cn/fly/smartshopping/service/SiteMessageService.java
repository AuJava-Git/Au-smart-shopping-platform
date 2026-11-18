package cn.fly.smartshopping.service;
import cn.fly.smartshopping.entity.SiteMessage;
import cn.fly.smartshopping.mapper.SiteMessageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class SiteMessageService {
 private final SiteMessageMapper mapper; public SiteMessageService(SiteMessageMapper m){mapper=m;}
 public List<SiteMessage> list(Long uid){return mapper.selectList(new LambdaQueryWrapper<SiteMessage>().eq(SiteMessage::getUserId,uid).orderByDesc(SiteMessage::getCreateTime));}
 public SiteMessage send(Long uid,String title,String content,String type){SiteMessage m=new SiteMessage();m.setUserId(uid);m.setTitle(title);m.setContent(content);m.setMessageType(type==null?"SYSTEM":type);m.setIsRead(false);mapper.insert(m);return m;}
 public void read(Long uid,Long id){SiteMessage m=mapper.selectOne(new LambdaQueryWrapper<SiteMessage>().eq(SiteMessage::getId,id).eq(SiteMessage::getUserId,uid));if(m==null)throw new IllegalArgumentException("消息不存在");m.setIsRead(true);mapper.updateById(m);}
 public long unread(Long uid){return mapper.selectCount(new LambdaQueryWrapper<SiteMessage>().eq(SiteMessage::getUserId,uid).eq(SiteMessage::getIsRead,false));}
}
