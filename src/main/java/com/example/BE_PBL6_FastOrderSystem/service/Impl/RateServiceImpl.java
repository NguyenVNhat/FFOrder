package com.example.BE_PBL6_FastOrderSystem.service.Impl;

import com.example.BE_PBL6_FastOrderSystem.model.Combo;
import com.example.BE_PBL6_FastOrderSystem.model.ImageRating;
import com.example.BE_PBL6_FastOrderSystem.model.Product;
import com.example.BE_PBL6_FastOrderSystem.model.Rate;
import com.example.BE_PBL6_FastOrderSystem.repository.*;
import com.example.BE_PBL6_FastOrderSystem.request.RateRequest;
import com.example.BE_PBL6_FastOrderSystem.response.APIRespone;
import com.example.BE_PBL6_FastOrderSystem.response.RateResponse;
import com.example.BE_PBL6_FastOrderSystem.service.IRateService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RateServiceImpl implements IRateService {
    @Autowired
    private final RateRepository rateRepository;
    @Autowired
    private final ImageRatingRepository imageRatingRepository;
    @Autowired
    private final ProductRepository productRepository;
    @Autowired
    private final ComboRepository comboRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<APIRespone> rateProduct(Long userId, RateRequest rateRequest,List<MultipartFile> files) {
        if (rateRequest.getProductId() != null && rateRequest.getComboId() != null) {
            return ResponseEntity.ok(new APIRespone(false, "You can only rate either a product or a combo, not both", null));
        }
        if (rateRequest.getProductId() == null && rateRequest.getComboId() == null) {
            return ResponseEntity.ok(new APIRespone(false, "You must provide either a product ID or a combo ID", null));
        }

        Rate rate = new Rate();
        rate.setRate(rateRequest.getRate());
        rate.setComment(rateRequest.getComment());
        rate.setUserId(userId);

        if (rateRequest.getProductId() != null) {
            Optional<Rate> existingRate = rateRepository.findByUserIdAndProductId(userId, rateRequest.getProductId());
            if (existingRate.isPresent()) {
                return ResponseEntity.ok(new APIRespone(false, "You have already rated this product", null));
            }
            Product product = productRepository.findById(rateRequest.getProductId()).orElse(null);
            if (product == null) {
                return ResponseEntity.ok(new APIRespone(false, "Product not found", null));
            }
            rate.setProduct(product);
        } else {
            Optional<Rate> existingRate = rateRepository.findByUserIdAndComboId(userId, rateRequest.getComboId());
            if (existingRate.isPresent()) {
                return ResponseEntity.ok(new APIRespone(false, "You have already rated this combo", null));
            }
            Combo combo = comboRepository.findById(rateRequest.getComboId()).orElse(null);
            if (combo == null) {
                return ResponseEntity.ok(new APIRespone(false, "Combo not found", null));
            }
            rate.setCombo(combo);
        }

        rateRepository.save(rate);

        // Nếu có ảnh, chuyển đổi ảnh thành Base64 và lưu vào ImageRating
        if (files != null && !files.isEmpty()) {
            for (MultipartFile imageFile : files) {
                try {
                    String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());  // Chuyển ảnh thành Base64

                    ImageRating imageRating = new ImageRating();
                    imageRating.setImage(base64Image);  // Lưu chuỗi Base64 vào ImageRating
                    imageRating.setRate(rate);  // Liên kết ImageRating với Rate
                    imageRatingRepository.save(imageRating);
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new APIRespone(false, "Error while processing image", null));
                }
            }
        }
        return ResponseEntity.ok(new APIRespone(true, "Rate success", null));
    }

    @Override
    public ResponseEntity<APIRespone> getRateByProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.ok(new APIRespone(false, "Product not found", null));
        }
        List<RateResponse> rateResponses = rateRepository.findByProductId(productId).stream()
                .map(rate -> new RateResponse(
                        rate.getRateId(),
                        rate.getUserId(),
                        rate.getRate(),
                        rate.getComment(),
                        rate.getCreatedAt(),
                        rate.getUpdatedAt(),
                        rate.getProduct() != null ? rate.getProduct().getProductId() : null,
                        rate.getCombo() != null ? rate.getCombo().getComboId() : null,
                        rate.getImageRatings() != null ? // Kiểm tra null
                                rate.getImageRatings().stream()
                                        .map(ImageRating::getImage) // Lấy danh sách đường dẫn hình ảnh
                                        .collect(Collectors.toList()) : // List<String>
                                new ArrayList<>()
                ))
                .toList();
        return ResponseEntity.ok(new APIRespone(true, "Rates retrieved successfully", rateResponses));
    }
    @Override
    public ResponseEntity<APIRespone> getRateByCombo(Long comboId) {
        Combo combo = comboRepository.findById(comboId).orElse(null);
        if (combo == null) {
            return ResponseEntity.ok(new APIRespone(false, "Combo not found", null));
        }
        List<RateResponse> rateResponses = rateRepository.findByComboId(comboId).stream()
                .map(rate -> new RateResponse(
                        rate.getRateId(),
                        rate.getUserId(),
                        rate.getRate(),
                        rate.getComment(),
                        rate.getCreatedAt(),
                        rate.getUpdatedAt(),
                        rate.getProduct() != null ? rate.getProduct().getProductId() : null,
                        rate.getCombo() != null ? rate.getCombo().getComboId() : null,
                        rate.getImageRatings() != null ? // Kiểm tra null
                                rate.getImageRatings().stream()
                                        .map(ImageRating::getImage) // Lấy danh sách đường dẫn hình ảnh
                                        .collect(Collectors.toList()) : // List<String>
                                new ArrayList<>()
                ))
                .toList();
        return ResponseEntity.ok(new APIRespone(true, "Rates retrieved successfully", rateResponses));
    }
}

