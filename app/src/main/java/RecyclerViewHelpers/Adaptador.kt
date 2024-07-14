package RecyclerViewHelpers

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import modelo.listaHistorial
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.activity_detalle_venta

class Adaptador(
    private var Datos: List<listaHistorial>)
    : RecyclerView.Adapter<ViewHolder>(){
    fun actualizarRecyclerView(nuevaLista: List<listaHistorial>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notifica que hay datos nuevos
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }



}
