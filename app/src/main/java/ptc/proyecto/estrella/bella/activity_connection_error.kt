package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import modelo.ClaseConexion

class activity_connection_error : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_connection_error)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnReintentar = findViewById<Button>(R.id.btnReintentar)
        val AnimCarga = findViewById<LottieAnimationView>(R.id.AnimCarga)

        btnReintentar.setOnClickListener {
            btnReintentar.visibility = View.GONE
            AnimCarga.visibility = View.VISIBLE

            GlobalScope.launch(Dispatchers.IO) {
                val conexion = withContext(Dispatchers.IO) {
                    withTimeoutOrNull(5000) {
                        ClaseConexion().cadenaConexion()
                    }
                }


                withContext(Dispatchers.Main) {
                    if (conexion != null) {
                        val intent = Intent(this@activity_connection_error, activity_login::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        btnReintentar.visibility = View.VISIBLE
                        AnimCarga.visibility = View.GONE
                    }
                }
            }
        }
    }
}