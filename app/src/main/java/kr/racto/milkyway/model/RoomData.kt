package kr.racto.milkyway.model

data class RoomData(
    val reviewList: List<Review>,
    val roomName: String,
    val address: String,
    val ratingAvg: Int,
    val reviewCount: Int
)
