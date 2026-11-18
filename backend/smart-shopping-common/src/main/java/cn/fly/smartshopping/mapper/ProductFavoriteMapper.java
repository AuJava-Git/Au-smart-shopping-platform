package cn.fly.smartshopping.mapper;

import cn.fly.smartshopping.entity.ProductFavorite;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductFavoriteMapper
        extends BaseMapper<ProductFavorite> {

    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "productId", column = "product_id"),
            @Result(property = "createTime", column = "create_time"),
            @Result(property = "productName", column = "product_name"),
            @Result(property = "productPrice", column = "product_price"),
            @Result(property = "productDescription",
                    column = "product_description"),
            @Result(property = "imageUrl", column = "image_url")
    })
    @Select("""
        SELECT
            f.id,
            f.user_id,
            f.product_id,
            f.create_time,
            p.name AS product_name,
            p.price AS product_price,
            p.description AS product_description,
            p.image_url
        FROM product_favorite f
        JOIN product p ON p.id = f.product_id
        WHERE f.user_id = #{userId}
        ORDER BY f.create_time DESC
        """)
    List<ProductFavorite> selectMyFavorites(Long userId);
}
