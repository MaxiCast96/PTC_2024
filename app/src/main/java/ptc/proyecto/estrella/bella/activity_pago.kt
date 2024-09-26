package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class activity_pago : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        val imgAtrasPago = findViewById<ImageView>(R.id.imgAtrasPago)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        val txtNumeroTarjeta =
            findViewById<TextInputLayout>(R.id.txtNumeroTarjeta).editText as TextInputEditText
        val txtRepresentante =
            findViewById<TextInputLayout>(R.id.txtRepresentante).editText as TextInputEditText
        val txtCVV = findViewById<TextInputLayout>(R.id.txtCVV).editText as TextInputEditText
        val txtCodigoPostal =
            findViewById<TextInputLayout>(R.id.txtCodigoPostal).editText as TextInputEditText
        val txtFechaCaducidad =
            findViewById<TextInputLayout>(R.id.txtFechaCaducidad).editText as TextInputEditText

        // Recuperar los datos de los asientos seleccionados
        val asientosSeleccionados =
            intent.getStringArrayListExtra("ASIENTOS_SELECCIONADOS") ?: arrayListOf()

        val peliculaId = intent.getIntExtra("PELICULA_ID", 0)
        val horaSeleccionada = intent.getStringExtra("HORA_SELECCIONADA")
        val filaSeleccionada = intent.getStringExtra("FILA_SELECCIONADA")
        val numeroSeleccionado = intent.getIntExtra("NUMERO_SELECCIONADO", -1)
        val salaId = intent.getIntExtra("SALA_ID", 0)

        // Carga la información del usuario
        userViewModel = UserViewModel()
        userViewModel.loadUserInfo(this)

        imgAtrasPago.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        txtNumeroTarjeta.filters = arrayOf(android.text.InputFilter.LengthFilter(16))

        // TextWatcher para insertar la pleca "/" automáticamente
        txtFechaCaducidad.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val separator = "/"

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                val input = s.toString().replace(separator, "")
                if (input.length <= 4) {
                    isUpdating = true
                    val formattedInput = input.chunked(2).joinToString(separator)
                    txtFechaCaducidad.setText(formattedInput)
                    txtFechaCaducidad.setSelection(formattedInput.length)
                    isUpdating = false
                }
            }
        })

        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        txtNumeroTarjeta.addTextChangedListener(watcher)
        txtRepresentante.addTextChangedListener(watcher)
        txtCVV.addTextChangedListener(watcher)
        txtCodigoPostal.addTextChangedListener(watcher)
        txtFechaCaducidad.addTextChangedListener(watcher)

        btnPagar.setOnClickListener {
            val isValid = validateInputs(
                txtNumeroTarjeta,
                txtRepresentante,
                txtCVV,
                txtCodigoPostal,
                txtFechaCaducidad
            )
            if (isValid) {
                // Realizar la inserción en la base de datos
                val uuidUsuario = userViewModel.uuid.value ?: ""
                val fechaReserva = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val totalPago = 5.00

                btnPagar.isEnabled = false
                btnPagar.text = "Procesando..."

                CoroutineScope(Dispatchers.Main).launch {
                    val conexion: Connection? =
                        withContext(Dispatchers.IO) { ClaseConexion().cadenaConexion() }
                    if (conexion != null) {
                        try {
                            withContext(Dispatchers.IO) {
                                // Inserción en la tabla de reservas
                                val query = """
                                    INSERT INTO Reservas_Android (funcionand_id, usuario_id, pelicula_id, sala_id, Hora_funcion, fecha_reserva, total_pago)
                                    VALUES (SEQ_FUNCIONAND_ID.NEXTVAL, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)
                                """.trimIndent()
                                val preparedStatement: PreparedStatement =
                                    conexion.prepareStatement(query)
                                preparedStatement.setString(1, uuidUsuario)
                                preparedStatement.setInt(2, peliculaId)
                                preparedStatement.setInt(3, salaId)
                                preparedStatement.setString(4, horaSeleccionada)
                                preparedStatement.setString(5, fechaReserva)
                                preparedStatement.setDouble(6, totalPago)

                                preparedStatement.executeUpdate()

                                // Update para marcar los asientos como ocupados
                                val updateQuery = "UPDATE Asientos SET ocupado = 1 WHERE sala_id = ? AND fila = ? AND numero = ?"
                                val updateStatement: PreparedStatement = conexion.prepareStatement(updateQuery)

                                updateStatement.setInt(1, salaId)
                                updateStatement.setString(2, filaSeleccionada)
                                updateStatement.setInt(3, numeroSeleccionado)

                                updateStatement.executeUpdate()

                                // Confirmar la transacción
                                val commitQuery = "COMMIT"
                                val statement = conexion.prepareStatement(commitQuery)
                                statement.executeUpdate()

                                preparedStatement.close()
                                updateStatement.close()
                                conexion.close()
                            }

                            // Redirigir a la actividad principal
                            val intent = Intent(this@activity_pago, MainActivity::class.java)
                            intent.putExtra("openFragment", "fragment_historial")
                            startActivity(intent)
                        } catch (e: SQLException) {
                            Toast.makeText(
                                this@activity_pago,
                                "Error en la base de datos: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e("DB_ERROR", "Error al realizar la inserción en la base de datos", e)
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@activity_pago,
                                "Error inesperado: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e("DB_ERROR", "Error inesperado al realizar la inserción en la base de datos", e)
                        } finally {
                            btnPagar.isEnabled = true
                            btnPagar.text = "Pagar"
                        }
                    } else {
                        Toast.makeText(
                            this@activity_pago,
                            "Error al conectar con la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
                        btnPagar.isEnabled = true
                        btnPagar.text = "Pagar"
                    }
                }
            }
        }
    }



    private fun validateInputs(
        numeroTarjeta: TextInputEditText,
        representante: TextInputEditText,
        cvv: TextInputEditText,
        codigoPostal: TextInputEditText,
        fechaCaducidad: TextInputEditText
    ): Boolean {
        var isValid = true

        if (numeroTarjeta.text.isNullOrEmpty() || numeroTarjeta.text?.length != 16) {
            numeroTarjeta.error = "Número de tarjeta inválido"
            isValid = false
        }

        if (representante.text.isNullOrEmpty()) {
            representante.error = "Nombre del titular requerido"
            isValid = false
        }

        if (cvv.text.isNullOrEmpty() || cvv.text?.length != 3 || !cvv.text?.matches("\\d+".toRegex())!!) {
            cvv.error = "CVV inválido"
            isValid = false
        }

        if (codigoPostal.text.isNullOrEmpty() || codigoPostal.text?.length != 5 || !codigoPostal.text?.matches("\\d+".toRegex())!!) {
            codigoPostal.error = "Código postal inválido"
            isValid = false
        }

        if (!fechaCaducidad.text.isNullOrEmpty()) {
            val fecha = fechaCaducidad.text.toString().split("/")
            if (fecha.size == 2) {
                val mes = fecha[0].toIntOrNull()
                val año = "20${fecha[1]}".toIntOrNull()
                val calendar = Calendar.getInstance()

                val mesActual = calendar.get(Calendar.MONTH) + 1
                val añoActual = calendar.get(Calendar.YEAR)

                if (mes == null || mes !in 1..12 || año == null || año < añoActual || (año == añoActual && mes < mesActual)) {
                    fechaCaducidad.error = "Fecha de caducidad inválida o tarjeta caducada"
                    isValid = false
                }
            } else {
                fechaCaducidad.error = "Fecha de caducidad inválida (MM/AA)"
                isValid = false
            }
        } else {
            fechaCaducidad.error = "Fecha de caducidad requerida"
            isValid = false
        }

        return isValid
    }
}
