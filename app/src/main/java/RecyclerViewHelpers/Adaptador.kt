package RecyclerViewHelpers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import modelo.listaHistorial
import ptc.proyecto.estrella.bella.R

class Adaptador(
    private var Datos: List<listaHistorial>,
    coroutineScope: CoroutineScope
)
    : RecyclerView.Adapter<ViewHolder>(){
    fun actualizarRecyclerView(nuevaLista: List<listaHistorial>) {
        Datos = nuevaLista
        notifyDataSetChanged() // Notifica que hay datos nuevos
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context).inflate(R.layout.activity_item_card, parent, false)
        return ViewHolder(vista)
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


}
