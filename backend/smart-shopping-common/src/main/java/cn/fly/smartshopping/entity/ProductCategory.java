package cn.fly.smartshopping.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@TableName("product_category") public class ProductCategory { @TableId private Long id; @NotBlank @Size(max=50) private String name; private Integer sortOrder; private Boolean enabled; public Long getId(){return id;} public void setId(Long v){id=v;} public String getName(){return name;} public void setName(String v){name=v;} public Integer getSortOrder(){return sortOrder;} public void setSortOrder(Integer v){sortOrder=v;} public Boolean getEnabled(){return enabled;} public void setEnabled(Boolean v){enabled=v;} }
