package com.mycompany.tiendaderopa.vistas;

import com.mycompany.tiendaderopa.modelos.DetalleVenta;
import com.mycompany.tiendaderopa.modelos.Producto;
import com.mycompany.tiendaderopa.modelos.Venta;
import com.mycompany.tiendaderopa.servicios.ProductoServicio;
import com.mycompany.tiendaderopa.servicios.VentaServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Panel para gestionar ventas.
 *
 * CORRECCIÓN CRÍTICA en guardarVenta():
 * El flujo anterior era:
 *   1. registrarVenta()  → guarda en BD con detalles VACÍOS
 *   2. buscarVenta()     → recupera de BD (sigue con detalles vacíos)
 *   3. agregarDetalle()  → modifica el objeto en RAM, NUNCA persiste en BD
 *
 * El flujo correcto ahora es:
 *   1. Validar carrito
 *   2. Llamar a ventaServicio.registrarVentaCompleta() que pasa cabecera
 *      + detalles juntos → una sola transacción atómica en la BD
 *   3. Reducir stock de cada producto vendido
 */
public class VentaPanel extends JPanel {

    private JTextField txtNumeroFactura;
    private JTextField txtCliente;
    private JTextField txtFecha;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnEliminar;

    private JTable tablaVentas;
    private DefaultTableModel tableModel;

    private JTable tablaDetalles;
    private DefaultTableModel detalleModel;

    private JComboBox<String> cmbProductos;
    private JTextField txtCantidad;
    private JButton btnAgregarProducto;
    private JButton btnQuitarProducto;

    private List<DetalleVenta> carrito;

    private VentaServicio ventaServicio;
    private ProductoServicio productoServicio;

    public VentaPanel(VentaServicio ventaServicio, ProductoServicio productoServicio) {
        this.ventaServicio = ventaServicio;
        this.productoServicio = productoServicio;
        this.carrito = new ArrayList<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(5, 5));

        // ===== PANEL IZQUIERDO: Formulario =====
        JPanel panelFormulario = new JPanel(new GridLayout(0, 1, 3, 3));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva Venta"));

        panelFormulario.add(new JLabel("N° Factura:"));
        txtNumeroFactura = new JTextField();
        panelFormulario.add(txtNumeroFactura);

        panelFormulario.add(new JLabel("Cédula del Cliente:"));
        txtCliente = new JTextField();
        panelFormulario.add(txtCliente);

        panelFormulario.add(new JLabel("Fecha (automática):"));
        txtFecha = new JTextField();
        txtFecha.setEditable(false);
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("Total (calculado):"));
        txtTotal = new JTextField("$0.00");
        txtTotal.setEditable(false);
        panelFormulario.add(txtTotal);

        // ===== PANEL CARRITO =====
        JPanel panelCarrito = new JPanel(new GridLayout(0, 1, 3, 3));
        panelCarrito.setBorder(BorderFactory.createTitledBorder("Agregar Producto a la Venta"));

        panelCarrito.add(new JLabel("Seleccionar Producto:"));
        cmbProductos = new JComboBox<>();
        panelCarrito.add(cmbProductos);
        cmbProductos.addPopupMenuListener(new javax.swing.event.PopupMenuListener() {
            public void popupMenuWillBecomeVisible(javax.swing.event.PopupMenuEvent e) {
                cargarComboProductos();
            }
            public void popupMenuWillBecomeInvisible(javax.swing.event.PopupMenuEvent e) {}
            public void popupMenuCanceled(javax.swing.event.PopupMenuEvent e) {}
        });

        panelCarrito.add(new JLabel("Cantidad:"));
        txtCantidad = new JTextField("1");
        panelCarrito.add(txtCantidad);

        btnAgregarProducto = new JButton("+ Agregar al carrito");
        btnQuitarProducto = new JButton("- Quitar seleccionado");
        JPanel panelBotonesCarrito = new JPanel(new GridLayout(1, 2, 5, 5));
        panelBotonesCarrito.add(btnAgregarProducto);
        panelBotonesCarrito.add(btnQuitarProducto);
        panelCarrito.add(panelBotonesCarrito);

        // ===== BOTONES PRINCIPALES =====
        JPanel panelBotones = new JPanel(new GridLayout(1, 2, 5, 5));
        btnGuardar = new JButton("✔ Confirmar Venta");
        btnEliminar = new JButton("✖ Eliminar Venta");
        panelBotones.add(btnGuardar);
        panelBotones.add(btnEliminar);

        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.add(panelFormulario, BorderLayout.NORTH);
        panelIzquierdo.add(panelCarrito, BorderLayout.CENTER);
        panelIzquierdo.add(panelBotones, BorderLayout.SOUTH);
        panelIzquierdo.setPreferredSize(new Dimension(280, 0));
        add(panelIzquierdo, BorderLayout.WEST);

        // ===== TABLAS DERECHAS =====
        detalleModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"Producto", "Talla", "Color", "Precio", "Cantidad", "Subtotal"}
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        tablaDetalles = new JTable(detalleModel);
        JScrollPane scrollDetalles = new JScrollPane(tablaDetalles);
        scrollDetalles.setBorder(BorderFactory.createTitledBorder("Productos en esta venta (carrito)"));

        tableModel = new DefaultTableModel(
                new Object[][]{},
                new String[]{"N° Factura", "Cliente", "Fecha", "Total"}
        ) { public boolean isCellEditable(int r, int c) { return false; } };
        tablaVentas = new JTable(tableModel);
        JScrollPane scrollVentas = new JScrollPane(tablaVentas);
        scrollVentas.setBorder(BorderFactory.createTitledBorder("Ventas Registradas"));

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollDetalles, scrollVentas);
        splitPane.setDividerLocation(200);
        add(splitPane, BorderLayout.CENTER);

        // ===== LISTENERS =====
        btnAgregarProducto.addActionListener(e -> agregarProductoAlCarrito());
        btnQuitarProducto.addActionListener(e -> quitarProductoDelCarrito());
        btnGuardar.addActionListener(e -> guardarVenta());
        btnEliminar.addActionListener(e -> eliminarVenta());

        tablaVentas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarVentaSeleccionada();
        });

        cargarComboProductos();
        cargarVentas();
    }

    private void cargarComboProductos() {
        cmbProductos.removeAllItems();
        List<Producto> productos = productoServicio.listarProductos();
        for (Producto p : productos) {
            cmbProductos.addItem(p.getCodigo() + " - " + p.getNombre() + " ($" + p.getPrecio() + ")");
        }
    }

    private void agregarProductoAlCarrito() {
        if (cmbProductos.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int cantidad;
        try {
            cantidad = Integer.parseInt(txtCantidad.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (cantidad <= 0) {
            JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Producto> productos = productoServicio.listarProductos();
        int indice = cmbProductos.getSelectedIndex();
        if (indice >= productos.size()) return;
        Producto productoSeleccionado = productos.get(indice);

        if (productoSeleccionado.getStock() < cantidad) {
            JOptionPane.showMessageDialog(this, "Stock insuficiente. Disponible: " + productoSeleccionado.getStock(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DetalleVenta detalle = new DetalleVenta(productoSeleccionado, cantidad);
        carrito.add(detalle);
        actualizarTablaCarrito();
    }

    private void quitarProductoDelCarrito() {
        int fila = tablaDetalles.getSelectedRow();
        if (fila < 0) {
            JOptionPane.showMessageDialog(this, "Selecciona un producto del carrito para quitarlo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        carrito.remove(fila);
        actualizarTablaCarrito();
    }

    private void actualizarTablaCarrito() {
        detalleModel.setRowCount(0);
        double totalCarrito = 0;
        for (DetalleVenta d : carrito) {
            detalleModel.addRow(new Object[]{
                    d.getProducto().getNombre(),
                    d.getProducto().getTalla(),
                    d.getProducto().getColor(),
                    String.format("$%.2f", d.getProducto().getPrecio()),
                    d.getCantidad(),
                    String.format("$%.2f", d.getSubtotal())
            });
            totalCarrito += d.getSubtotal();
        }
        txtTotal.setText(String.format("$%.2f", totalCarrito));
        txtFecha.setText(java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    }

    /**
     * CORRECCIÓN: Ahora usa registrarVentaCompleta() en lugar de
     * registrarVenta() + agregarDetalle() en memoria.
     *
     * Flujo correcto:
     * 1. Pasar carrito completo al servicio
     * 2. El servicio construye el objeto Venta con detalles
     * 3. VentaRepositorio.guardar() inserta cabecera + detalles en UNA transacción
     * 4. Solo después de confirmar en BD se reduce el stock
     */
    private void guardarVenta() {
        try {
            String numeroFactura = txtNumeroFactura.getText().trim();
            String cedulaCliente = txtCliente.getText().trim();

            if (carrito.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Agrega al menos un producto antes de confirmar la venta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Una sola llamada — guarda cabecera + detalles en una transacción
            ventaServicio.registrarVentaCompleta(numeroFactura, cedulaCliente, carrito);

            // Ahora que la venta está confirmada en BD, reducir stock
            for (DetalleVenta detalle : carrito) {
                productoServicio.reducirStock(detalle.getProducto().getCodigo(), detalle.getCantidad());
            }

            JOptionPane.showMessageDialog(this, "Venta guardada exitosamente.");
            limpiarFormulario();
            cargarVentas();
            cargarComboProductos();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarVenta() {
        int selectedRow = tablaVentas.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroFactura = (String) tableModel.getValueAt(selectedRow, 0);
            ventaServicio.eliminarVenta(numeroFactura);
            JOptionPane.showMessageDialog(this, "Venta eliminada.");
            limpiarFormulario();
            cargarVentas();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una venta para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarVentaSeleccionada() {
        int selectedRow = tablaVentas.getSelectedRow();
        if (selectedRow >= 0) {
            String numeroFactura = (String) tableModel.getValueAt(selectedRow, 0);
            Venta venta = ventaServicio.buscarVenta(numeroFactura);
            if (venta != null) {
                txtNumeroFactura.setText(venta.getNumeroFactura());
                txtCliente.setText(venta.getCliente().getCedula());
                txtFecha.setText(venta.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                txtTotal.setText(String.format("$%.2f", venta.getTotal()));
                detalleModel.setRowCount(0);
                for (DetalleVenta d : venta.getDetalles()) {
                    detalleModel.addRow(new Object[]{
                            d.getProducto().getNombre(),
                            d.getProducto().getTalla(),
                            d.getProducto().getColor(),
                            String.format("$%.2f", d.getProducto().getPrecio()),
                            d.getCantidad(),
                            String.format("$%.2f", d.getSubtotal())
                    });
                }
            }
        }
    }

    private void cargarVentas() {
        tableModel.setRowCount(0);
        List<Venta> ventas = ventaServicio.listarVentas();
        for (Venta v : ventas) {
            tableModel.addRow(new Object[]{
                    v.getNumeroFactura(),
                    v.getCliente().getNombre(),
                    v.getFecha().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    String.format("$%.2f", v.getTotal())
            });
        }
    }

    private void limpiarFormulario() {
        txtNumeroFactura.setText("");
        txtCliente.setText("");
        txtFecha.setText("");
        txtTotal.setText("$0.00");
        carrito.clear();
        detalleModel.setRowCount(0);
        txtCantidad.setText("1");
    }
}