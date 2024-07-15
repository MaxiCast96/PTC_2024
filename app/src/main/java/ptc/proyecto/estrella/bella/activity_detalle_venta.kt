package ptc.proyecto.estrella.bella

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import ptc.proyecto.estrella.bella.R.*
import java.sql.SQLException

class activity_detalle_venta : AppCompatActivity() {

    private lateinit var txtReserva: TextView
    private lateinit var txtUsuario: TextView
    private lateinit var txtFuncion: TextView
    private lateinit var txtFechaReserva: TextView
    private lateinit var txtPagoTotal: TextView
    private lateinit var txtMetodoPago: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle_venta)

        txtReserva = findViewById(id.txtReservaId)
        txtUsuario = findViewById(id.txtUsuarioId)
        txtFuncion = findViewById(id.txtFuncionId)
        txtFechaReserva = findViewById(id.txtFechaReserva)
        txtPagoTotal = findViewById(id.txtTotalPago)
        txtMetodoPago = findViewById(id.txtMetodoPago)

        // Obtener el ID del ticket enviado desde MainActivity
        val reserva_id = intent.getIntExtra("reserva_id", -1)
        if (reserva_id == -1) {
            // Manejar el caso donde no se recibe correctamente el ID del ticket
            // Puedes mostrar un mensaje de error o regresar a la actividad anterior
            finish()
        } else {
            // Obtener y mostrar la información del ticket desde la base de datos
            obtenerInformacionHistorial(reserva_id)
        }
    }



    private fun obtenerInformacionHistorial(reserva_id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                val statement = objConexion?.createStatement()
                val query = "SELECT * FROM Reservas_PTC WHERE reserva_id = $reserva_id"
                val resultSet = statement?.executeQuery(query)

                // Verificar si se encontró el ticket
                if (resultSet?.next() == true) {
                    // Obtener los datos del ticket
                    val Reserva = resultSet.getString("Reserva")
                    val Usuario = resultSet.getString("Usuario")
                    val Funcion = resultSet.getString("Funcion")
                    val Fecha = resultSet.getString("Fecha")
                    val PagoTotal = resultSet.getString("Pago_Total")
                    val MetodoPago = resultSet.getString("Metodo_Pago")

                    // Actualizar UI en el hilo principal
                    withContext(Dispatchers.Main) {
                        // Mostrar la información en los TextView con títulos
                        txtReserva.text = "Reserva N: $reserva_id"
                        txtUsuario.text = "Descripción del ticket: $Usuario"
                        txtFuncion.text = "Autor: $Funcion"
                        txtFechaReserva.text = "Autor: $Fecha"
                        txtPagoTotal.text = "E-mail: $PagoTotal"
                        txtMetodoPago.text = "Teléfono del Autor: $MetodoPago"
                    }
                }

                // Cerrar conexiones
                resultSet?.close()
                statement?.close()
                objConexion?.close()

            } catch (e: SQLException) {
                e.printStackTrace()
                runOnUiThread() {
                    // Manejar el error de conexión o consulta
                    // Puedes mostrar un mensaje de error al usuario
                    finish()
                }
            }
        }
    }
}