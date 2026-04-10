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
public class ClienteService {

    private final IClienteRepository clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public void registrarCliente(String cedula, String nombre, String telefono) {

        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }

        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        Cliente cliente = new Cliente(cedula, nombre, telefono);
        clienteRepository.guardar(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.obtenerTodos();
    }
}
