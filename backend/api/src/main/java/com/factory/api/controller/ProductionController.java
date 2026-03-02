package com.factory.api.controller;

import com.factory.api.dto.ProductionOptimizationResultDTO;
import com.factory.api.service.ProductionOptimizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/production")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProductionController {

    private final ProductionOptimizationService service;

    @GetMapping("/optimize")
    public ResponseEntity<ProductionOptimizationResultDTO> optimizeProduction() {
        return ResponseEntity.ok(service.optimizeProduction());
    }
}
