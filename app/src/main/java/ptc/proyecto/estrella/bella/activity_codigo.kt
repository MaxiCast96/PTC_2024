package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout

class activity_codigo : AppCompatActivity() {

    private lateinit var txtCodigo: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_codigo)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        txtCodigo = findViewById(R.id.txtCodigo)

        val btnReenviar = findViewById<Button>(R.id.btnReenviar)

        val btnCodigo: Button = findViewById(R.id.btnComprobarCodigo)

        btnReenviar.setOnClickListener {
            val intent = Intent(this, activity_correo::class.java)
            startActivity(intent)
            finish()
        }
        btnCodigo.setOnClickListener {
            if (validarCodigo()) {
                val intent = Intent(this, activity_RepuperarContra::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validarCodigo(): Boolean {
        val codigoIngresado = txtCodigo.editText?.text.toString().trim()
        val codigoEnviado = intent.getIntExtra("codigo_recuperacion", -1) // Código de recuperación enviado

        return when {
            codigoIngresado.isEmpty() -> {
                txtCodigo.error = "El código es obligatorio"
                false
            }
            codigoIngresado.length != 6 -> {
                txtCodigo.error = "El código debe tener 6 dígitos"
                false
            }
            codigoIngresado.toIntOrNull() != codigoEnviado -> {
                txtCodigo.error = "El código ingresado no es correcto"
                false
            }
            else -> {
                txtCodigo.error = null
                true
            }
        }
    }
}