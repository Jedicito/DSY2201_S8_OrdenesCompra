package s8.dsy2201.ordenescompra;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Verifica que el contexto de Spring Boot levanta correctamente
 * con todas las dependencias (HATEOAS, JPA, etc.).
 *
 * Para ejecutarse necesita conexión a Oracle Cloud configurada
 * en application.properties (o un perfil de test con H2).
 */
@SpringBootTest
class OrdenesCompraApplicationTests {

    @Test
    void contextLoads() {
        // Si el contexto falla al arrancar, este test fallará automáticamente.
    }
}
