package com.factory.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionSuggestionDTO {

    private Long productId;
    private String productCode;
    private String productName;
    private Double price;
    private Integer quantityToProduce;
    private Double totalValue;
}
