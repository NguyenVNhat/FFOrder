package com.example.BE_PBL6_FastOrderSystem.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ImageRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(name = "image", columnDefinition = "LONGTEXT")
    private String image;

    @ManyToOne
    @JoinColumn(name = "rating_id")
    private Rate rate;
}
