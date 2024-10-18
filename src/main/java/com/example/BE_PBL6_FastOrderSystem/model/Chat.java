package com.example.BE_PBL6_FastOrderSystem.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

@Entity
@Table(name = "chat")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne
    @JoinColumn(name = "sender, cascade = CascadeType.REMOVE", nullable = false)
    User sender;
    @ManyToOne
    @JoinColumn(name = "receiver , cascade = CascadeType.REMOVE", nullable = false)
    User receiver;
    String message;
    LocalDateTime local_time;

    @Column(nullable = false, columnDefinition = "boolean default false")
    Boolean isRead = false; // Mặc định là false

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String image; // Lưu ảnh dưới dạng chuỗi Base64

    @PrePersist
    protected void onCreate() {
        local_time = LocalDateTime.now(); // Thiết lập thời gian hiện tại
    }
}
