/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Producto;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz para el repositorio de productos.
 * CORRECCIÓN: Se agrega "throws SQLException" a todos los métodos
 * para que la capa de servicio pueda manejar errores de BD correctamente.
 *
 * @version 3.0 - Actualizado para BD PostgreSQL
 */
public interface IProductoRepositorio {

    void guardar(Producto producto) throws SQLException;

    List<Producto> obtenerTodos() throws SQLException;

    void eliminar(String codigo) throws SQLException;

    Producto buscarPorCodigo(String codigo) throws SQLException;

    void actualizar(Producto producto) throws SQLException;
}