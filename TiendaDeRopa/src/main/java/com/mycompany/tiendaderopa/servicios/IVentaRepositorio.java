package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Venta;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para el repositorio de ventas.
 * CORRECCIÓN: Se agrega "throws SQLException" a todos los métodos
 * para que la capa de servicio pueda manejar errores de BD correctamente.
 *
 * @version 3.0 - Actualizado para BD PostgreSQL
 */
public interface IVentaRepositorio {

    void guardar(Venta venta) throws SQLException;

    List<Venta> obtenerTodas() throws SQLException;

    Venta buscarPorNumeroFactura(String numeroFactura) throws SQLException;

    void eliminar(String numeroFactura) throws SQLException;

    void actualizar(Venta venta) throws SQLException;
}