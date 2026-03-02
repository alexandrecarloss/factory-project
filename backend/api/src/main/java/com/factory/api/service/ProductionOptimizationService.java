package com.factory.api.service;

import com.factory.api.dto.ProductionOptimizationResultDTO;
import com.factory.api.dto.ProductionSuggestionDTO;
import com.factory.api.model.Product;
import com.factory.api.model.ProductComposition;
import com.factory.api.repository.ProductRepository;
import com.factory.api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductionOptimizationService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductionOptimizationResultDTO optimizeProduction() {
        List<Product> products = productRepository.findAll();
        Map<Long, Double> currentStock = getCurrentStock();

        if (products.isEmpty()) {
            return new ProductionOptimizationResultDTO(
                    new ArrayList<>(),
                    0.0,
                    "No products available for production"
            );
        }

        // Calculate production capacity for each product
        List<ProductCapacity> capacities = new ArrayList<>();
        for (Product product : products) {
            int maxUnitsCanProduce = calculateMaxUnitsCanProduce(product, currentStock);
            capacities.add(new ProductCapacity(
                    product,
                    maxUnitsCanProduce,
                    product.getPrice() * maxUnitsCanProduce
            ));
        }

        // Sort by total value in descending order
        capacities.sort((a, b) -> Double.compare(b.getTotalValue(), a.getTotalValue()));

        // Greedy algorithm to select products
        List<ProductionSuggestionDTO> suggestions = new ArrayList<>();
        double totalValue = 0.0;
        Map<Long, Double> stockUsed = new HashMap<>(currentStock);

        for (ProductCapacity capacity : capacities) {
            if (capacity.getMaxUnits() > 0) {
                // Try to produce as many units as possible
                int unitsToProducce = canProduceUnits(capacity.getProduct(), stockUsed, capacity.getMaxUnits());

                if (unitsToProducce > 0) {
                    // Update stock
                    consumeRawMaterials(capacity.getProduct(), stockUsed, unitsToProducce);

                    // Add suggestion
                    ProductionSuggestionDTO suggestion = new ProductionSuggestionDTO(
                            capacity.getProduct().getId(),
                            capacity.getProduct().getCode(),
                            capacity.getProduct().getName(),
                            capacity.getProduct().getPrice(),
                            unitsToProducce,
                            capacity.getProduct().getPrice() * unitsToProducce
                    );
                    suggestions.add(suggestion);
                    totalValue += suggestion.totalValue();
                }
            }
        }

        String message = suggestions.isEmpty() ? "No production possible with current stock" : 
                        "Production optimized successfully";

        return new ProductionOptimizationResultDTO(suggestions, totalValue, message);
    }

    private Map<Long, Double> getCurrentStock() {
        Map<Long, Double> stock = new HashMap<>();
        rawMaterialRepository.findAll().forEach(material ->
                stock.put(material.getId(), material.getStockQuantity())
        );
        return stock;
    }

    private int calculateMaxUnitsCanProduce(Product product, Map<Long, Double> stock) {
        int maxUnits = Integer.MAX_VALUE;

        for (ProductComposition composition : product.getComposition()) {
            Double stockQuantity = stock.getOrDefault(composition.getRawMaterial().getId(), 0.0);
            int unitsFromThisMaterial = (int) (stockQuantity / composition.getQuantityRequired());
            maxUnits = Math.min(maxUnits, unitsFromThisMaterial);
        }

        return maxUnits == Integer.MAX_VALUE ? 0 : maxUnits;
    }

    private int canProduceUnits(Product product, Map<Long, Double> stock, int maxUnits) {
        for (int units = maxUnits; units >= 1; units--) {
            boolean canProduce = true;

            for (ProductComposition composition : product.getComposition()) {
                Double required = composition.getQuantityRequired() * units;
                Double available = stock.getOrDefault(composition.getRawMaterial().getId(), 0.0);

                if (available < required) {
                    canProduce = false;
                    break;
                }
            }

            if (canProduce) {
                return units;
            }
        }

        return 0;
    }

    private void consumeRawMaterials(Product product, Map<Long, Double> stock, int units) {
        for (ProductComposition composition : product.getComposition()) {
            Long materialId = composition.getRawMaterial().getId();
            Double consumed = composition.getQuantityRequired() * units;
            stock.put(materialId, stock.getOrDefault(materialId, 0.0) - consumed);
        }
    }

    @lombok.Data
    @lombok.AllArgsConstructor
    private static class ProductCapacity {
        private Product product;
        private int maxUnits;
        private double totalValue;
    }
}
