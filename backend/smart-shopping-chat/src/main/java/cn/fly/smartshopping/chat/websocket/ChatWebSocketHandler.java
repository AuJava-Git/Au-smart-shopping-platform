package cn.fly.smartshopping.chat.websocket;

import cn.fly.smartshopping.chat.dto.ChatDtos.ChatRequest;
import cn.fly.smartshopping.chat.dto.ChatDtos.ChatResponse;
import cn.fly.smartshopping.chat.service.ProductSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ProductSearchService productSearchService;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public ChatWebSocketHandler(ObjectMapper objectMapper,
                                ProductSearchService productSearchService) {
        this.objectMapper = objectMapper;
        this.productSearchService = productSearchService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        send(session, ChatResponse.text("你好，我可以帮你搜索商品。你可以输入：帮我找耳机。"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatRequest request;
        try {
            request = objectMapper.readValue(message.getPayload(), ChatRequest.class);
        } catch (Exception ex) {
            send(session, ChatResponse.text("消息格式不正确，请发送 JSON：{\"message\":\"帮我找耳机\"}"));
            return;
        }

        if (request == null || request.message() == null || request.message().isBlank()) {
            send(session, ChatResponse.text("请输入你想查询的商品关键词。"));
            return;
        }
        send(session, productSearchService.searchFromMessage(request.message()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }

    private void send(WebSocketSession session, ChatResponse response) throws Exception {
        if (session.isOpen()) {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(response)));
        }
    }
}
