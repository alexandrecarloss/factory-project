package com.factory.api.service;

import com.factory.api.dto.ProductCompositionDTO;
import com.factory.api.dto.ProductDTO;
import com.factory.api.model.Product;
import com.factory.api.model.ProductComposition;
import com.factory.api.model.RawMaterial;
import com.factory.api.repository.ProductRepository;
import com.factory.api.repository.ProductCompositionRepository;
import com.factory.api.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductCompositionRepository compositionRepository;

    @Mock
    private RawMaterialRepository rawMaterialRepository;

    @InjectMocks
    private ProductService service;

    private Product product;
    private ProductDTO productDTO;
    private RawMaterial rawMaterial;

    @BeforeEach
    void setUp() {
        rawMaterial = new RawMaterial(1L, "FLOUR", "Flour", 1000.0, "g", "Wheat flour");
        
        product = new Product();
        product.setId(1L);
        product.setCode("BREAD");
        product.setName("Bread");
        product.setPrice(10.0);
        product.setDescription("White bread");
        product.setComposition(new HashSet<>());

        ProductComposition composition = new ProductComposition(1L, product, rawMaterial, 200.0);
        product.getComposition().add(composition);

        ProductCompositionDTO compositionDTO = new ProductCompositionDTO(1L, 1L, "Flour", 200.0);
        Set<ProductCompositionDTO> compositionSet = new HashSet<>();
        compositionSet.add(compositionDTO);

        productDTO = new ProductDTO(1L, "BREAD", "Bread", 10.0, "White bread", compositionSet);
    }

    @Test
    void testSaveProduct() {
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(compositionRepository.save(any(ProductComposition.class))).thenReturn(new ProductComposition());

        ProductDTO result = service.save(productDTO);

        assertNotNull(result);
        assertEquals("BREAD", result.getCode());
        assertEquals("Bread", result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testFindAllProducts() {
        when(productRepository.findAll()).thenReturn(Arrays.asList(product));

        List<ProductDTO> results = service.findAll();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("BREAD", results.get(0).getCode());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals("BREAD", result.getCode());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(999L));
        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(rawMaterialRepository.findById(1L)).thenReturn(Optional.of(rawMaterial));
        when(productRepository.save(any(Product.class))).thenReturn(product);
        when(compositionRepository.save(any(ProductComposition.class))).thenReturn(new ProductComposition());

        ProductDTO updateDTO = new ProductDTO(1L, "BREAD2", "Bread Type 2", 15.0, "Updated", productDTO.getComposition());
        ProductDTO result = service.update(1L, updateDTO);

        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(productRepository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.delete(999L));
        verify(productRepository, never()).deleteById(999L);
    }
}
