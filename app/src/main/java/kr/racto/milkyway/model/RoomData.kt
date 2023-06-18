package kr.racto.milkyway.model

data class RoomData(
    val reviewList: List<Review>,
    val roomName: String,
    val address: String,
    val ratingAvg: Double,
    val reviewCount: Int,
    val roomId: Int
)
