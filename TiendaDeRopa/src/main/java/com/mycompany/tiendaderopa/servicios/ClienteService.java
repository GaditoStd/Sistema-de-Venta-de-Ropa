package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.List;

/**
 * Servicio para manejar operaciones relacionadas con clientes.
 * @author Simon Cardona
 */
public class ClienteService {

    private final IClienteRepository clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    //Hallazgo 2: Validación de cédula duplicada
    public void registrarCliente(String cedula, String nombre, String telefono) {
        if (cedula == null || cedula.isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria");
        }
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        // Verifica duplicado antes de guardar
        if (clienteRepository.buscarPorCedula(cedula) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cedula);
        }
        Cliente cliente = new Cliente(cedula, nombre, telefono);
        clienteRepository.guardar(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.obtenerTodos();
    }

    public void eliminarCliente(String cedula) {
        clienteRepository.eliminar(cedula);
    }

    // Hallazgo 4: Se elimina actualizarCliente() (código muerto duplicado)
    // y se usa SOLO editarCliente() con las validaciones correctas.
    public void editarCliente(String cedula, String nombre, String telefono) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        Cliente cliente = new Cliente(cedula, nombre, telefono);
        clienteRepository.actualizar(cliente);
    }
}