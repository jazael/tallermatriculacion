/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.managementdb;

/**
 *
 * @author miguelfaubla
 */
public class DatabaseConnectException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DatabaseConnectException(String msg) {
        super(msg);
    }
}
