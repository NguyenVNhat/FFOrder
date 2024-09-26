package com.example.BE_PBL6_FastOrderSystem.request;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatRequest {
    Long sender;
    Long receiver;
    String message;
    MultipartFile image;
    Boolean isRead;
}
