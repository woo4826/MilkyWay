package kr.racto.milkyway.model

data class Review(
    val title: String,
    val description: String,
    val rating: Int,
    val userId: Int,
    val roomId: Int
)
