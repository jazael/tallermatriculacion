/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingenieria.controllers;

import com.ingenieria.managementdb.ConnectionDriver;
import com.ingenieria.models.DTOProductoJComboBox;
import com.ingenieria.models.Movimiento;
import com.ingenieria.services.MatriculacionDAO;
import java.awt.HeadlessException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
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
public class MatriculacionController {

    private final MatriculacionDAO movimientoDAO;

    public MatriculacionController() {
        this.movimientoDAO = new MatriculacionDAO();
    }

    public void insertarMovimiento(JComboBox cbocodigoproducto, JComboBox cbotipomovimiento, JTextField txtcantidad, JFormattedTextField txtfechaingreso, JComboBox cboresponsable) {
        DTOProductoJComboBox productoSeleccionado = (DTOProductoJComboBox) cbocodigoproducto.getSelectedItem();
        Integer codigoproducto = productoSeleccionado.getId();
        String tipomovimiento = String.valueOf(cbotipomovimiento.getSelectedItem());
        String cantidad = txtcantidad.getText();
        String fechaingreso = txtfechaingreso.getText();
        String responsable = String.valueOf(cboresponsable.getSelectedItem());

        if (0 == codigoproducto || "".equals(tipomovimiento) || "".equals(cantidad) || "".equals(fechaingreso) || "".equals(responsable)) {
            JOptionPane.showMessageDialog(null, "Estimado usuario los campos son obligatorios");
        } else {
            try {
                Movimiento movimiento = this.movimientoDAO.insertarMovimiento(codigoproducto, tipomovimiento, cantidad, fechaingreso, responsable);
                if (movimiento != null) {
                    JOptionPane.showMessageDialog(null, "Los datos del movimiento se ingresaron correctamente");

                    cbocodigoproducto.setSelectedIndex(0);
                    cbotipomovimiento.setSelectedIndex(0);
                    txtcantidad.setText("");
                    Date date = new Date();
                    txtfechaingreso.setValue(date);
                    cboresponsable.setSelectedIndex(0);
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al registrar los datos");
            }
        }
    }

    public void editarMovimiento(Integer id, JComboBox cbocodigoproducto, JComboBox cbotipomovimiento, JTextField txtcantidad, JFormattedTextField txtfechaingreso, JComboBox cboresponsable) {
        DTOProductoJComboBox productoSeleccionado = (DTOProductoJComboBox) cbocodigoproducto.getSelectedItem();
        Integer codigoproducto = productoSeleccionado.getId();
        String tipomovimiento = String.valueOf(cbotipomovimiento.getSelectedItem());
        String cantidad = txtcantidad.getText();
        String fechaingreso = txtfechaingreso.getText();
        String responsable = String.valueOf(cboresponsable.getSelectedItem());

        if (0 == codigoproducto || "".equals(tipomovimiento) || "".equals(cantidad) || "".equals(fechaingreso) || "".equals(responsable)) {
            JOptionPane.showMessageDialog(null, "Estimado usuario los campos son obligatorios");
        } else {
            try {
                boolean updateProducto = this.movimientoDAO.editarMovimiento(id, codigoproducto, tipomovimiento, cantidad, fechaingreso, responsable);
                if (updateProducto) {
                    JOptionPane.showMessageDialog(null, "Los datos del movimiento se actualizaron correctamente");
                    
                    cbocodigoproducto.setSelectedIndex(0);
                    cbotipomovimiento.setSelectedIndex(0);
                    txtcantidad.setText("");
                    Date date = new Date();
                    txtfechaingreso.setValue(date);
                    cboresponsable.setSelectedIndex(0);
                }
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, "Ocurrio un error al actualizar los datos");
            }
        }
    }

    public void eliminarMovimiento(Integer id) {
        try {
            boolean isDeleted = this.movimientoDAO.eliminarMovimiento(id);
            if (isDeleted) {
                JOptionPane.showMessageDialog(null, "Los datos del movimiento se eliminaron correctamente");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al eliminar los datos");
        }
    }

    public void listarMovimientos(JTable tblMovimientos) {
        DefaultTableModel modelo = new DefaultTableModel();

        modelo.addColumn("Código");
        modelo.addColumn("Tipo movimiento");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Fecha");
        modelo.addColumn("Responsable");
        modelo.addColumn("Producto Id");
        modelo.addColumn("Producto");
        modelo.addColumn("Precio");
        modelo.addColumn("Stock");
        modelo.addColumn("Unidad medida");

        tblMovimientos.setModel(modelo);

        try {
            DefaultTableModel modeloMovimiento = this.movimientoDAO.listarMovimientos(modelo);
            tblMovimientos.setModel(modeloMovimiento);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al carga los datos del movimiento");
        }
    }

    public void seleccionarMovimiento(JTable tblMovimiento, JComboBox cboproducto, JComboBox cbotipomovimiento, JTextField txtcantidad, JFormattedTextField txtfechaingreso, JComboBox cboresponsable) {
        try {
            int fila = tblMovimiento.getSelectedRow();

            if (fila >= 0) {
                int productoId = Integer.parseInt((String) tblMovimiento.getValueAt(fila, 5));
                String productoNombre = (String) tblMovimiento.getValueAt(fila, 6);

                cboproducto.setSelectedItem(new DTOProductoJComboBox(productoId, productoNombre));

                cbotipomovimiento.setSelectedItem(tblMovimiento.getValueAt(fila, 1));
                txtcantidad.setText(tblMovimiento.getValueAt(fila, 2).toString());
                txtfechaingreso.setText(tblMovimiento.getValueAt(fila, 3).toString());
                cboresponsable.setSelectedItem(tblMovimiento.getValueAt(fila, 4));

            } else {
                JOptionPane.showMessageDialog(null, "Fila no seleccionada");
            }
        } catch (HeadlessException e) {
            JOptionPane.showMessageDialog(null, "Ocurrio un error al carga los datos del movimiento");
        }

    }
    
    public JasperPrint reporteProducto() {
        URL resourceUrl = AlumnoController.class.getResource("/reports/Movimientos.jasper");
        File reporte = null;
        
        try {
            reporte = new File(resourceUrl.toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(AlumnoController.class.getName()).log(Level.SEVERE, null, ex);
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
