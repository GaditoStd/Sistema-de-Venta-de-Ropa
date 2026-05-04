/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Producto;
import java.util.List;

/**
 * Interfaz para el repositorio de productos.
 * @author juanj
 */
public interface IProductoRepositorio {
    void guardar(Producto producto);

    List<Producto> obtenerTodos();

    void eliminar(String codigo);

    Producto buscarPorCodigo(String codigo);

    //  Hallazgo 5: Se agrega el método que actualiza la interfaz
    void actualizar(Producto producto);
}