/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.controllers;

import com.ingenieria.managementdb.ConnectionDriver;
import com.ingenieria.managementdb.DatabaseConnectException;
import com.ingenieria.models.Persona;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author miguelfaubla
 */
public class PersonaController {

    Connection cn = ConnectionDriver.getConnection();
    PreparedStatement statement = null;

    public void registrarPersona(Persona persona) {
        try {
            statement = cn.prepareStatement("INSERT INTO estudiante(institucion, nivel, paralelo, id_animador, cedula) VALUES (?,?,?,?,?)");
            statement.setString(1, String.valueOf(persona.getCedula()));
//            statement.setString(2, e.getNivel());
//            statement.setString(3, e.getParalelo());
//            statement.setString(4, e.getAni().getIdanimador());
//            statement.setInt(5, e.getPer().getCedula());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseConnectException(e.getMessage());
        } finally {
            ConnectionDriver.closeStatement(statement);
        }
    }
}
