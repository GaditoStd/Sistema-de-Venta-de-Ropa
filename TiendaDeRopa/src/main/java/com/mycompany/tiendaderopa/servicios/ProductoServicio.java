/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Producto;
import java.util.List;
/**
 *
 * @author juanj
 */
public class ProductoServicio {

    private final IProductoRepositorio productoRepositorio;

    public ProductoServicio(IProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public void registrarProducto(String codigo, String nombre, String talla, String color, double precio, int stock) {
        if (codigo == null || codigo.isEmpty()) {
            throw new IllegalArgumentException("El código es obligatorio.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo.");
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

    public void actualizarProducto(String codigo, String nombre, String talla, String color, double precio, int stock) {
        List<Producto> lista = productoRepositorio.obtenerTodos();
        for (Producto p : lista) {
            if (p.getCodigo().equals(codigo)) {
                p.setNombre(nombre);
                p.setTalla(talla);
                p.setColor(color);
                p.setPrecio(precio);
                p.setStock(stock);
                break; 
            }
        }
    }

    // Regla de negocio vital para cuando hagamos el módulo de Ventas
    public boolean reducirStock(String codigo, int cantidadVendida) {
        Producto p = productoRepositorio.buscarPorCodigo(codigo);
        if (p != null && p.getStock() >= cantidadVendida) {
            p.setStock(p.getStock() - cantidadVendida);
            return true; // Se pudo reducir
        }
        return false; // No hay stock suficiente o el producto no existe
    }
}
