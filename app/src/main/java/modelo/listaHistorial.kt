package modelo

data class listaHistorial (
    val reserva_id: Int,
    val usuario_id: Int,
    val funcion_id: Int,
    val fecha_reserva: Int,
    val total_pago: Int,
    val metodo_pago: Int,
)