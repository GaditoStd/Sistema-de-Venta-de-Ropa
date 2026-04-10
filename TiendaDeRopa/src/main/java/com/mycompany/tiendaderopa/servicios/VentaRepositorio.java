/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Venta;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author juanj
 */
public class VentaRepositorio implements IVentaRepositorio {

    private final List<Venta> ventas;

    public VentaRepositorio() {
        this.ventas = new ArrayList<>();
    }

    @Override
    public void guardar(Venta venta) {
        ventas.add(venta);
    }

    @Override
    public List<Venta> obtenerTodas() {
        return new ArrayList<>(ventas);
    }

    @Override
    public Venta buscarPorNumeroFactura(String numeroFactura) {
        for (Venta v : ventas) {
            if (v.getNumeroFactura().equals(numeroFactura)) {
                return v;
            }
        }
        return null;
    }
}