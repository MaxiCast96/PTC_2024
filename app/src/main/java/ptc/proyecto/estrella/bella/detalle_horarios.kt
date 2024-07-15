package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Space
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaHorarioFunciones

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

        val spSeleccionarSala = findViewById<Spinner>(R.id.spSeleccionarSala)
        val btnReservarEntradas = findViewById<Button>(R.id.btnReservarEntradas)
        val imgDetalleHorarios1 = findViewById<ImageView>(R.id.imgDetalleHorarios1)
        val imgAtras = findViewById<ImageView>(R.id.imgAtras)

        imgAtras.setOnClickListener {
            val intent = Intent(this, MainActivity ::class.java)
            startActivity(intent)
        }

        btnReservarEntradas.setOnClickListener{
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

        fun obtenerHorarioFuncion(): List<listaHorarioFunciones>{
            val objConexion = ClaseConexion().cadenaConexion()

            //Creo un Statement que me ejecutará el select
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select * from HorarioFuncion")!!

            val listaHorarioFunciones= mutableListOf<listaHorarioFunciones>()

            while (resultSet.next()){
                val horario_id = resultSet.getInt("horario_id")
                val hora = resultSet.getString("hora")
                val unHorarioCompleto = listaHorarioFunciones(horario_id, hora)
                listaHorarioFunciones.add(unHorarioCompleto)
            }
            return listaHorarioFunciones
        }

        //Programar el spinner para que me muestre los datos del select
        CoroutineScope(Dispatchers.IO).launch {
            //1- Obtengo los datos
            val listaHorarioFunciones = obtenerHorarioFuncion()
            val horaFuncion = listaHorarioFunciones.map { it.hora }

            withContext(Dispatchers.Main)  {
                //2- Crear y modificar el adaptador
                val miAdaptador = ArrayAdapter(this@detalle_horarios, android.R.layout.simple_spinner_dropdown_item, horaFuncion)
                spSeleccionarSala.adapter = miAdaptador
            }
        }

    }
}