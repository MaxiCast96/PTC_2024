package ptc.proyecto.estrella.bella

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import modelo.ClaseConexion
import modelo.listaUsuarios
import java.security.MessageDigest
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.UUID
import kotlin.concurrent.thread

class activity_signup : AppCompatActivity() {

    private lateinit var imgFoto: ImageView
    private lateinit var imgCrearCuenta: ImageView
    private lateinit var txtNombre: TextInputLayout
    private lateinit var txtCorreo: TextInputLayout
    private lateinit var txtContraseña: TextInputLayout
    private lateinit var txtConfirmarContraseña: TextInputLayout
    private var imageUri: Uri? = null

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            imgCrearCuenta.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtContraseña = findViewById(R.id.txtContraseña)
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña)
        imgFoto = findViewById(R.id.AnimFoto)
        imgCrearCuenta = findViewById(R.id.AnimCrearCuenta)

        val btnCrearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val btn_login: Button = findViewById(R.id.btn_login)

        imgFoto.setOnClickListener {
            openGallery()
        }

        btnCrearCuenta.setOnClickListener {
            if (validarFormulario()) {
                subirImagenYCrearCuenta()
            }
        }

        btn_login.setOnClickListener {
            val intent = Intent(this, activity_login::class.java)
            startActivity(intent)
        }

        supportActionBar?.hide()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getResult.launch(intent)
    }

    private fun validarFormulario(): Boolean {
        val nombre = txtNombre.editText?.text.toString().trim()
        val correo = txtCorreo.editText?.text.toString().trim()
        val contraseña = txtContraseña.editText?.text.toString().trim()
        val confirmarContraseña = txtConfirmarContraseña.editText?.text.toString().trim()

        if (nombre.isEmpty()) {
            txtNombre.error = "El nombre es obligatorio"
            return false
        } else {
            txtNombre.error = null
        }

        if (correo.isEmpty()) {
            txtCorreo.error = "El correo es obligatorio"
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.error = "El correo es invalido"
            return false
        } else {
            txtCorreo.error = null
        }

        if (contraseña.isEmpty()) {
            txtContraseña.error = "La contraseña es obligatoria"
            return false
        } else if (contraseña.length < 6) {
            txtContraseña.error = "La contraseña debe tener al menos 6 caracteres"
            return false
        } else {
            txtContraseña.error = null
        }

        if (confirmarContraseña.isEmpty()) {
            txtConfirmarContraseña.error = "Debes confirmar la contraseña"
            return false
        } else if (contraseña != confirmarContraseña) {
            txtConfirmarContraseña.error = "Las contraseñas no coinciden"
            return false
        } else {
            txtConfirmarContraseña.error = null
        }

        return true
    }

    private fun subirImagenYCrearCuenta() {
        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val fileRef = storageRef.child("perfil/${UUID.randomUUID()}.jpg")
            val uploadTask = fileRef.putFile(imageUri!!)

            uploadTask.addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    val fotoPerfil = uri.toString()
                    crearCuenta(fotoPerfil)
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
            }
        } else {
            crearCuenta("https://static-00.iconduck.com/assets.00/profile-default-icon-512x511-v4sw4m29.png")
        }
    }

    private fun crearCuenta(fotoPerfil: String) {
        val nombre = txtNombre.editText?.text.toString().trim()
        val correo = txtCorreo.editText?.text.toString().trim()
        val contraseña = txtContraseña.editText?.text.toString().trim()
        val contraseñaEncriptada = encriptarSHA256(contraseña)
        val rolId = 2
        val uuid = UUID.randomUUID().toString()

        val nuevoUsuario = listaUsuarios(uuid, nombre, correo, contraseñaEncriptada, rolId, fotoPerfil)

        thread {
            val connection = obtenerConexionBD()
            if (connection != null) {
                val sql = "INSERT INTO Usuarios (usuario_id, nombre, email, contraseña, rol_id, foto_perfil) VALUES (?, ?, ?, ?, ?, ?)"
                val statement: PreparedStatement = connection.prepareStatement(sql)
                statement.setString(1, nuevoUsuario.uuid)
                statement.setString(2, nuevoUsuario.nombre)
                statement.setString(3, nuevoUsuario.email)
                statement.setString(4, nuevoUsuario.contraseña)
                statement.setInt(5, nuevoUsuario.rol_id)
                statement.setString(6, nuevoUsuario.foto_perfil)

                val result = statement.executeUpdate()
                if (result > 0) {
                    runOnUiThread {
                        Toast.makeText(this, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, activity_login::class.java)
                        startActivity(intent)
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                    }
                }
                statement.close()
                connection.close()
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun encriptarSHA256(texto: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(texto.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun obtenerConexionBD(): Connection? {
        return try {
            val claseConexion = ClaseConexion()
            claseConexion.cadenaConexion()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
