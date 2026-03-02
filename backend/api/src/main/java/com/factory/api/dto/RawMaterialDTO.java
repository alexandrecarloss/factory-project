package com.factory.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RawMaterialDTO {

    private Long id;
    private String code;
    private String name;
    private Double stockQuantity;
    private String unit;
    private String description;
}
