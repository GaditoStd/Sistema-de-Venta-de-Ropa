package com.mycompany.tiendaderopa.vistas;

import javax.swing.*;
import java.awt.*;

public class VentaPanel extends JPanel {

    private JTextField txtNumeroFactura;
    private JTextField txtCliente;
    private JTextField txtFecha;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnEliminar;
    private JButton btnActualizar;
    private JTable tablaVentas;

    public VentaPanel() {
        setLayout(new BorderLayout());

        // Panel de formulario (izquierda)
        JPanel panelFormulario = new JPanel();
        panelFormulario.setLayout(new GridLayout(8, 1, 5, 5));

        panelFormulario.add(new JLabel("N° Factura:"));
        txtNumeroFactura = new JTextField();
        panelFormulario.add(txtNumeroFactura);

        panelFormulario.add(new JLabel("Cliente:"));
        txtCliente = new JTextField();
        panelFormulario.add(txtCliente);

        panelFormulario.add(new JLabel("Fecha:"));
        txtFecha = new JTextField();
        panelFormulario.add(txtFecha);

        panelFormulario.add(new JLabel("Total:"));
        txtTotal = new JTextField();
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
        tablaVentas = new JTable(
            new Object[][] {},
            new String[] { "N° Factura", "Cliente", "Fecha", "Total" }
        );
        add(new JScrollPane(tablaVentas), BorderLayout.CENTER);
    }
}