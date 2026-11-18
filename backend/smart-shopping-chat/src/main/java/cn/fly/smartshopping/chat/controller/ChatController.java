package cn.fly.smartshopping.chat.controller;

import cn.fly.smartshopping.chat.dto.ChatDtos.ChatRequest;
import cn.fly.smartshopping.chat.dto.ChatDtos.ChatResponse;
import cn.fly.smartshopping.chat.service.ProductSearchService;
import cn.fly.smartshopping.common.Result;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ProductSearchService productSearchService;

    public ChatController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    /**
     * REST 调试入口：POST /api/chat/message。
     * WebSocket 尚未接入前，可以先用这个接口验证商品查询链路。
     */
    @PostMapping("/message")
    public Result<ChatResponse> message(@Valid @RequestBody ChatRequest request) {
        if (request == null || request.message() == null || request.message().isBlank()) {
            return Result.error("消息内容不能为空");
        }
        return Result.success(productSearchService.searchFromMessage(request.message()));
    }
}
