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

    public void registrarProducto(String codigo, String nombre, String talla, String color, double precio, int stock) {

        // --- Validar código ---
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (codigo.trim().length() < 2) {
            throw new IllegalArgumentException("El código debe tener al menos 2 caracteres.");
        }

        // --- Validar nombre ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }
        if (nombre.trim().length() < 2) {
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres.");
        }

        // --- Validar talla ---
        if (talla == null || talla.trim().isEmpty()) {
            throw new IllegalArgumentException("La talla es obligatoria.");
        }

        // --- Validar color ---
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("El color es obligatorio.");
        }
        if (!color.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El color solo puede contener letras, sin números ni símbolos.");
        }

        // --- Validar precio ---
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (precio > 99_999_999) {
            throw new IllegalArgumentException("El precio no puede superar 99,999,999.");
        }

        // --- Validar stock ---
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (stock > 9999) {
            throw new IllegalArgumentException("El stock no puede ser mayor a 9,999 unidades.");
        }

        // --- Validar duplicado ---
        if (productoRepositorio.buscarPorCodigo(codigo.trim()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código: " + codigo);
        }

        Producto producto = new Producto(codigo.trim(), nombre.trim(), talla.trim(), color.trim(), precio, stock);
        productoRepositorio.guardar(producto);
    }

    public List<Producto> listarProductos() {
        return productoRepositorio.obtenerTodos();
    }

    public void eliminarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecciona un producto para eliminar.");
        }
        if (productoRepositorio.buscarPorCodigo(codigo.trim()) == null) {
            throw new IllegalArgumentException("No existe un producto con el código: " + codigo);
        }
        productoRepositorio.eliminar(codigo.trim());
    }

    public void actualizarProducto(String codigo, String nombre, String talla, String color, double precio, int stock) {

        // --- Validar que el producto exista ---
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalArgumentException("Selecciona un producto de la tabla para actualizar.");
        }
        if (productoRepositorio.buscarPorCodigo(codigo.trim()) == null) {
            throw new IllegalArgumentException("No existe un producto con el código: " + codigo);
        }

        // --- Validar nombre ---
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        }

        // --- Validar talla ---
        if (talla == null || talla.trim().isEmpty()) {
            throw new IllegalArgumentException("La talla es obligatoria.");
        }

        // --- Validar color ---
        if (color == null || color.trim().isEmpty()) {
            throw new IllegalArgumentException("El color es obligatorio.");
        }
        if (!color.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El color solo puede contener letras, sin números ni símbolos.");
        }

        // --- Validar precio ---
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (precio > 99_999_999) {
            throw new IllegalArgumentException("El precio no puede superar 99,999,999.");
        }

        // --- Validar stock ---
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        }
        if (stock > 9999) {
            throw new IllegalArgumentException("El stock no puede ser mayor a 9,999 unidades.");
        }

        Producto producto = new Producto(codigo.trim(), nombre.trim(), talla.trim(), color.trim(), precio, stock);
        productoRepositorio.actualizar(producto);
    }

    public boolean reducirStock(String codigo, int cantidadVendida) {
        if (cantidadVendida <= 0) {
            throw new IllegalArgumentException("La cantidad a vender debe ser mayor a cero.");
        }
        Producto p = productoRepositorio.buscarPorCodigo(codigo);
        if (p == null) {
            throw new IllegalArgumentException("No existe un producto con el código: " + codigo);
        }
        if (p.getStock() < cantidadVendida) {
            return false;
        }
        p.setStock(p.getStock() - cantidadVendida);
        return true;
    }
}