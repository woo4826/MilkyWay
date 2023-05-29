package kr.racto.milkyway

import com.google.gson.annotations.SerializedName


data class RoomModel (

    @SerializedName("room_name"      ) var roomName      : String?           = null,
    @SerializedName("lon"            ) var lon           : Double?           = null,
    @SerializedName("lat"            ) var lat           : Double?           = null,
    @SerializedName("address"        ) var address       : String?           = null,
    @SerializedName("detail_address" ) var detailAddress : String?           = null,
    @SerializedName("reviews"        ) var reviews       : ArrayList<String> = arrayListOf()

)