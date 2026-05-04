package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Venta;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.List;

/**
 * Servicio para manejar operaciones relacionadas con ventas.
 *  Valida número de factura duplicado antes de registrar.
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
     *  Verifica que no exista una venta con el mismo número de factura.
     */
    public void registrarVenta(String numeroFactura, String cedulaCliente) {
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            throw new IllegalArgumentException("El número de factura es obligatorio");
        }
        // Validar duplicado de factura
        if (ventaRepositorio.buscarPorNumeroFactura(numeroFactura) != null) {
            throw new IllegalArgumentException("Ya existe una venta con el número de factura: " + numeroFactura);
        }
        Cliente cliente = clienteRepository.buscarPorCedula(cedulaCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado con cédula: " + cedulaCliente);
        }
        Venta venta = new Venta(numeroFactura, cliente);
        ventaRepositorio.guardar(venta);
    }

    public List<Venta> listarVentas() {
        return ventaRepositorio.obtenerTodas();
    }

    public void eliminarVenta(String numeroFactura) {
        ventaRepositorio.eliminar(numeroFactura);
    }

    public void actualizarVenta(Venta venta) {
        ventaRepositorio.actualizar(venta);
    }

    public Venta buscarVenta(String numeroFactura) {
        return ventaRepositorio.buscarPorNumeroFactura(numeroFactura);
    }
}