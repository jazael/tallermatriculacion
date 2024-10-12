/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.models;

import java.util.Objects;

/**
 *
 * @author miguelfaubla
 */
public class DTOProductoJComboBox {

    private final int id;
    private final String nombre;

    public DTOProductoJComboBox(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DTOProductoJComboBox that = (DTOProductoJComboBox) o;
        return id == that.id;  // Compara solo el ID o los atributos que consideres necesarios
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return nombre;  // Para que se muestre el nombre en el JComboBox
    }
}
