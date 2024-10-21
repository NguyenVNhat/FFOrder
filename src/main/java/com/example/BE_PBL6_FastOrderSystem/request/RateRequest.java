package com.example.BE_PBL6_FastOrderSystem.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateRequest {
    private int rate;
    private String comment;
    private List<Long> productIds;
    private List<Long> comboIds;
}
