package com.factory.api.dto;

public record ProductionSuggestionDTO(
        Long productId,
        String productCode,
        String productName,
        Double price,
        Integer quantityToProduce,
        Double totalValue
) {}