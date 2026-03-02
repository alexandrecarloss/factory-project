package com.factory.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    private Long id;
    private String code;
    private String name;
    private Double price;
    private String description;
    private Set<ProductCompositionDTO> composition;
}
