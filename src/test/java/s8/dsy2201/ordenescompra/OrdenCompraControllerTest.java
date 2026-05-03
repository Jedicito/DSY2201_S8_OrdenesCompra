package s8.dsy2201.ordenescompra;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import s8.dsy2201.ordenescompra.controlador.OrdenCompraController;
import s8.dsy2201.ordenescompra.dto.OrdenCompraRequestDTO;
import s8.dsy2201.ordenescompra.dto.OrdenCompraResponseDTO;
import s8.dsy2201.ordenescompra.servicio.OrdenCompraService;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Prueba de capa web sobre {@link OrdenCompraController}.
 *
 * @WebMvcTest levanta solo el contexto MVC (sin BD ni Spring Data),
 * por lo que el servicio se sustituye con un mock de Mockito.
 *
 * Anotaciones relevantes:
 *   @WebMvcTest   – contexto liviano: solo controladores, filtros y MVC
 *   @MockitoBean  – registra un mock del servicio en el contexto de Spring
 *   @BeforeEach   – prepara datos reutilizables antes de cada test
 *   @Test         – caso de prueba JUnit 5
 */
@WebMvcTest(OrdenCompraController.class)
class OrdenCompraControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrdenCompraService service;

    private OrdenCompraResponseDTO dto1;
    private OrdenCompraResponseDTO dto2;

    @BeforeEach
    void setUp() {
        dto1 = new OrdenCompraResponseDTO(1, "Alimento para perro", 3, "Pendiente");
        dto2 = new OrdenCompraResponseDTO(2, "Arena para gato",    1, "Aprobada");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 4: GET /api/ordenes → 200 OK con dos elementos en el JSON
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /api/ordenes retorna 200 OK y la colección en JSON HAL")
    void testGetOrdenes_retorna200ConColeccion() throws Exception {
        // Arrange
        when(service.obtenerTodas()).thenReturn(List.of(dto1, dto2));

        // Act + Assert
        mockMvc.perform(get("/api/ordenes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ordenCompraResponseDTOList").isArray())
                .andExpect(jsonPath("$._embedded.ordenCompraResponseDTOList[0].producto")
                        .value("Alimento para perro"))
                .andExpect(jsonPath("$._embedded.ordenCompraResponseDTOList[1].estado")
                        .value("Aprobada"))
                .andExpect(jsonPath("$._links.self").exists());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 5: GET /api/ordenes/{id} inexistente → 404 Not Found
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("GET /api/ordenes/{id} retorna 404 cuando la orden no existe")
    void testGetPorId_retorna404CuandoNoExiste() throws Exception {
        // Arrange
        when(service.obtenerPorId(99)).thenReturn(Optional.empty());

        // Act + Assert
        mockMvc.perform(get("/api/ordenes/99")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // TEST 6: POST /api/ordenes → 201 Created con los datos y los _links
    // ─────────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("POST /api/ordenes retorna 201 Created con HATEOAS links")
    void testPost_retorna201ConLinks() throws Exception {
        // Arrange
        OrdenCompraRequestDTO request = new OrdenCompraRequestDTO();
        request.setProducto("Juguete para conejo");
        request.setCantidad(5);

        OrdenCompraResponseDTO creada = new OrdenCompraResponseDTO(3, "Juguete para conejo", 5, "Pendiente");
        when(service.crear(any(OrdenCompraRequestDTO.class))).thenReturn(creada);

        // Act + Assert
        mockMvc.perform(post("/api/ordenes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.producto").value("Juguete para conejo"))
                .andExpect(jsonPath("$.estado").value("Pendiente"))
                .andExpect(jsonPath("$._links.self").exists())
                .andExpect(jsonPath("$._links.ordenes").exists())
                .andExpect(jsonPath("$._links.eliminar").exists());
    }
}
