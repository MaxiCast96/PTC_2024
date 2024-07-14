package RecyclerViewHelpers

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ptc.proyecto.estrella.bella.R

class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

    val textView: TextView = view.findViewById(R.id.txtDetalles)
    val btnDetalles: Button = view.findViewById(R.id.btnDetalles)

}