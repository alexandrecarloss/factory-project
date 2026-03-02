package com.factory.api.dto;

import java.util.Set;

public record ProductDTO(
        Long id,
        String code,
        String name,
        Double price,
        String description,
        Set<ProductCompositionDTO> composition
) {}