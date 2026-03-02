package com.factory.api.dto;

public record RawMaterialDTO(
        Long id,
        String code,
        String name,
        Double stockQuantity,
        String unit,
        String description
) {}