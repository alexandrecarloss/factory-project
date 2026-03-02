package com.factory.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "raw_materials")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Code is required")
    @Column(nullable = false, unique = true)
    private String code;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @Min(value = 0, message = "Stock quantity must be greater than or equal to 0")
    @Column(nullable = false)
    private Double stockQuantity;

    @NotBlank(message = "Unit is required")
    @Column(nullable = false)
    private String unit;

    @Column(columnDefinition = "TEXT")
    private String description;
}
