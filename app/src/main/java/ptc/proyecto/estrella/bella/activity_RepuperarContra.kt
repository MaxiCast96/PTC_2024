package ptc.proyecto.estrella.bella

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ptc.proyecto.estrella.bella.databinding.ActivityMainBinding
import modelo.ClaseConexion
import java.security.MessageDigest
import java.sql.Connection
import java.sql.PreparedStatement

class activity_RepuperarContra : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var correoUsuario: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        correoUsuario = intent.getStringExtra("correo").orEmpty()

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.fragment_usuario
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_repuperar_contra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCambiarContraseña: Button = findViewById(R.id.btnCambiarContraseña)
        val txtNuevaContraseña = findViewById<TextInputLayout>(R.id.txtNuevaContraseña)
        val txtRecuperarNuevaContraseña = findViewById<TextInputLayout>(R.id.txtRecuperarNuevaContraseña)

        // Hacer que al tocar cualquier parte de la pantalla se deseleccionen los EditText
        val rootLayout = findViewById<ConstraintLayout>(R.id.main)
        rootLayout.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                currentFocus?.let { view ->
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                    view.clearFocus()
                }
            }
            false
        }

        btnCambiarContraseña.setOnClickListener {
            val nuevaContraseña = txtNuevaContraseña.editText?.text.toString()
            val confirmarContraseña = txtRecuperarNuevaContraseña.editText?.text.toString()

            if (validarContraseñas(nuevaContraseña, confirmarContraseña, txtNuevaContraseña, txtRecuperarNuevaContraseña)) {
                val contraseñaEncriptada = encriptarSHA256(nuevaContraseña)
                GlobalScope.launch(Dispatchers.Main) {
                    val result = withContext(Dispatchers.IO) {
                        actualizarContraseñaEnBaseDeDatos(correoUsuario, contraseñaEncriptada)
                    }
                    if (result) {
                        val intent = Intent(this@activity_RepuperarContra, activity_login::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@activity_RepuperarContra, "Error al actualizar la contraseña", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun validarContraseñas(
        contraseña: String,
        confirmarContraseña: String,
        txtNuevaContraseña: TextInputLayout,
        txtRecuperarNuevaContraseña: TextInputLayout
    ): Boolean {
        var isValid = true

        if (contraseña.isEmpty()) {
            txtNuevaContraseña.error = "La contraseña es obligatoria"
            isValid = false
        } else if (contraseña.length < 6) {
            txtNuevaContraseña.error = "La contraseña debe tener al menos 6 caracteres"
            isValid = false
        } else {
            txtNuevaContraseña.error = null
        }

        if (confirmarContraseña.isEmpty()) {
            txtRecuperarNuevaContraseña.error = "Por favor, confirme la nueva contraseña"
            isValid = false
        } else if (contraseña != confirmarContraseña) {
            txtRecuperarNuevaContraseña.error = "Las contraseñas no coinciden"
            isValid = false
        } else {
            txtRecuperarNuevaContraseña.error = null
        }

        return isValid
    }

    private fun encriptarSHA256(texto: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(texto.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }

    private fun actualizarContraseñaEnBaseDeDatos(correo: String, nuevaContraseña: String): Boolean {
        var result = false
        try {
            val conexion: Connection? = ClaseConexion().cadenaConexion()
            if (conexion != null) {
                val query = "UPDATE usuarios SET contraseña = ? WHERE email = ?"
                val preparedStatement: PreparedStatement = conexion.prepareStatement(query)
                preparedStatement.setString(1, nuevaContraseña)
                preparedStatement.setString(2, correo)
                Log.d("RecuperarContra","Este es el correo que usaré para cambiar la contraseña: $correo")
                preparedStatement.executeUpdate()

                val commitQuery = "COMMIT"
                val statement = conexion.prepareStatement(commitQuery)
                statement.executeUpdate()

                preparedStatement.close()
                conexion.close()
                result = true
            } else {
                println("No se pudo establecer conexión con la base de datos.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}
