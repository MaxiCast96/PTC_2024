package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide

class fragment_usuario : Fragment() {
    private val userViewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_usuario, container, false)

        // Obtener referencias a los TextViews y ImageView
        val lblNombre = view.findViewById<TextView>(R.id.lblNombre)
        val lblEmail = view.findViewById<TextView>(R.id.lblEmail)
        val imgProfilePicture = view.findViewById<ImageView>(R.id.imgProfilePicture)
        val btnCerrarSesion = view.findViewById<Button>(R.id.btnLogout)
        val btnEditAccount = view.findViewById<Button>(R.id.btnEditAccount)

        btnEditAccount.setOnClickListener {
            val intent = Intent(requireContext(), activity_edit_account::class.java)
            startActivity(intent)

        }

        btnCerrarSesion.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Cierre de sesión")
                .setMessage("¿Estás seguro de que deseas cerrar sesión?")
                .setPositiveButton("Sí") { dialogInterface, _ ->
                    // Acción al confirmar cierre de sesión
                    val intent = Intent(requireActivity(), activity_login::class.java)
                    startActivity(intent)
                    dialogInterface.dismiss()
                }
                .setNegativeButton("No") { dialogInterface, _ ->
                    // Acción al cancelar
                    dialogInterface.dismiss()
                }
                .show()
        }

        // Observar los datos ViewModel
        userViewModel.nombre.observe(viewLifecycleOwner, { nombre ->
            lblNombre.text = nombre
        })

        userViewModel.email.observe(viewLifecycleOwner, { email ->
            lblEmail.text = email
        })

        userViewModel.profilePicture.observe(viewLifecycleOwner, { profilePicture ->
            Glide.with(this).load(profilePicture).into(imgProfilePicture)
        })

        return view
    }
}
