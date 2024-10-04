package RecyclerViewHelpers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        val animCarga: LottieAnimationView = itemView.findViewById(R.id.AnimCarga)


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


        val requestOptions = RequestOptions()
            .transform(CenterCrop())

        Glide.with(holder.itemView.context)
            .load(pelicula.poster)
            .apply(requestOptions)
            .into(holder.posterImageView)

    }

    override fun getItemCount() = peliculas.size
}
