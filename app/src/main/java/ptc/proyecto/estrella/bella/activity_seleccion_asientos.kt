package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.*
import modelo.ClaseConexion
import modelo.listaAseintos
import modelo.listaSalas_PTC

class activity_seleccion_asientos : AppCompatActivity() {

    companion object VariablesGlobales {
        lateinit var fila: String
        lateinit var numero: String
    }

    private var listaSalasGlobal: List<listaSalas_PTC> = emptyList()

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

        val peliculaId = intent.getIntExtra("PELICULA_ID", 0)
        val horaSeleccionada = intent.getStringExtra("HORA_SELECCIONADA")

        spSeleccionarAsientos.isEnabled = false
        btnContinuarAlPago.isEnabled = false

        imgAtrasSalas.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        suspend fun obtenerSalaPTC(): List<listaSalas_PTC> = withContext(Dispatchers.IO) {
            val listaSalas = mutableListOf<listaSalas_PTC>()
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                if (objConexion != null) {
                    val statement = objConexion.createStatement()
                    val resultSet = statement.executeQuery("SELECT * FROM Salas_PTC")

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
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@activity_seleccion_asientos,
                            "Error al conectar con la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@activity_seleccion_asientos,
                        "Error al obtener salas: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            listaSalas
        }

        suspend fun obtenerListaAsientos(salaId: Int): List<listaAseintos> = withContext(Dispatchers.IO) {
            val listaAsientos = mutableListOf<listaAseintos>()
            try {
                val objConexion = ClaseConexion().cadenaConexion()
                if (objConexion != null) {
                    val statement = objConexion.createStatement()
                    val resultSet = statement.executeQuery("SELECT * FROM Asientos WHERE sala_id = $salaId AND ocupado = 0")

                    while (resultSet.next()) {
                        val asiento_id = resultSet.getInt("asiento_id")
                        val fila = resultSet.getString("fila")
                        val numero = resultSet.getInt("numero")
                        listaAsientos.add(listaAseintos(asiento_id, salaId, fila, numero))
                    }

                    resultSet.close()
                    statement.close()
                    objConexion.close()
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@activity_seleccion_asientos,
                            "Error al conectar con la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@activity_seleccion_asientos,
                        "Error al cargar los asientos: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            listaAsientos
        }

        CoroutineScope(Dispatchers.Main).launch {
            listaSalasGlobal = obtenerSalaPTC()
            val nombresSalas = mutableListOf<String>()
            nombresSalas.add("Elige una opción")
            nombresSalas.addAll(listaSalasGlobal.map { it.nombre })

            val miAdaptador = ArrayAdapter(
                this@activity_seleccion_asientos,
                android.R.layout.simple_spinner_dropdown_item,
                nombresSalas
            )
            spSeleccionarSala.adapter = miAdaptador
        }

        spSeleccionarSala.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val salaSeleccionada = spSeleccionarSala.selectedItem.toString()
                if (salaSeleccionada != "Elige una opción") {
                    val salaId = listaSalasGlobal.find { it.nombre == salaSeleccionada }?.sala_id ?: -1
                    CoroutineScope(Dispatchers.Main).launch {
                        val listaAsientos = obtenerListaAsientos(salaId)
                        val asientos = listaAsientos.map { "${it.fila} ${it.numero}" }

                        val adaptadorAsientos = ArrayAdapter(
                            this@activity_seleccion_asientos,
                            android.R.layout.simple_spinner_dropdown_item,
                            asientos
                        )
                        spSeleccionarAsientos.adapter = adaptadorAsientos
                        spSeleccionarAsientos.isEnabled = true
                        btnContinuarAlPago.isEnabled = false
                    }
                } else {
                    spSeleccionarAsientos.isEnabled = false
                    btnContinuarAlPago.isEnabled = false
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spSeleccionarAsientos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val asientoSeleccionado = spSeleccionarAsientos.selectedItem.toString()
                btnContinuarAlPago.isEnabled = asientoSeleccionado != "Elige una opción" && asientoSeleccionado.isNotBlank()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnContinuarAlPago.setOnClickListener {
            val salaSeleccionada = spSeleccionarSala.selectedItem.toString()
            val asientoSeleccionado = spSeleccionarAsientos.selectedItem.toString()

            if (salaSeleccionada != "Elige una opción" && asientoSeleccionado.isNotBlank()) {
                val filaNumero = asientoSeleccionado.split(" ")
                if (filaNumero.size == 2) {
                    val filaSeleccionada = filaNumero[0]
                    val numeroSeleccionado = filaNumero[1].toIntOrNull()
                    if (numeroSeleccionado != null) {
                        // Enviar los datos seleccionados a la pantalla de pago
                        val intent = Intent(this, activity_pago::class.java).apply {
                            putExtra("PELICULA_ID", peliculaId)
                            putExtra("HORA_SELECCIONADA", horaSeleccionada)
                            putExtra("SALA_ID", listaSalasGlobal.find { it.nombre == salaSeleccionada }?.sala_id)
                            putExtra("FILA_SELECCIONADA", filaSeleccionada)
                            putExtra("NUMERO_SELECCIONADO", numeroSeleccionado)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Número de asiento inválido", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Formato de asiento inválido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Selecciona una sala y un asiento válidos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
