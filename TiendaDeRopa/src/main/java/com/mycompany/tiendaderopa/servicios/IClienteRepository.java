/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.List;

/**
 * Interfaz para el repositorio de clientes.
 * Define las operaciones CRUD para los clientes.
 * @author Simon Cardona
 */
public interface IClienteRepository {
    /**
     * Guarda un cliente en el repositorio.
     * @param cliente El cliente a guardar.
     */
    void guardar(Cliente cliente);

    /**
     * Obtiene todos los clientes del repositorio.
     * @return Lista de todos los clientes.
     */
    List<Cliente> obtenerTodos();

    /**
     * Elimina un cliente por su cédula.
     * @param cedula La cédula del cliente a eliminar.
     */
    void eliminar(String cedula);

    /**
     * Busca un cliente por su cédula.
     * @param cedula La cédula del cliente.
     * @return El cliente encontrado o null si no existe.
     */
    Cliente buscarPorCedula(String cedula);
    /**
     * Actualiza la información de un cliente existente.
     * @param cliente El cliente con la información actualizada.
     */
    void actualizar(Cliente cliente);  
}
