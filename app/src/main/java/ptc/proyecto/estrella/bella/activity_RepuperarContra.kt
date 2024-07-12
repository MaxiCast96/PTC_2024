package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.Button
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
import ptc.proyecto.estrella.bella.databinding.ActivityMainBinding

class activity_RepuperarContra : AppCompatActivity() {
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
        setContentView(R.layout.activity_repuperar_contra)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnCambiarContraseña: Button = findViewById(R.id.btnCambiarContraseña)
        val txtNuevaContraseña = findViewById<TextInputLayout>(R.id.txtNuevaContraseña)
        val txtRecuperarNuevaContraseña = findViewById<TextInputLayout>(R.id.txtRecuperarNuevaContraseña)

        btnCambiarContraseña.setOnClickListener {
            val nuevaContraseña = txtNuevaContraseña.editText?.text.toString()
            val confirmarContraseña = txtRecuperarNuevaContraseña.editText?.text.toString()

            if (validarContraseñas(nuevaContraseña, confirmarContraseña, txtNuevaContraseña, txtRecuperarNuevaContraseña)) {
                val intent = Intent(this, activity_login::class.java)
                startActivity(intent)
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
}
