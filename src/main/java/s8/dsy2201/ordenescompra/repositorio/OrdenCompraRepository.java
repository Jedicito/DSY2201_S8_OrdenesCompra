package s8.dsy2201.ordenescompra.repositorio;

import s8.dsy2201.ordenescompra.modelo.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {

    List<OrdenCompra> findByEstado(String estado);

    List<OrdenCompra> findByProductoContainingIgnoreCase(String producto);
}
