package com.example.clinic_skin_be.controller.chat;

import com.example.clinic_skin_be.model.message.ChatMessageRequest;
import com.example.clinic_skin_be.model.message.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRestController {

    private final SimpMessagingTemplate messagingTemplate;

    private final Map<String, List<ChatMessageResponse>> chatStore = new ConcurrentHashMap<>();

    private String getKey(Long senderId, String guestId) {
        return senderId != null ? "user-" + senderId : "guest-" + guestId;
    }

    @PostMapping("/send")
    public ChatMessageResponse sendMessage(@RequestBody ChatMessageRequest request) {
        String key = getKey(request.getSenderId(), request.getGuestId());

        if (request.getSenderId() != null && request.getGuestId() != null) {
            String guestKey = "guest-" + request.getGuestId();
            List<ChatMessageResponse> guestMsgs = chatStore.getOrDefault(guestKey, new ArrayList<>());
            chatStore.computeIfAbsent(key, k -> new ArrayList<>()).addAll(guestMsgs);
        }

        ChatMessageResponse response = ChatMessageResponse.builder()
                .senderId(request.getSenderId())
                .guestId(request.getGuestId())
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .sentAt(System.currentTimeMillis())
                .isRead(false)
                .build();

        chatStore.computeIfAbsent(key, k -> new ArrayList<>()).add(response);
        messagingTemplate.convertAndSend("/topic/message/" + key, response);
        messagingTemplate.convertAndSend("/topic/message/staff-1", response);
        messagingTemplate.convertAndSend("/topic/inbox-updates", "staff-1");

        return response;
    }

    @PostMapping("/reply")
    public ChatMessageResponse replyMessage(@RequestBody ChatMessageRequest request,
                                            @RequestParam String role) {

        String key = request.getGuestId() != null
                ? "guest-" + request.getGuestId()
                : "user-" + request.getReceiverId();

        ChatMessageResponse response = ChatMessageResponse.builder()
                .senderId(request.getSenderId())
                .guestId(request.getGuestId())
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .sentAt(System.currentTimeMillis())
                .isRead(false)
                .build();

        chatStore.computeIfAbsent(key, k -> new ArrayList<>()).add(response);

        messagingTemplate.convertAndSend("/topic/message/" + key, response);
        messagingTemplate.convertAndSend("/topic/message/staff-" + request.getSenderId(), response);
        messagingTemplate.convertAndSend("/topic/inbox-updates", "staff-1");

        return response;
    }

    @GetMapping("/history/{key}")
    public List<ChatMessageResponse> getHistory(@PathVariable String key) {
        return chatStore.getOrDefault(key, Collections.emptyList());
    }



    @GetMapping("/inbox/{staffId}")
    public List<Map<String, Object>> getInbox(@PathVariable Long staffId) {
        List<Map<String, Object>> inbox = new ArrayList<>();

        chatStore.forEach((key, msgs) -> {
            List<ChatMessageResponse> relatedMsgs = msgs.stream()
                    .filter(m -> Objects.equals(m.getReceiverId(), staffId))
                    .toList();

            if (!relatedMsgs.isEmpty()) {
                Map<String, Object> conv = new HashMap<>();

                ChatMessageResponse lastMsg = msgs.get(msgs.size() - 1);

                Long customerId = msgs.stream()
                        .map(ChatMessageResponse::getSenderId)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);

                String guestId = msgs.stream()
                        .map(ChatMessageResponse::getGuestId)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .orElse(null);

                conv.put("customerId", customerId);
                conv.put("guestId", guestId);
                conv.put("customerName", customerId != null
                        ? "Người dùng " + customerId
                        : "Khách " + guestId);

                conv.put("messages", msgs);

                boolean hasUnread = msgs.stream()
                        .anyMatch(m -> !m.isRead() && !Objects.equals(m.getSenderId(), staffId));

                conv.put("hasUnread", hasUnread);

                inbox.add(conv);
            }
        });

        return inbox;
    }


    @PostMapping("/mark-read")
    public ResponseEntity<Map<String, Object>> markAsRead(@RequestBody Map<String, String> body) {
        String key = body.get("key");
        Map<String, Object> response = new HashMap<>();

        if (key == null || !chatStore.containsKey(key)) {
            response.put("status", "error");
            response.put("message", "Invalid conversation key");
            return ResponseEntity.badRequest().body(response);
        }

        List<ChatMessageResponse> msgs = chatStore.get(key);
        msgs.forEach(msg -> msg.setRead(true));

        messagingTemplate.convertAndSend("/topic/message/" + key, "read");

        response.put("status", "success");
        response.put("key", key);
        response.put("message", "Marked as read");
        return ResponseEntity.ok(response);
    }
}
