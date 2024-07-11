package modelo

data class listaUsuarios (
    val uuid: String,
    var nombre: String,
    val email: String,
    val contraseña: String,
    val rol_id: Int,
    val foto_perfil: String
)