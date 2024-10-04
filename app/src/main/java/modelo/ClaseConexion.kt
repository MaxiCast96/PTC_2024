package modelo

import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.println

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {

            val ip = "jdbc:oracle:thin:@192.168.56.1:1521:xe"
            val usuario = "GUILLE_PTC"
            val contrasena = "holacanola123"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion



        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}