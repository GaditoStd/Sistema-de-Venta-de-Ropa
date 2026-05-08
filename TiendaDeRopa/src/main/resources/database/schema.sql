-- ====================================
-- BASE DE DATOS: TIENDA DE ROPA
-- SCRIPT DE CREACIÓN DE TABLAS
-- ====================================
-- Fecha: 2026-05-08
-- Autor: Sistema de Venta de Ropa
-- Descripción: Estructura de BD PostgreSQL para tienda de ropa

-- Tabla 1: CLIENTES
-- Almacena información de los clientes
CREATE TABLE clientes (
    cedula VARCHAR(12) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(10) NOT NULL
);

-- Tabla 2: PRODUCTOS
-- Inventario de productos disponibles en la tienda
CREATE TABLE productos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    talla VARCHAR(10) NOT NULL,
    color VARCHAR(50) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL CHECK (precio > 0),
    stock INT NOT NULL CHECK (stock >= 0)
);

-- Tabla 3: VENTAS
-- Registro de facturas/transacciones
CREATE TABLE ventas (
    numero_factura VARCHAR(20) PRIMARY KEY,
    fecha DATE NOT NULL,
    cedula_cliente VARCHAR(12) NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_ventas_clientes FOREIGN KEY (cedula_cliente) 
        REFERENCES clientes(cedula)
);

-- Tabla 4: DETALLES_VENTA
-- Detalles de productos en cada venta (relación muchos-a-muchos)
CREATE TABLE detalles_venta (
    id SERIAL PRIMARY KEY,
    numero_factura VARCHAR(20) NOT NULL,
    codigo_producto VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    subtotal DECIMAL(10, 2) NOT NULL,
    CONSTRAINT fk_detalles_ventas FOREIGN KEY (numero_factura) 
        REFERENCES ventas(numero_factura) ON DELETE CASCADE,
    CONSTRAINT fk_detalles_productos FOREIGN KEY (codigo_producto) 
        REFERENCES productos(codigo)
);

-- ====================================
-- FIN DEL SCRIPT
-- ====================================
