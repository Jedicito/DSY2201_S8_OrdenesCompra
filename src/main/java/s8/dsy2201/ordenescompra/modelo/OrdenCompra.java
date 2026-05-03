package s8.dsy2201.ordenescompra.modelo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ORDENES_COMPRA")
@Getter
@Setter
@NoArgsConstructor
public class OrdenCompra {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_oc")
    @SequenceGenerator(name = "seq_oc", sequenceName = "SEQ_ORDENES_COMPRA", allocationSize = 1)
    @Column(name = "ID_ORDEN")
    private int id;

    @Column(name = "PRODUCTO", nullable = false, length = 150)
    private String producto;

    @Column(name = "CANTIDAD", nullable = false)
    private int cantidad;

    @Column(name = "ESTADO", nullable = false, length = 30)
    private String estado;

    public OrdenCompra(int id, String producto, int cantidad, String estado) {
        this.id       = id;
        this.producto = producto;
        this.cantidad = cantidad;
        this.estado   = estado;
    }

    public OrdenCompra(String producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.estado   = "Pendiente";
    }
}
