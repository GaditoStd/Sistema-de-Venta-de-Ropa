/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Cliente;
import com.mycompany.tiendaderopa.conexion.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementación del repositorio de clientes usando PostgreSQL en Neon.
 * 
 * RESPONSABILIDAD: Acceder a la BD para operaciones CRUD de clientes.
 * - Los datos se persisten en PostgreSQL
 * - Maneja excepciones de BD
 * - Usa PreparedStatement para evitar inyección SQL
 * 
 * @author Simon Cardona
 * @version 2.0 - Migrado a BD PostgreSQL
 */
public class ClienteRepository implements IClienteRepository {

    private static final Logger logger = Logger.getLogger(ClienteRepository.class.getName());

    /**
     * Constructor vacío.
     * No necesita ArrayList porque usa BD.
     */
    public ClienteRepository() {
        // No requiere inicialización (los datos están en BD)
    }

    /**
     * Guarda un cliente en la BD.
     * 
     * SQL: INSERT INTO clientes (cedula, nombre, telefono) VALUES (?, ?, ?)
     * 
     * @param cliente El cliente a guardar
     * @throws SQLException Si falla la inserción en BD
     */
    @Override
    public void guardar(Cliente cliente) throws SQLException {
        String sql = "INSERT INTO clientes (cedula, nombre, telefono) VALUES (?, ?, ?)";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Asignar parámetros (los ? se llenan con datos del cliente)
            stmt.setString(1, cliente.getCedula());
            stmt.setString(2, cliente.getNombre());
            stmt.setString(3, cliente.getTelefono());
            
            // Ejecutar el INSERT
            int filasAfectadas = stmt.executeUpdate();
            
            logger.log(Level.INFO, "Cliente guardado: " + cliente.getCedula() + 
                    " (" + filasAfectadas + " fila(s) insertada(s))");
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al guardar cliente: " + e.getMessage(), e);
            throw new SQLException("Error al guardar cliente en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene todos los clientes de la BD.
     * 
     * SQL: SELECT cedula, nombre, telefono FROM clientes
     * 
     * @return Lista con todos los clientes
     * @throws SQLException Si falla la consulta a BD
     */
    @Override
    public List<Cliente> obtenerTodos() throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT cedula, nombre, telefono FROM clientes";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            // Recorrer resultados y crear objetos Cliente
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("telefono")
                );
                clientes.add(cliente);
            }
            
            logger.log(Level.INFO, "Se obtuvieron " + clientes.size() + " clientes de BD");
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al obtener clientes: " + e.getMessage(), e);
            throw new SQLException("Error al obtener clientes de BD: " + e.getMessage(), e);
        }
        
        return clientes;
    }

    /**
     * Busca un cliente por su cédula.
     * 
     * SQL: SELECT cedula, nombre, telefono FROM clientes WHERE cedula = ?
     * 
     * @param cedula La cédula a buscar
     * @return El cliente si existe, null si no existe
     * @throws SQLException Si falla la consulta a BD
     */
    @Override
    public Cliente buscarPorCedula(String cedula) throws SQLException {
        String sql = "SELECT cedula, nombre, telefono FROM clientes WHERE cedula = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cedula);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Cliente cliente = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("telefono")
                    );
                    logger.log(Level.INFO, "Cliente encontrado: " + cedula);
                    return cliente;
                }
            }
            
            logger.log(Level.INFO, "Cliente no encontrado: " + cedula);
            return null;
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al buscar cliente: " + e.getMessage(), e);
            throw new SQLException("Error al buscar cliente en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un cliente por su cédula.
     * 
     * SQL: DELETE FROM clientes WHERE cedula = ?
     * 
     * @param cedula La cédula del cliente a eliminar
     * @throws SQLException Si falla la eliminación en BD
     */
    @Override
    public void eliminar(String cedula) throws SQLException {
        String sql = "DELETE FROM clientes WHERE cedula = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cedula);
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.log(Level.INFO, "Cliente eliminado: " + cedula + 
                        " (" + filasAfectadas + " fila(s) eliminada(s))");
            } else {
                logger.log(Level.WARNING, "No se encontró cliente para eliminar: " + cedula);
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al eliminar cliente: " + e.getMessage(), e);
            throw new SQLException("Error al eliminar cliente en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Actualiza un cliente existente.
     * 
     * SQL: UPDATE clientes SET nombre = ?, telefono = ? WHERE cedula = ?
     * 
     * @param cliente El cliente con datos actualizados
     * @throws SQLException Si falla la actualización en BD
     */
    @Override
    public void actualizar(Cliente cliente) throws SQLException {
        String sql = "UPDATE clientes SET nombre = ?, telefono = ? WHERE cedula = ?";
        
        try (Connection conn = ConexionDB.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getTelefono());
            stmt.setString(3, cliente.getCedula());
            
            int filasAfectadas = stmt.executeUpdate();
            
            if (filasAfectadas > 0) {
                logger.log(Level.INFO, "Cliente actualizado: " + cliente.getCedula() + 
                        " (" + filasAfectadas + " fila(s) actualizada(s))");
            } else {
                logger.log(Level.WARNING, "No se encontró cliente para actualizar: " + cliente.getCedula());
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error al actualizar cliente: " + e.getMessage(), e);
            throw new SQLException("Error al actualizar cliente en BD: " + e.getMessage(), e);
        }
    }
} 