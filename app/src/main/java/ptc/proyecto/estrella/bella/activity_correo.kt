package ptc.proyecto.estrella.bella

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import ptc.proyecto.estrella.bella.ui.enviarCorreo
import ptc.proyecto.estrella.bella.ui.crearMensajeHTML
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class activity_correo : AppCompatActivity() {
    private lateinit var txtCorreoRecuperacion: TextInputLayout
    private lateinit var animCorreo: LottieAnimationView

    override fun onBackPressed() {

        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correo)

        // Obtener referencias a los elementos de la UI
        txtCorreoRecuperacion = findViewById(R.id.txtCorreoRecuperacion)
        animCorreo = findViewById(R.id.AnimCorreo)
        val imgGoBack = findViewById<ImageView>(R.id.imgGoBack)
        val btnEnviarCodigo: Button = findViewById(R.id.btnEnviarCodigo)
        val AnimCarga = findViewById<ImageView>(R.id.AnimCarga)

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

        imgGoBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        btnEnviarCodigo.setOnClickListener {
            btnEnviarCodigo.visibility = View.GONE
            AnimCarga.visibility = View.VISIBLE
            if (validarCorreo()) {
                val correoUsuario = txtCorreoRecuperacion.editText?.text.toString().trim()
                CoroutineScope(Dispatchers.Main).launch {
                    val correoExiste = withContext(Dispatchers.IO) {
                        verificarCorreo(correoUsuario)
                    }

                    if (correoExiste) {
                        val codigoAleatorio = (100000..999999).random()
                        val mensajeHTML = crearMensajeHTML(codigoAleatorio)

                        enviarCorreo(
                            correoUsuario,
                            "Recuperación de contraseña",
                            mensajeHTML
                        )

                        animCorreo.playAnimation()
                        delay(2000)

                        val intent = Intent(this@activity_correo, activity_codigo::class.java)
                        intent.putExtra("codigo_recuperacion", codigoAleatorio)
                        intent.putExtra("correo_usuario", correoUsuario)
                        startActivity(intent)
                        finish()
                        imgGoBack.setOnClickListener {
                            onBackPressedDispatcher.onBackPressed()
                        }
                    } else {
                        txtCorreoRecuperacion.error = "Correo no encontrado"
                        btnEnviarCodigo.visibility = View.VISIBLE
                        AnimCarga.visibility = View.GONE
                    }
                }
            } else {
                txtCorreoRecuperacion.error = "Correo inválido"
                btnEnviarCodigo.visibility = View.VISIBLE
                AnimCarga.visibility = View.GONE
            }
        }

        supportActionBar?.hide()
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun validarCorreo(): Boolean {
        val correo = txtCorreoRecuperacion.editText?.text.toString().trim()
        return if (correo.isEmpty()) {
            val btnEnviarCodigo: Button = findViewById(R.id.btnEnviarCodigo)
            val AnimCarga = findViewById<ImageView>(R.id.AnimCarga)
            txtCorreoRecuperacion.error = "El correo es obligatorio"
            btnEnviarCodigo.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            false
        } else if (!correo.contains('@') || !correo.contains('.')) {
            val btnEnviarCodigo: Button = findViewById(R.id.btnEnviarCodigo)
            val AnimCarga = findViewById<ImageView>(R.id.AnimCarga)
            txtCorreoRecuperacion.error = "El correo no es valido"
            btnEnviarCodigo.visibility = View.VISIBLE
            AnimCarga.visibility = View.GONE
            false
        } else {
            true
        }
    }

    private fun verificarCorreo(correo: String): Boolean {
        var existe = false
        val sql = "SELECT COUNT(*) FROM Usuarios WHERE email = ?"
        val conexion = ClaseConexion().cadenaConexion()

        if (conexion != null) {
            try {
                val statement: PreparedStatement = conexion.prepareStatement(sql)
                statement.setString(1, correo)
                Log.d("DBQuery", "Consulta ejecutada: $sql con parámetro $correo")
                val resultSet: ResultSet = statement.executeQuery()

                if (resultSet.next()) {
                    val count = resultSet.getInt(1)
                    Log.d("DBResult", "Resultado de la consulta: $count")
                    existe = count > 0
                }

                resultSet.close()
                statement.close()
                conexion.close()
            } catch (e: SQLException) {
                Log.e("DBError", "Error al verificar el correo", e)
            }
        } else {
            Log.e("DBError", "No se pudo establecer la conexión con la base de datos")
        }

        return existe
    }
}
