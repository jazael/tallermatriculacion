
import com.ingenieria.managementdb.ConnectionDriver;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Usuario
 */
public class ApplicationJUnitTest {

    public ApplicationJUnitTest() {
    }

    @BeforeAll
    public static void setUpClass() {
    }

    @AfterAll
    public static void tearDownClass() {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testConexion() {
        Connection connection;

        connection = ConnectionDriver.getConnection();

        assertNotNull(connection, "La conexión debe ser exitosa");

        if (connection != null) {
            try {
                assertFalse(connection.isClosed(), "La conexión no debe estar cerrada");
            } catch (SQLException e) {
                fail("Error al verificar la conexión: " + e.getMessage());
            }
        } else {
            fail("No se pudo establecer la conexión.");
        }

    }

}
