/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;
import com.mycompany.tiendaderopa.modelos.Venta;
import java.util.ArrayList;
import java.util.List;
/**
 * Implementación del repositorio de ventas usando una lista en memoria.
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
    public void eliminar(String numeroFactura) {
        ventas.removeIf(v -> v.getNumeroFactura().equals(numeroFactura));
    }

    @Override
    public void actualizar(Venta venta) {
        for (int i = 0; i < ventas.size(); i++) {
            if (ventas.get(i).getNumeroFactura().equals(venta.getNumeroFactura())) {
                ventas.set(i, venta);
                break;
            }
        }
    }

    @Override
    public Venta buscarPorNumeroFactura(String numeroFactura) {
        for (Venta venta : ventas) {
            if (venta.getNumeroFactura().equals(numeroFactura)) {
                return venta;
            }
        }
        return null;
    }

}

