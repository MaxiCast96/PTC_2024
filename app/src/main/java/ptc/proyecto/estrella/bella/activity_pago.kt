package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout


class activity_pago : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pago)

        val imgAtrasPago = findViewById<ImageView>(R.id.imgAtrasPago)
        val btnPagar = findViewById<Button>(R.id.btnPagar)

        // Obtén el TextInputEditText a través de su TextInputLayout
        val txtNumeroTarjeta = findViewById<TextInputLayout>(R.id.txtNumeroTarjeta).editText as TextInputEditText
        val txtRepresentante = findViewById<TextInputLayout>(R.id.txtRepresentante).editText as TextInputEditText
        val txtCVV = findViewById<TextInputLayout>(R.id.txtCVV).editText as TextInputEditText
        val txtCodigoPostal = findViewById<TextInputLayout>(R.id.txtCodigoPostal).editText as TextInputEditText
        val txtFechaCaducidad = findViewById<TextInputLayout>(R.id.txtFechaCaducidad).editText as TextInputEditText

        // Formato automático de la fecha MM/AA
        txtFechaCaducidad.addTextChangedListener(object : TextWatcher {
            private var isUpdating = false
            private val dividerChar = '/'

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isUpdating) return

                isUpdating = true

                val length = s?.length ?: 0

                if (length == 2 && !s.toString().contains(dividerChar.toString())) {
                    s?.append(dividerChar)
                } else if (length > 2 && !s.toString().contains(dividerChar.toString())) {
                    s?.insert(2, dividerChar.toString())
                }

                isUpdating = false
            }
        })

        imgAtrasPago.setOnClickListener {
            val intent = Intent(this, activity_seleccion_asientos::class.java)
            startActivity(intent)
        }

        btnPagar.setOnClickListener {
            val isValid = validateInputs(txtNumeroTarjeta, txtRepresentante, txtCVV, txtCodigoPostal, txtFechaCaducidad)
            if (isValid) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
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

        if (fechaCaducidad.text.isNullOrEmpty() || !fechaCaducidad.text?.matches(Regex("^(0[1-9]|1[0-2])/$2\$"))!!) {
            fechaCaducidad.error = "Fecha de caducidad inválida (MM/AA)"
            isValid = false
        }

        return isValid
    }
}
