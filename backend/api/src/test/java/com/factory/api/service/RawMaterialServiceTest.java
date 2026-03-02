package com.factory.api.service;

import com.factory.api.dto.RawMaterialDTO;
import com.factory.api.model.RawMaterial;
import com.factory.api.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RawMaterialServiceTest {

    @Mock
    private RawMaterialRepository repository;

    @InjectMocks
    private RawMaterialService service;

    private RawMaterial material;
    private RawMaterialDTO materialDTO;

    @BeforeEach
    void setUp() {
        material = new RawMaterial(1L, "FLOUR", "Flour", 1000.0, "g", "Wheat flour");
        materialDTO = new RawMaterialDTO(1L, "FLOUR", "Flour", 1000.0, "g", "Wheat flour");
    }

    @Test
    void testSaveRawMaterial() {
        when(repository.save(any(RawMaterial.class))).thenReturn(material);

        RawMaterialDTO result = service.save(materialDTO);

        assertNotNull(result);
        assertEquals("FLOUR", result.getCode());
        assertEquals("Flour", result.getName());
        verify(repository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testFindAllRawMaterials() {
        when(repository.findAll()).thenReturn(Arrays.asList(material));

        List<RawMaterialDTO> results = service.findAll();

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("FLOUR", results.get(0).getCode());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testFindByIdRawMaterial() {
        when(repository.findById(1L)).thenReturn(Optional.of(material));

        RawMaterialDTO result = service.findById(1L);

        assertNotNull(result);
        assertEquals("FLOUR", result.getCode());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.findById(999L));
        verify(repository, times(1)).findById(999L);
    }

    @Test
    void testUpdateRawMaterial() {
        when(repository.findById(1L)).thenReturn(Optional.of(material));
        when(repository.save(any(RawMaterial.class))).thenReturn(material);

        RawMaterialDTO updateDTO = new RawMaterialDTO(1L, "FLOUR2", "Flour Type 2", 2000.0, "g", "Updated");
        RawMaterialDTO result = service.update(1L, updateDTO);

        assertNotNull(result);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(RawMaterial.class));
    }

    @Test
    void testDeleteRawMaterial() {
        when(repository.existsById(1L)).thenReturn(true);

        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        when(repository.existsById(999L)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> service.delete(999L));
        verify(repository, never()).deleteById(999L);
    }
}
