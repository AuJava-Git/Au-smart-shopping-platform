package cn.fly.smartshopping.service;
import cn.fly.smartshopping.entity.UserAddress;
import cn.fly.smartshopping.mapper.UserAddressMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class UserAddressService {
 private final UserAddressMapper mapper; public UserAddressService(UserAddressMapper m){mapper=m;}
 public List<UserAddress> list(Long uid){return mapper.selectList(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getUserId,uid).orderByDesc(UserAddress::getIsDefault).orderByDesc(UserAddress::getId));}
 public UserAddress save(Long uid,UserAddress a){a.setUserId(uid); if(Boolean.TRUE.equals(a.getIsDefault())) clearDefault(uid); else if(a.getId()==null && list(uid).isEmpty()) a.setIsDefault(true); mapper.insert(a); return a;}
 public UserAddress update(Long uid,Long id,UserAddress a){UserAddress old=require(uid,id); old.setReceiverName(a.getReceiverName()); old.setReceiverPhone(a.getReceiverPhone()); old.setProvince(a.getProvince()); old.setCity(a.getCity()); old.setDistrict(a.getDistrict()); old.setDetailAddress(a.getDetailAddress()); old.setIsDefault(a.getIsDefault()); if(Boolean.TRUE.equals(old.getIsDefault())) clearDefault(uid); mapper.updateById(old); return old;}
 public boolean delete(Long uid,Long id){require(uid,id); return mapper.delete(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getId,id).eq(UserAddress::getUserId,uid))>0;}
 public UserAddress setDefault(Long uid,Long id){UserAddress a=require(uid,id); clearDefault(uid); a.setIsDefault(true); mapper.updateById(a); return a;}
 private void clearDefault(Long uid){mapper.update(null,new LambdaUpdateWrapper<UserAddress>().eq(UserAddress::getUserId,uid).set(UserAddress::getIsDefault,false));}
 private UserAddress require(Long uid,Long id){UserAddress a=mapper.selectOne(new LambdaQueryWrapper<UserAddress>().eq(UserAddress::getId,id).eq(UserAddress::getUserId,uid)); if(a==null) throw new IllegalArgumentException("收货地址不存在"); return a;}
}
