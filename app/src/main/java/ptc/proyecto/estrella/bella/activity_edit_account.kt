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
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import ptc.proyecto.estrella.bella.ui.enviarCorreo
import java.sql.Connection
import java.sql.PreparedStatement
import kotlin.random.Random

class activity_edit_account : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var imgFotoPerfil: ImageView
    private lateinit var imgEditNombre: ImageView
    private lateinit var imgEditCorreo: ImageView
    private var imageUri: Uri? = null
    private lateinit var btnGuardarCambios: Button
    private lateinit var storageReference: StorageReference
    private var verificationCode: String = "" // Código de verificación como string

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            imgFotoPerfil.setImageURI(imageUri)

            // No actualizar el ViewModel aquí, solo almacenar la URI localmente
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)
        storageReference = FirebaseStorage.getInstance().reference

        // Obtener referencias de la UI
        val lblNombre = findViewById<TextView>(R.id.lblNombre)
        val lblEmail = findViewById<TextView>(R.id.lblEmail)
        imgFotoPerfil = findViewById(R.id.AnimCrearCuenta)
        imgEditNombre = findViewById(R.id.imgEditNombre)
        imgEditCorreo = findViewById(R.id.imgEditCorreo)
        btnGuardarCambios = findViewById(R.id.btnGuardarCambios)
        val btnGoBack = findViewById<ImageView>(R.id.imgGoBack)
        val btnContraseña = findViewById<Button>(R.id.btnContraseña)

        btnGoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnContraseña.setOnClickListener {
            val intent = Intent(this, activity_correo::class.java)
            startActivity(intent)
        }

        imgFotoPerfil.setOnClickListener {
            openGallery()
        }




        imgEditNombre.setOnClickListener {
            showEditDialog("nombre", lblNombre.text.toString()) { nuevoNombre ->
                userViewModel.setUserInfo(nuevoNombre, userViewModel.email.value ?: "", userViewModel.profilePicture.value, userViewModel.uuid.value ?: "")
                userViewModel.saveUserInfo(this, nuevoNombre, userViewModel.email.value ?: "", userViewModel.profilePicture.value, userViewModel.uuid.value ?: "")
                lblNombre.text = nuevoNombre

                lifecycleScope.launch {
                    actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre)
                }
            }
        }

        imgEditCorreo.setOnClickListener {
            showEditDialog("correo", lblEmail.text.toString()) { nuevoCorreo ->
                // Generar el código de verificación
                verificationCode = generarCodigoVerificacion()

                // Obtener el correo actual del usuario y enviar el código a ese correo
                val correoActual = userViewModel.email.value ?: ""

                // Enviar correo de verificación al correo original
                lifecycleScope.launch {
                    enviarCorreoVerificacion(correoActual, verificationCode)
                }

                // Mostrar un diálogo para ingresar el código de verificación
                mostrarDialogoCodigoVerificacion(nuevoCorreo) { codigoIntroducido ->
                    if (codigoIntroducido == verificationCode) {
                        // Si el código es correcto, actualizar el correo en la base de datos
                        userViewModel.setUserInfo(userViewModel.nombre.value ?: "", nuevoCorreo, userViewModel.profilePicture.value, userViewModel.uuid.value ?: "")
                        userViewModel.saveUserInfo(this, userViewModel.nombre.value ?: "", nuevoCorreo, userViewModel.profilePicture.value, userViewModel.uuid.value ?: "")
                        lblEmail.text = nuevoCorreo

                        // Actualizar en la base de datos
                        lifecycleScope.launch {
                            actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo)
                        }
                    } else {
                        // Si el código es incorrecto, mostrar un mensaje de error
                        Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        btnGuardarCambios.setOnClickListener {
            val nuevoNombre = lblNombre.text.toString()
            val nuevoCorreo = lblEmail.text.toString()

            // Si hay una nueva imagen seleccionada, subirla antes de guardar los cambios
            if (imageUri != null) {
                subirImagenYGuardarDatos(imageUri!!) { imageUrl ->
                    // Actualiza el ViewModel con la URL de la nueva imagen después de subirla
                    guardarCambiosUsuario(nuevoNombre, nuevoCorreo, imageUrl)
                }
            } else {
                // Si no se seleccionó una nueva imagen, guardar los cambios con la imagen actual del ViewModel
                guardarCambiosUsuario(nuevoNombre, nuevoCorreo, userViewModel.profilePicture.value ?: "")
            }
        }




        // Observadores para actualizar la UI
        userViewModel.nombre.observe(this, { nombre ->
            lblNombre.text = nombre
        })

        userViewModel.email.observe(this, { email ->
            lblEmail.text = email
        })

        userViewModel.profilePicture.observe(this, { profilePicture ->
            Glide.with(this).load(profilePicture).into(imgFotoPerfil)
        })

        // Cargar datos
        userViewModel.loadUserInfo(this)
    }

    private fun guardarCambiosUsuario(nuevoNombre: String, nuevoCorreo: String, imageUrl: String) {
        // Actualizar el ViewModel y guardar los cambios en la base de datos
        userViewModel.setUserInfo(
            nuevoNombre,
            nuevoCorreo,
            imageUrl, // Actualiza el ViewModel con la nueva URL de la imagen
            userViewModel.uuid.value ?: ""
        )

        userViewModel.saveUserInfo(this, nuevoNombre, nuevoCorreo, imageUrl, userViewModel.uuid.value ?: "")

        // Actualizar la base de datos
        lifecycleScope.launch {
            try {
                actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre)
                actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo)
                actualizarUsuarioEnBaseDeDatos("foto_perfil", imageUrl)
                Toast.makeText(this@activity_edit_account, "Cambios guardados", Toast.LENGTH_SHORT).show()

                // Regresar a la pantalla principal
                val intent = Intent(this@activity_edit_account, MainActivity::class.java)
                intent.putExtra("openFragment", "fragment_usuario")
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_edit_account, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }



    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getResult.launch(intent)
    }

    private fun subirImagenYGuardarDatos(uri: Uri, onSuccess: (String) -> Unit) {
        val fileReference = storageReference.child("profile_pictures/${System.currentTimeMillis()}.jpg")
        fileReference.putFile(uri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString())
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al obtener el enlace de la imagen.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen.", Toast.LENGTH_LONG).show()
            }
    }

    private fun showEditDialog(campo: String, valorActual: String, onSave: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_edit_text, null)
        val editText = dialogLayout.findViewById<TextView>(R.id.editText)

        // Configurar el diálogo según el campo que se esté editando
        val titulo = if (campo == "nombre") "Ingresa el nuevo nombre" else "Ingresa el nuevo correo"
        builder.setTitle(titulo)

        // Establecer el valor actual en el campo de edición
        editText.text = valorActual

        // Definir los botones del diálogo
        builder.setView(dialogLayout)
        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoValor = editText.text.toString()
            onSave(nuevoValor) // Llamar a la función onSave pasando el nuevo valor ingresado
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        // Mostrar el diálogo
        builder.show()
    }

    private fun generarCodigoVerificacion(): String {
        // Genera un código de 6 dígitos
        return (100000..999999).random().toString()
    }

    private suspend fun enviarCorreoVerificacion(correo: String, codigoVerificacion: String) {
        val mensajeHTML = crearMensajeHTML(codigoVerificacion)
        enviarCorreo(
            correo,
            "Confirmación de correo electrónico",
            mensajeHTML
        )

        Toast.makeText(this, "Correo de verificación enviado", Toast.LENGTH_SHORT).show()
    }

    private fun crearMensajeHTML(codigo: String): String {
        return """
            <html>
            <body>
                <h1>Verificación de correo electrónico</h1>
                <p>Tu código de verificación es: <strong>$codigo</strong></p>
            </body>
            </html>
        """.trimIndent()
    }

    private fun mostrarDialogoCodigoVerificacion(nuevoCorreo: String, onCodeEntered: (String) -> Unit) {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.dialog_edit_text, null)
        val editText = dialogLayout.findViewById<TextView>(R.id.editText)
        // Configurar el diálogo
        builder.setTitle("Código de verificación")
        builder.setView(dialogLayout)
        builder.setPositiveButton("Verificar") { _, _ ->
            val codigoIntroducido = editText.text.toString()
            onCodeEntered(codigoIntroducido)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        // Mostrar el diálogo
        builder.show()
    }

    private suspend fun actualizarUsuarioEnBaseDeDatos(campo: String, nuevoValor: String) {
        val conexion: Connection? = ClaseConexion().cadenaConexion()
        if (conexion != null) {
            val consultaSQL = "UPDATE Usuarios SET $campo = ? WHERE uuid = ?"
            val preparedStatement: PreparedStatement = conexion.prepareStatement(consultaSQL)
            preparedStatement.setString(1, nuevoValor)
            preparedStatement.setString(2, userViewModel.uuid.value)
            preparedStatement.executeUpdate()
        }
    }


}