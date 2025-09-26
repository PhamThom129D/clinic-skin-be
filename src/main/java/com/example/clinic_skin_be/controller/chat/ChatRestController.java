package com.example.clinic_skin_be.controller.chat;

import com.example.clinic_skin_be.model.message.ChatMessageRequest;
import com.example.clinic_skin_be.model.message.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
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
//            chatStore.remove(guestKey);
        }

        ChatMessageResponse response = ChatMessageResponse.builder()
                .senderId(request.getSenderId())
                .guestId(request.getGuestId())
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .sentAt(System.currentTimeMillis())
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
                .build();

        chatStore.computeIfAbsent(key, k -> new ArrayList<>()).add(response);
        messagingTemplate.convertAndSend("/topic/message/" + key, response);
        messagingTemplate.convertAndSend("/topic/message/staff-" + request.getSenderId(), response);

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
            List<ChatMessageResponse> staffMsgs = msgs.stream()
                    .filter(m -> staffId.equals(m.getReceiverId()))
                    .toList();

            if (!staffMsgs.isEmpty()) {
                Map<String, Object> conv = new HashMap<>();
                conv.put("customerId", msgs.get(0).getSenderId());
                conv.put("guestId", msgs.get(0).getGuestId());
                conv.put("customerName", "Customer " + key);
                conv.put("messages", staffMsgs);
                inbox.add(conv);
            }
        });

        return inbox;
    }
}
