package com.mycompany.tiendaderopa.vistas;

import com.mycompany.tiendaderopa.servicios.*;

/**
 *  Hallazgo 10: El look-and-feel Nimbus se configura aquí,
 * en el único punto de entrada real (TiendaDeRopa.main() llama a este main).
 * TiendaDeRopa.java ya no necesita su propio main duplicado (ver TiendaDeRopa.java).
 *  Hallazgo 8: Al cambiar a la pestaña Historial se refresca automáticamente.
 */
public class MainFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(MainFrame.class.getName());

    // Referencia al historial para refrescarlo al cambiar de pestaña
    private HistorialPanel historialPanel;

    public MainFrame() {
        initComponents();

        IClienteRepository clienteRepository = new ClienteRepository();
        IProductoRepositorio productoRepository = new ProductoRepositorio();
        IVentaRepositorio ventaRepository = new VentaRepositorio();

        ClienteService clienteService = new ClienteService(clienteRepository);
        ProductoServicio productoService = new ProductoServicio(productoRepository);
        VentaServicio ventaService = new VentaServicio(ventaRepository, clienteRepository);

        // Clientes
        tabPrincipal.setComponentAt(0, new ClientePanel(clienteService));

        // Productos
        tabPrincipal.setComponentAt(1, new ProductoPanel(productoService));

        //  Hallazgo 1: VentaPanel ahora recibe también ProductoServicio
        tabPrincipal.setComponentAt(2, new VentaPanel(ventaService, productoService, clienteService));

        // Historial
        historialPanel = new HistorialPanel(ventaService);
        tabPrincipal.addTab("Historial", historialPanel);

        //  Hallazgo 8: Refresca historial automáticamente al seleccionarlo
        tabPrincipal.addChangeListener(e -> {
            int idx = tabPrincipal.getSelectedIndex();
            // La pestaña Historial es la 4ª (índice 3)
            if (idx == 3 && historialPanel != null) {
                historialPanel.cargarVentas();
            }
        });
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tabPrincipal = new javax.swing.JTabbedPane();
        tabClientes = new javax.swing.JTabbedPane();
        tabProductos = new javax.swing.JTabbedPane();
        tabVentas = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Tienda de Ropa");

        tabPrincipal.addTab("Clientes", tabClientes);
        tabPrincipal.addTab("Productos", tabProductos);
        tabPrincipal.addTab("Ventas", tabVentas);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, 1000)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, 500, 500)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     *  Hallazgo 10: Este es el ÚNICO punto de entrada con Nimbus.
     * TiendaDeRopa.java simplemente llama a este método o instancia MainFrame directamente.
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new MainFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane tabClientes;
    private javax.swing.JTabbedPane tabPrincipal;
    private javax.swing.JTabbedPane tabProductos;
    private javax.swing.JTabbedPane tabVentas;
    // End of variables declaration//GEN-END:variables
}
