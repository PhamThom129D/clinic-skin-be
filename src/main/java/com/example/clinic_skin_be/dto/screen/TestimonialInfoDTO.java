package com.example.clinic_skin_be.dto.screen;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TestimonialInfoDTO {
    private Long testimonialId;
    private String content, img;
    private Long accountId;
    private String fullName;
}
