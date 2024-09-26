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
import kotlinx.coroutines.*
import modelo.ClaseConexion
import modelo.Pelicula
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val scope = CoroutineScope(Dispatchers.Main + Job()) // Scope para manejar las corrutinas
    private val peliculas = mutableListOf<Pelicula>()
    private lateinit var peliculaAdapter: PeliculaAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val recyclerView = root.findViewById<RecyclerView>(R.id.recyclerView)
        peliculaAdapter = PeliculaAdapter(peliculas, requireContext())
        recyclerView.adapter = peliculaAdapter
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        // Llama a la función que ejecuta la consulta cada segundo
        startQueryLoop()

        return root
    }

    private fun startQueryLoop() {
        scope.launch(Dispatchers.IO) {
            while (isActive) { // Mantiene la corrutina activa mientras el scope esté activo
                fetchPeliculas() // Ejecuta la consulta
                delay(1000) // Espera 1 segundo antes de la siguiente ejecución
            }
        }
    }

    private suspend fun fetchPeliculas() {
        try {
            val connection = ClaseConexion().cadenaConexion()
            val statement = connection?.createStatement()
            val resultSet = statement?.executeQuery("SELECT * FROM Peliculas")

            // Limpiar lista para evitar duplicados
            peliculas.clear()

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

            // Actualiza el RecyclerView en el hilo principal
            withContext(Dispatchers.Main) {
                peliculaAdapter.notifyDataSetChanged()
            }

            resultSet?.close()
            statement?.close()
            connection?.close()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        // Cancela la corrutina cuando se destruye la vista
        scope.cancel()
    }
}
