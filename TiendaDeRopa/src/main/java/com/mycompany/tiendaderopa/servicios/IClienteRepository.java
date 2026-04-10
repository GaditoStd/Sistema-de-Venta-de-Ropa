/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.List;

/**
 *
 * @author Simon Cardona
 */
public interface IClienteRepository {
    void guardar(Cliente cliente);
    List<Cliente> obtenerTodos();
    void eliminar(String cedula);
}
