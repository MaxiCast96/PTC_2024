package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class activity_edit_account : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private var imageUri: Uri? = null
    val AnimFoto = findViewById<ImageView>(R.id.AnimFoto)
    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            imageUri = result.data?.data
            AnimFoto.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_account)

        userViewModel.loadUserInfo(this)

        // Obtener referencias de la UI
        val lblNombre = findViewById<TextView>(R.id.lblNombre)
        val lblEmail = findViewById<TextView>(R.id.lblEmail)
        val imgProfilePicture = findViewById<ImageView>(R.id.AnimCrearCuenta)
        val btnGoBack = findViewById<ImageView>(R.id.imgGoBack)
        val btnContraseña = findViewById<Button>(R.id.btnContraseña)
        val AnimFoto = findViewById<ImageView>(R.id.AnimFoto)

        btnGoBack.setOnClickListener {

        }

        btnContraseña.setOnClickListener {

        }

        AnimFoto.setOnClickListener {
            openGallery()
        }


            // Observar los datos del ViewModel
        userViewModel.nombre.observe(this, { nombre ->
            lblNombre.text = nombre
            println("Este es el nombre que traigo del viewmodel $nombre")
        })

        userViewModel.email.observe(this, { email ->
            lblEmail.text = email
            println("Este es el correo que traigo del viewmodel $email")
        })

        userViewModel.profilePicture.observe(this, { profilePicture ->
            Glide.with(this).load(profilePicture).into(imgProfilePicture)
        })



    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        getResult.launch(intent)
    }
}

