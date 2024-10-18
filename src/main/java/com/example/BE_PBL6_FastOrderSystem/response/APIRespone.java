package com.example.BE_PBL6_FastOrderSystem.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class APIRespone {
    private Boolean success;
    private String message;
    private Object data;
}
