/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.services;

import static com.ingenieria.managementdb.ConnectionDriver.closeResultSet;
import static com.ingenieria.managementdb.ConnectionDriver.closeStatement;
import static com.ingenieria.managementdb.ConnectionDriver.getConnection;
import com.ingenieria.managementdb.DatabaseConnectException;
import com.ingenieria.models.Movimiento;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author miguelfaubla
 */
public class MatriculacionDAO {

    private final Connection connection;
    private final AlumnoDAO productoDAO;

    public MatriculacionDAO() {
        this.connection = getConnection();
        this.productoDAO = new AlumnoDAO();
    }

    public Movimiento insertarMovimiento(Integer codigoproducto, String tipomovimiento, String cantidad, String fecha, String responsable) {
        Movimiento movimiento = null;
        String query = "INSERT INTO movimiento (producto_id, tipomovimiento, cantidad, fecha, responsable) VALUES (?, ?, ?, ?, ?) RETURNING *";

        try {

            PreparedStatement stmt = this.connection.prepareStatement(query);
            
            Date fechaIngres = Date.valueOf(fecha);

            stmt.setInt(1, codigoproducto);
            stmt.setString(2, tipomovimiento);
            stmt.setDouble(3, Double.parseDouble(cantidad));
            stmt.setDate(4, fechaIngres);
            stmt.setString(5, responsable);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                movimiento = new Movimiento();
                movimiento.setProducto_id(rs.getInt("producto_id"));
                movimiento.setTipomovimiento(rs.getString("tipomovimiento"));
                movimiento.setCantidad(rs.getDouble("cantidad"));
                movimiento.setFecha(rs.getString("fecha"));
                movimiento.setResponsable(rs.getString("responsable"));
                
                boolean egreso = tipomovimiento.equals("EGRESO");
                
                // Actualizar stock
                this.productoDAO.actualizarStock(codigoproducto, Double.valueOf(cantidad), egreso);
            }

            closeStatement(stmt);
            closeResultSet(rs);

        } catch (SQLException e) {
            Logger.getLogger(MatriculacionDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return movimiento;
    }

    public boolean editarMovimiento(Integer id, Integer codigoproducto, String tipomovimiento, String cantidad, String fecha, String responsable) {
        boolean isUpdated = false;
        String query = "UPDATE movimiento SET producto_id = ?, tipomovimiento = ?, cantidad = ?, fecha = ?, responsable = ? WHERE id = ?";

        try {
            PreparedStatement stmt = this.connection.prepareStatement(query);
            
            Date fechaIngres = Date.valueOf(fecha);

            stmt.setInt(1, codigoproducto);
            stmt.setString(2, tipomovimiento);
            stmt.setDouble(3, Double.parseDouble(cantidad));
            stmt.setDate(4, fechaIngres);
            stmt.setString(5, responsable);
            stmt.setInt(6, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                isUpdated = true;
                
                boolean egreso = tipomovimiento.equals("EGRESO");
                
                // Actualizar stock
                this.productoDAO.actualizarStock(codigoproducto, Double.valueOf(cantidad), egreso);
            }

            closeStatement(stmt);
        } catch (SQLException e) {
            Logger.getLogger(MatriculacionDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return isUpdated;
    }
    
    public boolean eliminarMovimiento(Integer id) {
        boolean isDeleted = false;
        String query = "UPDATE movimiento SET estado = ? WHERE id = ?";

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
            Logger.getLogger(MatriculacionDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return isDeleted;
    }


    public DefaultTableModel listarMovimientos(DefaultTableModel modeloMovimientos) {
        String query = """
                       SELECT 
                           movimiento.id AS movimiento_id,
                           movimiento.tipomovimiento,
                           movimiento.cantidad,
                           movimiento.fecha,
                           movimiento.responsable,
                           producto.id AS producto_id,
                           producto.nombre AS producto_nombre,
                           producto.precio,
                           producto.stock,
                           producto.unidadmedida
                       FROM 
                           movimiento
                       JOIN 
                           producto
                       ON 
                           movimiento.producto_id = producto.id
                       WHERE 
                           movimiento.estado = true 
                           AND producto.estado = true;""";
        String[] datos = new String[10];

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
                datos[6] = rs.getString(7);
                datos[7] = rs.getString(8);
                datos[8] = rs.getString(9);
                datos[9] = rs.getString(10);

                modeloMovimientos.addRow(datos);
            }
            
            closeStatement(stmt);
            closeResultSet(rs);
        } catch (SQLException e) {
            Logger.getLogger(MatriculacionDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return modeloMovimientos;
    }
}
