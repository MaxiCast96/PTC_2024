package RecyclerViewHelpers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> get() = _nombre

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _fotoPerfil = MutableLiveData<String>()
    val fotoPerfil: LiveData<String> get() = _fotoPerfil

    fun setNombre(nombre: String) {
        _nombre.value = nombre
    }

    fun setEmail(email: String) {
        _email.value = email
    }

    fun setFotoPerfil(fotoPerfil: String) {
        _fotoPerfil.value = fotoPerfil
    }
}