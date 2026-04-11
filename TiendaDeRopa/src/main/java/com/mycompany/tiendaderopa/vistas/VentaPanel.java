package com.mycompany.tiendaderopa.vistas;

import com.mycompany.tiendaderopa.modelos.Venta;
import com.mycompany.tiendaderopa.servicios.ClienteService;
import com.mycompany.tiendaderopa.servicios.VentaServicio;
import com.mycompany.tiendaderopa.servicios.VentaRepositorio;
import com.mycompany.tiendaderopa.servicios.ClienteRepository;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Panel para gestionar ventas: crear, listar, actualizar y eliminar.
 */
public class VentaPanel extends JPanel {

    private JTextField txtNumeroFactura;
    private JTextField txtCliente;
    private JTextField txtFecha;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JTable tablaVentas;
    private DefaultTableModel tableModel;
    private VentaServicio ventaServicio;
    private JTable tablaDetalles;
    private DefaultTableModel detalleModel;

    public VentaPanel(VentaServicio ventaServicio) {
        // Inicializar servicios
        this.ventaServicio = ventaServicio;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Panel de formulario (izquierda)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(8, 1, 5, 5));

        panelFormulario.add(new JLabel("N° Factura:"));
        txtNumeroFactura = new JTextField();
        panelFormulario.add(txtNumeroFactura);

        panelFormulario.add(new JLabel("Cliente (Cédula):"));
        txtCliente = new JTextField();
        panelFormulario.add(txtCliente);

        panelFormulario.add(new JLabel("Fecha:"));
        txtFecha = new JTextField();
        txtFecha.setEditable(false); // Fecha automática
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        txtTotal.setEditable(false); // Total calculado
        panelFormulario.add(txtTotal);

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

        // Tabla de ventas (derecha)
        tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] { "N° Factura", "Cliente", "Fecha", "Total" }
        );
        tablaVentas = new JTable(tableModel);
        
        // Tabla de detalles de venta
        detalleModel = new DefaultTableModel(
            new Object[][] {},
            new String[] { "Producto", "Talla", "Color", "Precio", "Cantidad", "Subtotal" }
        );
        tablaDetalles = new JTable(detalleModel);

        // Panel central con ambas tablas
        JPanel panelTablas = new JPanel(new GridLayout(2, 1, 5, 5));
        panelTablas.add(new JScrollPane(tablaVentas));
        panelTablas.add(new JScrollPane(tablaDetalles));
        add(panelTablas, BorderLayout.CENTER);



        // Agregar listeners a los botones
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarVenta();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarVenta();
            }
        });

        btnActualizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actualizarVenta();
            }
        });

        // Listener para seleccionar fila en tabla
        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarVentaSeleccionada();
            }
        });

        // Cargar ventas iniciales
        cargarVentas();
    }

    /**
     * Guarda una nueva venta.
     */
    private void guardarVenta() {
        try {
            String numeroFactura = txtNumeroFactura.getText().trim();
            String cedulaCliente = txtCliente.getText().trim();
            ventaServicio.registrarVenta(numeroFactura, cedulaCliente);
            JOptionPane.showMessageDialog(this, "Venta guardada exitosamente.");
            limpiarCampos();
            cargarVentas();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina la venta seleccionada.
     */
    private void eliminarVenta() {
        int selectedRow = tablaVentas.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroFactura = (String) tableModel.getValueAt(selectedRow, 0);
            ventaServicio.eliminarVenta(numeroFactura);
            JOptionPane.showMessageDialog(this, "Venta eliminada.");
            limpiarCampos();
            cargarVentas();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Actualiza la venta seleccionada.
     */
    private void actualizarVenta() {
        int selectedRow = tablaVentas.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroFactura = txtNumeroFactura.getText().trim();
            // Para actualizar, necesitaríamos más campos, pero por ahora solo actualizamos cliente
            // Asumiendo que solo cambia cliente
            Venta venta = ventaServicio.buscarVenta(numeroFactura);
            if (venta != null) {
                // Aquí podríamos actualizar otros campos si los tuviéramos
                ventaServicio.actualizarVenta(venta);
                JOptionPane.showMessageDialog(this, "Venta actualizada.");
                cargarVentas();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para actualizar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Carga los datos de la venta seleccionada en la tabla a los campos del formulario.
     */
    private void cargarVentaSeleccionada() {
        int selectedRow = tablaVentas.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroFactura = (String) tableModel.getValueAt(selectedRow, 0);
            Venta venta = ventaServicio.buscarVenta(numeroFactura);
            if (venta != null) {
                txtNumeroFactura.setText(venta.getNumeroFactura());
                txtCliente.setText(venta.getCliente().getCedula());
                txtFecha.setText(venta.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                txtTotal.setText(String.valueOf(venta.getTotal()));
                cargarDetalles(venta);
            }
        }
    }

    /**
     * Carga todas las ventas en la tabla.
     */
    private void cargarVentas() {
        tableModel.setRowCount(0);
        List<Venta> ventas = ventaServicio.listarVentas();
        for (Venta v : ventas) {
            tableModel.addRow(new Object[]{
                v.getNumeroFactura(),
                v.getCliente().getNombre(),
                v.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                v.getTotal()
            });
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void limpiarCampos() {
        txtNumeroFactura.setText("");
        txtCliente.setText("");
        txtFecha.setText("");
        txtTotal.setText("");
    }


    private void cargarDetalles(Venta venta) {
        detalleModel.setRowCount(0);
        for (com.mycompany.tiendaderopa.modelos.DetalleVenta d : venta.getDetalles()) {
            detalleModel.addRow(new Object[]{
                d.getProducto().getNombre(),
                d.getProducto().getTalla(),
                d.getProducto().getColor(),
                d.getProducto().getPrecio(),
                d.getCantidad(),
                d.getSubtotal()
            });
        }
    }
}