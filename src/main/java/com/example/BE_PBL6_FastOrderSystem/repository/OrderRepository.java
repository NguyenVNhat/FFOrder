package com.example.BE_PBL6_FastOrderSystem.repository;

import com.example.BE_PBL6_FastOrderSystem.entity.Order;
import com.example.BE_PBL6_FastOrderSystem.entity.StatusOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    boolean existsByOrderCode(String orderCode);

    @Query("SELECT o FROM Order o WHERE o.orderCode = ?1")
    Optional<Order> findByOrderCode(String orderCode);

    @Query("SELECT o FROM Order o WHERE o.orderId = ?1")
    Optional<Order> findByOrderId(Long orderId);

    List<Order> findAllByStatusAndUserId(StatusOrder statusOrder, Long userId);

    @Query("SELECT o FROM Order o WHERE o.status = ?1")
    boolean findByStatusOrder(StatusOrder statusOrder);

    @Query("SELECT o FROM Order o WHERE o.user.id = ?1")
    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByStatus(StatusOrder statusOrder);
    @Query("SELECT o FROM Order o WHERE o.status = ?1")
    List<Order> findAllByOrderCode(String orderCode);
    @Query("SELECT o FROM Order o JOIN o.orderDetails od WHERE o.user.id = ?1 AND (od.product.productId = ?2 OR od.combo.comboId = ?3)")
    List<Order> findByUserIdAndProductIdOrComboId(Long userId, Long productId, Long comboId);
}