package ptc.proyecto.estrella.bella

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import RecyclerViewHelpers.ReservaAdapter
import modelo.Reserva
import java.util.Date

class fragment_historial : Fragment() {

    private lateinit var reservasRecyclerView: RecyclerView
    private lateinit var reservaAdapter: ReservaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_historial, container, false)

        reservasRecyclerView = view.findViewById(R.id.recyclerViewReservas)
        reservasRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        val reservas = obtenerReservasDesdeBaseDeDatos()
        reservaAdapter = ReservaAdapter(reservas)
        reservasRecyclerView.adapter = reservaAdapter

        return view
    }

    private fun obtenerReservasDesdeBaseDeDatos(): List<Reserva> {
        return listOf(
            Reserva(1, "user123", 101, Date(), 20.0, "Tarjeta"),
            Reserva(2, "user456", 102, Date(), 15.0, "Efectivo")
        )
    }
}
