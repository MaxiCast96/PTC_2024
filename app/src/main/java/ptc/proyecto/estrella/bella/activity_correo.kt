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
import kotlinx.coroutines.delay

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
                animCorreo.playAnimation()
                val intent = Intent(this, activity_RepuperarContra::class.java)
                startActivity(intent)
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

        if (correo.isEmpty()) {
            txtCorreoRecuperacion.error = "El correo es obligatorio"
            return false
        } else if (!correo.contains('@') || !correo.contains('.')) {
            txtCorreoRecuperacion.error = "El correo debe contener '@' y '.'"
            return false


        }

        return true
    }
}
