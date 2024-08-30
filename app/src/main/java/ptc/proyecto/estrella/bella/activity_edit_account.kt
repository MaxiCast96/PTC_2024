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
import java.sql.Connection
import java.sql.PreparedStatement

class activity_edit_account : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var imgFotoPerfil: ImageView
    private lateinit var imgEditNombre: ImageView
    private lateinit var imgEditCorreo: ImageView
    private var imageUri: Uri? = null
    private lateinit var btnGuardarCambios: Button
    private lateinit var storageReference: StorageReference

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            imgFotoPerfil.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)
        //Firebase
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
                userViewModel.setUserInfo(nuevoNombre, userViewModel.email.value ?: "", userViewModel.profilePicture.value)
                userViewModel.saveUserInfo(this, nuevoNombre, userViewModel.email.value ?: "", userViewModel.profilePicture.value)
                lblNombre.text = nuevoNombre

                lifecycleScope.launch {
                    actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre)
                }
            }
        }

        imgEditCorreo.setOnClickListener {
            showEditDialog("correo", lblEmail.text.toString()) { nuevoCorreo ->
                userViewModel.setUserInfo(userViewModel.nombre.value ?: "", nuevoCorreo, userViewModel.profilePicture.value)
                userViewModel.saveUserInfo(this, userViewModel.nombre.value ?: "", nuevoCorreo, userViewModel.profilePicture.value)
                lblEmail.text = nuevoCorreo

                lifecycleScope.launch {
                    actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo)
                }
            }
        }

        btnGuardarCambios.setOnClickListener {
            val nuevoNombre = lblNombre.text.toString()
            val nuevoCorreo = lblEmail.text.toString()

            if (imageUri != null) {
                subirImagenYGuardarDatos(imageUri!!) { imageUrl ->
                    lifecycleScope.launch {
                        try {
                            actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre)
                            actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo)
                            actualizarUsuarioEnBaseDeDatos("profilePicture", imageUrl)
                            Toast.makeText(this@activity_edit_account, "Cambios guardados", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@activity_edit_account, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                lifecycleScope.launch {
                    try {
                        actualizarUsuarioEnBaseDeDatos("nombre", nuevoNombre)
                        actualizarUsuarioEnBaseDeDatos("email", nuevoCorreo)
                        Toast.makeText(this@activity_edit_account, "Cambios guardados", Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@activity_edit_account, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }

        // Actualizar la UI cuando los datos cambian
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
        val titulo = if (campo == "nombre") "Ingresa el nuevo nombre" else "Ingresa el nuevo correo"

        editText.text = valorActual
        builder.setTitle(titulo)
        builder.setView(dialogLayout)
        builder.setPositiveButton("Guardar") { _, _ ->
            val nuevoValor = editText.text.toString()
            onSave(nuevoValor)
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    private suspend fun actualizarUsuarioEnBaseDeDatos(campo: String, nuevoValor: String) {
        withContext(Dispatchers.IO) {
            val conexion: Connection? = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                val query = "UPDATE usuarios SET $campo = ? WHERE email = ?"
                val preparedStatement: PreparedStatement = conexion.prepareStatement(query)
                preparedStatement.setString(1, nuevoValor)
                preparedStatement.setString(2, userViewModel.email.value ?: "")
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
