package com.Quintero.Service_Producto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Quintero.Service_Producto.dto.ProductoDTO;
import com.Quintero.Service_Producto.model.Marca;
import com.Quintero.Service_Producto.model.Producto;
import com.Quintero.Service_Producto.repository.MarcaRepository;
import com.Quintero.Service_Producto.repository.ProductoRepository;
import com.Quintero.Service_Producto.service.ProductoService;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {
    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private MarcaRepository marcaRepository;

    @InjectMocks
    private ProductoService productoService;

    private ProductoDTO buildDefaultDTO() {
        ProductoDTO dto = new ProductoDTO();
        dto.setSku("SKU123");
        dto.setNombre("Producto prueba");
        dto.setDescripcion("Descripción de prueba");
        dto.setPrecio(100.0);
        dto.setMarcaId(1L);
        return dto;
    }

    @Test
    @DisplayName("Debe lanzar ConflictException cuando el SKU ya existe en base de datos")
    void crearProducto_WhenSkuExists_ThrowsConflictException() {
        // Arrange
        ProductoDTO dto = buildDefaultDTO();
        when(productoRepository.existsBySku(dto.getSku())).thenReturn(true);

        // Act & Assert
        Throwable thrown = assertThrows(Throwable.class, () -> productoService.crearProducto(dto));
        assertEquals("ConflictException", thrown.getClass().getSimpleName());
        verify(productoRepository, never()).save(any(Producto.class)); // Verificación de comportamiento
    }

    @Test
    @DisplayName("Debe persistir producto exitosamente cuando los datos son válidos")
    void crearProducto_WhenDataIsValid_ReturnsSavedProductoDTO() {
        // Arrange
        ProductoDTO dto = buildDefaultDTO();
        Marca marca = new Marca();
        marca.setId(1L);
        marca.setNombre("Xiaomi");

        when(productoRepository.existsBySku(anyString())).thenReturn(false);
        when(marcaRepository.findById(dto.getMarcaId())).thenReturn(Optional.of(marca));
        when(productoRepository.save(any(Producto.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        ProductoDTO result = productoService.crearProducto(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getSku(), result.getSku());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }
}
