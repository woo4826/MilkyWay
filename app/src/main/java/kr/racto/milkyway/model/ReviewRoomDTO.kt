package kr.racto.milkyway.model

data class ReviewRoomDTO(
    val title: String,
    val description: String,
    val rating: Double,
    val userEmail: String,
    val roomId: Int,
    val roomName: String,
    val address: String
)
