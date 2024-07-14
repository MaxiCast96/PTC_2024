package ptc.proyecto.estrella.bella.ui.home

import android.content.Intent
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.databinding.FragmentHomeBinding
import ptc.proyecto.estrella.bella.detalle_horarios
import java.sql.SQLException

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
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

//todo el codigo aqui

        val imgIntensamente = root.findViewById<ImageView>(R.id.imgIntensamente)
        val imgUp = root.findViewById<ImageView>(R.id.imgUp)
        val imgMarioBros = root.findViewById<ImageView>(R.id.imgMarioBros)
        val imgMonsterInc = root.findViewById<ImageView>(R.id.imgMonsterInc)
        val imgVenomLetThereBeCarnage = root.findViewById<ImageView>(R.id.imgVenomLetThereBeCarnage)
        val imgVenom = root.findViewById<ImageView>(R.id.imgVenom)
        val imgAcrossTheSpiderverse = root.findViewById<ImageView>(R.id.imgAcrossTheSpiderverse)
        val imgIntoTheSpiderverse = root.findViewById<ImageView>(R.id.imgIntoTheSpiderverse)

        val urlIntensamente = Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fp_insideout_19751_af12286c.jpeg?alt=media&token=362b1c16-1102-4b68-a4d8-9bd68cafda57").into(imgIntensamente)
        val urlUp = Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fup.jpg?alt=media&token=627bd3d6-399e-4b79-9ed9-4618c91aaf28").into(imgUp)
        val urlMarioBros = Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fmariobros.jpg?alt=media&token=2a32fe15-d3e7-4119-a181-3e953d69658d").into(imgMarioBros)
        val urlMonsterInc = Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fmonstersinc.jpg?alt=media&token=ae03a5d3-0131-418f-af7d-ff12e16e69b4").into(imgMonsterInc)
        val urlVenomLetThereBeCarnage = Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fvenom2.jpg?alt=media&token=ddbf588f-61ca-4ce5-91a0-cb3cb60aeaa1").into(imgVenomLetThereBeCarnage)
        val urlVenom= Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fvenom.jpg?alt=media&token=0ca19971-42b0-4f93-8e02-f09c889ba973").into(imgVenom)
        val urlAcrossTheSpiderverse= Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fspidermanacrossthespidervese.jpeg?alt=media&token=e87f94aa-3bb6-43b3-9393-80c75c2f2127").into(imgAcrossTheSpiderverse)
        val urlIntoTheSpiderverse = Glide.with(this).load("https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fspidermanintothespiderverse.jpeg?alt=media&token=a8826668-6649-4126-9bf2-0b7cfc0a8e0e").into(imgIntoTheSpiderverse)

        val uriIntensamente = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fp_insideout_19751_af12286c.jpeg?alt=media&token=362b1c16-1102-4b68-a4d8-9bd68cafda57"
        val uriUp = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fup.jpg?alt=media&token=627bd3d6-399e-4b79-9ed9-4618c91aaf28"
        val uriMarioBros = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fmariobros.jpg?alt=media&token=2a32fe15-d3e7-4119-a181-3e953d69658d"
        val uriMonsterInc = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fmonstersinc.jpg?alt=media&token=ae03a5d3-0131-418f-af7d-ff12e16e69b4"
        val uriVenomLetThereBeCarnage = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fvenom2.jpg?alt=media&token=ddbf588f-61ca-4ce5-91a0-cb3cb60aeaa1"
        val uriVenom = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fvenom.jpg?alt=media&token=0ca19971-42b0-4f93-8e02-f09c889ba973"
        val uriAcrossTheSpiderverse = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fspidermanacrossthespidervese.jpeg?alt=media&token=e87f94aa-3bb6-43b3-9393-80c75c2f2127"
        val uriIntoTheSpiderverse = "https://firebasestorage.googleapis.com/v0/b/cinemanow-ptc-2024.appspot.com/o/posters%2Fspidermanintothespiderverse.jpeg?alt=media&token=a8826668-6649-4126-9bf2-0b7cfc0a8e0e"
        imgIntensamente.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "uriIntensamente",
                uriIntensamente
            )
            startActivity(intent)
        }

        imgUp.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "uriUp",
                uriUp
            )
            startActivity(intent)
        }


        imgMarioBros.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "uriMarioBros",
                uriMarioBros
            )
            startActivity(intent)
        }

        imgMonsterInc.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra("uriMonsterInc",
                uriMonsterInc)
            startActivity(intent)
        }

        imgVenomLetThereBeCarnage.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "uriVenomLetThereBeCarnage",
                uriVenomLetThereBeCarnage
            )
            startActivity(intent)
        }

        imgVenom.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "uriVenom",
                uriVenom
            )
            startActivity(intent)
        }

        imgAcrossTheSpiderverse.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "uriAcrossTheSpiderVerse",
                uriAcrossTheSpiderverse
            )
            startActivity(intent)
        }

        imgIntoTheSpiderverse.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra("uriIntoTheSpiderVerse",
            uriIntoTheSpiderverse)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}