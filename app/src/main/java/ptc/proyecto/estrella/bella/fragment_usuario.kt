package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class fragment_usuario : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_usuario, container, false)

        // Obtener referencias a los TextViews
        val lblNombre = view.findViewById<TextView>(R.id.lblNombre)
        val lblEmail = view.findViewById<TextView>(R.id.lblEmail)

        val btnCerrarSesion = view.findViewById<Button>(R.id.btnLogout)

        btnCerrarSesion.setOnClickListener {
            val intent = Intent(requireActivity(), activity_login::class.java)
            startActivity(intent)
        }

        // Observar los datos ViewModel
        userViewModel.nombre.observe(viewLifecycleOwner, { nombre ->
            lblNombre.text = nombre
        })

        userViewModel.email.observe(viewLifecycleOwner, { email ->
            lblEmail.text = email
        })

        return view
    }
}
