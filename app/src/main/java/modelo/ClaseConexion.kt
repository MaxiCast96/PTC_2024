package modelo

import java.sql.Connection
import java.sql.DriverManager

class ClaseConexion {
    fun cadenaConexion(): Connection? {
        try {
            /*
            Agreguen sus datos de Conexión acá.
            Guarden 2 variables, para cuando trabajen desde casa o en el laboratorio

            IMPORTANTE
            NO OLVIDAR CAMBIAR LA IPv4 Y EL VALOR A RETORNAR EN ClaseConexion
            */

            //Aldo
            //val Aldo = DriverManager.getConnection("", "", "")
            //val LaboratorioAldo = DriverManager.getConnection("", "", "")

            val ip = "jdbc:oracle:thin:@192.168.0.9:1521:xe"
            val usuario = "Nightmare"
            val contrasena = "PTCMID"

            val conexion = DriverManager.getConnection(ip, usuario, contrasena)
            return conexion

            //Guillermo
            //val Guillermo = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.17:1521:xe", "Guille", "fortnite_2017")
            //val LaboratorioGuille = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.17:1521:xe","GUILLE_PTC","holacanola123")

            //Luis
            //val Luis = DriverManager.getConnection("", "", "")
            //val LaboratorioLuis = DriverManager.getConnection("", "", "")

            //Lima
            //val Lima = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521:xe", "Lima_PTC", "del1al6")
            //val LaboratorioLima = DriverManager.getConnection("jdbc:oracle:thin:@10.10.2.70:1521:xe", "Lima_PTC", "del1al6")

            //Rene
            //val Rene = DriverManager.getConnection("", "", "")
            //val LaboratorioRene = DriverManager.getConnection("", "", "")

            //val default = LaboratorioGuille
            //return default

        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}