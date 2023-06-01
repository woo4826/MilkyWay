import com.google.gson.annotations.SerializedName

data class SearchResDTO(

    @SerializedName("nursingRoomSearchList") val nursingRoomDTO: List<NursingRoomDTO>,
    @SerializedName("pagination") val pagination: Pagination
)