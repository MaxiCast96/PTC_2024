package ptc.proyecto.estrella.bella

import RecyclerViewHelpers.UserViewModel
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.activity.viewModels
import ptc.proyecto.estrella.bella.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.loadUserInfo(this)

        val fragmentToOpen = intent.getStringExtra("openFragment")

        if (fragmentToOpen == "fragment_usuario") {
            // Aquí agregas el código para abrir el fragment_usuario
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment_usuario())
                .commit()
        }

        if (fragmentToOpen == "fragment_historial") {
            // Aquí agregas el código para abrir el fragment_usuario
            supportFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_activity_main, fragment_historial())
                .commit()
        }

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_home, R.id.navigation_dashboard, R.id.fragment_usuario)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        supportActionBar?.hide()
    }
}
