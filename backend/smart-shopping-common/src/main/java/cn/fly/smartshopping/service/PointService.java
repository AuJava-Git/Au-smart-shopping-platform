package cn.fly.smartshopping.service;
import cn.fly.smartshopping.entity.*;
import cn.fly.smartshopping.mapper.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service public class PointService {
 private final PointAccountMapper accountMapper; private final PointRecordMapper recordMapper;
 public PointService(PointAccountMapper a,PointRecordMapper r){accountMapper=a;recordMapper=r;}
 public PointAccount account(Long uid){PointAccount a=accountMapper.selectById(uid);if(a==null){a=new PointAccount();a.setUserId(uid);a.setBalance(0);a.setTotalEarned(0);accountMapper.insert(a);}return a;}
 public List<PointRecord> records(Long uid){return recordMapper.selectList(new LambdaQueryWrapper<PointRecord>().eq(PointRecord::getUserId,uid).orderByDesc(PointRecord::getCreateTime));}
 @Transactional public PointAccount earn(Long uid,int amount,String reason,Long orderId){if(amount<=0)return account(uid);PointAccount a=account(uid);a.setBalance(a.getBalance()+amount);a.setTotalEarned(a.getTotalEarned()+amount);accountMapper.updateById(a);PointRecord r=new PointRecord();r.setUserId(uid);r.setChangeAmount(amount);r.setReason(reason);r.setOrderId(orderId);recordMapper.insert(r);return a;}
 @Transactional public PointAccount spend(Long uid,int amount,String reason){if(amount<=0)throw new IllegalArgumentException("积分扣减必须大于0");PointAccount a=account(uid);if(a.getBalance()<amount)throw new IllegalArgumentException("积分余额不足");a.setBalance(a.getBalance()-amount);accountMapper.updateById(a);PointRecord r=new PointRecord();r.setUserId(uid);r.setChangeAmount(-amount);r.setReason(reason);recordMapper.insert(r);return a;}
}
