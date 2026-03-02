package com.factory.api.dto;

public record ProductCompositionDTO(
        Long id,
        Long rawMaterialId,
        String rawMaterialName,
        Double quantityRequired
) {}