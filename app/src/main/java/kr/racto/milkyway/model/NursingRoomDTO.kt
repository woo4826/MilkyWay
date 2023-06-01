import com.google.gson.annotations.SerializedName

data class NursingRoomDTO(

    @SerializedName("pageNo") val pageNo: Int?,
    @SerializedName("countPerPage") val countPerPage: Int?,
    @SerializedName("totalCount") val totalCount: Int?,
//    @SerializedName("articleNo") val articleNo: String?,
//    @SerializedName("contentsNo") val contentsNo: String?,
//    @SerializedName("articleTitle") val articleTitle: String?,
//    @SerializedName("articleSearchTarget") val articleSearchTarget: String?,
//    @SerializedName("menuNo") val menuNo: String?,
//    @SerializedName("menuName") val menuName: String?,
//    @SerializedName("upperMenuNo") val upperMenuNo: String?,
//    @SerializedName("upperMenuName") val upperMenuName: String?,
//    @SerializedName("matchingRate") val matchingRate: Double?,
    @SerializedName("roomNo") val roomNo: String?,
    @SerializedName("roomName") val roomName: String?,
    @SerializedName("cityName") val cityName: String?,
    @SerializedName("zoneName") val zoneName: String?,
    @SerializedName("townName") val townName: String?,
    @SerializedName("managerName") val managerName: String?,
    @SerializedName("managerTelNo") val managerTelNo: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("locationName") val locationName: String?,
    @SerializedName("gpsLat") val gpsLat: String?,
    @SerializedName("gpsLong") val gpsLong: String?,
    @SerializedName("roomTypeCode") val roomTypeCode: String?,
    @SerializedName("beforeWord") val beforeWord: String?,
    @SerializedName("afterWord") val afterWord: String?,
    @SerializedName("fatherUseYn") val fatherUseYn: String?,
//    @SerializedName("startNo") val startNo: Int
)