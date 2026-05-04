package com.mycompany.tiendaderopa.vistas;

import com.mycompany.tiendaderopa.modelos.Venta;
import com.mycompany.tiendaderopa.modelos.DetalleVenta;
import com.mycompany.tiendaderopa.servicios.VentaServicio;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HistorialPanel extends JPanel {

private VentaServicio ventaServicio;
private JTable tablaVentas;
private DefaultTableModel modeloVentas;
private JTable tablaDetalles;
private DefaultTableModel modeloDetalles;
private JLabel lblTotal;

public HistorialPanel(VentaServicio ventaServicio) {
    this.ventaServicio = ventaServicio;
    initComponents();
    cargarVentas();
}

private void initComponents() {
    setLayout(new BorderLayout(10, 10));

    // Tabla superior: lista de ventas
    modeloVentas = new DefaultTableModel(
        new Object[][]{},
        new String[]{"N° Factura", "Cliente", "Fecha", "Total"}
    ) {
        public boolean isCellEditable(int row, int col) { return false; }
    };
    tablaVentas = new JTable(modeloVentas);
    JScrollPane scrollVentas = new JScrollPane(tablaVentas);
    scrollVentas.setBorder(BorderFactory.createTitledBorder("Ventas"));

    // Tabla inferior: detalles de la venta seleccionada
    modeloDetalles = new DefaultTableModel(
        new Object[][]{},
        new String[]{"Producto", "Talla", "Color", "Precio Unit.", "Cantidad", "Subtotal"}
    ) {
        public boolean isCellEditable(int row, int col) { return false; }
    };
    tablaDetalles = new JTable(modeloDetalles);
    JScrollPane scrollDetalles = new JScrollPane(tablaDetalles);
    scrollDetalles.setBorder(BorderFactory.createTitledBorder("Detalle de venta seleccionada"));

    // Label total
    lblTotal = new JLabel("Total: $0.00");
    lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
    lblTotal.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

    // Panel con las dos tablas
    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollVentas, scrollDetalles);
    splitPane.setDividerLocation(200);

    add(splitPane, BorderLayout.CENTER);
    add(lblTotal, BorderLayout.SOUTH);

    // Al seleccionar una venta, mostrar sus detalles
    tablaVentas.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            mostrarDetalles();
        }
    });
}

private void cargarVentas() {
    modeloVentas.setRowCount(0);
    List<Venta> ventas = ventaServicio.listarVentas();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    for (Venta v : ventas) {
        modeloVentas.addRow(new Object[]{
            v.getNumeroFactura(),
            v.getCliente().getNombre(),
            v.getFecha().format(fmt),
            String.format("$%.2f", v.getTotal())
        });
    }
}

private void mostrarDetalles() {
    modeloDetalles.setRowCount(0);
    int fila = tablaVentas.getSelectedRow();
    if (fila < 0) return;

    String numeroFactura = (String) modeloVentas.getValueAt(fila, 0);
    Venta venta = ventaServicio.buscarVenta(numeroFactura);
    if (venta == null) return;

    for (DetalleVenta d : venta.getDetalles()) {
        modeloDetalles.addRow(new Object[]{
            d.getProducto().getNombre(),
            d.getProducto().getTalla(),
            d.getProducto().getColor(),
            String.format("$%.2f", d.getProducto().getPrecio()),
            d.getCantidad(),
            String.format("$%.2f", d.getSubtotal())
        });
    }
    lblTotal.setText(String.format("Total de la venta: $%.2f", venta.getTotal()));
}
}