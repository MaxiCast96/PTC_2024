package RecyclerViewHelpers

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class UserViewModel : ViewModel() {
    val nombre = MutableLiveData<String?>()
    val email = MutableLiveData<String?>()
    val profilePicture = MutableLiveData<String?>()
    val uuid = MutableLiveData<String?>()

    fun setUserInfo(nombre: String, email: String, profilePicture: String?, uuid: String?) {
        this.nombre.value = nombre
        this.email.value = email
        this.profilePicture.value = profilePicture
        this.uuid.value = uuid
    }

    fun saveUserInfo(context: Context, nombre: String, email: String, profilePicture: String?, uuid: String?) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("nombre", nombre)
        editor.putString("email", email)
        editor.putString("profilePicture", profilePicture)
        editor.putString("uuid", uuid)
        editor.apply()
    }

    fun loadUserInfo(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        nombre.value = sharedPreferences.getString("nombre", "")
        email.value = sharedPreferences.getString("email", "")
        profilePicture.value = sharedPreferences.getString("profilePicture", "")
        uuid.value = sharedPreferences.getString("uuid", "")
    }

    fun clearUserInfo(context: Context) {
        val sharedPreferences = context.getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
