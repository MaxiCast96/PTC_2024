package modelo

data class Pelicula(
    val peliculaId: Int,
    val titulo: String,
    val descripcion: String,
    val duracion: Int,
    val clasificacionId: Int,
    val generoId: Int,
    val poster: String,
    val trailer: String
)
