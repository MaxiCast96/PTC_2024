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

        val reservaId = intent.getIntExtra("reservaId", -1)
        val peliculaId = intent.getStringExtra("peliculaId") ?: "Desconocido"
        val salaId = intent.getIntExtra("salaId", 1)
        val fechaReserva = intent.getLongExtra("fechaReserva", -1L)
        val totalPago = intent.getDoubleExtra("totalPago", 0.0)
        val metodoPago = intent.getStringExtra("metodoPago") ?: "No disponible"
        val nombreUsuario = intent.getStringExtra("nombreUsuario") ?: "Usuario Desconocido"


        val fechaReservaDate = Date(fechaReserva)

        val imgGoBack: ImageView = findViewById(R.id.imgGoBack1)
        imgGoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Mostrar los datos en los TextViews
        findViewById<TextView>(R.id.txtUsuarioId).text = nombreUsuario // Mostrar el nombre de usuario
        findViewById<TextView>(R.id.txtPeliculaId).text = peliculaId
        findViewById<TextView>(R.id.txtSalaId).text = salaId.toString()
        findViewById<TextView>(R.id.txtHoraFuncion).text = fechaReservaDate.toString()
        findViewById<TextView>(R.id.txtFechaReserva).text = fechaReservaDate.toString()
        findViewById<TextView>(R.id.txtTotalPago).text = "$totalPago"
    }
}
