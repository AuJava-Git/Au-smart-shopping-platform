package cn.fly.smartshopping.mapper;

import cn.fly.smartshopping.entity.OrderItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
    @Select("SELECT COUNT(1) FROM order_item oi " +
            "JOIN orders o ON o.id = oi.order_id " +
            "WHERE o.user_id = #{userId} " +
            "AND o.status = 2 " +
            "AND oi.product_id = #{productId}")
    int countPaidPurchase(@Param("userId") Long userId, @Param("productId") Long productId);

}
