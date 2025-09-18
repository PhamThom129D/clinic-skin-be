package com.example.clinic_skin_be.model.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageRequest {
    private Long senderId;
    private String guestId;
    private Long receiverId;
    private String content;
}
