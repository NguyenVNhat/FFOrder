package com.example.BE_PBL6_FastOrderSystem.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Builder
public class AdminProductRequest {
    private String storeId;
    private List<Long> productIds;
    private List<Integer> quantity;
}
