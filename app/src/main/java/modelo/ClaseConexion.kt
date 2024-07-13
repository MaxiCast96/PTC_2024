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
            val Aldo = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.30:1521:xe", "ANDRE_DEVELOPER", "1234")
            //val LaboratorioAldo = DriverManager.getConnection("", "", "")

            //Guillermo
            //val Guillermo = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.17:1521:xe", "Guille", "fortnite_2017")
            //val LaboratorioGuille = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.17:1521:xe","GUILLE_PTC","holacanola123")

            //Luis
            //val Luis = DriverManager.getConnection("", "", "")
            //val LaboratorioLuis = DriverManager.getConnection("", "", "")

            //Lima
            val Lima = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.8:1521:xe", "Lima_PTC", "del1al6")
            val LaboratorioLima = DriverManager.getConnection("jdbc:oracle:thin:@10.10.2.70:1521:xe", "Lima_PTC", "del1al6")

            //Rene
            //val Rene = DriverManager.getConnection("", "", "")
            //val LaboratorioRene = DriverManager.getConnection("", "", "")

<<<<<<< HEAD
<<<<<<< HEAD
<<<<<<< HEAD
            return Aldo
=======
=======
>>>>>>> main
=======
>>>>>>> main
            val default = Guillermo
            return default
>>>>>>> main

        }catch (e: Exception){
            println("El error es este: $e")
            return null
        }
    }
}