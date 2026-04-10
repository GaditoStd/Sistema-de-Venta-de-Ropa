/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Venta;
import java.util.List;

/**
 * Interfaz para el repositorio de ventas.
 * Define las operaciones CRUD para las ventas.
 * @author juanj
 */
public interface IVentaRepositorio {
    /**
     * Guarda una venta en el repositorio.
     * @param venta La venta a guardar.
     */
    void guardar(Venta venta);

    /**
     * Obtiene todas las ventas del repositorio.
     * @return Lista de todas las ventas.
     */
    List<Venta> obtenerTodas();

    /**
     * Busca una venta por su número de factura.
     * @param numeroFactura El número de factura.
     * @return La venta encontrada o null si no existe.
     */
    Venta buscarPorNumeroFactura(String numeroFactura);

    /**
     * Elimina una venta por su número de factura.
     * @param numeroFactura El número de factura de la venta a eliminar.
     */
    void eliminar(String numeroFactura);

    /**
     * Actualiza una venta existente.
     * @param venta La venta actualizada.
     */
    void actualizar(Venta venta);
}