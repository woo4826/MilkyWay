package kr.racto.milkyway.ui

import NursingRoomDTO
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel:ViewModel() {
    val selectedRoom = MutableLiveData<NursingRoomDTO>()
    fun setLiveData(room: NursingRoomDTO){
        selectedRoom.value = room
    }
    fun getLiveData(): NursingRoomDTO? {
        return selectedRoom.value
    }
}