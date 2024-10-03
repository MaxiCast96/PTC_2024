package ptc.proyecto.estrella.bella

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaUsuarios
import ptc.proyecto.estrella.bella.ui.crearMensajeHTML
import ptc.proyecto.estrella.bella.ui.enviarCorreo
import java.security.MessageDigest
import java.sql.Connection
import java.sql.PreparedStatement
import java.util.UUID

class activity_signup : AppCompatActivity() {

    private lateinit var animFoto: ImageView
    private lateinit var animCrearCuenta: ImageView
    private lateinit var txtNombre: TextInputLayout
    private lateinit var txtCorreo: TextInputLayout
    private lateinit var txtContraseña: TextInputLayout
    private lateinit var txtConfirmarContraseña: TextInputLayout
    private var imageUri: Uri? = null

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            animCrearCuenta.setImageURI(imageUri)
        }
    }

    override fun onBackPressed() {

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Obtener referencias a los elementos de la UI
        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtContraseña = findViewById(R.id.txtContraseña)
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña)
        animFoto = findViewById(R.id.AnimFoto)
        animCrearCuenta = findViewById(R.id.AnimCrearCuenta)
        val AnimCarga = findViewById<LottieAnimationView>(R.id.AnimCarga)
        val imgGoBack = findViewById<ImageView>(R.id.imgGoBack)

        val btnCrearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val btn_login: Button = findViewById(R.id.btn_login)

        //hacer que al tocar cualquier parte de la pantalla se deseleccionen los edit text
        val rootLayout = findViewById<ConstraintLayout>(R.id.main)

        rootLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentFocus?.let { view ->
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
            false
        }



        val nombreEditText = txtNombre.editText
        val correoEditText = txtCorreo.editText
        val contraseñaEditText = txtContraseña.editText
        val confirmarContraseñaEditText = txtConfirmarContraseña.editText
        nombreEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtNombre.error = null
            }
        })

        correoEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtCorreo.error = null
            }
        })

        contraseñaEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtContraseña.error = null
            }
        })

        confirmarContraseñaEditText?.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtConfirmarContraseña.error = null
            }
        })

        imgGoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        animFoto.setOnClickListener {
            openGallery()
        }

        btnCrearCuenta.setOnClickListener {
            btnCrearCuenta.visibility = View.GONE
            AnimCarga.visibility = View.VISIBLE
            if (validarFormulario()) {
                val codigoVerificacion = generarCodigoVerificacion()
                val correo = txtCorreo.editText?.text.toString().trim()

                // Lanzar una corrutina para enviar el correo
                GlobalScope.launch(Dispatchers.Main) {
                    enviarCorreoVerificacion(correo, codigoVerificacion)

                    // Llamamos a la Activity de confirmación de correo
                    val intent = Intent(this@activity_signup, activity_ConfirmarCorreo::class.java)
                    intent.putExtra("nombre", txtNombre.editText?.text.toString().trim())
                    intent.putExtra("correo", correo)
                    intent.putExtra("contraseña", encriptarSHA256(txtContraseña.editText?.text.toString().trim()))
                    intent.putExtra("fotoPerfil", imageUri.toString())
                    intent.putExtra("codigoVerificacion", codigoVerificacion)
                    startActivity(intent)
                }
            } else {
                btnCrearCuenta.visibility = View.VISIBLE
                AnimCarga.visibility = View.GONE
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

    private suspend fun enviarCorreoVerificacion(correo: String, codigoVerificacion: String) {
        val mensajeHTML = crearMensajeHTML(codigoVerificacion)
        enviarCorreo(
            correo,
            "Confirmación de correo electronico",
            mensajeHTML
        )

        val intent = Intent(this@activity_signup, activity_codigo::class.java)
        intent.putExtra("codigo_recuperacion", codigoVerificacion)
        intent.putExtra("correo_usuario", correo)
        intent.putExtra("correo", correo)
        startActivity(intent)
        finish()

        Toast.makeText(this, "Correo de verificación enviado", Toast.LENGTH_SHORT).show()
    }


    private fun generarCodigoVerificacion(): String {
        return (100000..999999).random().toString()  // Genera un código de 6 dígitos
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
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val AnimCarga = findViewById<LottieAnimationView>(R.id.AnimCarga)

        if (nombre.isEmpty()) {
            txtNombre.error = "El nombre es obligatorio"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else {
            txtNombre.error = null
        }

        if (correo.isEmpty()) {
            txtCorreo.error = "El correo es obligatorio"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            txtCorreo.error = "El correo es invalido"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else {
            txtCorreo.error = null
        }

        if (contraseña.isEmpty()) {
            txtContraseña.error = "La contraseña es obligatoria"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else if (contraseña.length < 6) {
            txtContraseña.error = "La contraseña debe tener al menos 6 caracteres"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else {
            txtContraseña.error = null
        }

        if (confirmarContraseña.isEmpty()) {
            txtConfirmarContraseña.error = "Debes confirmar la contraseña"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else if (contraseña != confirmarContraseña) {
            txtConfirmarContraseña.error = "Las contraseñas no coinciden"
            btnCrearCuenta.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            return false
        } else {
            txtConfirmarContraseña.error = null
        }

        return true
    }

    private suspend fun subirImagenYCrearCuenta() {
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val AnimCarga = findViewById<LottieAnimationView>(R.id.AnimCarga)
        if (imageUri != null) {
            val storageRef = FirebaseStorage.getInstance().reference
            val fileRef = storageRef.child("perfil/${UUID.randomUUID()}.jpg")
            val upload = fileRef.putFile(imageUri!!)

            upload.addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    val fotoPerfil = uri.toString()
                    Log.d("SignupActivity", "Imagen subida exitosamente: $fotoPerfil")
                    GlobalScope.launch(Dispatchers.IO) {
                        crearCuenta(fotoPerfil)
                    }
                }
            }.addOnFailureListener {
                runOnUiThread {
                    Toast.makeText(this, "Error al subir la imagen", Toast.LENGTH_SHORT).show()
                    Log.e("SignupActivity", "Error al subir la imagen", it)
                    btnCrearCuenta.visibility = View.VISIBLE
                    AnimCarga.visibility = View.GONE
                }
            }
        } else {
            Log.d("SignupActivity", "No se seleccionó imagen, usando imagen por defecto")
            GlobalScope.launch(Dispatchers.IO) {
                crearCuenta("https://i.imgur.com/FvJsrt9.png")
            }
        }
    }

    private suspend fun crearCuenta(fotoPerfil: String) {
        val nombre = txtNombre.editText?.text.toString().trim()
        val correo = txtCorreo.editText?.text.toString().trim()
        val contraseña = txtContraseña.editText?.text.toString().trim()
        val contraseñaEncriptada = encriptarSHA256(contraseña)
        val rolId = 1
        val uuid = UUID.randomUUID().toString()
        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val AnimCarga = findViewById<LottieAnimationView>(R.id.AnimCarga)

        val nuevoUsuario = listaUsuarios(uuid, nombre, correo, contraseñaEncriptada, rolId, fotoPerfil)

        val connection = obtenerConexionBD()
        if (connection != null) {
            Log.d("SignupActivity", "Conexión a la base de datos exitosa")
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
                Log.d("SignupActivity", "Cuenta creada exitosamente")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_signup, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@activity_signup, activity_login::class.java)
                    startActivity(intent)
                }
            } else {
                Log.e("SignupActivity", "Error al crear la cuenta")
                btnCrearCuenta.visibility = View.VISIBLE
                AnimCarga.visibility = View.GONE
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_signup, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
            statement.close()
            connection.close()
        } else {
            Log.e("SignupActivity", "Error al conectar con la base de datos")

            withContext(Dispatchers.Main) {
                btnCrearCuenta.visibility = View.VISIBLE
                AnimCarga.visibility = View.GONE
                Toast.makeText(this@activity_signup, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun encriptarSHA256(texto: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(texto.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}

    private fun obtenerConexionBD(): Connection? {
        return try {
            val claseConexion = ClaseConexion()
            claseConexion.cadenaConexion()
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("SignupActivity", "Error al obtener conexión a la base de datos", e)
            null
        }
    }

