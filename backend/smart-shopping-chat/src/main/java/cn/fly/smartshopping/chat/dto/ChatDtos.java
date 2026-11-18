package cn.fly.smartshopping.chat.dto;

import cn.fly.smartshopping.entity.Product;

import java.util.List;

public final class ChatDtos {
    private ChatDtos() {
    }

    public record ChatRequest(String message) {
    }

    /** type: text 表示普通文字；product_card 表示商品卡片。 */
    public record ChatResponse(
            String type,
            String message,
            String keyword,
            List<Product> products
    ) {
        public static ChatResponse text(String message) {
            return new ChatResponse("text", message, null, List.of());
        }

        public static ChatResponse productCard(String message, String keyword, List<Product> products) {
            return new ChatResponse("product_card", message, keyword, products);
        }
    }
}
