package com.factory.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCompositionDTO {

    private Long id;
    private Long rawMaterialId;
    private String rawMaterialName;
    private Double quantityRequired;
}
