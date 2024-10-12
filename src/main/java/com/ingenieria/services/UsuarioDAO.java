/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.services;

import static com.ingenieria.managementdb.ConnectionDriver.closeResultSet;
import static com.ingenieria.managementdb.ConnectionDriver.closeStatement;
import static com.ingenieria.managementdb.ConnectionDriver.getConnection;
import com.ingenieria.managementdb.DatabaseConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author miguelfaubla
 */
public class UsuarioDAO {

    private final Connection connection;

    public UsuarioDAO() {
        this.connection = getConnection();
    }

    public boolean validarUsuario(String username, String password) {
        boolean isValid = false;

        try {
            String sql = "SELECT * FROM usuarios WHERE username = ? AND password = ?";
            PreparedStatement stmt = this.connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                isValid = true;
            }
            
            closeStatement(stmt);
            closeResultSet(rs);
        } catch (SQLException e) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, e);
            throw new DatabaseConnectException(e.getMessage());
        }

        return isValid;
    }
}
