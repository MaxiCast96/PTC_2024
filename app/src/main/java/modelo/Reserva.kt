package modelo

import java.util.Date

data class Reserva(
    val reservaId: Int,
    val usuarioId: String,
    val funcionId: Int,
    val fechaReserva: Date,
    val totalPago: Double,
    val metodoPago: String
)
