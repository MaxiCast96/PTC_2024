package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_correo)

        txtCorreoRecuperacion = findViewById(R.id.txtCorreoRecuperacion)
        animCorreo = findViewById(R.id.AnimCorreo)
        val btnEnviarCodigo: Button = findViewById(R.id.btnEnviarCodigo)

        btnEnviarCodigo.setOnClickListener {
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
                    } else {
                        txtCorreoRecuperacion.error = "Correo no encontrado"
                    }
                }
            } else {
                txtCorreoRecuperacion.error = "Correo inválido"
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
            txtCorreoRecuperacion.error = "El correo es obligatorio"
            false
        } else if (!correo.contains('@') || !correo.contains('.')) {
            txtCorreoRecuperacion.error = "El correo debe contener '@' y '.'"
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
