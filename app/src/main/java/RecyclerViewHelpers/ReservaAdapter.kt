package RecyclerViewHelpers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import modelo.Reserva
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.activity_detalle_venta

class ReservaAdapter(private val reservas: List<Reserva>) :
    RecyclerView.Adapter<ReservaAdapter.ReservaViewHolder>() {

    inner class ReservaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDetalles: TextView = itemView.findViewById(R.id.txtDetalles)
        val btnDetalles: Button = itemView.findViewById(R.id.btnDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ReservaViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservaViewHolder, position: Int) {
        val reserva = reservas[position]
        holder.txtDetalles.text = "Fecha de Compra: ${reserva.fechaReserva}"

        holder.btnDetalles.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, activity_detalle_venta::class.java)
            intent.putExtra("reservaId", reserva.reservaId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = reservas.size
}
