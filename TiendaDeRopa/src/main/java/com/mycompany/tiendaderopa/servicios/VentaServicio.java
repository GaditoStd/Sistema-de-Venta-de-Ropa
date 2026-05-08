package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Cliente;
import com.mycompany.tiendaderopa.modelos.DetalleVenta;
import com.mycompany.tiendaderopa.modelos.Venta;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para manejar operaciones de Venta.
 *
 * CORRECCIONES v3.1:
 * 1. Se agrega manejo de SQLException en todos los métodos.
 * 2. Se agrega registrarVentaCompleta() — método principal que arma
 *    el objeto Venta con todos sus detalles ANTES de llamar a guardar(),
 *    de modo que VentaRepositorio pueda insertar cabecera + detalles
 *    en una sola transacción atómica.
 *    (El método registrarVenta() anterior solo guardaba la cabecera vacía.)
 */
public class VentaServicio {

    private static final Logger logger = Logger.getLogger(VentaServicio.class.getName());

    private final IVentaRepositorio ventaRepositorio;
    private final IClienteRepository clienteRepository;

    public VentaServicio(IVentaRepositorio ventaRepositorio, IClienteRepository clienteRepository) {
        this.ventaRepositorio = ventaRepositorio;
        this.clienteRepository = clienteRepository;
    }

    /**
     * MÉTODO PRINCIPAL: Registra una venta completa con todos sus detalles.
     * 
     * Flujo correcto:
     * 1. Validar datos
     * 2. Verificar cliente en BD
     * 3. Construir objeto Venta con todos los DetalleVenta del carrito
     * 4. Llamar a ventaRepositorio.guardar() → transacción única (cabecera + detalles)
     *
     * @param numeroFactura  Número de factura
     * @param cedulaCliente  Cédula del cliente
     * @param detalles       Lista de productos del carrito (no puede estar vacía)
     */
    public void registrarVentaCompleta(String numeroFactura, String cedulaCliente,
                                       List<DetalleVenta> detalles) {
        // Validaciones de negocio
        if (numeroFactura == null || numeroFactura.trim().isEmpty())
            throw new IllegalArgumentException("El número de factura es obligatorio.");
        if (numeroFactura.trim().length() < 3)
            throw new IllegalArgumentException("El número de factura debe tener al menos 3 caracteres.");
        if (cedulaCliente == null || cedulaCliente.trim().isEmpty())
            throw new IllegalArgumentException("La cédula del cliente es obligatoria.");
        if (!cedulaCliente.trim().matches("\\d+"))
            throw new IllegalArgumentException("La cédula solo puede contener números.");
        if (detalles == null || detalles.isEmpty())
            throw new IllegalArgumentException("La venta debe tener al menos un producto.");

        try {
            // Verificar factura duplicada
            if (ventaRepositorio.buscarPorNumeroFactura(numeroFactura.trim()) != null)
                throw new IllegalArgumentException("Ya existe una venta con el número: " + numeroFactura);

            // Verificar que el cliente exista
            Cliente cliente = clienteRepository.buscarPorCedula(cedulaCliente.trim());
            if (cliente == null)
                throw new IllegalArgumentException(
                    "No existe ningún cliente con la cédula: " + cedulaCliente +
                    ". Regístralo primero en la pestaña Clientes.");

            // Armar la Venta completa con todos los detalles antes de guardar
            Venta venta = new Venta(numeroFactura.trim(), cliente);
            for (DetalleVenta detalle : detalles) {
                venta.agregarDetalle(detalle); // calcula subtotal y total
            }

            // Una sola llamada → una sola transacción en BD
            ventaRepositorio.guardar(venta);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al registrar venta completa: {0}", e.getMessage());
            throw new RuntimeException("Error al registrar venta en BD: " + e.getMessage(), e);
        }
    }

    public List<Venta> listarVentas() {
        try {
            return ventaRepositorio.obtenerTodas();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al listar ventas: {0}", e.getMessage());
            throw new RuntimeException("Error al obtener ventas de BD: " + e.getMessage(), e);
        }
    }

    public void eliminarVenta(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty())
            throw new IllegalArgumentException("Selecciona una venta para eliminar.");

        try {
            if (ventaRepositorio.buscarPorNumeroFactura(numeroFactura.trim()) == null)
                throw new IllegalArgumentException("No existe una venta con el número: " + numeroFactura);

            ventaRepositorio.eliminar(numeroFactura.trim());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al eliminar venta: {0}", e.getMessage());
            throw new RuntimeException("Error al eliminar venta en BD: " + e.getMessage(), e);
        }
    }

    public void actualizarVenta(Venta venta) {
        if (venta == null)
            throw new IllegalArgumentException("La venta no puede ser nula.");

        try {
            ventaRepositorio.actualizar(venta);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al actualizar venta: {0}", e.getMessage());
            throw new RuntimeException("Error al actualizar venta en BD: " + e.getMessage(), e);
        }
    }

    public Venta buscarVenta(String numeroFactura) {
        if (numeroFactura == null || numeroFactura.trim().isEmpty())
            return null;

        try {
            return ventaRepositorio.buscarPorNumeroFactura(numeroFactura.trim());
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al buscar venta: {0}", e.getMessage());
            throw new RuntimeException("Error al buscar venta en BD: " + e.getMessage(), e);
        }
    }
}