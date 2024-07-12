package ptc.proyecto.estrella.bella.ui.home

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.databinding.FragmentHomeBinding
import ptc.proyecto.estrella.bella.detalle_horarios

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


        imgIntensamente.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)

            intent.putExtra(
                "imagenIntensamente"
                item.imagenIntensamente
            )
            startActivity(intent)
        }

        imgUp.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenUp"
                        item.imagenUp
            )
            startActivity(intent)
        }


        imgMarioBros.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenMarioBros"
                        item.imagenMarioBros
            )
            startActivity(intent)
        }

        imgMonsterInc.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenMonsterInc"
                        item.imagenMonsterInc
            )
            startActivity(intent)
        }

        imgVenomLetThereBeCarnage.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenVenomLetThereBeCarnage"
                        item.imagenVenomLetThereBeCarnage
            )
            startActivity(intent)
        }

        imgVenom.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenVenom"
                        item.imagenVenom
            )
            startActivity(intent)
        }

        imgAcrossTheSpiderverse.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenAcrossTheSpiderverse"
                        item.imagenAcrossTheSpiderverse
            )
            startActivity(intent)
        }

        imgIntoTheSpiderverse.setOnClickListener {
            val intent = Intent(requireContext(), detalle_horarios::class.java)
            intent.putExtra(
                "imagenIntoTheSpiderverse"
                        item.imagenIntoTheSpiderverse
            )
            startActivity(intent)
        }








        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}