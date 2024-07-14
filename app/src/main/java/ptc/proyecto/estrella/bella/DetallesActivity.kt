package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DetallesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val ScreenSplash = installSplashScreen()

        ScreenSplash.setKeepOnScreenCondition{true}
        val intent = Intent(this, activity_login::class.java)
        startActivity(intent)
        finish()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalles)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
}