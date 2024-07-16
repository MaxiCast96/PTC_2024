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
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
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
import java.sql.ResultSet

class activity_login : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtCorreo = findViewById<TextInputLayout>(R.id.txtCorreo)
        val inputCorreo = txtCorreo.editText as TextInputEditText
        val txtContraseña = findViewById<TextInputLayout>(R.id.txtContraseña)
        val inputContraseña = txtContraseña.editText as TextInputEditText
        val btnLogin = findViewById<Button>(R.id.btn_login)

        btnLogin.setOnClickListener {
            val correo = inputCorreo.text.toString()
            val contraseña = inputContraseña.text.toString()

            var valid = true

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                txtCorreo.error = "Correo no válido"
                valid = false
            } else {
                txtCorreo.error = null
            }

            if (contraseña.length < 6) {
                txtContraseña.error = "La contraseña debe tener al menos 6 caracteres"
                valid = false
            } else {
                txtContraseña.error = null
            }

            if (valid) {
                GlobalScope.launch(Dispatchers.Main) {
                    val isValid = withContext(Dispatchers.IO) {
                        verificarCredenciales(correo, contraseña)
                    }
                    if (isValid) {
                        val intent = Intent(this@activity_login, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        txtContraseña.error = "Correo o contraseña incorrectos"
                    }
                }
            }
        }

        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        btnCrearCuenta.setOnClickListener {
            val intent = Intent(this, activity_signup::class.java)
            startActivity(intent)
        }

        val btnRecuContra: Button = findViewById(R.id.btnRecuContra)
        btnRecuContra.setOnClickListener {
            val intent = Intent(this, activity_correo::class.java)
            startActivity(intent)
        }
    }

    private fun verificarCredenciales(correo: String, contraseña: String): Boolean {
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        var isValid = false

        try {
            connection = ClaseConexion().cadenaConexion()
            if (connection != null) {
                val query = "SELECT * FROM Usuarios WHERE email = ?"
                preparedStatement = connection.prepareStatement(query)
                preparedStatement.setString(1, correo)
                resultSet = preparedStatement.executeQuery()

                if (resultSet.next()) {
                    val contraseñaAlmacenada = resultSet.getString("contraseña")
                    val contraseñaIngresadaEncriptada = encriptarSHA256(contraseña)
                    if (contraseñaAlmacenada == contraseñaIngresadaEncriptada) {
                        isValid = true
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }

        return isValid
    }

    private fun encriptarSHA256(texto: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(texto.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}
