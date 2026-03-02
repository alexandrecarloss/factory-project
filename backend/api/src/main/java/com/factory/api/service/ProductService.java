package com.factory.api.service;

import com.factory.api.dto.ProductCompositionDTO;
import com.factory.api.dto.ProductDTO;
import com.factory.api.model.Product;
import com.factory.api.model.ProductComposition;
import com.factory.api.model.RawMaterial;
import com.factory.api.repository.ProductRepository;
import com.factory.api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public ProductDTO save(ProductDTO dto) {
        Product product = new Product();
        updateProductFields(product, dto);
        Product saved = productRepository.save(product);
        return convertToDTO(saved);
    }

    public ProductDTO update(Long id, ProductDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.getComposition().clear();
        updateProductFields(product, dto);

        Product updated = productRepository.save(product);
        return convertToDTO(updated);
    }

    private void updateProductFields(Product product, ProductDTO dto) {
        product.setCode(dto.getCode());
        product.setName(dto.getName());
        product.setPrice(dto.getPrice());
        product.setDescription(dto.getDescription());

        if (dto.getComposition() != null) {
            for (ProductCompositionDTO compDTO : dto.getComposition()) {
                RawMaterial material = rawMaterialRepository.findById(compDTO.getRawMaterialId())
                        .orElseThrow(() -> new IllegalArgumentException("Material " + compDTO.getRawMaterialId() + " not found"));

                ProductComposition comp = new ProductComposition();
                comp.setProduct(product);
                comp.setRawMaterial(material);
                comp.setQuantityRequired(compDTO.getQuantityRequired());

                product.getComposition().add(comp);
            }
        }
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
                .map(this::convertToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        productRepository.deleteById(id);
    }

    private ProductDTO convertToDTO(Product product) {
        Set<ProductCompositionDTO> compDTOs = new HashSet<>();

        if (product.getComposition() != null) {
            compDTOs = product.getComposition().stream()
                    .map(comp -> new ProductCompositionDTO(
                            comp.getId(),
                            comp.getRawMaterial().getId(),
                            comp.getRawMaterial().getName(),
                            comp.getQuantityRequired()
                    ))
                    .collect(Collectors.toSet());
        }

        return new ProductDTO(
                product.getId(),
                product.getCode(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                compDTOs
        );
    }
}