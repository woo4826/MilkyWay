import com.google.gson.annotations.SerializedName

data class SearchReqDTO(
    @SerializedName("searchKeyword") val searchKeyword: String,
    @SerializedName("roomTypeCode") val roomTypeCode: String,
    @SerializedName("pageNo") val pageNo: String,
    @SerializedName("mylat") val mylat: String,
    @SerializedName("mylng") val mylng: String
)