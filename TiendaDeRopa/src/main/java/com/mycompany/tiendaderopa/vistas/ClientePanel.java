package com.mycompany.tiendaderopa.vistas;
import com.mycompany.tiendaderopa.servicios.ClienteService;
//  Hallazgo 12: Se elimina la importación de ClienteRepository que no se usaba
import javax.swing.table.DefaultTableModel;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.List;

/**
 * @author Simon Cardona
 */
public class ClientePanel extends javax.swing.JPanel {
    private final ClienteService clienteService;

    private void actualizarTabla() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) tblClientes.getModel();
            modelo.setRowCount(0);
            List<Cliente> lista = clienteService.listarClientes();
            for (Cliente c : lista) {
                modelo.addRow(new Object[]{
                        c.getCedula(),
                        c.getNombre(),
                        c.getTelefono()
                });
            }
        } catch (RuntimeException e) {
            // Error al conectar con BD
            javax.swing.JOptionPane.showMessageDialog(this, 
                    "Error al cargar clientes: " + e.getMessage(), 
                    "Error de conexión", 
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    public ClientePanel(ClienteService clienteService) {
        this.clienteService = clienteService;
        initComponents();
        actualizarTabla();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblCedula = new javax.swing.JLabel();
        txtCedula = new javax.swing.JTextField();
        lblNombre = new javax.swing.JLabel();
        txtNombre = new javax.swing.JTextField();
        lblTelefono = new javax.swing.JLabel();
        txtTelefono = new javax.swing.JTextField();
        btnGuardar = new javax.swing.JButton();
        scrTabla = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        btnEliminar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();

        lblCedula.setText("Cedula");
        txtCedula.addActionListener(this::txtCedulaActionPerformed);
        lblNombre.setText("Nombre");
        lblTelefono.setText("Telefono");

        btnGuardar.setText("Guardar");
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] {"Cédula", "Nombre", "Telefono"}
        ));
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        scrTabla.setViewportView(tblClientes);

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(this::btnActualizarActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnGuardar)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(29, 29, 29)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblNombre)
                                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnActualizar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnEliminar)))
                                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCedula, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblNombre)
                                        .addComponent(lblTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtCedula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnGuardar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(scrTabla, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEliminar)
                                        .addComponent(btnActualizar))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtCedulaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaActionPerformed
        // No action needed
    }//GEN-LAST:event_txtCedulaActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event-btnGuardarActionPerformed
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        try {
            clienteService.registrarCliente(cedula, nombre, telefono);
            actualizarTabla();
            txtCedula.setText("");
            txtNombre.setText("");
            txtTelefono.setText("");
            //  Hallazgo 3: JOptionPane en vez de System.out.println
            javax.swing.JOptionPane.showMessageDialog(this, "Cliente guardado exitosamente.");
        } catch (Exception e) {
            //  Hallazgo 3: Muestra error en diálogo visual, no en consola
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event-btnGuardarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event-btnEliminarActionPerformed
        int fila = tblClientes.getSelectedRow();

        if (fila == -1) {
            //  Hallazgo 3: JOptionPane en vez de System.out.println
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un cliente para eliminar.", "Advertencia", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String cedula = tblClientes.getValueAt(fila, 0).toString();
            clienteService.eliminarCliente(cedula);
            actualizarTabla();
            javax.swing.JOptionPane.showMessageDialog(this, "Cliente eliminado.");
        } catch (RuntimeException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event-btnEliminarActionPerformed

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event-tblClientesMouseClicked
        // Hallazgo 9: Verifica que la fila sea válida antes de acceder
        int fila = tblClientes.getSelectedRow();
        if (fila < 0) return;

        txtCedula.setText(tblClientes.getValueAt(fila, 0).toString());
        txtNombre.setText(tblClientes.getValueAt(fila, 1).toString());
        txtTelefono.setText(tblClientes.getValueAt(fila, 2).toString());
    }//GEN-LAST:event-tblClientesMouseClicked

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event-btnActualizarActionPerformed
        String cedula = txtCedula.getText().trim();
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();

        try {
            // Hallazgo 4: Usa editarCliente() que sí tiene validaciones correctas
            clienteService.editarCliente(cedula, nombre, telefono);
            actualizarTabla();
            javax.swing.JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
        } catch (Exception e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event-btnActualizarActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JLabel lblCedula;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblTelefono;
    private javax.swing.JScrollPane scrTabla;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTextField txtCedula;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
