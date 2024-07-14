package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
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
import ptc.proyecto.estrella.bella.ui.enviarCorreo
import ptc.proyecto.estrella.bella.ui.crearMensajeHTML

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
                    val codigoAleatorio = (100000..999999).random()
                    val mensajeHTML = crearMensajeHTML(codigoAleatorio)

                    enviarCorreo(
                        correoUsuario,
                        "Recuperación de contraseña",
                        mensajeHTML
                    )

                    CoroutineScope(Dispatchers.Main).launch {
                        animCorreo.playAnimation()
                        delay(2000)
                    }
                    val intent = Intent(this@activity_correo, activity_codigo::class.java)
                    intent.putExtra("codigo_recuperacion", codigoAleatorio)
                    intent.putExtra("correo_usuario", correoUsuario)
                    startActivity(intent)
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
}
