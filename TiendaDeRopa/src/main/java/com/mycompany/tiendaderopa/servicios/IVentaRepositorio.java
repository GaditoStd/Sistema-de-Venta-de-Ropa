/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.mycompany.tiendaderopa.servicios;

import com.mycompany.tiendaderopa.modelos.Venta;
import java.util.List;

public interface IVentaRepositorio {
    void guardar(Venta venta);
    List<Venta> obtenerTodas();
    Venta buscarPorNumeroFactura(String numeroFactura);
}