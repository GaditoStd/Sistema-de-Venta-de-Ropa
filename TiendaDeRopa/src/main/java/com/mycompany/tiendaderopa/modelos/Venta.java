/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.tiendaderopa.modelos;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author juanj
 */
public class Venta {
    private String numeroFactura;
    private LocalDate fecha;
    private Cliente cliente;
    private List<DetalleVenta> detalles;
    private double total;

    // Constructor vacío por defecto
    public Venta() {
        this.detalles = new ArrayList<>();
        this.fecha = LocalDate.now(); // Asigna la fecha actual automáticamente
    }

    // Constructor principal
    public Venta(String numeroFactura, Cliente cliente) {
        this.numeroFactura = numeroFactura;
        this.cliente = cliente;
        this.fecha = LocalDate.now();
        this.detalles = new ArrayList<>();
        this.total = 0.0;
    }

    // Método vital: Agrega un detalle a la lista y recalcula el total general
    public void agregarDetalle(DetalleVenta detalle) {
        if (detalle != null) {
            this.detalles.add(detalle);
            this.total = calcularTotal();
        }
    }

    // Método para sumar todos los subtotales de la lista
    public final double calcularTotal() {
        double suma = 0.0;
        for (DetalleVenta d : detalles) {
            suma += d.getSubtotal();
        }
        return suma;
    }

        public String getNumeroFactura() {
            return numeroFactura;
        }

        public void setNumeroFactura(String numeroFactura) {
            this.numeroFactura = numeroFactura;
        }

        public LocalDate getFecha() {
            return fecha;
        }

        public void setFecha(LocalDate fecha) {
            this.fecha = fecha;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public void setCliente(Cliente cliente) {
            this.cliente = cliente;
        }

        public List<DetalleVenta> getDetalles() {
            return detalles;
        }

        public void setDetalles(List<DetalleVenta> detalles) {
            this.detalles = detalles;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }
    
    }

