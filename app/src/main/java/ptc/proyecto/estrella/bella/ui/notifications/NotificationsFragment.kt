package ptc.proyecto.estrella.bella.ui.notifications

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

        val btnEditUser = root.findViewById<Button>(R.id.btnEditUser)
        val btnEditName = root.findViewById<Button>(R.id.btnEditName)
        val btnEditPayment = root.findViewById<Button>(R.id.btnDetalleFacturaEdit)

        btnEditUser.setOnClickListener {
            //Editar Usuario
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Editar Usuario")

            //Textbox
            val textBox = EditText(context)
            textBox.setHint("Nombre")
            builder.setView(textBox)

            //Botones
            builder.setPositiveButton("Guardar"){
                    dialog, wich ->
                println("NO TERMINADO")
            }

            builder.setNegativeButton("Cancelar"){
                    dialog, wich ->
                dialog.dismiss()
            }
            builder.show()
        }

        btnEditName.setOnClickListener {
            //Editar Nombre
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Editar Usuario")

            //Textbox
            val textBox = EditText(context)
            textBox.setHint("Nombre")
            builder.setView(textBox)

            //Botones
            builder.setPositiveButton("Guardar"){
                    dialog, wich ->
                println("NO TERMINADO")
            }

            builder.setNegativeButton("Cancelar"){
                    dialog, wich ->
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