package cn.fly.smartshopping.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@TableName("user_address")
public class UserAddress {
    @TableId(type = IdType.AUTO) private Long id;
    private Long userId;
    @NotBlank @Size(max=50) private String receiverName;
    @NotBlank @Size(max=20) private String receiverPhone;
    @NotBlank @Size(max=50) private String province;
    @NotBlank @Size(max=50) private String city;
    @NotBlank @Size(max=50) private String district;
    @NotBlank @Size(max=200) private String detailAddress;
    private Boolean isDefault;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public Long getUserId(){return userId;} public void setUserId(Long v){userId=v;}
    public String getReceiverName(){return receiverName;} public void setReceiverName(String v){receiverName=v;}
    public String getReceiverPhone(){return receiverPhone;} public void setReceiverPhone(String v){receiverPhone=v;}
    public String getProvince(){return province;} public void setProvince(String v){province=v;}
    public String getCity(){return city;} public void setCity(String v){city=v;}
    public String getDistrict(){return district;} public void setDistrict(String v){district=v;}
    public String getDetailAddress(){return detailAddress;} public void setDetailAddress(String v){detailAddress=v;}
    public Boolean getIsDefault(){return isDefault;} public void setIsDefault(Boolean v){isDefault=v;}
    public LocalDateTime getCreateTime(){return createTime;} public void setCreateTime(LocalDateTime v){createTime=v;}
    public LocalDateTime getUpdateTime(){return updateTime;} public void setUpdateTime(LocalDateTime v){updateTime=v;}
}
