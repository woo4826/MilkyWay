import android.service.autofill.UserData
import kr.racto.milkyway.model.Review
import kr.racto.milkyway.model.RoomData
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("/review/room/{roomId}") // API 엔드포인트 경로에 {roomId} 변수가 포함됨
    fun getRoomReviewData(@Path("roomId") roomId: Int): Call<RoomData>
    @GET("/review/user/{userId}")
    fun getUserReviewData(@Path("userId") userId: Int): Call<List<Review>>
}