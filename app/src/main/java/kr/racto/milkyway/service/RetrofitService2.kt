import kr.racto.milkyway.model.Review
import kr.racto.milkyway.model.ReviewRoomDTO
import kr.racto.milkyway.model.RoomData
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("/review/room/{roomId}") // API 엔드포인트 경로에 {roomId} 변수가 포함됨
    fun getRoomReviewData(@Path("roomId") roomId: Int): Call<RoomData>
    @GET("/review/user")
    fun getUserReviewData(@Query("email") email: String): Call<List<Review>>
    @POST("/review")
    fun insertReview(@Body body: RequestBody): Call<Boolean>
}