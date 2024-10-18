package com.example.BE_PBL6_FastOrderSystem.controller.Onwer;
import com.example.BE_PBL6_FastOrderSystem.response.APIRespone;
import com.example.BE_PBL6_FastOrderSystem.security.user.FoodUserDetails;
import com.example.BE_PBL6_FastOrderSystem.service.IOrderService;
import com.example.BE_PBL6_FastOrderSystem.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/owner/products")
@RequiredArgsConstructor
public class OwnerProductController {
    @Autowired
    private final IProductService productService;
    private final IOrderService orderService;

    @GetMapping("/get-all-products")
    public ResponseEntity<APIRespone> getAllProducts(@RequestParam Long storeId) {
        return productService.getProductsByStoreId(storeId);
    }

    @DeleteMapping("/remove-from-store")
    public ResponseEntity<APIRespone> removeProductFromStore(@RequestParam Long storeId, @RequestParam Long productId) {
        return productService.removeProductFromStore(storeId, productId);
    }

    @PostMapping("add-to-store")
    public ResponseEntity<APIRespone> addProductToStore(@RequestParam List<Integer> quantity, @RequestParam List<Long> productIds) {
        Long ownerId = FoodUserDetails.getCurrentUserId();
        return productService.addProductsToStore(productIds,ownerId,quantity);
    }
}