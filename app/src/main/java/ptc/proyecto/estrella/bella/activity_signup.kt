package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputLayout
import modelo.ClaseConexion
import modelo.listaUsuarios
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.util.UUID
import kotlin.concurrent.thread

class activity_signup : AppCompatActivity() {

    private lateinit var animFoto: LottieAnimationView
    private lateinit var txtNombre: TextInputLayout
    private lateinit var txtCorreo: TextInputLayout
    private lateinit var txtContraseña: TextInputLayout
    private lateinit var txtConfirmarContraseña: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        txtNombre = findViewById(R.id.txtNombre)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtContraseña = findViewById(R.id.txtContraseña)
        txtConfirmarContraseña = findViewById(R.id.txtConfirmarContraseña)
        animFoto = findViewById(R.id.AnimFoto)

        val btnCrearCuenta: Button = findViewById(R.id.btnCrearCuenta)
        val btn_login: Button = findViewById(R.id.btn_login)

        animFoto.setOnClickListener {
            animFoto.playAnimation()
            // Aquí podría ir la lógica para elegir una imagen de la galería
        }

        btnCrearCuenta.setOnClickListener {
            if (validarFormulario()) {
                crearCuenta()
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

    private fun crearCuenta() {
        val nombre = txtNombre.editText?.text.toString().trim()
        val correo = txtCorreo.editText?.text.toString().trim()
        val contraseña = txtContraseña.editText?.text.toString().trim()
        val fotoPerfil = "https://static-00.iconduck.com/assets.00/profile-default-icon-512x511-v4sw4m29.png"
        val rolId = 1
        val uuid = UUID.randomUUID().toString()

        val nuevoUsuario = listaUsuarios(uuid, nombre, correo, contraseña, rolId, fotoPerfil)

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
