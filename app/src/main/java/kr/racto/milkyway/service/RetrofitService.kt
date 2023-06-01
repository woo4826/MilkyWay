import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface APIS {

    @POST("/home/searchRoomList.do")
    @Headers(
        "accept: application/json",
        "content-type: application/json"
    )
    fun roomListByLatLon(
        @Body jsonparams: SearchReqDTO
    ): Call<SearchResDTO>


    companion object {
        private const val BASE_URL = "https://sooyusil.com/" // 주소

        fun create(): APIS {


            val gson: Gson = GsonBuilder().setLenient().create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(APIS::class.java)
        }
    }
}