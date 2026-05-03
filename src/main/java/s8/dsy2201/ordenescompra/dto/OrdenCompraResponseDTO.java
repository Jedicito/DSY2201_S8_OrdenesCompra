package s8.dsy2201.ordenescompra.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

/**
 * DTO de respuesta enriquecido con enlaces HATEOAS.
 * Al extender {@link RepresentationModel}, Jackson serializa el campo
 * "_links" de manera automática junto con los datos de la orden.
 */
@Getter
@Setter
@NoArgsConstructor
public class OrdenCompraResponseDTO extends RepresentationModel<OrdenCompraResponseDTO> {

    private int    id;
    private String producto;
    private int    cantidad;
    private String estado;

    public OrdenCompraResponseDTO(int id, String producto, int cantidad, String estado) {
        this.id       = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.estado   = estado;
    }
}
