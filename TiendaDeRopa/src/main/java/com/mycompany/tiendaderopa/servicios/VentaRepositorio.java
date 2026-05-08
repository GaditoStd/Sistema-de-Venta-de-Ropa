package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.conexion.ConexionDB;
import com.mycompany.tiendaderopa.modelos.Cliente;
import com.mycompany.tiendaderopa.modelos.DetalleVenta;
import com.mycompany.tiendaderopa.modelos.Producto;
import com.mycompany.tiendaderopa.modelos.Venta;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación del repositorio de ventas usando PostgreSQL en Neon.
 *
 * CORRECCIÓN PRINCIPAL: Se reemplaza la lista en memoria (ArrayList) por
 * operaciones JDBC contra las tablas "ventas" y "detalles_venta".
 *
 * PATRÓN USADO: Una venta se guarda en dos pasos dentro de una transacción:
 *   1. INSERT en "ventas"
 *   2. INSERT por cada fila en "detalles_venta"
 * Se usa una Transaction (conn.setAutoCommit(false)) para que ambas
 * operaciones sean atómicas (todo o nada).
 *
 * @version 3.0 - Migrado de ArrayList a BD PostgreSQL
 */
public class VentaRepositorio implements IVentaRepositorio {

    private static final Logger logger = Logger.getLogger(VentaRepositorio.class.getName());

    public VentaRepositorio() {
        // No requiere inicialización
    }

    @Override
    public void guardar(Venta venta) throws SQLException {
        String sqlVenta   = "INSERT INTO ventas (numero_factura, fecha, cedula_cliente, total) VALUES (?, ?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalles_venta (numero_factura, codigo_producto, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        // Transacción: si algo falla, se hace rollback automático
        try (Connection conn = ConexionDB.conectar()) {
            conn.setAutoCommit(false);  // INICIO DE TRANSACCIÓN

            try {
                // 1. Insertar la cabecera de la venta
                try (PreparedStatement stmtVenta = conn.prepareStatement(sqlVenta)) {
                    stmtVenta.setString(1, venta.getNumeroFactura());
                    stmtVenta.setDate(2, Date.valueOf(venta.getFecha()));
                    stmtVenta.setString(3, venta.getCliente().getCedula());
                    stmtVenta.setDouble(4, venta.calcularTotal());
                    stmtVenta.executeUpdate();
                }

                // 2. Insertar cada línea de detalle
                try (PreparedStatement stmtDetalle = conn.prepareStatement(sqlDetalle)) {
                    for (DetalleVenta d : venta.getDetalles()) {
                        stmtDetalle.setString(1, venta.getNumeroFactura());
                        stmtDetalle.setString(2, d.getProducto().getCodigo());
                        stmtDetalle.setInt(3, d.getCantidad());
                        stmtDetalle.setDouble(4, d.getSubtotal());
                        stmtDetalle.addBatch();  // Se agrupan para eficiencia
                    }
                    stmtDetalle.executeBatch();
                }

                conn.commit();  // CONFIRMAR TRANSACCIÓN
                logger.log(Level.INFO, "Venta guardada: {0}", venta.getNumeroFactura());

            } catch (SQLException e) {
                conn.rollback();  // REVERTIR si algo falla
                logger.log(Level.SEVERE, "Rollback en guardar venta: {0}", e.getMessage());
                throw new SQLException("Error al guardar venta en BD (rollback aplicado): " + e.getMessage(), e);
            }
        }
    }

    @Override
    public List<Venta> obtenerTodas() throws SQLException {
        // Usamos un JOIN para traer todo en una sola consulta (eficiente)
        String sql =
            "SELECT v.numero_factura, v.fecha, v.total, " +
            "       c.cedula, c.nombre AS nombre_cliente, c.telefono, " +
            "       d.id AS detalle_id, d.cantidad, d.subtotal, " +
            "       p.codigo AS cod_prod, p.nombre AS nombre_prod, " +
            "       p.talla, p.color, p.precio, p.stock " +
            "FROM ventas v " +
            "JOIN clientes c ON v.cedula_cliente = c.cedula " +
            "LEFT JOIN detalles_venta d ON v.numero_factura = d.numero_factura " +
            "LEFT JOIN productos p ON d.codigo_producto = p.codigo " +
            "ORDER BY v.numero_factura";

        List<Venta> ventas = new ArrayList<>();

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            Venta ventaActual = null;

            while (rs.next()) {
                String numFactura = rs.getString("numero_factura");

                // Si es una nueva factura, crear objeto Venta
                if (ventaActual == null || !ventaActual.getNumeroFactura().equals(numFactura)) {
                    Cliente cliente = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre_cliente"),
                        rs.getString("telefono")
                    );
                    ventaActual = new Venta(numFactura, cliente);
                    ventaActual.setFecha(rs.getDate("fecha").toLocalDate());
                    ventaActual.setTotal(rs.getDouble("total"));
                    ventas.add(ventaActual);
                }

                // Si la fila tiene un detalle (LEFT JOIN puede traer NULL)
                if (rs.getString("cod_prod") != null) {
                    Producto prod = new Producto(
                        rs.getString("cod_prod"),
                        rs.getString("nombre_prod"),
                        rs.getString("talla"),
                        rs.getString("color"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                    DetalleVenta detalle = new DetalleVenta(prod, rs.getInt("cantidad"));
                    ventaActual.getDetalles().add(detalle);
                }
            }

            logger.log(Level.INFO, "Se obtuvieron {0} ventas de BD", ventas.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener ventas: {0}", e.getMessage());
            throw new SQLException("Error al obtener ventas de BD: " + e.getMessage(), e);
        }

        return ventas;
    }

    /**
     * Busca una venta específica con todos sus detalles.
     * Reutiliza obtenerTodas() para simplificar (aceptable en proyectos pequeños).
     * En producción, se haría una consulta filtrada por numero_factura.
     */
    @Override
    public Venta buscarPorNumeroFactura(String numeroFactura) throws SQLException {
        String sql =
            "SELECT v.numero_factura, v.fecha, v.total, " +
            "       c.cedula, c.nombre AS nombre_cliente, c.telefono, " +
            "       d.cantidad, d.subtotal, " +
            "       p.codigo AS cod_prod, p.nombre AS nombre_prod, " +
            "       p.talla, p.color, p.precio, p.stock " +
            "FROM ventas v " +
            "JOIN clientes c ON v.cedula_cliente = c.cedula " +
            "LEFT JOIN detalles_venta d ON v.numero_factura = d.numero_factura " +
            "LEFT JOIN productos p ON d.codigo_producto = p.codigo " +
            "WHERE v.numero_factura = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroFactura);

            try (ResultSet rs = stmt.executeQuery()) {
                Venta venta = null;

                while (rs.next()) {
                    if (venta == null) {
                        Cliente cliente = new Cliente(
                            rs.getString("cedula"),
                            rs.getString("nombre_cliente"),
                            rs.getString("telefono")
                        );
                        venta = new Venta(rs.getString("numero_factura"), cliente);
                        venta.setFecha(rs.getDate("fecha").toLocalDate());
                        venta.setTotal(rs.getDouble("total"));
                    }
                    if (rs.getString("cod_prod") != null) {
                        Producto prod = new Producto(
                            rs.getString("cod_prod"),
                            rs.getString("nombre_prod"),
                            rs.getString("talla"),
                            rs.getString("color"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                        );
                        venta.getDetalles().add(new DetalleVenta(prod, rs.getInt("cantidad")));
                    }
                }
                return venta;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar venta: {0}", e.getMessage());
            throw new SQLException("Error al buscar venta en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina una venta y sus detalles.
     * Los detalles se eliminan automáticamente por el ON DELETE CASCADE definido en el schema.
     *
     * SQL: DELETE FROM ventas WHERE numero_factura = ?
     */
    @Override
    public void eliminar(String numeroFactura) throws SQLException {
        String sql = "DELETE FROM ventas WHERE numero_factura = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeroFactura);
            int filas = stmt.executeUpdate();
            logger.log(Level.INFO, "Venta eliminada: {0} ({1} fila(s))", new Object[]{numeroFactura, filas});

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar venta: {0}", e.getMessage());
            throw new SQLException("Error al eliminar venta en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza el total de una venta existente.
     * En este sistema las ventas no se editan (sus detalles no cambian tras confirmarse),
     * pero se provee el método para cumplir con la interfaz.
     *
     * SQL: UPDATE ventas SET total=? WHERE numero_factura=?
     */
    @Override
    public void actualizar(Venta venta) throws SQLException {
        String sql = "UPDATE ventas SET total=? WHERE numero_factura=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, venta.calcularTotal());
            stmt.setString(2, venta.getNumeroFactura());
            stmt.executeUpdate();
            logger.log(Level.INFO, "Venta actualizada: {0}", venta.getNumeroFactura());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar venta: {0}", e.getMessage());
            throw new SQLException("Error al actualizar venta en BD: " + e.getMessage(), e);
        }
    }
}