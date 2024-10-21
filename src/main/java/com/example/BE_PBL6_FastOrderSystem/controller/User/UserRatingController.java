package com.example.BE_PBL6_FastOrderSystem.controller.User;

import com.example.BE_PBL6_FastOrderSystem.request.RateRequest;
import com.example.BE_PBL6_FastOrderSystem.response.APIRespone;
import com.example.BE_PBL6_FastOrderSystem.security.user.FoodUserDetails;
import com.example.BE_PBL6_FastOrderSystem.service.IRateService;
import com.example.BE_PBL6_FastOrderSystem.service.IRoleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user/product")
@RequiredArgsConstructor
public class UserRatingController {
    final private IRateService rateService;

    @PostMapping("/rating")
    public ResponseEntity<APIRespone> RatingProduct(
            @RequestPart("rateRequest") String rateRequestJson,
            @RequestPart("files") List<MultipartFile> files) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RateRequest rateRequest = objectMapper.readValue(rateRequestJson, RateRequest.class);
        Long userId = FoodUserDetails.getCurrentUserId();
        return rateService.rateProduct(userId, rateRequest,files);
    }

    @GetMapping("/get/rating/product")
    public ResponseEntity<APIRespone> getRatingProduct(@RequestParam Long productId) {
        Long userId = FoodUserDetails.getCurrentUserId();
        return rateService.getRateByProduct(productId);
    }

    @GetMapping("/get/rating/combo")
    public ResponseEntity<APIRespone> getRatingCombo(@RequestParam Long comboId) {
        Long userId = FoodUserDetails.getCurrentUserId();
        return rateService.getRateByCombo(comboId);
    }


}
