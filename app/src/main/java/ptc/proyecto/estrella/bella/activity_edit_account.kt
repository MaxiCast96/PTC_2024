package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class activity_edit_account : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        // Obtener referencias a los TextViews y ImageView
        val lblNombre = findViewById<TextView>(R.id.lblNombre)
        val lblEmail = findViewById<TextView>(R.id.lblEmail)
        val imgProfilePicture = findViewById<ImageView>(R.id.AnimCrearCuenta)

        // Observar los datos del ViewModel
        userViewModel.nombre.observe(this, { nombre ->
            lblNombre.text = nombre
        })

        userViewModel.email.observe(this, { email ->
            lblEmail.text = email
        })

        userViewModel.profilePicture.observe(this, { profilePicture ->
            Glide.with(this).load(profilePicture).into(imgProfilePicture)
        })

        // Aquí podrías agregar listeners para botones, etc.
    }
}

