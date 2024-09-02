package ptc.proyecto.estrella.bella.ui.home

import RecyclerViewHelpers.PeliculaAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.Pelicula
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val peliculas = mutableListOf<Pelicula>()

        GlobalScope.launch(Dispatchers.IO) {
            val connection = ClaseConexion().cadenaConexion()
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Peliculas")

            while (resultSet?.next() == true) {
                val pelicula = Pelicula(
                    resultSet.getInt("pelicula_id"),
                    resultSet.getString("titulo"),
                    resultSet.getString("descripcion"),
                    resultSet.getInt("duracion"),
                    resultSet.getInt("clasificacion_id"),
                    resultSet.getInt("genero_id"),
                    resultSet.getString("poster"),
                    resultSet.getString("trailer")
                )
                peliculas.add(pelicula)

            }

            withContext(Dispatchers.Main) {
                // Aquí inicializas el RecyclerView y el adaptador
                val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.adapter = PeliculaAdapter(peliculas, requireContext()) // Pasa el contexto al adaptador
                recyclerView.layoutManager = GridLayoutManager(context, 2)

                // Agregamos una película de ejemplo con una URL de imagen conocida
                peliculas.add(
                    Pelicula(
                        999,
                        "Shrek",
                        "Descripción de ejemplo",
                        120,
                        1,
                        1,
                        "https://enfilme.com/img/content/shrek_Enfilme_74n51.jpeg",
                        "https://www.youtube.com/watch?v=W37DlG1i61s"
                    )
                )

                peliculas.add(
                    Pelicula(
                        999,
                        "Deadpool y Wolverine",
                        "Descripción de ejemplo",
                        120,
                        1,
                        1,
                        "https://img-tomatazos.buscafs.com/442321/442321.jpg",
                        "https://www.youtube.com/watch?v=W37DlG1i61s"
                    )
                )

                peliculas.add(
                    Pelicula(
                        999,
                        "Lightyear",
                        "Descripción de ejemplo",
                        120,
                        1,
                        1,
                        "https://lumiere-a.akamaihd.net/v1/images/p_243_ster_lightyear_9209f1b3.jpeg",
                        "https://www.youtube.com/watch?v=W37DlG1i61s"
                    )
                )

                peliculas.add(
                    Pelicula(
                        999,
                        "Los Increibles 2",
                        "Descripción de ejemplo",
                        120,
                        1,
                        1,
                        "https://depor.com/resizer/_ibOEcgLBpoJ7QHACA-5E8-CczM=/620x0/smart/filters:format(jpeg):quality(75)/cloudfront-us-east-1.images.arcpublishing.com/elcomercio/OFFQIVE6KZHX5JVFIDZ2C2HDFU.jpg",
                        "https://www.youtube.com/watch?v=W37DlG1i61s"
                    )
                )

                peliculas.add(
                    Pelicula(
                        999,
                        "Mario Bros. La Pelicula",
                        "Descripción de ejemplo",
                        120,
                        1,
                        1,
                        "https://cloudfront-us-east-1.images.arcpublishing.com/copesa/ZPEQXUEQKFELNAMFUTACTVA6NY.jpeg",
                        "https://www.youtube.com/watch?v=W37DlG1i61s"
                    )
                )

                peliculas.add(
                    Pelicula(
                        999,
                        "Garfield" +
                                ".",
                        "Descripción de ejemplo",
                        120,
                        1,
                        1,
                        "https://imagenes.gatotv.com/categorias/peliculas/posters/garfield_fuera_de_casa.jpg",
                        "https://www.youtube.com/watch?v=W37DlG1i61s"
                    )
                )

                peliculas.add(
                    Pelicula(
                        999,
                        "Iron-Man 3" +
                                ".",
                        "El descarado y brillante Tony Stark, tras ver destruido todo su universo personal, debe encontrar y enfrentarse a un enemigo cuyo poder no conoce límites. Este viaje pondrá a prueba su entereza una y otra vez, y le obligará a confiar en su ingenio.",
                        120,
                        1,
                        1,
                        "https://movieposter.gr/4020-large_default/iron-man-3.jpg",
                        "https://youtu.be/2afDGFEB4cc?si=it1GUhO-Soq2xIno"
                    )
                )

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
