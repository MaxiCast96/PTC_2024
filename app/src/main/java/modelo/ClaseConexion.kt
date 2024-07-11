package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            val Guillermo = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.17:1521:xe", "Guille", "fortnite_2017")
            val Laboratorio = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.17:1521:xe","GUILLE_PTC","holacanola123")
            //Pongan sus datos de conexion aqui:
            //val Aldo = DriverManager.getConnection("", "", "")
            //val Lima = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521:xe", "Lima", "del1al6")
            //val Luis = DriverManager.getConnection("", "", "")
            //val Rene = DriverManager.getConnection("", "", "")


            return Laboratorio
        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}