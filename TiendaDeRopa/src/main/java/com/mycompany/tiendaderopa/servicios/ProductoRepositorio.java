/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Producto;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author juanj
 */
public class ProductoRepositorio implements IProductoRepositorio {

    private final List<Producto> productos;

    public ProductoRepositorio() {
        this.productos = new ArrayList<>();
    }

    @Override
    public void guardar(Producto producto) {
        productos.add(producto);
    }

    @Override
    public List<Producto> obtenerTodos() {
        return new ArrayList<>(productos); // Retornamos una copia por seguridad
    }

    @Override
    public void eliminar(String codigo) {
        productos.removeIf(p -> p.getCodigo().equals(codigo));
    }

    @Override
    public Producto buscarPorCodigo(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equals(codigo)) {
                return p;
            }
        }
        return null;
    }
}
