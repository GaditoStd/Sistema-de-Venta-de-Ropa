package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para el repositorio de clientes.
 * Define las operaciones CRUD para los clientes.
 * 
 * NOTA: Los métodos lanzan SQLException porque ahora operan con BD PostgreSQL.
 * 
 * @author Simon Cardona
 * @version 2.0 - Actualizado para BD
 */
public interface IClienteRepository {
    /**
     * Guarda un cliente en la BD.
     * @param cliente El cliente a guardar.
     * @throws SQLException Si falla la operación en BD.
     */
    void guardar(Cliente cliente) throws SQLException;

    /**
     * Obtiene todos los clientes de la BD.
     * @return Lista de todos los clientes.
     * @throws SQLException Si falla la operación en BD.
     */
    List<Cliente> obtenerTodos() throws SQLException;

    /**
     * Elimina un cliente por su cédula.
     * @param cedula La cédula del cliente a eliminar.
     * @throws SQLException Si falla la operación en BD.
     */
    void eliminar(String cedula) throws SQLException;

    /**
     * Busca un cliente por su cédula.
     * @param cedula La cédula del cliente.
     * @return El cliente encontrado o null si no existe.
     * @throws SQLException Si falla la operación en BD.
     */
    Cliente buscarPorCedula(String cedula) throws SQLException;

    /**
     * Actualiza un cliente existente.
     * @param cliente El cliente con datos actualizados.
     * @throws SQLException Si falla la operación en BD.
     */
    void actualizar(Cliente cliente) throws SQLException;
}
