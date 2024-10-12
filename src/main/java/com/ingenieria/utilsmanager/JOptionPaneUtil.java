/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.utilsmanager;

import java.awt.Component;

/**
 *
 * @author miguelfaubla
 */
public class JOptionPaneUtil {

    private String message;

    public void showMessageDialog(Component parentComponent, Object message) {
        this.message = message.toString();
    }

    public String getCapturedMessage() {
        return message;
    }

    public void clear() {
        message = null;
    }
}
