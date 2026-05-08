package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Venta;
import com.mycompany.tiendaderopa.modelos.Cliente;
import com.mycompany.tiendaderopa.modelos.Venta;
import java.sql.SQLException;
import java.util.List;

/**
 * @author juanj
 */
public class VentaServicio {

    private final IVentaRepositorio ventaRepositorio;
    private final IClienteRepository clienteRepository;

    public VentaServicio(IVentaRepositorio ventaRepositorio, IClienteRepository clienteRepository) {
        this.ventaRepositorio = ventaRepositorio;
        this.clienteRepository = clienteRepository;
    }

    /**
     * Registra una nueva venta.
     * @param numeroFactura El número de factura de la venta.
     * @param cedulaCliente La cédula del cliente.
     */
    public void registrarVenta(String numeroFactura, String cedulaCliente) {

        //  Validar número de factura
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException("El número de factura es obligatorio.");
        }
        if (numeroFactura.trim().length() < 3) {
            throw new IllegalArgumentException("El número de factura debe tener al menos 3 caracteres.");
        }

        //  Validar cédula del cliente
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula del cliente es obligatoria.");
        }
        if (!cedulaCliente.trim().matches("\\d+")) {
            throw new IllegalArgumentException("La cédula solo puede contener números.");
        }

        //  Validar factura duplicada
        if (ventaRepositorio.buscarPorNumeroFactura(numeroFactura.trim()) != null) {
            throw new IllegalArgumentException("Ya existe una venta con el número de factura: " + numeroFactura);
        }

        //  Validar que el cliente exista
        Cliente cliente = clienteRepository.buscarPorCedula(cedulaCliente.trim());
        if (cliente == null) {
            throw new IllegalArgumentException("No existe ningún cliente con la cédula: " + cedulaCliente + ". Regístralo primero en la pestaña Clientes.");
        }

        Venta venta = new Venta(numeroFactura.trim(), cliente);
        ventaRepositorio.guardar(venta);
    }

    /**
     * Lista todas las ventas registradas.
     * @return Lista de ventas.
     */
    public List<Venta> listarVentas() {
        return ventaRepositorio.obtenerTodas();
    }

    /**
     * Elimina una venta por su número de factura.
     * @param numeroFactura El número de factura de la venta a eliminar.
     */
    public void eliminarVenta(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecciona una venta para eliminar.");
        }
        if (ventaRepositorio.buscarPorNumeroFactura(numeroFactura.trim()) == null) {
            throw new IllegalArgumentException("No existe una venta con el número de factura: " + numeroFactura);
        }
        ventaRepositorio.eliminar(numeroFactura.trim());
    }

    /**
     * Actualiza una venta existente.
     * @param venta La venta actualizada.
     */
    public void actualizarVenta(Venta venta) {
        if (venta == null) {
            throw new IllegalArgumentException("La venta no puede ser nula.");
        }
        ventaRepositorio.actualizar(venta);
    }

    /**
     * Busca una venta por su número de factura.
     * @param numeroFactura El número de factura.
     * @return La venta encontrada o null si no existe.
     */
    public Venta buscarVenta(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty()) {
            return null;
        }
        return ventaRepositorio.buscarPorNumeroFactura(numeroFactura.trim());
    }
}