package com.mycompany.tiendaderopa.vistas;

import com.mycompany.tiendaderopa.servicios.ProductoServicio;
import com.mycompany.tiendaderopa.servicios.ProductoRepositorio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.TitledBorder;
import com.mycompany.tiendaderopa.modelos.Producto;

/**
 * Panel para gestionar productos: registrar, editar, eliminar y listar.
 */

public class ProductoPanel extends JPanel {

    private JTextField txtCodigo;
    private JTextField txtNombre;
    private JTextField txtTalla;
    private JTextField txtColor;
    private JTextField txtPrecio;
    private JTextField txtStock;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JTable tablaProductos;
    private DefaultTableModel tableModel;
    private ProductoServicio productoService;

    public ProductoPanel(ProductoServicio productoService) {
        this.productoService = productoService;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(new TitledBorder("Gestión de Productos"));

        // Panel de formulario (izquierda)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(12, 1, 5, 5));

        panelFormulario.add(new JLabel("Código:"));
        txtCodigo = new JTextField();
        panelFormulario.add(txtCodigo);

        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Talla:"));
        txtTalla = new JTextField();
        panelFormulario.add(txtTalla);

        panelFormulario.add(new JLabel("Color:"));
        txtColor = new JTextField();
        panelFormulario.add(txtColor);

        panelFormulario.add(new JLabel("Precio:"));
        txtPrecio = new JTextField();
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Stock:"));
        txtStock = new JTextField();
        panelFormulario.add(txtStock);

        // Panel de botones (debajo del formulario)
        JPanel panelBotones = new JPanel(new GridLayout(1, 3, 5, 5));
        btnGuardar = new JButton("Guardar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnActualizar);

        // Panel izquierdo con formulario y botones
        JPanel panelIzquierdo = new JPanel(new BorderLayout());
        panelIzquierdo.add(panelFormulario, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);

        add(panelIzquierdo, BorderLayout.WEST);

        // Tabla de productos (derecha)
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "Código", "Nombre", "Talla", "Color", "Precio", "Stock" }
        );
        tablaProductos = new JTable(tableModel);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        // Agregar listeners
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarProducto();
            }
        });

        // Listener para seleccionar fila en tabla
        tablaProductos.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarProductoSeleccionado();
            }
        });

        // Cargar productos iniciales
        cargarProductos();
    }

    private void guardarProducto() {
        try {
            String codigo = txtCodigo.getText().trim();
            String nombre = txtNombre.getText().trim();
            String talla = txtTalla.getText().trim();
            String color = txtColor.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int stock = Integer.parseInt(txtStock.getText().trim());
            productoService.registrarProducto(codigo, nombre, talla, color, precio, stock);
            JOptionPane.showMessageDialog(this, "Producto guardado exitosamente.");
            limpiarCampos();
            cargarProductos();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarProducto() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow >= 0) {
            String codigo = (String) tableModel.getValueAt(selectedRow, 0);
            productoService.eliminarProducto(codigo);
            JOptionPane.showMessageDialog(this, "Producto eliminado.");
            limpiarCampos();
            cargarProductos();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void actualizarProducto() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                String codigo = txtCodigo.getText().trim();
                String nombre = txtNombre.getText().trim();
                String talla = txtTalla.getText().trim();
                String color = txtColor.getText().trim();
                double precio = Double.parseDouble(txtPrecio.getText().trim());
                int stock = Integer.parseInt(txtStock.getText().trim());
                productoService.actualizarProducto(codigo, nombre, talla, color, precio, stock);
                JOptionPane.showMessageDialog(this, "Producto actualizado.");
                cargarProductos();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarProductoSeleccionado() {
        int selectedRow = tablaProductos.getSelectedRow();
        if (selectedRow >= 0) {
            txtCodigo.setText((String) tableModel.getValueAt(selectedRow, 0));
            txtNombre.setText((String) tableModel.getValueAt(selectedRow, 1));
            txtTalla.setText((String) tableModel.getValueAt(selectedRow, 2));
            txtColor.setText((String) tableModel.getValueAt(selectedRow, 3));
            txtPrecio.setText(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
            txtStock.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5)));
        }
    }

    private void cargarProductos() {
        tableModel.setRowCount(0);
        List<Producto> productos = productoService.listarProductos();
        for (Producto p : productos) {
            tableModel.addRow(new Object[]{
                p.getCodigo(),
                p.getNombre(),
                p.getTalla(),
                p.getColor(),
                p.getPrecio(),
                p.getStock()
            });
        }
    }

    private void limpiarCampos() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtTalla.setText("");
        txtColor.setText("");
        txtPrecio.setText("");
        txtStock.setText("");
    }
}