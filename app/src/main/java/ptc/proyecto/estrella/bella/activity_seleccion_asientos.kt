package ptc.proyecto.estrella.bella


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import modelo.listaAseintos
import modelo.listaHorarioFunciones
import modelo.listaSalas_PTC

class activity_seleccion_asientos : AppCompatActivity() {

    companion object VariablesGlobales {
        lateinit var fila: String
        lateinit var numero: String
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seleccion_asientos)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnContinuarAlPago = findViewById<Button>(R.id.btnContinuarAlPago)
        val spSeleccionarSala = findViewById<Spinner>(R.id.spSeleccionarSala)
        val spSeleccionarAsientos = findViewById<Spinner>(R.id.spSeleccionarAsientos)
        val imgAtrasSalas = findViewById<ImageView>(R.id.imgAtrasSalas)

        // Inicialmente deshabilitamos el spinner de asientos y el botón
        spSeleccionarAsientos.isEnabled = false
        btnContinuarAlPago.isEnabled = false

        imgAtrasSalas.setOnClickListener {
            val intent = Intent(this, detalle_horarios::class.java)
            startActivity(intent)
        }

        fun obtenerSalaPTC(): List<listaSalas_PTC> {
            val listaSalas = mutableListOf<listaSalas_PTC>()

            try {
                val objConexion = ClaseConexion().cadenaConexion()
                if (objConexion != null) {
                    val statement = objConexion.createStatement()
                    val resultSet = statement.executeQuery("select * from Salas_PTC")

                    while (resultSet.next()) {
                        val sala_id = resultSet.getInt("sala_id")
                        val nombre = resultSet.getString("nombre")
                        val capacidad_asientos = resultSet.getInt("capacidad_asientos")
                        listaSalas.add(listaSalas_PTC(sala_id, nombre, capacidad_asientos))
                    }

                    resultSet.close()
                    statement.close()
                    objConexion.close()
                } else {
                    // Manejo de error cuando la conexión es null
                    runOnUiThread {
                        Toast.makeText(this@activity_seleccion_asientos, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                // Manejo de cualquier otra excepción
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@activity_seleccion_asientos, "Error al obtener salas: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            return listaSalas
        }

        fun obtenerListaAsientos(salaId: Int): List<listaAseintos> {
            val listaAsientos = mutableListOf<listaAseintos>()

            try {
                val objConexion = ClaseConexion().cadenaConexion()
                if (objConexion == null) {
                    Toast.makeText(this@activity_seleccion_asientos, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
                    return listaAsientos
                }

                val statement = objConexion.createStatement()
                val resultSet = statement.executeQuery("SELECT * FROM Asientos WHERE sala_id = $salaId")

                while (resultSet.next()) {
                    val asiento_id = resultSet.getInt("asiento_id")
                    val fila = resultSet.getString("fila")
                    val numero = resultSet.getInt("numero")
                    listaAsientos.add(listaAseintos(asiento_id, salaId, fila, numero))
                }

                resultSet.close()
                statement.close()
                objConexion.close()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@activity_seleccion_asientos, "Error al cargar los asientos: ${e.message}", Toast.LENGTH_SHORT).show()
            }

            return listaAsientos
        }

        // Cargar las salas en el spinner
        CoroutineScope(Dispatchers.IO).launch {
            val listaSalas = obtenerSalaPTC()
            val nombresSalas = listaSalas.map { it.nombre }

            withContext(Dispatchers.Main) {
                val miAdaptador = ArrayAdapter(this@activity_seleccion_asientos, android.R.layout.simple_spinner_dropdown_item, nombresSalas)
                spSeleccionarSala.adapter = miAdaptador
            }
        }

        // Manejo del evento de selección de sala
        spSeleccionarSala.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val salaSeleccionada = spSeleccionarSala.selectedItem.toString()

                // Habilitar el spinner de asientos si se selecciona una sala válida
                if (salaSeleccionada != "Elige una opción") {
                    val salaId = obtenerSalaPTC().find { it.nombre == salaSeleccionada }?.sala_id ?: -1

                    // Cargar los asientos correspondientes a la sala seleccionada
                    CoroutineScope(Dispatchers.IO).launch {
                        val listaAsientos = obtenerListaAsientos(salaId)
                        val asientos = listaAsientos.map { "${it.fila} ${it.numero}" }

                        withContext(Dispatchers.Main) {
                            val adaptadorAsientos = ArrayAdapter(this@activity_seleccion_asientos, android.R.layout.simple_spinner_dropdown_item, asientos)
                            spSeleccionarAsientos.adapter = adaptadorAsientos
                            spSeleccionarAsientos.isEnabled = true
                        }
                    }
                } else {
                    spSeleccionarAsientos.isEnabled = false
                    btnContinuarAlPago.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Manejo del evento de selección de asiento
        spSeleccionarAsientos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val asientoSeleccionado = spSeleccionarAsientos.selectedItem.toString()

                // Habilitar el botón "Continuar" si se ha seleccionado un asiento válido
                btnContinuarAlPago.isEnabled = asientoSeleccionado != "Elige una opción"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Acción del botón Continuar
        btnContinuarAlPago.setOnClickListener {
            // Actualizar el asiento como ocupado en la base de datos
            CoroutineScope(Dispatchers.IO).launch {
                val salaSeleccionada = spSeleccionarSala.selectedItem.toString()
                val asientoSeleccionado = spSeleccionarAsientos.selectedItem.toString()

                val salaId = obtenerSalaPTC().find { it.nombre == salaSeleccionada }?.sala_id ?: -1
                val filaNumero = asientoSeleccionado.split(" ")
                fila = filaNumero[0]
                numero = filaNumero[1]

                val objConexion = ClaseConexion().cadenaConexion()
                val statement = objConexion?.prepareStatement("UPDATE Asientos SET ocupado = 1 WHERE sala_id = ? AND fila = ? AND numero = ?")
                statement?.setInt(1, salaId)
                statement?.setString(2, fila)
                statement?.setString(3, numero)
                statement?.executeUpdate()

                withContext(Dispatchers.Main) {
                    // Navegar a la pantalla de pago
                    val intent = Intent(this@activity_seleccion_asientos, activity_pago::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
