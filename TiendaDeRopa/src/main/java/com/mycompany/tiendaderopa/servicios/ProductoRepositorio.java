/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.conexion.ConexionDB;
import com.mycompany.tiendaderopa.modelos.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación del repositorio de productos usando PostgreSQL en Neon.
 *
 * CORRECCIÓN PRINCIPAL: Se reemplaza la lista en memoria (ArrayList) por
 * operaciones JDBC reales contra la tabla "productos" en PostgreSQL.
 * Los datos ahora persisten entre sesiones de la aplicación.
 *
 * @version 3.0 - Migrado de ArrayList a BD PostgreSQL
 */
public class ProductoRepositorio implements IProductoRepositorio {

    private static final Logger logger = Logger.getLogger(ProductoRepositorio.class.getName());

    public ProductoRepositorio() {
        // No requiere inicialización; los datos viven en la BD
    }

    @Override
    public void guardar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (codigo, nombre, talla, color, precio, stock) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getCodigo());
            stmt.setString(2, producto.getNombre());
            stmt.setString(3, producto.getTalla());
            stmt.setString(4, producto.getColor());
            stmt.setDouble(5, producto.getPrecio());
            stmt.setInt(6, producto.getStock());

            stmt.executeUpdate();
            logger.log(Level.INFO, "Producto guardado: {0}", producto.getCodigo());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al guardar producto: {0}", e.getMessage());
            throw new SQLException("Error al guardar producto en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Recupera todos los productos de la tabla "productos".
     *
     * SQL: SELECT codigo, nombre, talla, color, precio, stock FROM productos
     */
    @Override
    public List<Producto> obtenerTodos() throws SQLException {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre, talla, color, precio, stock FROM productos";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Producto(
                    rs.getString("codigo"),
                    rs.getString("nombre"),
                    rs.getString("talla"),
                    rs.getString("color"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                ));
            }
            logger.log(Level.INFO, "Se obtuvieron {0} productos de BD", lista.size());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener productos: {0}", e.getMessage());
            throw new SQLException("Error al obtener productos de BD: " + e.getMessage(), e);
        }

        return lista;
    }

    /**
     * Elimina un producto por su código.
     *
     * SQL: DELETE FROM productos WHERE codigo = ?
     */
    @Override
    public void eliminar(String codigo) throws SQLException {
        String sql = "DELETE FROM productos WHERE codigo = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            int filas = stmt.executeUpdate();
            logger.log(Level.INFO, "Producto eliminado: {0} ({1} fila(s))", new Object[]{codigo, filas});

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar producto: {0}", e.getMessage());
            throw new SQLException("Error al eliminar producto en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Busca un producto por su código.
     *
     * SQL: SELECT ... FROM productos WHERE codigo = ?
     *
     * @return El producto si existe, null si no existe.
     */
    @Override
    public Producto buscarPorCodigo(String codigo) throws SQLException {
        String sql = "SELECT codigo, nombre, talla, color, precio, stock FROM productos WHERE codigo = ?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                        rs.getString("codigo"),
                        rs.getString("nombre"),
                        rs.getString("talla"),
                        rs.getString("color"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                    );
                }
            }
            return null;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar producto: {0}", e.getMessage());
            throw new SQLException("Error al buscar producto en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza nombre, talla, color, precio y stock de un producto existente.
     *
     * SQL: UPDATE productos SET nombre=?, talla=?, color=?, precio=?, stock=? WHERE codigo=?
     */
    @Override
    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre=?, talla=?, color=?, precio=?, stock=? WHERE codigo=?";

        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, producto.getNombre());
            stmt.setString(2, producto.getTalla());
            stmt.setString(3, producto.getColor());
            stmt.setDouble(4, producto.getPrecio());
            stmt.setInt(5, producto.getStock());
            stmt.setString(6, producto.getCodigo());

            int filas = stmt.executeUpdate();
            logger.log(Level.INFO, "Producto actualizado: {0} ({1} fila(s))", new Object[]{producto.getCodigo(), filas});

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar producto: {0}", e.getMessage());
            throw new SQLException("Error al actualizar producto en BD: " + e.getMessage(), e);
        }
    }
}