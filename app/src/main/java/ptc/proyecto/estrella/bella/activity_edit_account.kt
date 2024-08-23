package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.sql.Connection
import java.sql.PreparedStatement

class activity_edit_account : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var AnimFoto: ImageView
    private lateinit var imgFotoPerfil: ImageView
    private lateinit var imgEditNombre: ImageView
    private lateinit var imgEditCorreo: ImageView
    private var imageUri: Uri? = null
    private lateinit var btnGuardarCambios: Button

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            imgFotoPerfil.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        userViewModel.loadUserInfo(this)

        // Obtener referencias de la UI
        val lblNombre = findViewById<TextView>(R.id.lblNombre)
        val lblEmail = findViewById<TextView>(R.id.lblEmail)
        imgFotoPerfil = findViewById(R.id.AnimCrearCuenta)
        imgEditNombre = findViewById(R.id.imgEditNombre)
        imgEditCorreo = findViewById(R.id.imgEditCorreo)
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios)
        val btnGoBack = findViewById<ImageView>(R.id.imgGoBack)
        val btnContraseña = findViewById<Button>(R.id.btnContraseña)
        AnimFoto = findViewById(R.id.AnimFoto)

        btnGoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnContraseña.setOnClickListener {
            val intent = Intent(this, activity_correo::class.java)
            startActivity(intent)
        }

        AnimFoto.setOnClickListener {
            openGallery()
        }

        imgEditNombre.setOnClickListener {
            showEditDialog("nombre", lblNombre.text.toString()) { nuevoNombre ->
                userViewModel.setUserInfo(nuevoNombre, userViewModel.email.value!!, userViewModel.profilePicture.value!!)
                lifecycleScope.launch {
                    actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre, userViewModel.email.value!!)
                }
            }
        }

        imgEditCorreo.setOnClickListener {
            showEditDialog("correo", lblEmail.text.toString()) { nuevoCorreo ->
                userViewModel.setUserInfo(userViewModel.nombre.value!!, nuevoCorreo, userViewModel.profilePicture.value!!)
                lifecycleScope.launch {
                    actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo, userViewModel.email.value!!)
                }
            }
        }

        btnGuardarCambios.setOnClickListener {
            val nuevoNombre = lblNombre.text.toString()
            val nuevoCorreo = lblEmail.text.toString()

            userViewModel.setUserInfo(nuevoNombre, nuevoCorreo, userViewModel.profilePicture.value!!)

            lifecycleScope.launch {
                actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre, userViewModel.email.value!!)
                actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo, nuevoCorreo)
            }
        }

        // Observar los datos del ViewModel
        userViewModel.nombre.observe(this, { nombre ->
            lblNombre.text = nombre
        })

        userViewModel.email.observe(this, { email ->
            lblEmail.text = email
        })

        userViewModel.profilePicture.observe(this, { profilePicture ->
            Glide.with(this).load(profilePicture).into(imgFotoPerfil)
        })

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getResult.launch(intent)
    }

    private fun showEditDialog(campo: String, valorActual: String, onSave: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_edit_text, null)
        val editText = dialogLayout.findViewById<TextInputEditText>(R.id.editText)
        val titulo = if (campo == "nombre") "Ingresa el nuevo nombre" else "Ingresa el nuevo correo"

        editText.setText(valorActual)
        builder.setTitle(titulo)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoValor = editText.text.toString()
            onSave(nuevoValor)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private suspend fun actualizarUsuarioEnBaseDeDatos(campo: String, nuevoValor: String, emailActual: String) {
        withContext(Dispatchers.IO) {
            val conexion: Connection? = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                val query = "UPDATE usuarios SET $campo = ? WHERE email = ?"
                val preparedStatement: PreparedStatement = conexion.prepareStatement(query)
                preparedStatement.setString(1, nuevoValor)
                preparedStatement.setString(2, emailActual)
                preparedStatement.executeUpdate()

                val commitQuery = "COMMIT"
                val statement = conexion.prepareStatement(commitQuery)
                statement.executeUpdate()

                preparedStatement.close()
                conexion.close()
            } else {
                println("No se pudo establecer conexión con la base de datos.")
            }
        }
    }
}
