package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Cliente;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para manejar operaciones relacionadas con clientes.
 * 
 * RESPONSABILIDAD:
 * - Validar datos de clientes (reglas de negocio)
 * - Orquestar llamadas al repositorio
 * - Manejar excepciones de BD y convertirlas en excepciones de negocio
 * 
 * @author Simon Cardona
 * @version 2.0 - Manejo de excepciones de BD
 */
public class ClienteService {

    private static final Logger logger = Logger.getLogger(ClienteService.class.getName());
    private final IClienteRepository clienteRepository;

    public ClienteService(IClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    /**
     * Registra un nuevo cliente.
     * Valida datos y guarda en BD.
     * 
     * @param cedula Cédula del cliente
     * @param nombre Nombre del cliente
     * @param telefono Teléfono del cliente
     * @throws IllegalArgumentException Si los datos no cumplen validaciones de negocio
     * @throws RuntimeException Si falla la operación en BD
     */
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

        try {
            // --- Validar duplicado ---
            if (clienteRepository.buscarPorCedula(cedula.trim()) != null) {
                throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cedula);
            }

            Cliente cliente = new Cliente(cedula.trim(), nombre.trim(), telefono.trim());
            clienteRepository.guardar(cliente);
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al registrar cliente: " + e.getMessage(), e);
            throw new RuntimeException("Error al guardar cliente en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Obtiene la lista de todos los clientes.
     * 
     * @return Lista de clientes desde BD
     * @throws RuntimeException Si falla la consulta en BD
     */
    public List<Cliente> listarClientes() {
        try {
            return clienteRepository.obtenerTodos();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al listar clientes: " + e.getMessage(), e);
            throw new RuntimeException("Error al obtener clientes de BD: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un cliente existente.
     * 
     * @param cedula Cédula del cliente a eliminar
     * @throws IllegalArgumentException Si la cédula es vacía o no existe
     * @throws RuntimeException Si falla la operación en BD
     */
    public void eliminarCliente(String cedula) {
        try {
            if (cedula == null || cedula.trim().isEmpty()) {
                throw new IllegalArgumentException("Selecciona un cliente para eliminar.");
            }
            if (clienteRepository.buscarPorCedula(cedula.trim()) == null) {
                throw new IllegalArgumentException("No existe un cliente con la cédula: " + cedula);
            }
            clienteRepository.eliminar(cedula.trim());
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al eliminar cliente: " + e.getMessage(), e);
            throw new RuntimeException("Error al eliminar cliente en BD: " + e.getMessage(), e);
        }
    }

    /**
     * Edita/actualiza un cliente existente.
     * Valida datos y actualiza en BD.
     * 
     * @param cedula Cédula del cliente a actualizar
     * @param nombre Nuevo nombre
     * @param telefono Nuevo teléfono
     * @throws IllegalArgumentException Si los datos no cumplen validaciones
     * @throws RuntimeException Si falla la operación en BD
     */
    public void editarCliente(String cedula, String nombre, String telefono) {
        try {
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
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al editar cliente: " + e.getMessage(), e);
            throw new RuntimeException("Error al actualizar cliente en BD: " + e.getMessage(), e);
        }
    }
}