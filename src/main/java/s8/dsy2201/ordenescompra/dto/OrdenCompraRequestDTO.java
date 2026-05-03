package s8.dsy2201.ordenescompra.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdenCompraRequestDTO {

    @NotBlank(message = "El producto no puede estar vacío")
    private String producto;

    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private int cantidad;

    /** Si llega null o en blanco, el servicio asigna "Pendiente". */
    private String estado;
}
