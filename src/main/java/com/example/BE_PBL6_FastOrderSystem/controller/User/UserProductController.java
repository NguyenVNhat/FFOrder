package com.example.BE_PBL6_FastOrderSystem.controller.User;

import com.example.BE_PBL6_FastOrderSystem.request.RateRequest;
import com.example.BE_PBL6_FastOrderSystem.response.APIRespone;
import com.example.BE_PBL6_FastOrderSystem.security.user.FoodUserDetails;
import com.example.BE_PBL6_FastOrderSystem.security.user.FoodUserDetailsService;
import com.example.BE_PBL6_FastOrderSystem.service.IRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user/product")
@RequiredArgsConstructor
public class UserProductController {
    final private IRateService rateService;
    private Long getCurrentUserId() {
        return FoodUserDetails.getCurrentUserId();
    }

    @PostMapping("/rate")
    public ResponseEntity<APIRespone> rateProduct(@RequestBody RateRequest rateRequest) {
        Long userId = FoodUserDetails.getCurrentUserId();
        return rateService.rateProduct(userId, rateRequest);
    }
    @GetMapping("/rate/history")
    public ResponseEntity<APIRespone> getRateByUserId() {
        Long userId = FoodUserDetails.getCurrentUserId();
        return rateService.getRateByUserId(userId);
    }
}