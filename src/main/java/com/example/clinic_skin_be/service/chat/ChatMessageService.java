//package com.example.clinic_skin_be.service.chat;
//
//import com.example.clinic_skin_be.model.message.ChatMessageRequest;
//import com.example.clinic_skin_be.model.message.ChatMessageResponse;
//import com.example.clinic_skin_be.model.message.Message;
//import com.example.clinic_skin_be.model.user.Account;
//import com.example.clinic_skin_be.repository.message.ChatMessageRepository;
//import com.example.clinic_skin_be.repository.user.IAccountRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ChatMessageService {
//    private final ChatMessageRepository chatMessageRepository;
//    private final IAccountRepository accountRepository;
//
//    public Message saveMessage(ChatMessageRequest req) {
//        Account sender = accountRepository.getReferenceById(req.getSenderId());
//        Account receiver = accountRepository.getReferenceById(req.getReceiverId());
//
//        Message m = Message.builder()
//                .sender(sender)
//                .receiver(receiver)
//                .content(req.getContent())
//                .sentAt(LocalDateTime.now())
//                .build();
//        return chatMessageRepository.save(m);
//    }
//
//    public ChatMessageResponse toResponse(Message message){
//        return ChatMessageResponse.builder()
//                .id(message.getId())
//                .senderId(message.getSender().getId())
//                .receiverId(message.getReceiver().getId())
//                .content(message.getContent())
//                .sentAt(message.getSentAt())
//                .build();
//    }
//
//    public List<ChatMessageResponse> getConversation(Long userA, Long userB){
//        List<Message> aToB = chatMessageRepository.findBySenderIdAndReceiverIdOrderBySentAtAsc(userA, userB);
//
//        List<Message> bToA = chatMessageRepository.findBySenderIdAndReceiverIdOrderBySentAtAsc(userB, userA);
//
//        List<Message> messages = new ArrayList<>();
//        messages.addAll(aToB);
//        messages.addAll(bToA);
//        messages.sort((m1, m2) -> m1.getSentAt().compareTo(m2.getSentAt()));
//
//        List<ChatMessageResponse> responses = new ArrayList<>();
//        for (Message message : messages){
//            responses.add(toResponse(message));
//        }
//
//        return responses;
//    }
//
//    public ChatMessageResponse saveAndReturnResponse(ChatMessageRequest request){
//        Message saved = saveMessage(request);
//        return toResponse(saved);
//    }
//}
