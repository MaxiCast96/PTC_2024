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
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
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

        // Recupera los datos pasados desde la actividad anterior
        val peliculaId = intent.getIntExtra("PELICULA_ID", 0)
        val horaSeleccionada = intent.getStringExtra("HORA_SELECCIONADA")
        val salaId = intent.getIntExtra("SALA_ID", 0)

        // Carga la información del usuario
        userViewModel = UserViewModel()
        userViewModel.loadUserInfo(this)

        imgAtrasPago.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

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

                CoroutineScope(Dispatchers.Main).launch {
                    val conexion: Connection? =
                        withContext(Dispatchers.IO) { ClaseConexion().cadenaConexion() }
                    if (conexion != null) {
                        try {
                            withContext(Dispatchers.IO) {
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

                                val commitQuery = "COMMIT"
                                val statement = conexion.prepareStatement(commitQuery)
                                statement.executeUpdate()

                                preparedStatement.close()
                                conexion.close()
                            }

                            val intent = Intent(this@activity_pago, MainActivity::class.java)
                            intent.putExtra("openFragment", "fragment_historial")
                            startActivity(intent)
                        } catch (e: SQLException) {
                            Toast.makeText(
                                this@activity_pago,
                                "Error en la base de datos: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e(
                                "DB_ERROR",
                                "Error al realizar la inserción en la base de datos",
                                e
                            )
                        } catch (e: Exception) {
                            // Captura cualquier otra excepción y muestra un mensaje de error
                            Toast.makeText(
                                this@activity_pago,
                                "Error inesperado: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                            Log.e(
                                "DB_ERROR",
                                "Error inesperado al realizar la inserción en la base de datos",
                                e
                            )
                        }

                    } else {
                        Toast.makeText(
                            this@activity_pago,
                            "Error al conectar con la base de datos",
                            Toast.LENGTH_SHORT
                        ).show()
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

        if (cvv.text.isNullOrEmpty() || cvv.text?.length != 3) {
            cvv.error = "CVV inválido"
            isValid = false
        }

        if (codigoPostal.text.isNullOrEmpty() || codigoPostal.text?.length != 5) {
            codigoPostal.error = "Código postal inválido"
            isValid = false
        }

        if (fechaCaducidad.text.isNullOrEmpty()) {
            fechaCaducidad.error = "Fecha de caducidad inválida (MM/AA)"
            isValid = false
        }

        return isValid
    }
}

