package RecyclerViewHelpers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    val nombre = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val profilePicture = MutableLiveData<String?>()

    fun setUserInfo(nombre: String, email: String, profilePicture: String?) {
        this.nombre.value = nombre
        this.email.value = email
        this.profilePicture.value = profilePicture
    }

    fun saveUserInfo(context: Context, nombre: String, email: String, profilePicture: String?) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("nombre", nombre)
        editor.putString("email", email)
        editor.putString("profilePicture", profilePicture)
        editor.apply()
    }

    fun loadUserInfo(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        nombre.value = sharedPreferences.getString("nombre", "")
        email.value = sharedPreferences.getString("email", "")
        profilePicture.value = sharedPreferences.getString("profilePicture", "")
    }

    fun clearUserInfo(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
