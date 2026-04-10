package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Venta;
import com.mycompany.tiendaderopa.modelos.Cliente;
import java.util.List;

/**
 * Servicio para manejar operaciones relacionadas con ventas.
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
     * Registra una nueva venta con el número de factura y la cédula del cliente.
     * @param numeroFactura El número de factura de la venta.
     * @param cedulaCliente La cédula del cliente.
     * @throws IllegalArgumentException Si el número de factura es vacío o el cliente no existe.
     */
    public void registrarVenta(String numeroFactura, String cedulaCliente) {
        if (numeroFactura == null || numeroFactura.isEmpty()) {
            throw new IllegalArgumentException("El número de factura es obligatorio");
        }
        Cliente cliente = clienteRepository.buscarPorCedula(cedulaCliente);
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }
        Venta venta = new Venta(numeroFactura, cliente);
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
        ventaRepositorio.eliminar(numeroFactura);
    }

    /**
     * Actualiza una venta existente.
     * @param venta La venta actualizada.
     */
    public void actualizarVenta(Venta venta) {
        ventaRepositorio.actualizar(venta);
    }

    /**
     * Busca una venta por su número de factura.
     * @param numeroFactura El número de factura.
     * @return La venta encontrada o null si no existe.
     */
    public Venta buscarVenta(String numeroFactura) {
        return ventaRepositorio.buscarPorNumeroFactura(numeroFactura);
    }
}
