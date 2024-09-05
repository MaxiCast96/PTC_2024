package modelo

import java.util.Date

data class Reserva(
    val id: Int,
    val nombreUsuario: String,
    val nombrePelicula: String,
    val salaId: String,
    val fechaReserva: Date,
    val totalPago: Double,
    val metodoPago: String
)
