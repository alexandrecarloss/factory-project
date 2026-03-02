package com.factory.api.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "product_composition")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductComposition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Product product;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "raw_material_id", nullable = false)
    @NotNull(message = "Raw material is required")
    private RawMaterial rawMaterial;

    @Min(value = 0, message = "Quantity required must be greater than 0")
    @Column(nullable = false)
    private Double quantityRequired;
}
