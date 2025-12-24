package com.example.clinic_skin_be.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long senderId;
    private String guestId;
    private Long receiverId;
    private String content;
    private long sentAt;
    private boolean isRead;
}
