package s8.dsy2201.ordenescompra.controlador;

import jakarta.validation.Valid;
import s8.dsy2201.ordenescompra.dto.OrdenCompraRequestDTO;
import s8.dsy2201.ordenescompra.dto.OrdenCompraResponseDTO;
import s8.dsy2201.ordenescompra.servicio.OrdenCompraService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

/**
 * Controlador REST para Órdenes de Compra.
 *
 * Todos los endpoints devuelven enlaces HATEOAS:
 *  - "self"       → URL del propio recurso
 *  - "ordenes"    → URL de la colección completa
 *  - "actualizar" → URL para hacer PUT sobre el recurso
 *  - "eliminar"   → URL para hacer DELETE sobre el recurso
 */
@RestController
@RequestMapping("/api/ordenes")
public class OrdenCompraController {

    @Autowired
    private OrdenCompraService service;

    // ─────────────────────────────────────────────────────────────────────────
    // Método auxiliar: agrega los cuatro enlaces estándar a un DTO individual
    // ─────────────────────────────────────────────────────────────────────────
    private OrdenCompraResponseDTO agregarLinks(OrdenCompraResponseDTO dto) {
        int id = dto.getId();

        Link selfLink = linkTo(methodOn(OrdenCompraController.class)
                .obtenerPorId(id)).withSelfRel();

        Link coleccionLink = linkTo(methodOn(OrdenCompraController.class)
                .obtenerTodas()).withRel("ordenes");

        Link actualizarLink = linkTo(methodOn(OrdenCompraController.class)
                .actualizar(id, null)).withRel("actualizar");

        Link eliminarLink = linkTo(methodOn(OrdenCompraController.class)
                .eliminar(id)).withRel("eliminar");

        dto.add(selfLink, coleccionLink, actualizarLink, eliminarLink);
        return dto;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/ordenes  →  200 OK
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<CollectionModel<OrdenCompraResponseDTO>> obtenerTodas() {
        List<OrdenCompraResponseDTO> lista = service.obtenerTodas()
                .stream()
                .map(this::agregarLinks)
                .toList();

        Link selfLink = linkTo(methodOn(OrdenCompraController.class)
                .obtenerTodas()).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(lista, selfLink));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/ordenes/{id}  →  200 OK | 404 Not Found
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> obtenerPorId(@PathVariable int id) {
        return service.obtenerPorId(id)
                .map(this::agregarLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/ordenes/estado/{estado}  →  200 OK
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/estado/{estado}")
    public ResponseEntity<CollectionModel<OrdenCompraResponseDTO>> obtenerPorEstado(
            @PathVariable String estado) {

        List<OrdenCompraResponseDTO> lista = service.obtenerPorEstado(estado)
                .stream()
                .map(this::agregarLinks)
                .toList();

        Link selfLink = linkTo(methodOn(OrdenCompraController.class)
                .obtenerPorEstado(estado)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(lista, selfLink));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /api/ordenes/buscar/{producto}  →  200 OK
    // ─────────────────────────────────────────────────────────────────────────
    @GetMapping("/buscar/{producto}")
    public ResponseEntity<CollectionModel<OrdenCompraResponseDTO>> buscarPorProducto(
            @PathVariable String producto) {

        List<OrdenCompraResponseDTO> lista = service.buscarPorProducto(producto)
                .stream()
                .map(this::agregarLinks)
                .toList();

        Link selfLink = linkTo(methodOn(OrdenCompraController.class)
                .buscarPorProducto(producto)).withSelfRel();

        return ResponseEntity.ok(CollectionModel.of(lista, selfLink));
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /api/ordenes  →  201 Created
    // ─────────────────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<OrdenCompraResponseDTO> crear(
            @Valid @RequestBody OrdenCompraRequestDTO dto) {

        OrdenCompraResponseDTO creada = agregarLinks(service.crear(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUT /api/ordenes/{id}  →  200 OK | 404 Not Found
    // ─────────────────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<OrdenCompraResponseDTO> actualizar(
            @PathVariable int id,
            @RequestBody OrdenCompraRequestDTO dto) {

        return service.actualizar(id, dto)
                .map(this::agregarLinks)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // DELETE /api/ordenes/{id}  →  204 No Content | 404 Not Found
    // ─────────────────────────────────────────────────────────────────────────
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (service.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
