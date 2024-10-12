/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.utilsmanager;

import java.util.Arrays;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author miguelfaubla
 */
public class UtilsManager {

    public static void loadingCbo(JComboBox valueCbo, String[] valores) {
        valueCbo.removeAllItems();
        List<String> contenido = Arrays.asList(valores);
        for (String item : contenido) {
            valueCbo.addItem(item);
        }
    }

}
