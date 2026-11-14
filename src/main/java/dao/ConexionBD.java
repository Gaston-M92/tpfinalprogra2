package dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Centraliza la conexion con la base de datos MySQL.
 * Los datos de conexion se leen desde el archivo
 * db.properties que no se sube al repositorio
 */

public class ConexionBD {
    private static final String ARCHIVO_CONFIG = "db.properties";

    private static String url;
    private static String usuario;
    private static String clave;

    // La clase solo se usa a traves de sus metodos estaticos

    private ConexionBD(){

    }

    /**
     * Abre y devuelve una nueva conexión con la base de datos.
     * Quien la usa es responsable de cerrarla (idealmente con try-with-resources).
     *
     * @return una conexión abierta a turnos_db
     * @throws SQLException si no se puede leer la configuración o conectar
     */

    public static Connection getConexion() throws SQLException{
        if(url == null){
            cargarConfiguracion();
        }
        return DriverManager.getConnection(url,usuario, clave);
    }

    /**
     * Lee url, usuario y clave desde db.properties.
     *
     * @throws SQLException si el archivo no existe o no se puede leer
     */

    private static void cargarConfiguracion() throws SQLException{
        try(InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream(ARCHIVO_CONFIG)){
            if(entrada == null){
                throw new SQLException("No se encontro" + ARCHIVO_CONFIG + "en src/main/resources. Copia db.properties.example y completa tu clave.");
            }
            Properties propiedades = new Properties();
            propiedades.load(entrada);
            url = propiedades.getProperty("db.url");
            usuario = propiedades.getProperty("db.usuario");
            clave = propiedades.getProperty("db.clave");
        } catch (IOException e){
            throw new SQLException("Error al leer " + ARCHIVO_CONFIG, e);
        }
    }

}
