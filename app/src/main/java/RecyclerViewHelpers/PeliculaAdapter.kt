package RecyclerViewHelpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import modelo.Pelicula
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.detalle_horarios

class PeliculaAdapter(
    private val peliculas: List<Pelicula>,
    private val context: Context // Añadir contexto
) : RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder>() {

    inner class PeliculaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val posterImageView: ImageView = itemView.findViewById(R.id.posterImageView)
        val tituloTextView: TextView = itemView.findViewById(R.id.tituloTextView)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pelicula = peliculas[position]
                    val intent = Intent(context, detalle_horarios::class.java).apply {
                        putExtra("PELICULA_ID", pelicula.peliculaId)
                        putExtra("TITULO", pelicula.titulo)
                        putExtra("DESCRIPCION", pelicula.descripcion)
                        putExtra("DURACION", pelicula.duracion)
                        putExtra("CLASIFICACION_ID", pelicula.clasificacionId)
                        putExtra("GENERO_ID", pelicula.generoId)
                        putExtra("POSTER", pelicula.poster)
                        putExtra("TRAILER", pelicula.trailer)
                    }
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return PeliculaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val pelicula = peliculas[position]
        holder.tituloTextView.text = pelicula.titulo
        Glide.with(holder.itemView.context)
            .load(pelicula.poster)
            .into(holder.posterImageView)
    }

    override fun getItemCount() = peliculas.size
}
