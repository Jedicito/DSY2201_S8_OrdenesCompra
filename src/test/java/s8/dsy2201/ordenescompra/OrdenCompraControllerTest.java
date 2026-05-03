package s8.dsy2201.ordenescompra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import s8.dsy2201.ordenescompra.dto.OrdenCompraRequestDTO;
import s8.dsy2201.ordenescompra.dto.OrdenCompraResponseDTO;
import s8.dsy2201.ordenescompra.modelo.OrdenCompra;
import s8.dsy2201.ordenescompra.repositorio.OrdenCompraRepository;
import s8.dsy2201.ordenescompra.servicio.OrdenCompraService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdenCompraControllerTest {

    @Mock
    private OrdenCompraRepository repository;

    @InjectMocks
    private OrdenCompraService service;

    private OrdenCompra ordenExistente;

    @BeforeEach
    void setUp() {
        ordenExistente = new OrdenCompra(1, "Alimento para perro", 3, "Pendiente");
    }

    @Test
    @DisplayName("actualizar() retorna el DTO con los nuevos valores cuando el ID existe")
    void testActualizar_retornaDtoActualizadoCuandoIdExiste() {
        OrdenCompraRequestDTO dto = new OrdenCompraRequestDTO();
        dto.setProducto("Collar antipulgas");
        dto.setCantidad(10);
        dto.setEstado("Aprobada");

        OrdenCompra ordenActualizada = new OrdenCompra(1, "Collar antipulgas", 10, "Aprobada");

        when(repository.findById(1)).thenReturn(Optional.of(ordenExistente));
        when(repository.save(any(OrdenCompra.class))).thenReturn(ordenActualizada);

        Optional<OrdenCompraResponseDTO> resultado = service.actualizar(1, dto);

        assertTrue(resultado.isPresent());
        assertEquals("Collar antipulgas", resultado.get().getProducto());
        assertEquals(10,         resultado.get().getCantidad());
        assertEquals("Aprobada", resultado.get().getEstado());

        verify(repository, times(1)).findById(1);
        verify(repository, times(1)).save(any(OrdenCompra.class));
    }

    @Test
    @DisplayName("eliminar() retorna false cuando el ID no existe en el repositorio")
    void testEliminar_retornaFalseCuandoIdNoExiste() {
        when(repository.existsById(99)).thenReturn(false);

        boolean resultado = service.eliminar(99);

        assertFalse(resultado);
        verify(repository, never()).deleteById(any());
    }
}