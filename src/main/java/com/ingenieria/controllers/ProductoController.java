/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.controllers;

import com.ingenieria.managementdb.ConnectionDriver;
import com.ingenieria.models.Producto;
import com.ingenieria.services.ProductoDAO;
import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author miguelfaubla
 */
public class ProductoController {

    private final ProductoDAO productoDAO;

    public ProductoController() {
        this.productoDAO = new ProductoDAO();
    }

    public void insertarProducto(JTextField txtnombre, JTextField txtdescripcion, JTextField txtprecio, JTextField txtstock, JComboBox cbounidadmedida) {
        String nombre = txtnombre.getText();
        String descripcion = txtdescripcion.getText();
        String precio = txtprecio.getText();
        String stock = txtstock.getText();
        String unidadmedida = String.valueOf(cbounidadmedida.getSelectedItem());

        if ("".equals(nombre) || "".equals(descripcion) || "".equals(precio) || "".equals(stock) || "".equals(unidadmedida)) {
            JOptionPane.showMessageDialog(null, "Estimado usuario los campos son obligatorios");
        } else {
            try {
                Producto producto = this.productoDAO.insertarProducto(nombre, descripcion, precio, stock, unidadmedida);
                if (producto != null) {
                    JOptionPane.showMessageDialog(null, "Los datos del producto se ingresaron correctamente");

                    txtnombre.setText("");
                    txtdescripcion.setText("");
                    txtprecio.setText("");
                    txtstock.setText("");
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al registrar los datos");
            }
        }
    }

    public void editarProducto(Integer id, JTextField txtnombre, JTextField txtdescripcion, JTextField txtprecio, JTextField txtstock, JComboBox cbounidadmedida) {
        String nombre = txtnombre.getText();
        String descripcion = txtdescripcion.getText();
        String precio = txtprecio.getText();
        String stock = txtstock.getText();
        String unidadmedida = String.valueOf(cbounidadmedida.getSelectedItem());

        if ("".equals(nombre) || "".equals(descripcion) || "".equals(precio) || "".equals(stock) || "".equals(unidadmedida)) {
            JOptionPane.showMessageDialog(null, "Estimado usuario los campos son obligatorios");
        } else {
            try {
                boolean updateProducto = this.productoDAO.editarProducto(id, nombre, descripcion, precio, stock, unidadmedida);
                if (updateProducto) {
                    JOptionPane.showMessageDialog(null, "Los datos del producto se actualizaron correctamente");

                    txtnombre.setText("");
                    txtdescripcion.setText("");
                    txtprecio.setText("");
                    txtstock.setText("");
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al actualizar los datos");
            }
        }
    }

    public void eliminarProducto(Integer id) {
        try {
            boolean isDeleted = this.productoDAO.eliminarProducto(id);
            if (isDeleted) {
                JOptionPane.showMessageDialog(null, "Los datos del producto se eliminaron correctamente");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar los datos");
        }
    }

    public void listarProductos(JTable tblProductos) {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Código");
        modelo.addColumn("Nombre");
        modelo.addColumn("Descripción");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Unidad medida");

        tblProductos.setModel(modelo);

        try {
            DefaultTableModel modeloProducto = this.productoDAO.listarProductos(modelo);
            tblProductos.setModel(modeloProducto);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al carga los datos del producto");
        }
    }

    public void seleccionarProducto(JTable tblproducto, JTextField txtnombre, JTextField txtdescripcion, JTextField txtprecio, JTextField txtstock, JComboBox cbounidadmedida) {
        try {
            int fila = tblproducto.getSelectedRow();

            if (fila >= 0) {
                txtnombre.setText(tblproducto.getValueAt(fila, 1).toString());
                txtdescripcion.setText(tblproducto.getValueAt(fila, 2).toString());
                txtprecio.setText(tblproducto.getValueAt(fila, 3).toString());
                txtstock.setText(tblproducto.getValueAt(fila, 4).toString());
                cbounidadmedida.setSelectedItem(tblproducto.getValueAt(fila, 5));

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al carga los datos del producto");
        }

    }

    public void cargarComboProductos(JComboBox productos) {
        try {
            productoDAO.cargarComboProductos(productos);
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al carga los datos del producto");
        }
    }

    public JasperPrint reporteProducto() {
        URL resourceUrl = ProductoController.class.getResource("/reports/Productos.jasper");
        File reporte = null;
        
        try {
            reporte = new File(resourceUrl.toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(ProductoController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!reporte.exists()) {
            JOptionPane.showMessageDialog(null, "No existe archivo");
            return null;
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(reporte.getAbsoluteFile()));
            JasperReport jr = (JasperReport) JRLoader.loadObject(is);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, ConnectionDriver.getConnection());
            return jp;
        } catch (FileNotFoundException | JRException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.toString());
        }
        
        
        return null;
    }
}
