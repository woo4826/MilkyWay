import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kr.racto.milkyway.model.RoomData
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("/api/rooms/{roomId}") // API 엔드포인트 경로에 {roomId} 변수가 포함됨
    fun getRoomData(@Path("roomId") roomId: Int): Call<RoomData>
}