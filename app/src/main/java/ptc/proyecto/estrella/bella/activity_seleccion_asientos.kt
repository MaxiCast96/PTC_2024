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
                    // Cargar los asientos correspondientes a la sala seleccionada
                    CoroutineScope(Dispatchers.Main).launch {
                        val listaAsientos = obtenerListaAsientos(salaId)
                        val asientos = listaAsientos.map { "${it.fila} ${it.numero}" }

                        val adaptadorAsientos = ArrayAdapter(
                            this@activity_seleccion_asientos,
                            android.R.layout.simple_spinner_dropdown_item,
                            asientos
                        )
                        // Deshabilitar hasta que se seleccione un asiento
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
                        CoroutineScope(Dispatchers.Main).launch {
                            actualizarAsientoOcupado(salaSeleccionada, filaSeleccionada, numeroSeleccionado)
                        }
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

    private suspend fun actualizarAsientoOcupado(salaNombre: String, fila: String, numero: Int) = withContext(Dispatchers.IO) {
        try {
            val salaId = listaSalasGlobal.find { it.nombre == salaNombre }?.sala_id ?: -1
            if (salaId == -1) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_seleccion_asientos, "Sala no encontrada", Toast.LENGTH_SHORT).show()
                }
                return@withContext
            }

            val objConexion = ClaseConexion().cadenaConexion()
            if (objConexion != null) {
                val query = "UPDATE Asientos SET ocupado = 1 WHERE sala_id = ? AND fila = ? AND numero = ?"
                val statement = objConexion.prepareStatement(query)
                statement.setInt(1, salaId)
                statement.setString(2, fila)
                statement.setInt(3, numero)
                val filasActualizadas = statement.executeUpdate()

                statement.close()
                objConexion.close()

                withContext(Dispatchers.Main) {
                    if (filasActualizadas > 0) {
                        val intent = Intent(this@activity_seleccion_asientos, activity_pago::class.java).apply {
                            putExtra("PELICULA_ID", intent.getIntExtra("PELICULA_ID", 0)) // Pasar el ID de la película
                            putExtra("HORA_SELECCIONADA", intent.getStringExtra("HORA_SELECCIONADA")) // Pasar la hora seleccionada
                            putExtra("SALA_ID", salaId) // Pasar el ID de la sala seleccionada
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@activity_seleccion_asientos, "No se pudo actualizar el asiento", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_seleccion_asientos, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                Toast.makeText(this@activity_seleccion_asientos, "Error al actualizar el asiento: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
