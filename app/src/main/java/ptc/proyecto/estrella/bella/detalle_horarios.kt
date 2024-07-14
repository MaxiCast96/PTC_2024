package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide

class detalle_horarios : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detalle_horarios)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnReserarEntradas = findViewById<Button>(R.id.btnReservarEntradas)
        val imgDetalleHorarios1 = findViewById<ImageView>(R.id.imgDetalleHorarios1)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)

        imgAtras.setOnClickListener {
            val intent = Intent(this, MainActivity ::class.java)
            startActivity(intent)
        }

        btnReserarEntradas.setOnClickListener{
            val intent = Intent(this, activity_seleccion_asientos::class.java)
            startActivity(intent)
        }


        val uriIntensamente = intent.getStringExtra("uriIntensamente")
        val uriUp = intent.getStringExtra("uriUp")
        val uriMarioBros = intent.getStringExtra("uriMarioBros")
        val uriMonsterInc = intent.getStringExtra("uriMonsterInc")
        val uriVenomLetThereBeCarnage = intent.getStringExtra("uriVenomLetThereBeCarnage")
        val uriVenom = intent.getStringExtra("uriVenom")
        val uriAcrossTheSpiderverse = intent.getStringExtra("uriAcrossTheSpiderverse")
        val uriIntoTheSpiderverse = intent.getStringExtra("uriIntoTheSpiderverse")


        val urlDetalleHorario = Glide.with(this).load(uriIntensamente).into(imgDetalleHorarios1)
        


    }
}