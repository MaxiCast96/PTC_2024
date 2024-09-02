package ptc.proyecto.estrella.bella

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import modelo.Reserva
import java.util.Date

class activity_detalle_venta : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venta)

        val reservaId = intent.getIntExtra("reservaId", -1)
        if (reservaId != -1) {
            // Cargar detalles de la reserva usando el reservaId
            val reserva = obtenerDetallesReserva(reservaId)
            mostrarDetalles(reserva)
        }
    }

    private fun obtenerDetallesReserva(reservaId: Int): Reserva {
        // Implementa aquí la lógica para obtener los detalles de la reserva desde la base de datos
        return Reserva(reservaId, "user123", 101, Date(), 20.0, "Tarjeta")
    }

    private fun mostrarDetalles(reserva: Reserva) {
        // Aquí puedes mostrar los detalles de la reserva en los elementos de la interfaz
    }
}
