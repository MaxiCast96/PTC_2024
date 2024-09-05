package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import ptc.proyecto.estrella.bella.databinding.ActivityMainBinding
import java.security.MessageDigest
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet

class activity_login : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración de NavController y AppBar
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
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener referencias a los elementos de la UI
        val txtCorreo = findViewById<TextInputLayout>(R.id.txtCorreo)
        val inputCorreo = txtCorreo.editText as TextInputEditText
        val txtContraseña = findViewById<TextInputLayout>(R.id.txtContraseña)
        val inputContraseña = txtContraseña.editText as TextInputEditText
        val btnLogin = findViewById<Button>(R.id.btn_login)
        val AnimCarga = findViewById<LottieAnimationView>(R.id.AnimCarga)

        //hacer que al tocar cualquier parte de la pantalla se deseleccionen los edit text
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

        //Hacer que al escribir se quite el error de los edit text

        inputCorreo.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtCorreo.error = null
            }
        })

        inputContraseña.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtContraseña.error = null
            }
        })

        btnLogin.setOnClickListener {
            val correo = inputCorreo.text.toString()
            val contraseña = inputContraseña.text.toString()

            btnLogin.visibility = View.GONE
            AnimCarga.visibility = View.VISIBLE

            var valid = true

            if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
                txtCorreo.error = "Correo no válido"
                valid = false
                btnLogin.visibility = View.VISIBLE
                AnimCarga.visibility = View.GONE
            } else {
                txtCorreo.error = null
            }

            if (contraseña.length < 6) {
                txtContraseña.error = "La contraseña debe tener al menos 6 caracteres"
                valid = false
                btnLogin.visibility = View.VISIBLE
                AnimCarga.visibility = View.GONE
            } else {
                txtContraseña.error = null
            }

            if (valid) {
                GlobalScope.launch(Dispatchers.Main) {
                    val user = withContext(Dispatchers.IO) {
                        verificarCredenciales(correo, contraseña)
                    }
                    if (user != null) {
                        userViewModel.setUserInfo(user.nombre, user.email, user.fotoPerfil, user.uuid)

                        userViewModel.saveUserInfo(applicationContext, user.nombre, user.email, user.fotoPerfil, user.uuid)

                        val intent = Intent(this@activity_login, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        txtContraseña.error = "Correo o contraseña incorrectos"
                        btnLogin.visibility = View.VISIBLE
                        AnimCarga.visibility = View.GONE
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

    private fun verificarCredenciales(correo: String, contraseña: String): Usuario? {
        var connection: Connection? = null
        var preparedStatement: PreparedStatement? = null
        var resultSet: ResultSet? = null
        var user: Usuario? = null

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
                        val nombre = resultSet.getString("nombre")
                        val fotoPerfil = resultSet.getString("foto_perfil")
                        val uuid = resultSet.getString("usuario_id")

                        Log.d("Login", "Usuario encontrado: $nombre, $correo, $uuid")

                        user = Usuario(nombre, correo, contraseñaAlmacenada, fotoPerfil, uuid)
                    } else {
                        Log.d("Login", "Contraseña incorrecta para el correo: $correo")
                    }
                } else {
                    Log.d("Login", "No se encontró ningún usuario con el correo: $correo")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            resultSet?.close()
            preparedStatement?.close()
            connection?.close()
        }

        return user
    }

    private fun encriptarSHA256(texto: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(texto.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}

data class Usuario(val nombre: String, val email: String, val contraseña: String, val fotoPerfil: String?, val uuid:String)
