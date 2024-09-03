package ptc.proyecto.estrella.bella

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaHorarioFunciones

class detalle_horarios : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_horarios)

        val imgDetalleHorarios: ImageView = findViewById(R.id.imgDetalleHorarios)
        val txtDescripcion: TextView = findViewById(R.id.txtDescripcion)
        val txtElegirHora: TextView = findViewById(R.id.txtElegirHora)
        val spDetalleHorario: Spinner = findViewById(R.id.spDetalleHorario)
        val imgPlayTrailer: ImageView = findViewById(R.id.imgPlayTrailer)
        val imgAtras: ImageView = findViewById(R.id.imgAtras)
        val btnReservarEntradas: Button = findViewById(R.id.btnReservarEntradas)

        // Recibir datos del intent
        val peliculaId = intent.getIntExtra("PELICULA_ID", 0)
        val titulo = intent.getStringExtra("TITULO")
        val descripcion = intent.getStringExtra("DESCRIPCION")
        val duracion = intent.getIntExtra("DURACION", 0)
        val clasificacionId = intent.getIntExtra("CLASIFICACION_ID", 0)
        val generoId = intent.getIntExtra("GENERO_ID", 0)
        val poster = intent.getStringExtra("POSTER")
        val trailer = intent.getStringExtra("TRAILER")

        btnReservarEntradas.setOnClickListener {
            val horaSeleccionada = spDetalleHorario.selectedItem.toString()

            val intent = Intent(this, activity_seleccion_asientos::class.java).apply {
                putExtra("PELICULA_ID", peliculaId)
                putExtra("HORA_SELECCIONADA", horaSeleccionada)
            }
            startActivity(intent)
        }
        // Muestra los datos en la UI
        txtDescripcion.text = descripcion
        Glide.with(this)
            .load(poster)
            .into(imgDetalleHorarios)

        imgAtras.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        imgPlayTrailer.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(trailer))
            startActivity(intent)
        }

        fun obtenerHorarioFuncion(): List<listaHorarioFunciones>{
            val objConexion = ClaseConexion().cadenaConexion()

            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from Horario_Funcion")!!

            val listaHorarioFunciones= mutableListOf<listaHorarioFunciones>()

            while (resultSet.next()){
                val horario_id = resultSet.getInt("horario_id")
                val hora = resultSet.getString("hora")
                val unHorarioCompleto = listaHorarioFunciones(horario_id, hora)
                listaHorarioFunciones.add(unHorarioCompleto)
            }
            return listaHorarioFunciones
        }

        //spinner
        CoroutineScope(Dispatchers.IO).launch {
            val listaHorarioFunciones = obtenerHorarioFuncion()
            val horaFuncion = listaHorarioFunciones.map { it.hora }

            withContext(Dispatchers.Main)  {
                val miAdaptador = ArrayAdapter(this@detalle_horarios, android.R.layout.simple_spinner_dropdown_item, horaFuncion)
                spDetalleHorario.adapter = miAdaptador
            }
        }

    }
}
