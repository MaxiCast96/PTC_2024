package ptc.proyecto.estrella.bella

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import RecyclerViewHelpers.ReservaAdapter
import RecyclerViewHelpers.UserViewModel
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.Reserva
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class fragment_historial : Fragment() {

    private lateinit var reservasRecyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter
    private lateinit var userViewModel: UserViewModel
    private lateinit var textViewNoReservas: TextView
    private val handler = Handler(Looper.getMainLooper())
    private val updateInterval = 1000L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)

        reservasRecyclerView = view.findViewById(R.id.recyclerViewReservas)
        textViewNoReservas = view.findViewById(R.id.textViewNoReservas)
        reservasRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        userViewModel = (activity as MainActivity).userViewModel
        val usuarioId = userViewModel.uuid.value ?: ""

        iniciarActualizacionPeriodica(usuarioId)

        return view
    }

    private fun iniciarActualizacionPeriodica(usuarioId: String) {
        handler.post(object : Runnable {
            override fun run() {
                actualizarReservas(usuarioId)
                handler.postDelayed(this, updateInterval)
            }
        })
    }

    private fun actualizarReservas(usuarioId: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val reservas = obtenerReservasDesdeBaseDeDatos(usuarioId)

            if (reservas.isEmpty()) {
                textViewNoReservas.visibility = View.VISIBLE
                reservasRecyclerView.visibility = View.GONE
            } else {
                textViewNoReservas.visibility = View.GONE
                reservasRecyclerView.visibility = View.VISIBLE
                if (!::reservaAdapter.isInitialized) {
                    // Inicializar el adapter si no está inicializado
                    reservaAdapter = ReservaAdapter(reservas)
                    reservasRecyclerView.adapter = reservaAdapter
                }
            }
        }
    }

    private suspend fun obtenerReservasDesdeBaseDeDatos(usuarioId: String): List<Reserva> {
        val reservas = mutableListOf<Reserva>()

        withContext(Dispatchers.IO) {
            val conexion: Connection? = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                val query = """
                SELECT r.funcionand_id, u.nombre AS nombre_usuario, p.titulo AS nombre_pelicula, 
                       s.nombre AS nombre_sala, r.fecha_reserva, r.total_pago
                FROM Reservas_Android r 
                INNER JOIN Usuarios u ON r.usuario_id = u.usuario_id 
                INNER JOIN Peliculas p ON r.pelicula_id = p.pelicula_id 
                INNER JOIN Salas_PTC s ON r.sala_id = s.sala_id 
                WHERE r.usuario_id = ?
                """
                val preparedStatement: PreparedStatement = conexion.prepareStatement(query)
                preparedStatement.setString(1, usuarioId)
                val resultSet: ResultSet = preparedStatement.executeQuery()

                while (resultSet.next()) {
                    val reservaId = resultSet.getInt("funcionand_id")
                    val nombreUsuario = resultSet.getString("nombre_usuario")
                    val nombrePelicula = resultSet.getString("nombre_pelicula")
                    val nombreSala = resultSet.getString("nombre_sala")
                    val fechaReserva = resultSet.getDate("fecha_reserva")
                    val totalPago = resultSet.getDouble("total_pago")

                    reservas.add(
                        Reserva(
                            reservaId,
                            nombreUsuario,
                            nombrePelicula,
                            nombreSala,
                            fechaReserva,
                            totalPago,
                            "Tarjeta"
                        )
                    )
                }

                resultSet.close()
                preparedStatement.close()
                conexion.close()
            } else {
                println("Error al conectar a la base de datos.")
            }
        }

        return reservas
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
    }
}
