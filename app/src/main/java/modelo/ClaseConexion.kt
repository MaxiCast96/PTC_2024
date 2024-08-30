package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {

            val ip = "jdbc:oracle:thin:@192.168.1.17:1521:xe"
            val usuario = "Guille_Developer"
            val contrasena = "holacanola123"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion



        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}