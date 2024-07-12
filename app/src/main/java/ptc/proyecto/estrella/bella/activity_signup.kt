package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputLayout

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
                // Lógica para crear la cuenta
                val intent = Intent(this, activity_login::class.java)
                startActivity(intent)
            }
        }

        btn_login.setOnClickListener {
            // Lógica para iniciar sesión
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
        } else if (!correo.contains('@') || !correo.contains('.')) {
            txtCorreo.error = "El correo debe contener '@' y '.'"
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
            txtConfirmarContraseña.error = "Confirmar la contraseña es obligatorio"
            return false
        } else if (contraseña != confirmarContraseña) {
            txtConfirmarContraseña.error = "Las contraseñas no coinciden"
            return false
        } else {
            txtConfirmarContraseña.error = null
        }

        return true
    }
}
