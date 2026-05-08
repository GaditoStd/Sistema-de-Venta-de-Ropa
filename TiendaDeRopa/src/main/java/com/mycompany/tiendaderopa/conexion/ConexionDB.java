package com.mycompany.tiendaderopa.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase centralizada para gestionar la conexión a la base de datos PostgreSQL en Neon.
 * 
 * RESPONSABILIDAD: 
 * - Centralizar credenciales de conexión
 * - Proporcionar conexiones a la BD
 * - Manejar excepciones de conexión
 * 
 * @author Sistema de Venta de Ropa
 */
public class ConexionDB {
    
    // Logger para registrar eventos y errores
    private static final Logger logger = Logger.getLogger(ConexionDB.class.getName());
    
    // ====================================
    // CREDENCIALES DE NEON (PostgreSQL)
    // ====================================
    private static final String HOST = "ep-withered-river-ap8hrn5h-pooler.c-7.us-east-1.aws.neon.tech";
    private static final String PUERTO = "5432";  // Puerto por defecto PostgreSQL
    private static final String BASE_DATOS = "neondb";
    private static final String USUARIO = "neondb_owner";
    private static final String CONTRASEÑA = "npg_MAuJL1NT3KIS";
    
    // URL de conexión completa
    private static final String URL = 
        "jdbc:postgresql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS + 
        "?sslmode=require&channel_binding=require";
    
    /**
     * Constructor privado para evitar instanciación.
     * Esta clase solo debe usarse de forma estática.
     */
    private ConexionDB() {
    }
    
    /**
     * Establece conexión a la base de datos PostgreSQL en Neon.
     * 
     * @return Connection - Objeto de conexión a la BD
     * @throws SQLException - Si falla la conexión a la BD
     * 
     * EJEMPLO DE USO:
     * try {
     *     Connection conn = ConexionDB.conectar();
     *     // Usar la conexión
     * } catch (SQLException e) {
     *     System.err.println("Error de conexión: " + e.getMessage());
     * }
     */
    public static Connection conectar() throws SQLException {
        try {
            // Intenta cargar el driver JDBC de PostgreSQL
            Class.forName("org.postgresql.Driver");
            logger.log(Level.INFO, "Driver PostgreSQL cargado correctamente.");
            
            // Establece la conexión
            Connection conexion = DriverManager.getConnection(URL, USUARIO, CONTRASEÑA);
            logger.log(Level.INFO, "Conexión a BD exitosa: " + BASE_DATOS);
            
            return conexion;
            
        } catch (ClassNotFoundException e) {
            // El driver JDBC de PostgreSQL no está en el classpath
            logger.log(Level.SEVERE, "Error: Driver PostgreSQL no encontrado. " +
                    "Asegúrate de agregar la dependencia en pom.xml", e);
            throw new SQLException("Driver PostgreSQL no disponible", e);
            
        } catch (SQLException e) {
            // Error al conectar a la BD (credenciales incorrectas, BD no disponible, etc)
            logger.log(Level.SEVERE, "Error al conectar a la BD: " + e.getMessage(), e);
            throw new SQLException("No se pudo conectar a la base de datos: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cierra una conexión de forma segura.
     * 
     * @param conexion - La conexión a cerrar
     * 
     * EJEMPLO:
     * try {
     *     Connection conn = ConexionDB.conectar();
     *     // Usar conexión
     * } finally {
     *     ConexionDB.cerrarConexion(conexion);
     * }
     */
    public static void cerrarConexion(Connection conexion) {
        if (conexion != null) {
            try {
                conexion.close();
                logger.log(Level.INFO, "Conexión cerrada correctamente.");
            } catch (SQLException e) {
                logger.log(Level.WARNING, "Error al cerrar la conexión: " + e.getMessage(), e);
            }
        }
    }
}
