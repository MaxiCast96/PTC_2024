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
                val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
                recyclerView.adapter = PeliculaAdapter(peliculas, requireContext())
                recyclerView.layoutManager = GridLayoutManager(context, 2)

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
