import com.ingenieria.controllers.AlumnoController;
import com.ingenieria.models.Alumno;
import com.ingenieria.services.AlumnoDAO;
import com.ingenieria.utilsmanager.JOptionPaneUtil;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductoControllerTest {
    
    private AlumnoController productoController;
    private AlumnoDAO mockProductoDAO;
    
    @BeforeEach
    public void setUp() {
        mockProductoDAO = mock(AlumnoDAO.class);
        productoController = new AlumnoController() {
            public AlumnoDAO getProductoDAO() {
                return mockProductoDAO;
            }
        };
    }

    @Test
    public void testInsertarProductoSuccess() {
        JTextField txtnombre = new JTextField("Producto 1");
        JTextField txtdescripcion = new JTextField("Descripcion 1");
        JTextField txtprecio = new JTextField("100");
        JTextField txtstock = new JTextField("10");
        JComboBox<String> cbounidadmedida = new JComboBox<>(new String[]{"kg", "g", "l"});
        cbounidadmedida.setSelectedItem("kg");

        when(mockProductoDAO.insertarProducto(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(new Alumno());

        productoController.insertarProducto(txtnombre, txtdescripcion, txtprecio, txtstock, cbounidadmedida);

        assertEquals("", txtnombre.getText());
        assertEquals("", txtdescripcion.getText());
        assertEquals("", txtprecio.getText());
        assertEquals("", txtstock.getText());
    }

    @Test
    public void testInsertarProductoEmptyFields() {
        JTextField txtnombre = new JTextField("");
        JTextField txtdescripcion = new JTextField("");
        JTextField txtprecio = new JTextField("");
        JTextField txtstock = new JTextField("");
        JComboBox<String> cbounidadmedida = new JComboBox<>(new String[]{"kg", "g", "l"});

        productoController.insertarProducto(txtnombre, txtdescripcion, txtprecio, txtstock, cbounidadmedida);
    }

    @Test
    public void testInsertarProductoError() {
        JTextField txtnombre = new JTextField("Producto 2");
        JTextField txtdescripcion = new JTextField("Descripcion 2");
        JTextField txtprecio = new JTextField("200");
        JTextField txtstock = new JTextField("20");
        JComboBox<String> cbounidadmedida = new JComboBox<>(new String[]{"kg", "g", "l"});
        cbounidadmedida.setSelectedItem("kg");

        when(mockProductoDAO.insertarProducto(anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenThrow(new RuntimeException("Error de base de datos"));

        productoController.insertarProducto(txtnombre, txtdescripcion, txtprecio, txtstock, cbounidadmedida);
    }
}
