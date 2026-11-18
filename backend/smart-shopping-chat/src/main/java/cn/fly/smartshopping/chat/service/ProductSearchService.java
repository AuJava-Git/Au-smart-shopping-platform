package cn.fly.smartshopping.chat.service;

import cn.fly.smartshopping.chat.client.ChatProductClient;
import cn.fly.smartshopping.chat.dto.ChatDtos.ChatResponse;
import cn.fly.smartshopping.common.Result;
import cn.fly.smartshopping.entity.Product;
import feign.FeignException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ProductSearchService {
    private static final List<String> KEYWORDS = List.of(
            "蓝牙耳机", "头戴式耳机", "机械键盘", "无线鼠标", "显示器", "笔记本电脑",
            "手机", "平板电脑", "充电器", "移动电源", "摄像头", "投影仪", "耳机",
            "沙发", "办公椅", "电脑桌", "台灯", "空气净化器", "加湿器", "电饭煲",
            "扫地机器人", "吸尘器", "保温杯", "羽绒服", "背包", "旅行箱", "运动鞋",
            "瑜伽垫", "哑铃", "跑步", "手环", "健身", "洁面", "面霜", "防晒",
            "电动牙刷", "洗发", "香水", "护手霜"
    );

    private final ChatProductClient productClient;

    public ProductSearchService(ChatProductClient productClient) {
        this.productClient = productClient;
    }

    public ChatResponse searchFromMessage(String message) {
        String normalized = normalize(message);
        if (!StringUtils.hasText(normalized)) {
            return ChatResponse.text("请告诉我你想找什么商品，例如：帮我找耳机、显示器或运动鞋。");
        }

        String keyword = findKeyword(normalized);
        if (!StringUtils.hasText(keyword)) {
            return ChatResponse.text("我暂时没有识别出具体商品。你可以试试输入“耳机”“办公椅”“运动鞋”或“面霜”。");
        }

        try {
            Result<List<Product>> result = productClient.searchByName(keyword);
            List<Product> products = result == null || result.getData() == null
                    ? List.of()
                    : result.getData();
            if (result == null || !Integer.valueOf(200).equals(result.getCode()) || products.isEmpty()) {
                return ChatResponse.text("暂时没有找到“" + keyword + "”相关商品，你可以换一个关键词试试。");
            }
            return ChatResponse.productCard(
                    "为你找到 " + products.size() + " 件“" + keyword + "”相关商品：",
                    keyword,
                    products.stream().limit(8).toList()
            );
        } catch (FeignException ex) {
            return ChatResponse.text("商品服务暂时不可用，请稍后再试。");
        } catch (RuntimeException ex) {
            return ChatResponse.text("商品查询暂时遇到问题，请稍后再试。");
        }
    }

    private String findKeyword(String message) {
        return KEYWORDS.stream()
                .filter(message::contains)
                .sorted(Comparator.comparingInt(String::length).reversed())
                .findFirst()
                .orElseGet(() -> extractCandidate(message));
    }

    private String extractCandidate(String message) {
        String candidate = message
                .replaceAll("帮我|请帮我|查询|搜索|查找|推荐|看看|有没有|想买|我想要|商品|产品", "")
                .replaceAll("[，。！？、,.!?：:；;\\s]+", "")
                .trim();
        if (candidate.length() < 2 || candidate.length() > 30) {
            return null;
        }
        return candidate;
    }

    private String normalize(String message) {
        if (message == null) {
            return "";
        }
        return message.trim();
    }
}
