package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {

            val ip = "jdbc:oracle:thin:@10.10.0.144:1521:xe"
            val usuario = "system"
            val contrasena = "ITR2024"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion



        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}