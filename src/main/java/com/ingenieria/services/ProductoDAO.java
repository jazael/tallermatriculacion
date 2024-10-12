/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.services;

import static com.ingenieria.managementdb.ConnectionDriver.closeResultSet;
import static com.ingenieria.managementdb.ConnectionDriver.closeStatement;
import static com.ingenieria.managementdb.ConnectionDriver.getConnection;
import com.ingenieria.managementdb.DatabaseConnectException;
import com.ingenieria.models.DTOProductoJComboBox;
import com.ingenieria.models.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author miguelfaubla
 */
public class ProductoDAO {

    private final Connection connection;

    public ProductoDAO() {
        this.connection = getConnection();
    }

    public Producto insertarProducto(String nombre, String descripcion, String precio, String stock, String unidadMedida) {
        Producto producto = null;
        String query = "INSERT INTO producto (nombre, descripcion, precio, stock, unidadmedida) VALUES (?, ?, ?, ?, ?) RETURNING *";

        try {

            PreparedStatement stmt = this.connection.prepareStatement(query);

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, Double.parseDouble(precio));
            stmt.setDouble(4, Double.parseDouble(stock));
            stmt.setString(5, unidadMedida);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                producto = new Producto();
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setStock(rs.getDouble("stock"));
                producto.setUnidadmedida(rs.getString("unidadmedida"));
            }

            closeStatement(stmt);
            closeResultSet(rs);

        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return producto;
    }

    public boolean actualizarStock(Integer id, Double stockIngresado, boolean egreso) {
        boolean isUpdated = false;
        String selectQuery = "SELECT stock FROM producto WHERE id = ?";
        String updateQuery = "UPDATE producto SET stock = ? WHERE id = ?";

        try {
            PreparedStatement selectStmt = this.connection.prepareStatement(selectQuery);
            selectStmt.setInt(1, id);

            ResultSet resultSet = selectStmt.executeQuery();
            double stockActual = 0.0;

            if (resultSet.next()) {
                stockActual = resultSet.getDouble("stock");
            }

            double nuevoTotalStock = egreso ? (stockActual - stockIngresado) : (stockActual + stockIngresado);

            PreparedStatement updateStmt = this.connection.prepareStatement(updateQuery);
            updateStmt.setDouble(1, nuevoTotalStock);
            updateStmt.setInt(2, id);

            int rowsAffected = updateStmt.executeUpdate();
            if (rowsAffected > 0) {
                isUpdated = true;
            }

            closeStatement(selectStmt);
            closeStatement(updateStmt);
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return isUpdated;
    }

    public boolean editarProducto(Integer id, String nombre, String descripcion, String precio, String stock, String unidadMedida) {
        boolean isUpdated = false;
        String query = "UPDATE producto SET nombre = ?, descripcion = ?, precio = ?, stock = ?, unidadmedida = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, Double.parseDouble(precio));
            stmt.setDouble(4, Double.parseDouble(stock));
            stmt.setString(5, unidadMedida);
            stmt.setInt(6, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                isUpdated = true;
            }

            closeStatement(stmt);
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return isUpdated;
    }

    public boolean eliminarProducto(Integer id) {
        boolean isDeleted = false;
        String query = "UPDATE producto SET estado = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);

            stmt.setBoolean(1, false);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                isDeleted = true;
            }

            closeStatement(stmt);
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return isDeleted;
    }

    public DefaultTableModel listarProductos(DefaultTableModel modeloProducto) {
        String query = "SELECT id, nombre, descripcion, precio, stock, unidadmedida FROM producto WHERE estado=true order by id";
        String[] datos = new String[6];

        try {
            Statement stmt = this.connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                datos[0] = rs.getString(1);
                datos[1] = rs.getString(2);
                datos[2] = rs.getString(3);
                datos[3] = rs.getString(4);
                datos[4] = rs.getString(5);
                datos[5] = rs.getString(6);

                modeloProducto.addRow(datos);
            }
        } catch (SQLException e) {
            Logger.getLogger(ProductoDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return modeloProducto;
    }

    public void cargarComboProductos(JComboBox productos) {
        try {
            productos.removeAllItems();
            String query = "select id, nombre from producto where estado = true";
            PreparedStatement stmt = this.connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            productos.addItem(new DTOProductoJComboBox(0, "Seleccione el Producto"));
            while (rs.next()) {
                int codigoProducto = rs.getInt("id");
                String nombreProducto = rs.getString("nombre");
                productos.addItem(new DTOProductoJComboBox(codigoProducto, nombreProducto));
            }

            closeStatement(stmt);
            closeResultSet(rs);
        } catch (SQLException e) {
            Logger.getLogger(MovimientoDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }
    }
}
