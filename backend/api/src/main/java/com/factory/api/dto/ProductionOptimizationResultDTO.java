package com.factory.api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductionOptimizationResultDTO {

    private List<ProductionSuggestionDTO> suggestions;
    private Double totalValue;
    private String message;
}
