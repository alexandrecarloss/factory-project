package com.factory.api.service;

import com.factory.api.dto.RawMaterialDTO;
import com.factory.api.model.RawMaterial;
import com.factory.api.repository.RawMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RawMaterialService {

    private final RawMaterialRepository repository;

    public RawMaterialDTO save(RawMaterialDTO dto) {
        RawMaterial material = new RawMaterial();
        material.setCode(dto.code());
        material.setName(dto.name());
        material.setStockQuantity(dto.stockQuantity());
        material.setUnit(dto.unit());
        material.setDescription(dto.description());

        RawMaterial saved = repository.save(material);
        return convertToDTO(saved);
    }

    public RawMaterialDTO update(Long id, RawMaterialDTO dto) {
        RawMaterial material = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Raw material not found"));

        material.setCode(dto.code());
        material.setName(dto.name());
        material.setStockQuantity(dto.stockQuantity());
        material.setUnit(dto.unit());
        material.setDescription(dto.description());

        RawMaterial updated = repository.save(material);
        return convertToDTO(updated);
    }

    public RawMaterialDTO findById(Long id) {
        RawMaterial material = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Raw material not found"));
        return convertToDTO(material);
    }

    public List<RawMaterialDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Raw material not found");
        }
        repository.deleteById(id);
    }

    private RawMaterialDTO convertToDTO(RawMaterial material) {
        return new RawMaterialDTO(
                material.getId(),
                material.getCode(),
                material.getName(),
                material.getStockQuantity(),
                material.getUnit(),
                material.getDescription()
        );
    }
}
