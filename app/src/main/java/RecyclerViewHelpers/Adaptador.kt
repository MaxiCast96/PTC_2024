package RecyclerViewHelpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import modelo.listaHistorial
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.activity_detalle_venta

class Adaptador(
    private var Datos: List<listaHistorial>,
    coroutineScope: CoroutineScope,
) : RecyclerView.Adapter<ViewHolder>() {

    fun actualizarRecyclerView(nuevaLista: List<listaHistorial>) {
        Datos = nuevaLista
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount() = Datos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val Detalles = Datos[position]
        holder.textView.text = Detalles.reserva_id.toString()

        holder.btnDetalles.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, activity_detalle_venta::class.java)
            intent.putExtra("reserva_id", Detalles.reserva_id)
            context.startActivity(intent)
        }
    }
}
