package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaAseintos
import modelo.listaHorarioFunciones
import modelo.listaSalas_PTC


class activity_seleccion_asientos : AppCompatActivity() {

    companion object VariablesGloables {
        lateinit var fila: String
        lateinit var numero: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_seleccion_asientos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets



        }




        val btnContinuarAlPago = findViewById<Button>(R.id.btnContinuarAlPago)
        val spSeleccionarSala = findViewById<Spinner>(R.id.spSeleccionarSala)
        val spSeleccionarAsientos = findViewById<Spinner>(R.id.spSeleccionarAsientos)
        val imgAtrasSalas = findViewById<ImageView>(R.id.imgAtrasSalas)

        btnContinuarAlPago.setOnClickListener {
            val intent = Intent(this, activity_pago::class.java)
            startActivity(intent)
        }

        imgAtrasSalas.setOnClickListener{
            val intent = Intent(this, detalle_horarios ::class.java)
            startActivity(intent)
        }


        btnContinuarAlPago.setOnClickListener {
            val intent = Intent(this, activity_pago::class.java)
            startActivity(intent)
        }

        fun obtenerSalaPTC(): List<listaSalas_PTC>{
            val objConexion = ClaseConexion().cadenaConexion()

            //Creo un Statement que me ejecutará el select
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from Salas_PTC")!!

            val listaSalas = mutableListOf<listaSalas_PTC>()

            while (resultSet.next()){
                val sala_id = resultSet.getInt("sala_id")
                val nombre = resultSet.getString("nombre")
                val capacidad_asientos = resultSet.getInt("capacidad_asientos")
                val unaSalaCompleta = listaSalas_PTC(sala_id, nombre, capacidad_asientos)
                listaSalas.add(unaSalaCompleta)
            }
            return listaSalas
        }

        //Programar el spinner para que me muestre los datos del select
        CoroutineScope(Dispatchers.IO).launch {
            //1- Obtengo los datos
            val listaSalas = obtenerSalaPTC()
            val nombre = listaSalas.map { it.nombre }

            withContext(Dispatchers.Main)  {
                //2- Crear y modificar el adaptador
                val miAdaptador = ArrayAdapter(this@activity_seleccion_asientos, android.R.layout.simple_spinner_dropdown_item, nombre)
                spSeleccionarSala.adapter = miAdaptador
            }
        }

        fun obtenerlistaAseintos(): List<listaAseintos>{
            val objConexion = ClaseConexion().cadenaConexion()

            //Creo un Statement que me ejecutará el select
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from Asientos")!!

            val listaAsientos = mutableListOf<listaAseintos>()

            while (resultSet.next()){
                val asiento_id = resultSet.getInt("asiento_id")
                val sala_id = resultSet.getInt("sala_id")
                 fila = resultSet.getString("fila")
                 numero = resultSet.getString("numero")
                val unAsientoCompleto = listaAseintos(asiento_id, sala_id, fila, numero.toInt())
                listaAsientos.add(unAsientoCompleto)
            }
            return listaAsientos
        }


        //Programar el spinner para que me muestre los datos del select
        CoroutineScope(Dispatchers.IO).launch {
            //1- Obtengo los datos
            val listaAsientos = obtenerlistaAseintos()

            val Asientoss = listaAsientos.map { it.fila }


            withContext(Dispatchers.Main)  {
                //2- Crear y modificar el adaptador
                val miAdaptador = ArrayAdapter(this@activity_seleccion_asientos, android.R.layout.simple_spinner_dropdown_item, Asientoss)
                spSeleccionarAsientos.adapter = miAdaptador
            }
        }

    }
}

