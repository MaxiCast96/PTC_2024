package modelo

import java.sql.Connection
import java.sql.DriverManager
import java.sql.DriverManager.println

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {

            val ip = "jdbc:oracle:thin:@192.168.186.124:1521:xe"
            val usuario = "EXPO"
            val contrasena = "Estrella_Bella!"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion



        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}