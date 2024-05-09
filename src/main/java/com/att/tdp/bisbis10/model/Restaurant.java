package com.att.tdp.bisbis10.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Transient
    private Double averageRating;
    @Column(name = "is_kosher",nullable = false)
    private boolean isKosher;

    @ElementCollection
    @Column(name = "cuisine")
    private List<String> cuisines;

    @Transient
    private List<Dish> dishes;
}
