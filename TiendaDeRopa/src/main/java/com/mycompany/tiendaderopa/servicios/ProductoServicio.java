/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Producto;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servicio para manejar operaciones de Producto.
 *
 * CORRECCIONES:
 * 1. Se agrega manejo de SQLException en todos los métodos (antes no existía
 *    porque IProductoRepositorio no declaraba throws SQLException).
 * 2. reducirStock() ahora llama a productoRepositorio.actualizar() para
 *    persistir el nuevo stock en la BD en vez de solo mutar el objeto en memoria.
 *
 * @version 3.0
 */
public class ProductoServicio {

    private static final Logger logger = Logger.getLogger(ProductoServicio.class.getName());
    private final IProductoRepositorio productoRepositorio;

    public ProductoServicio(IProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public void registrarProducto(String codigo, String nombre, String talla,
                                  String color, double precio, int stock) {
        // --- Validaciones de negocio ---
        if (codigo == null || codigo.trim().isEmpty())
            throw new IllegalArgumentException("El código es obligatorio.");
        if (codigo.trim().length() < 2)
            throw new IllegalArgumentException("El código debe tener al menos 2 caracteres.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (nombre.trim().length() < 2)
            throw new IllegalArgumentException("El nombre debe tener al menos 2 caracteres.");
        if (talla == null || talla.trim().isEmpty())
            throw new IllegalArgumentException("La talla es obligatoria.");
        if (color == null || color.trim().isEmpty())
            throw new IllegalArgumentException("El color es obligatorio.");
        if (!color.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"))
            throw new IllegalArgumentException("El color solo puede contener letras.");
        if (precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        if (precio > 99_999_999)
            throw new IllegalArgumentException("El precio no puede superar 99,999,999.");
        if (stock < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        if (stock > 9999)
            throw new IllegalArgumentException("El stock no puede ser mayor a 9,999 unidades.");

        try {
            if (productoRepositorio.buscarPorCodigo(codigo.trim()) != null)
                throw new IllegalArgumentException("Ya existe un producto con el código: " + codigo);

            Producto producto = new Producto(codigo.trim(), nombre.trim(),
                                             talla.trim(), color.trim(), precio, stock);
            productoRepositorio.guardar(producto);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al registrar producto: {0}", e.getMessage());
            throw new RuntimeException("Error al guardar producto en BD: " + e.getMessage(), e);
        }
    }

    public List<Producto> listarProductos() {
        try {
            return productoRepositorio.obtenerTodos();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al listar productos: {0}", e.getMessage());
            throw new RuntimeException("Error al obtener productos de BD: " + e.getMessage(), e);
        }
    }

    public void eliminarProducto(String codigo) {
        if (codigo == null || codigo.trim().isEmpty())
            throw new IllegalArgumentException("Selecciona un producto para eliminar.");

        try {
            if (productoRepositorio.buscarPorCodigo(codigo.trim()) == null)
                throw new IllegalArgumentException("No existe un producto con el código: " + codigo);

            productoRepositorio.eliminar(codigo.trim());

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al eliminar producto: {0}", e.getMessage());
            throw new RuntimeException("Error al eliminar producto en BD: " + e.getMessage(), e);
        }
    }

    public void actualizarProducto(String codigo, String nombre, String talla,
                                   String color, double precio, int stock) {
        if (codigo == null || codigo.trim().isEmpty())
            throw new IllegalArgumentException("Selecciona un producto de la tabla para actualizar.");
        if (nombre == null || nombre.trim().isEmpty())
            throw new IllegalArgumentException("El nombre del producto es obligatorio.");
        if (talla == null || talla.trim().isEmpty())
            throw new IllegalArgumentException("La talla es obligatoria.");
        if (color == null || color.trim().isEmpty())
            throw new IllegalArgumentException("El color es obligatorio.");
        if (!color.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+"))
            throw new IllegalArgumentException("El color solo puede contener letras.");
        if (precio <= 0)
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        if (precio > 99_999_999)
            throw new IllegalArgumentException("El precio no puede superar 99,999,999.");
        if (stock < 0)
            throw new IllegalArgumentException("El stock no puede ser negativo.");
        if (stock > 9999)
            throw new IllegalArgumentException("El stock no puede ser mayor a 9,999 unidades.");

        try {
            if (productoRepositorio.buscarPorCodigo(codigo.trim()) == null)
                throw new IllegalArgumentException("No existe un producto con el código: " + codigo);

            Producto producto = new Producto(codigo.trim(), nombre.trim(),
                                             talla.trim(), color.trim(), precio, stock);
            productoRepositorio.actualizar(producto);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al actualizar producto: {0}", e.getMessage());
            throw new RuntimeException("Error al actualizar producto en BD: " + e.getMessage(), e);
        }
    }

    /**
     * CORRECCIÓN: Antes solo mutaba el objeto en memoria sin persistir el cambio.
     * Ahora llama a productoRepositorio.actualizar() para guardar el nuevo stock en BD.
     *
     * @return false si no hay suficiente stock, true si se redujo correctamente.
     */
    public boolean reducirStock(String codigo, int cantidadVendida) {
        if (cantidadVendida <= 0)
            throw new IllegalArgumentException("La cantidad a vender debe ser mayor a cero.");

        try {
            Producto p = productoRepositorio.buscarPorCodigo(codigo);
            if (p == null)
                throw new IllegalArgumentException("No existe un producto con el código: " + codigo);

            if (p.getStock() < cantidadVendida) {
                return false; // Stock insuficiente
            }

            p.setStock(p.getStock() - cantidadVendida);
            productoRepositorio.actualizar(p); // <-- CORRECCIÓN: persiste el nuevo stock en BD

            return true;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error BD al reducir stock: {0}", e.getMessage());
            throw new RuntimeException("Error al actualizar stock en BD: " + e.getMessage(), e);
        }
    }
}