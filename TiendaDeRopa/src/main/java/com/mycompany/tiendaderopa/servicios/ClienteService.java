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

    public void registrarCliente(String cedula, String nombre, String telefono) {

        // --- Validar cédula ---
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula es obligatoria.");
        }
        if (!cedula.trim().matches("\\d+")) {
            throw new IllegalArgumentException("La cédula solo puede contener números, sin letras ni espacios.");
        }
        if (cedula.trim().length() < 6 || cedula.trim().length() > 12) {
            throw new IllegalArgumentException("La cédula debe tener entre 6 y 12 dígitos.");
        }

        // --- Validar nombre ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres.");
        }
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios, sin números ni símbolos.");
        }

        // --- Validar teléfono ---
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        if (!telefono.trim().matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono solo puede contener números, sin letras ni espacios.");
        }
        if (telefono.trim().length() != 10) {
            throw new IllegalArgumentException("El teléfono debe tener exactamente 10 dígitos.");
        }

        // --- Validar duplicado ---
        if (clienteRepository.buscarPorCedula(cedula.trim()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cedula);
        }

        Cliente cliente = new Cliente(cedula.trim(), nombre.trim(), telefono.trim());
        clienteRepository.guardar(cliente);
    }

    public List<Cliente> listarClientes() {
        return clienteRepository.obtenerTodos();
    }

    public void eliminarCliente(String cedula) {
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecciona un cliente para eliminar.");
        }
        if (clienteRepository.buscarPorCedula(cedula.trim()) == null) {
            throw new IllegalArgumentException("No existe un cliente con la cédula: " + cedula);
        }
        clienteRepository.eliminar(cedula.trim());
    }

    public void editarCliente(String cedula, String nombre, String telefono) {

        // --- Validar que el cliente exista ---
        if (cedula == null || cedula.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecciona un cliente de la tabla para actualizar.");
        }
        if (clienteRepository.buscarPorCedula(cedula.trim()) == null) {
            throw new IllegalArgumentException("No existe un cliente con la cédula: " + cedula);
        }

        // --- Validar nombre ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres.");
        }
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y espacios, sin números ni símbolos.");
        }

        // --- Validar teléfono ---
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new IllegalArgumentException("El teléfono es obligatorio.");
        }
        if (!telefono.trim().matches("\\d+")) {
            throw new IllegalArgumentException("El teléfono solo puede contener números, sin letras ni espacios.");
        }
        if (telefono.trim().length() != 10) {
            throw new IllegalArgumentException("El teléfono debe tener exactamente 10 dígitos.");
        }

        Cliente cliente = new Cliente(cedula.trim(), nombre.trim(), telefono.trim());
        clienteRepository.actualizar(cliente);
    }
}