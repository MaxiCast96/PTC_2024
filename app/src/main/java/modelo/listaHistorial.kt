package modelo

data class listaHistorial (
    val reserva_id: Int,
    var usuario_id: Int,
    var funcion_id: Int,
    var fecha_reserva: Int,
    val total_pago: Int,
    val metodo_pago: Int,
)