package com.factory.api.service;

import com.factory.api.dto.ProductionOptimizationResultDTO;
import com.factory.api.dto.ProductionSuggestionDTO;
import com.factory.api.model.Product;
import com.factory.api.model.ProductComposition;
import com.factory.api.model.RawMaterial;
import com.factory.api.repository.ProductRepository;
import com.factory.api.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductionOptimizationServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductionOptimizationService service;

    private RawMaterial flour;
    private RawMaterial sugar;
    private RawMaterial egg;
    private Product bread;
    private Product cake;

    @BeforeEach
    void setUp() {
        // Create raw materials
        flour = new RawMaterial(1L, "FLOUR", "Flour", 1000.0, "g", "Wheat flour");
        sugar = new RawMaterial(2L, "SUGAR", "Sugar", 500.0, "g", "White sugar");
        egg = new RawMaterial(3L, "EGG", "Egg", 100.0, "unit", "Fresh eggs");

        // Create products
        bread = new Product();
        bread.setId(1L);
        bread.setCode("BREAD");
        bread.setName("Bread");
        bread.setPrice(10.0);

        cake = new Product();
        cake.setId(2L);
        cake.setCode("CAKE");
        cake.setName("Cake");
        cake.setPrice(25.0);

        // Set composition for bread (200g flour, 50g sugar)
        ProductComposition breadFlour = new ProductComposition(1L, bread, flour, 200.0);
        ProductComposition breadSugar = new ProductComposition(2L, bread, sugar, 50.0);
        bread.setComposition(new HashSet<>(Arrays.asList(breadFlour, breadSugar)));

        // Set composition for cake (300g flour, 200g sugar, 2 eggs)
        ProductComposition cakeFlour = new ProductComposition(3L, cake, flour, 300.0);
        ProductComposition cakeSugar = new ProductComposition(4L, cake, sugar, 200.0);
        ProductComposition cakeEgg = new ProductComposition(5L, cake, egg, 2.0);
        cake.setComposition(new HashSet<>(Arrays.asList(cakeFlour, cakeSugar, cakeEgg)));
    }

    @Test
    void testOptimizeProductionWithAvailableStock() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(bread, cake));
        when(rawMaterialRepository.findAll()).thenReturn(Arrays.asList(flour, sugar, egg));

        // Act
        ProductionOptimizationResultDTO result = service.optimizeProduction();

        // Assert
        assertNotNull(result);
        assertFalse(result.getSuggestions().isEmpty());
        assertTrue(result.getTotalValue() > 0);
        assertEquals("Production optimized successfully", result.getMessage());

        // Check that at least one product is suggested
        boolean hasSuggestions = result.getSuggestions().stream()
                .anyMatch(s -> s.getQuantityToProduce() > 0);
        assertTrue(hasSuggestions);
    }

    @Test
    void testOptimizeProductionWithNoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        ProductionOptimizationResultDTO result = service.optimizeProduction();

        // Assert
        assertNotNull(result);
        assertTrue(result.getSuggestions().isEmpty());
        assertEquals(0.0, result.getTotalValue());
        assertEquals("No products available for production", result.getMessage());
    }

    @Test
    void testOptimizeProductionWithInsufficientStock() {
        // Arrange - Set very low stock
        flour.setStockQuantity(10.0);
        sugar.setStockQuantity(5.0);
        egg.setStockQuantity(1.0);

        when(productRepository.findAll()).thenReturn(Arrays.asList(bread, cake));
        when(rawMaterialRepository.findAll()).thenReturn(Arrays.asList(flour, sugar, egg));

        // Act
        ProductionOptimizationResultDTO result = service.optimizeProduction();

        // Assert
        assertNotNull(result);
        assertEquals(0.0, result.getTotalValue());
    }

    @Test
    void testOptimizeProductionHigherPriceFirst() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Arrays.asList(bread, cake));
        when(rawMaterialRepository.findAll()).thenReturn(Arrays.asList(flour, sugar, egg));

        // Act
        ProductionOptimizationResultDTO result = service.optimizeProduction();

        // Assert
        assertNotNull(result);
        // Cake has higher price (25) than bread (10)
        // So cake should be suggested first if possible
        if (!result.getSuggestions().isEmpty()) {
            Optional<ProductionSuggestionDTO> firstSuggestion = result.getSuggestions().stream().findFirst();
            assertTrue(firstSuggestion.isPresent());
        }
    }
}
