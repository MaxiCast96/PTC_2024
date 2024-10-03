package ptc.proyecto.estrella.bella

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.sql.PreparedStatement
import java.util.UUID

class activity_ConfirmarCorreo : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmar_correo)

        val codigoVerificacion = intent.getStringExtra("codigoVerificacion")
        val nombre = intent.getStringExtra("nombre")
        val correo = intent.getStringExtra("correo")
        val contraseña = intent.getStringExtra("contraseña")
        val fotoPerfil = intent.getStringExtra("fotoPerfil")

        val editCodigoVerificacion = findViewById<TextInputLayout>(R.id.txtCodigo)
        val btnConfirmarCodigo = findViewById<Button>(R.id.btnComprobarCodigo)

        btnConfirmarCodigo.setOnClickListener {
            val codigoIngresado = editCodigoVerificacion.editText?.text.toString().trim()


            if (codigoIngresado == codigoVerificacion) {
                // Código correcto, hacer el INSERT en la base de datos
                GlobalScope.launch(Dispatchers.IO) {
                    crearCuentaEnBD(nombre, correo, contraseña, fotoPerfil)
                }
            } else {
                Toast.makeText(this, "Código incorrecto", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun crearCuentaEnBD(nombre: String?, correo: String?, contraseña: String?, fotoPerfil: String?) {
        val uuid = UUID.randomUUID().toString()
        val rolId = 1 // Asumiendo que es el rol de usuario

        val connection = ClaseConexion().cadenaConexion()
        if (connection != null) {
            val sql = "INSERT INTO Usuarios (usuario_id, nombre, email, contraseña, rol_id, foto_perfil) VALUES (?, ?, ?, ?, ?, ?)"
            val statement: PreparedStatement = connection.prepareStatement(sql)
            statement.setString(1, uuid)
            statement.setString(2, nombre)
            statement.setString(3, correo)
            statement.setString(4, contraseña)
            statement.setInt(5, rolId)
            statement.setString(6, fotoPerfil)

            val result = statement.executeUpdate()
            if (result > 0) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_ConfirmarCorreo, "Cuenta creada exitosamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@activity_ConfirmarCorreo, activity_login::class.java)
                    startActivity(intent)
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@activity_ConfirmarCorreo, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
                }
            }
            statement.close()
            connection.close()
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@activity_ConfirmarCorreo, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
