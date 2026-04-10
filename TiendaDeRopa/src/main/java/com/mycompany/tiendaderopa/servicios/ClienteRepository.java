/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Simon Cardona
 */
public class ClienteRepository implements IClienteRepository {

    private final List<Cliente> clientes;

    public ClienteRepository() {
        this.clientes = new ArrayList<>();
    }

    @Override
    public void guardar(Cliente cliente) {
        clientes.add(cliente);
    }

    @Override
    public List<Cliente> obtenerTodos() {
        return new ArrayList<>(clientes);
    }
}
