package ptc.proyecto.estrella.bella.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaUsuarios
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Botones
        val btnEditName = root.findViewById<Button>(R.id.btnEditName)
        val btnEditPayment = root.findViewById<Button>(R.id.btnDetalleFacturaEdit)
        val btnLogout = root.findViewById<Button>(R.id.btnLogout)

        //Labels
        val lblName = root.findViewById<TextView>(R.id.lblNombre)
        val lblEmail = root.findViewById<TextView>(R.id.lblEmail)

        //Obtener Nombre y Correo
        val nameReceived = arguments?.getString("nombre")
        val emailReceived = arguments?.getString("email")

        //Mostrar Nombre y Correo
        lblName.text = nameReceived
        lblEmail.text = emailReceived

        //Editar Nombre
        btnEditName.setOnClickListener {
            //Editar Nombre
            val builder = AlertDialog.Builder(requireContext())
            //Textbox
            val textBox = EditText(context)
            textBox.setHint("Nombre")
            builder.setView(textBox)

            //Botones
            builder.setPositiveButton("Guardar") { dialog, wich ->
                println("NO TERMINADO")
            }
            builder.setNegativeButton("Cancelar") { dialog, wich ->
                dialog.dismiss()
            }
            builder.show()
        }



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}