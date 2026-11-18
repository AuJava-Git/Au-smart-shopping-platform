package cn.fly.smartshopping.mapper;

import cn.fly.smartshopping.entity.ProductReview;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ProductReviewMapper
        extends BaseMapper<ProductReview> {

    @Results({
            @Result(
                    property = "id",
                    column = "id"
            ),
            @Result(
                    property = "userId",
                    column = "user_id"
            ),
            @Result(
                    property = "productId",
                    column = "product_id"
            ),
            @Result(
                    property = "rating",
                    column = "rating"
            ),
            @Result(
                    property = "content",
                    column = "content"
            ),
            @Result(
                    property = "anonymous",
                    column = "is_anonymous"
            ),
            @Result(
                    property = "createTime",
                    column = "create_time"
            ),
            @Result(
                    property = "username",
                    column = "username"
            ),
            @Result(
                    property = "nickname",
                    column = "nickname"
            ),
            @Result(
                    property = "mine",
                    column = "mine"
            )
    })
    @Select("""
        SELECT
            r.id,

            CASE
                WHEN r.is_anonymous = 1
                THEN NULL
                ELSE r.user_id
            END AS user_id,

            r.product_id,
            r.rating,
            r.content,
            r.is_anonymous,
            r.audit_status,
            r.audit_reason,
            r.audit_time,
            r.create_time,

            CASE
                WHEN r.is_anonymous = 1
                THEN NULL
                ELSE u.username
            END AS username,

            CASE
                WHEN r.is_anonymous = 1
                THEN '匿名用户'
                ELSE u.nickname
            END AS nickname,
            CASE WHEN #{userId} IS NOT NULL AND r.user_id = #{userId} THEN 1 ELSE 0 END AS mine

        FROM product_review r
        JOIN sys_user u
            ON u.id = r.user_id

        WHERE r.product_id = #{productId}
          AND (r.audit_status = 1 OR (#{userId} IS NOT NULL AND r.user_id = #{userId}))

        ORDER BY r.create_time DESC
        """)
    List<ProductReview> selectByProductId(@org.apache.ibatis.annotations.Param("productId") Long productId,
                                           @org.apache.ibatis.annotations.Param("userId") Long userId);

    @Select("SELECT COUNT(1) FROM product_review WHERE user_id = #{userId} AND product_id = #{productId}")
    int countByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);
}
