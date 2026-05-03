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

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias sobre {@link OrdenCompraService}.
 *
 * Se utiliza Mockito para aislar completamente el servicio
 * del repositorio; no se accede a Base de Datos.
 *
 * Anotaciones relevantes:
 *   @ExtendWith(MockitoExtension.class) – activa el soporte de Mockito en JUnit 5
 *   @Mock                              – crea un doble del repositorio
 *   @InjectMocks                       – inyecta los mocks en el servicio
 *   @BeforeEach                        – prepara datos comunes antes de cada test
 *   @Test                              – marca el método como caso de prueba
 */
@ExtendWith(MockitoExtension.class)
class OrdenCompraServiceTest {

    @Mock
    private OrdenCompraRepository repository;

    @InjectMocks
    private OrdenCompraService service;

    private OrdenCompra ordenExistente;

    @BeforeEach
    void setUp() {
        ordenExistente = new OrdenCompra(1, "Alimento para perro", 3, "Pendiente");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 1: obtenerTodas() debe retornar la lista mapeada a ResponseDTO
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("obtenerTodas() retorna la lista de órdenes mapeada correctamente")
    void testObtenerTodas_retornaListaMapeada() {
        // Arrange: el repositorio devuelve una lista con una orden
        when(repository.findAll()).thenReturn(List.of(ordenExistente));

        // Act
        List<OrdenCompraResponseDTO> resultado = service.obtenerTodas();

        // Assert
        assertNotNull(resultado, "El resultado no debe ser null");
        assertEquals(1, resultado.size(), "Debe haber exactamente 1 orden");
        assertEquals("Alimento para perro", resultado.get(0).getProducto());
        assertEquals(3,           resultado.get(0).getCantidad());
        assertEquals("Pendiente", resultado.get(0).getEstado());

        // Verificar que el repositorio fue invocado exactamente una vez
        verify(repository, times(1)).findAll();
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 2: crear() debe asignar "Pendiente" cuando estado llega null
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("crear() asigna estado 'Pendiente' cuando el DTO no envía estado")
    void testCrear_estadoPorDefectoCuandoEstadoEsNull() {
        // Arrange: DTO sin estado
        OrdenCompraRequestDTO dto = new OrdenCompraRequestDTO();
        dto.setProducto("Cama para gato");
        dto.setCantidad(2);
        dto.setEstado(null);   // <── sin estado explícito

        OrdenCompra ordenGuardada = new OrdenCompra(10, "Cama para gato", 2, "Pendiente");
        when(repository.save(any(OrdenCompra.class))).thenReturn(ordenGuardada);

        // Act
        OrdenCompraResponseDTO resultado = service.crear(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals("Pendiente", resultado.getEstado(),
                "El estado por defecto debe ser 'Pendiente'");
        assertEquals("Cama para gato", resultado.getProducto());
        assertEquals(2, resultado.getCantidad());

        // El repositorio debe haber recibido una entidad con estado "Pendiente"
        verify(repository, times(1)).save(argThat(o ->
                "Pendiente".equals(o.getEstado())
        ));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 3 (extra en service): obtenerPorId() retorna vacío cuando no existe
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("obtenerPorId() retorna Optional vacío para un ID inexistente")
    void testObtenerPorId_retornaVacioCuandoNoExiste() {
        // Arrange
        when(repository.findById(99)).thenReturn(Optional.empty());

        // Act
        Optional<OrdenCompraResponseDTO> resultado = service.obtenerPorId(99);

        // Assert
        assertTrue(resultado.isEmpty(), "Debe retornar Optional.empty() para un ID inexistente");
        verify(repository, times(1)).findById(99);
    }
}
