/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Producto;
import java.util.List;

/**
 * @author juanj
 */
public class ProductoServicio {

    private final IProductoRepositorio productoRepositorio;

    public ProductoServicio(IProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    //  Hallazgo 11: Valida que nombre, talla y color no estén vacíos
    //  Hallazgo 2 (productos): Verifica duplicado de código
    public void registrarProducto(String codigo, String nombre, String talla, String color, double precio, int stock) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (talla == null || talla.trim().isEmpty()) {
            throw new IllegalArgumentException("La talla es obligatoria.");
        }
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("El color es obligatorio.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        // Verifica duplicado antes de guardar
        if (productoRepositorio.buscarPorCodigo(codigo) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + codigo);
        }
        Producto producto = new Producto(codigo, nombre, talla, color, precio, stock);
        productoRepositorio.guardar(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepositorio.obtenerTodos();
    }

    public void eliminarProducto(String codigo) {
        productoRepositorio.eliminar(codigo);
    }

    // Hallazgo 5: Ahora usa productoRepositorio.actualizar()
    // en lugar de mutar objetos de la copia, respetando el contrato de la interfaz.
    public void actualizarProducto(String codigo, String nombre, String talla, String color, double precio, int stock) {
        Producto producto = new Producto(codigo, nombre, talla, color, precio, stock);
        productoRepositorio.actualizar(producto);
    }

    public boolean reducirStock(String codigo, int cantidadVendida) {
        Producto p = productoRepositorio.buscarPorCodigo(codigo);
        if (p != null && p.getStock() >= cantidadVendida) {
            p.setStock(p.getStock() - cantidadVendida);
            return true;
        }
        return false;
    }
}