package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import modelo.ClaseConexion

class splash_screenV2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen_v2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        GlobalScope.launch(Dispatchers.IO) {
            val conexion = withContext(Dispatchers.IO) {
                withTimeoutOrNull(5000) { // Timeout de 5 segundos
                    ClaseConexion().cadenaConexion()
                }
            }

            val pantallaSiguiente = if (conexion != null) {
                Intent(this@splash_screenV2, activity_login::class.java)
            } else {
                Intent(this@splash_screenV2, activity_connection_error::class.java)
            }

            startActivity(pantallaSiguiente)
            finish()
        }

    }
}