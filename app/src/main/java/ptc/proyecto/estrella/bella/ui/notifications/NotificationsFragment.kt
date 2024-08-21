package ptc.proyecto.estrella.bella.ui.notifications

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaUsuarios
import ptc.proyecto.estrella.bella.R
import ptc.proyecto.estrella.bella.databinding.FragmentNotificationsBinding
import ptc.proyecto.estrella.bella.edit_account_details
import java.sql.Connection
import kotlin.concurrent.thread

class NotificationsFragment(private var Datos: List<listaUsuarios>) : Fragment() {

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
        val btnEditAccount = root.findViewById<Button>(R.id.btnEditAccount)
        val btnEditPayment = root.findViewById<Button>(R.id.btnDetalleFacturaEdit)
        val btnLogout = root.findViewById<Button>(R.id.btnLogout)

        //Labels
        val lblName = root.findViewById<TextView>(R.id.lblNombre)
        val lblEmail = root.findViewById<TextView>(R.id.lblEmail)

        //ImageView
        val imgProfile = root.findViewById<ImageView>(R.id.imgProfilePicture)

        //Obtener Nombre, Correo y Foto de Perfil
        val nameReceived = arguments?.getString("nombre")
        val emailReceived = arguments?.getString("email")
        val pfpReceived = arguments?.getString("foto_perfil")

        //Mostrar Nombre y Correo
        lblName.text = nameReceived
        lblEmail.text = emailReceived
        // TODO:  imgProfile.setImageBitmap(pfpReceived)
        fun getUser(): List<listaUsuarios>{
            val objConnection = ClaseConexion().cadenaConexion()

            //Query Setup
            val statement = objConnection?.createStatement()
            val resultSet = statement?.executeQuery("select * from tbProductosLima")!!

            val listadoUsuarios = mutableListOf<listaUsuarios>()
            while (resultSet.next()){
                val uuid = resultSet.getString("uuid")
                val name = resultSet.getString("nombre")
                val email = resultSet.getString("email")
                val password = resultSet.getString("contraseña")
                val roleId = resultSet.getInt("rol_id")
                val profilePic = resultSet.getString("foto_perfil")
                val usuario = listaUsuarios(uuid, name, email, password, roleId, profilePic)
                listadoUsuarios.add(usuario)


            }
            return listadoUsuarios
        }
        CoroutineScope(Dispatchers.IO).launch{
            val executeFunction = getUser()

            withContext(Dispatchers.Main){

            }
        }
        



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
    companion object {
        lateinit var adapter: NotificationsFragment
    }
    
     */
}