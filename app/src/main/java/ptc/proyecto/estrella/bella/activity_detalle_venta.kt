package ptc.proyecto.estrella.bella

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Date

class activity_detalle_venta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venta)

        // Recibe los datos del Intent con las claves correctas
        val reservaId = intent.getIntExtra("reservaId", -1)
        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Usuario Desconocido"
        val nombrePelicula = intent.getStringExtra("nombrePelicula") ?: "Pelicula Desconocida"
        val nombreSala = intent.getStringExtra("nombreSala") ?: "Sala Desconocida"
        val horaFuncion = intent.getStringExtra("horaFuncion") ?: "Hora no disponible"
        val fechaReserva = intent.getLongExtra("fechaReserva", -1L)
        val totalPago = intent.getDoubleExtra("totalPago", 0.0)

        // Convertir la fecha
        val fechaReservaDate = Date(fechaReserva)

        // Configurar el botón de retroceso
        val imgGoBack: ImageView = findViewById(R.id.imgGoBack1)
        imgGoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Mostrar los datos recibidos en los TextViews
        findViewById<TextView>(R.id.txtUsuarioId).text = nombreUsuario
        findViewById<TextView>(R.id.txtPeliculaId).text = nombrePelicula
        findViewById<TextView>(R.id.txtSalaId).text = nombreSala
        findViewById<TextView>(R.id.txtHoraFuncion).text = horaFuncion
        findViewById<TextView>(R.id.txtFechaReserva).text = fechaReservaDate.toString()
        findViewById<TextView>(R.id.txtTotalPago).text = "$totalPago"
    }
}