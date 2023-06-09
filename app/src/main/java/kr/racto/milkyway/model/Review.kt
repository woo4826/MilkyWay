package kr.racto.milkyway.model

data class Review(
    val title: String,
    val description: String,
    val rating: Double,
    val email: String,
    val roomId: Int,
    val reviewId: Int,
    val roomName: String
)
