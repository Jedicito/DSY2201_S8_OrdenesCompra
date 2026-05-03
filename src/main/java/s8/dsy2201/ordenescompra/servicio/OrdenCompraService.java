package s8.dsy2201.ordenescompra.servicio;

import s8.dsy2201.ordenescompra.dto.OrdenCompraRequestDTO;
import s8.dsy2201.ordenescompra.dto.OrdenCompraResponseDTO;
import s8.dsy2201.ordenescompra.modelo.OrdenCompra;
import s8.dsy2201.ordenescompra.repositorio.OrdenCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrdenCompraService {

    @Autowired
    private OrdenCompraRepository repository;

    // ── Conversión entidad → ResponseDTO ──────────────────────────────────────
    public OrdenCompraResponseDTO convertirAResponse(OrdenCompra o) {
        return new OrdenCompraResponseDTO(o.getId(), o.getProducto(), o.getCantidad(), o.getEstado());
    }

    // ── GET todos ─────────────────────────────────────────────────────────────
    public List<OrdenCompraResponseDTO> obtenerTodas() {
        return repository.findAll()
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // ── GET por id ────────────────────────────────────────────────────────────
    public Optional<OrdenCompraResponseDTO> obtenerPorId(int id) {
        return repository.findById(id).map(this::convertirAResponse);
    }

    // ── GET por estado ────────────────────────────────────────────────────────
    public List<OrdenCompraResponseDTO> obtenerPorEstado(String estado) {
        return repository.findByEstado(estado)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // ── GET buscar por producto ───────────────────────────────────────────────
    public List<OrdenCompraResponseDTO> buscarPorProducto(String producto) {
        return repository.findByProductoContainingIgnoreCase(producto)
                .stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

    // ── POST ──────────────────────────────────────────────────────────────────
    public OrdenCompraResponseDTO crear(OrdenCompraRequestDTO dto) {
        OrdenCompra nueva = new OrdenCompra();
        nueva.setProducto(dto.getProducto());
        nueva.setCantidad(dto.getCantidad());
        nueva.setEstado(
            (dto.getEstado() == null || dto.getEstado().isBlank()) ? "Pendiente" : dto.getEstado()
        );
        return convertirAResponse(repository.save(nueva));
    }

    // ── PUT ───────────────────────────────────────────────────────────────────
    public Optional<OrdenCompraResponseDTO> actualizar(int id, OrdenCompraRequestDTO dto) {
        return repository.findById(id).map(o -> {
            o.setProducto(dto.getProducto());
            o.setCantidad(dto.getCantidad());
            o.setEstado(dto.getEstado());
            return convertirAResponse(repository.save(o));
        });
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    public boolean eliminar(int id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
