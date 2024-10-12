/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.controllers;

import com.ingenieria.services.UsuarioDAO;

/**
 *
 * @author miguelfaubla
 */
public class SecurityAuthController {

    private final UsuarioDAO usuarioDAO;

    public SecurityAuthController() {
        this.usuarioDAO = new UsuarioDAO();
    }

    public boolean validarCredenciales(String username, String password) {
        return usuarioDAO.validarUsuario(username, password);
    }
}
