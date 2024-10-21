package com.example.BE_PBL6_FastOrderSystem.request;

import com.example.BE_PBL6_FastOrderSystem.model.ImageRating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateRequest {
    private int rate;
    private String comment;
    private Long productId;
    private Long comboId;
//    private List<MultipartFile> imageFiles;
}
