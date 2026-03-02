package com.factory.api.dto;

import java.util.List;

public record ProductionOptimizationResultDTO(
        List<ProductionSuggestionDTO> suggestions,
        Double totalValue,
        String message
) {}