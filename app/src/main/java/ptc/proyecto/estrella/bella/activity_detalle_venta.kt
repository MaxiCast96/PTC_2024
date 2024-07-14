package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.Adaptador
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaHistorial
import java.sql.SQLException

class activity_detalle_venta : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_venta)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun obtenerDatos(): List<listaHistorial> {
        try {
            val objConexion = ClaseConexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Reservas_PTC") ?: return emptyList()

            val listaHistorial = mutableListOf<listaHistorial>()

            while (resultSet.next()) {
                val reserva = resultSet.getInt("reserva_id")
                val usuario = resultSet.getInt("usuario_id")
                val funcion = resultSet.getInt("funcion_id")
                val fecha = resultSet.getInt("fecha_reserva")
                val Total = resultSet.getInt("total_pago")
                val metodoPago = resultSet.getInt("metodo_pago")
                val historial = listaHistorial(reserva, usuario, funcion, fecha, Total, metodoPago)
                listaHistorial.add(historial)
            }
            resultSet.close()
            statement?.close()
            objConexion?.close()

            return listaHistorial
        } catch (e: SQLException) {
            e.printStackTrace()
            runOnUiThread {
                Toast.makeText(this, "Error de conexión a la base de datos: ${e.message}", Toast.LENGTH_LONG).show()
            }
            return             emptyList()
        }
    }

    private fun actualizarRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val ejecutarFuncion = obtenerDatos()

            withContext(Dispatchers.Main) {
                val Adaptador = Adaptador(ejecutarFuncion, this)
                findViewById<RecyclerView>(R.id.rcvHistorial).adapter = Adaptador

                // Mostrar u ocultar el mensaje según si hay tickets o no
                if (ejecutarFuncion.isEmpty()) {
                    findViewById<TextView>(R.id.textView3).visibility = View.VISIBLE
                } else {
                    findViewById<TextView>(R.id.textView3).visibility = View.GONE
                }
            }
        }
    }
}